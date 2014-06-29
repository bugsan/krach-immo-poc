package fr.krachimmo.job;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.zip.GZIPOutputStream;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.GSFileOptions.GSFileOptionsBuilder;

import fr.krachimmo.core.file.store.AppengineFileOutputStream;
import fr.krachimmo.seloger.Annonce;
import fr.krachimmo.seloger.AnnonceSearchResults;
import fr.krachimmo.seloger.SelogerSearchService;

/**
 *
 * @author Sébastien Chatel
 * @since 20 June 2014
 */
@Component
@SuppressWarnings("deprecation")
public class SelogerScrapJobImpl implements SelogerScrapJob {

	private static final Log log = LogFactory.getLog(SelogerScrapJobImpl.class);

	private static final int DEFAULT_MAX_CONCURRENT_REQUESTS = 10;

	private static final int DEFAULT_LAST_PAGE = 200;

	private static final long DEFAULT_WAIT_TIMEOUT_MILLIS = 25L;

	@Autowired
	FileService fileService;
	@Autowired
	SelogerSearchService selogerSearchService;
	@Autowired
	Session session;

	private int lastPage = DEFAULT_LAST_PAGE;

	LineAggregator<Annonce> lineAggregator = new AnnonceCsvLineAggregator();

	private int maxConcurrentRequests = DEFAULT_MAX_CONCURRENT_REQUESTS;

	private long waitTimeoutMillis = DEFAULT_WAIT_TIMEOUT_MILLIS;

	public void setMaxConcurrentRequests(int maxConcurrentRequests) {
		this.maxConcurrentRequests = maxConcurrentRequests;
	}

	public void setWaitTimeoutMillis(long waitTimeoutMillis) {
		this.waitTimeoutMillis = waitTimeoutMillis;
	}

	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}

	@Override
	public void run(Config config) throws Exception {
		log.info("Starting seloger scrap job");
		PrintWriter annoncesFile = openFileForWriting(config.getFileOptions());
		Set<Long> annoncesUniques = new HashSet<Long>();
		Set<SearchTask<AnnonceSearchResults>> tasks = new CopyOnWriteArraySet<SearchTask<AnnonceSearchResults>>();
		AnnoncesStats annoncesStats = new AnnoncesStats();
		int page = 1;

		while (page <= this.lastPage && tasks.size() < this.maxConcurrentRequests) {
			tasks.add(submitSearch(config.getCriteria().buildUri(page++)));
		}
		while (page <= this.lastPage || tasks.size() > 0) {
			for (SearchTask<AnnonceSearchResults> task : tasks) {
				try {
					AnnonceSearchResults results = task.getResult(this.waitTimeoutMillis);
					tasks.remove(task);
					if (page <= this.lastPage) {
						tasks.add(submitSearch(config.getCriteria().buildUri(page++)));
					}
					for (Annonce annonce : results.getAnnonces()) {
						if (!annoncesUniques.contains(annonce.getId())) {
							annoncesFile.println(this.lineAggregator.aggregate(annonce));
							annoncesStats.add(annonce);
							annoncesUniques.add(annonce.getId());
						}
					}
				}
				catch (TimeoutException ex) {
					// do nothing
				}
				catch (ExecutionException ex) {
					if (canRetrySearchTask(ex.getCause())) {
						log.warn("Search task failed, resubmitting " + task.getUri());
						tasks.remove(task);
						tasks.add(submitSearch(task.getUri()));
					}
					else {
						throw (Exception) ex.getCause();
					}
				}
			}
		}
		annoncesFile.close();
		sendEmail(
			"Krach-Immo Report <report@krach-immo.appspotmail.com>",
			config.getReportConfig().getMailTo(),
			"Mètre carré parisien " + annoncesStats.getPricePerSquareMeter() + " €",
			"<p>Mètre carré parisien <b>" + annoncesStats.getPricePerSquareMeter() + "</b> €</p>" +
			"<p>Données disponibles <a href='" + config.getFileOptions().getLocation() + "'><b>ici</b</a></p>");
		log.info("Successfully scraped " + annoncesUniques.size() + " annonces");
	}

	private SearchTask<AnnonceSearchResults> submitSearch(String uri) {
		if (log.isDebugEnabled()) {
			log.debug("Submit search task " + uri);
		}
		return new SearchTask<AnnonceSearchResults>(uri, this.selogerSearchService.search(uri));
	}

	private PrintWriter openFileForWriting(FileOptions fileOptions) throws IOException {
		log.info("Opening file for writing " + fileOptions.getLocation());
		GSFileOptionsBuilder optionsBuilder = new GSFileOptionsBuilder()
			.setBucket(fileOptions.getBucket())
			.setKey(fileOptions.getFilename())
			.setMimeType("text/plain; charset=" + fileOptions.getCharset())
			.setAcl("public_read");
		if (fileOptions.isGzip()) {
			optionsBuilder.setContentEncoding("gzip");
		}
		AppEngineFile appengineFile = this.fileService.createNewGSFile(optionsBuilder.build());
		OutputStream os = new AppengineFileOutputStream(this.fileService, appengineFile, true);
		os = new BufferedOutputStream(os);
		os = fileOptions.isGzip() ? new GZIPOutputStream(os, true) : os;
		Writer writer = new OutputStreamWriter(os, fileOptions.getCharset());
		writer = new BufferedWriter(writer);
		return new PrintWriter(writer);
	}

	private void sendEmail(String from, String to, String subject, String html) throws MessagingException {
		log.info("Sending report to " + to);
		MimeMessage message = new MimeMessage(this.session);
		message.setFrom(new InternetAddress(from));
		message.setRecipient(RecipientType.TO, new InternetAddress(to));
		message.setSubject(subject, "utf-8");
		MimeBodyPart textPart = new MimeBodyPart();
		textPart.setText(html, "utf-8");
		textPart.setHeader("Content-Type", "text/html; charset=\"utf-8\"");
		MimeMultipart multipart = new MimeMultipart("mixed");
		multipart.addBodyPart(textPart);
		message.setContent(multipart);
		Transport.send(message);
	}

	/**
	 * TODO leaky abstraction of urlfetch
	 * @param cause
	 * @return
	 */
	private boolean canRetrySearchTask(Throwable cause) {
		return cause.getMessage().startsWith("Could not fetch URL:");
	}
}
