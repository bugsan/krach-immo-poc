package fr.krachimmo.job;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.GSFileOptions;
import com.google.appengine.api.files.GSFileOptions.GSFileOptionsBuilder;

import fr.krachimmo.core.scrap.DocumentMapper;
import fr.krachimmo.core.scrap.ScrapOperations;
import fr.krachimmo.core.store.AppengineFileOutputStream;

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

	private static final int LAST_PAGE = 200;

	private static final long DEFAULT_WAIT_TIMEOUT_MILLIS = 25L;

	@Autowired
	ScrapOperations scrap;

	@Autowired
	FileService fileService;

	DocumentMapper<AnnonceSearchResults> documentMapper = new AnnonceSearchResultsDocumentMapper();

	private int maxConcurrentRequests = DEFAULT_MAX_CONCURRENT_REQUESTS;

	private long waitTimeoutMillis = DEFAULT_WAIT_TIMEOUT_MILLIS;

	public void setMaxConcurrentRequests(int maxConcurrentRequests) {
		this.maxConcurrentRequests = maxConcurrentRequests;
	}

	public void setWaitTimeoutMillis(long waitTimeoutMillis) {
		this.waitTimeoutMillis = waitTimeoutMillis;
	}

	@Override
	public void run(Config config) throws Exception {
		log.info("Starting seloger scrap job");
		log.info("Opening file for writing " + config.getFile().getLocation());
		PrintWriter out = openWriter(config.getFile());
		Set<ScrapTask<AnnonceSearchResults>> tasks = new CopyOnWriteArraySet<ScrapTask<AnnonceSearchResults>>();
		int page = 1;
		int annonceCount = 0;
		while (page <= LAST_PAGE && tasks.size() < this.maxConcurrentRequests) {
			tasks.add(submitTask(buildSearchUri(config.getCriteria(), page++)));
		}
		while (page <= LAST_PAGE || tasks.size() > 0) {
			for (ScrapTask<AnnonceSearchResults> task : tasks) {
				try {
					AnnonceSearchResults results = task.getResult(this.waitTimeoutMillis);
					tasks.remove(task);
					if (page <= LAST_PAGE) {
						tasks.add(submitTask(buildSearchUri(config.getCriteria(), page++)));
					}
					out.println("#" + task.getUri());
					for (Annonce annonce : results.getAnnonces()) {
						out.println(buildCsvLine(annonce));
						annonceCount++;
					}
				}
				catch (TimeoutException ex) {
					// do nothing
				}
				catch (ExecutionException ex) {
					if (canRetryRequest(ex.getCause())) {
						log.warn("Http request failed, resubmitting task " + task.getUri());
						tasks.remove(task);
						tasks.add(submitTask(task.getUri()));
					}
					else {
						throw (Exception) ex.getCause();
					}
				}
			}
		}
		out.close();
		log.info("Successfully scraped " + annonceCount + " annonces");
	}

	private ScrapTask<AnnonceSearchResults> submitTask(final String uri) {
		if (log.isDebugEnabled()) {
			log.debug("Submit scrap task " + uri);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept-Encoding", "gzip");
		headers.set("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		headers.set("Accept-Language", "fr-fr,fr;q=0.8,en-us;q=0.5,en;q=0.3");
		headers.set("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:30.0) Gecko/20100101 Firefox/30.0");
		return new ScrapTask<AnnonceSearchResults>(uri, this.scrap.scrapForObject(uri, headers, this.documentMapper));
	}

	private PrintWriter openWriter(CloudStorageFile file) throws IOException {
		AppEngineFile cloudFile = this.fileService.createNewGSFile(createFileOptions(file));
		OutputStream os = new AppengineFileOutputStream(this.fileService, cloudFile, true);
		os = new BufferedOutputStream(os);
		os = file.isGzip() ? new GZIPOutputStream(os) : os;
		Writer writer = new OutputStreamWriter(os, file.getCharset());
		writer = new BufferedWriter(writer);
		return new PrintWriter(writer);
	}

	private GSFileOptions createFileOptions(CloudStorageFile file) {
		GSFileOptionsBuilder builder = new GSFileOptionsBuilder()
			.setBucket(file.getBucket())
			.setKey(file.getFilename())
			.setMimeType("text/plain; charset=" + file.getCharset())
			.setAcl("public_read");
		if (file.isGzip()) {
			builder.setContentEncoding("gzip");
		}
		return builder.build();
	}

	private String buildSearchUri(SearchCriteria criteria, int page) {
		StringBuilder sb = new StringBuilder(256);
		sb.append("http://www.seloger.com/list.htm");
		sb.append("?idtt=2&ci=");
		appendChoices(sb, criteria.getCommunes());
		sb.append("&idtypebien=");
		appendChoices(sb, criteria.getTypeBiens());
		sb.append("&tri=").append(criteria.getTri());
		sb.append("&LISTING-LISTpg=").append(page);
		return sb.toString();
	}

	private void appendChoices(StringBuilder sb, List<?> choices) {
		boolean first = true;
		for (Object choice : choices) {
			if (!first) {
				sb.append(',');
			}
			sb.append(choice.toString());
			first = false;
		}
	}

	private String buildCsvLine(Annonce annonce) {
		StringBuilder sb = new StringBuilder(128);
		sb.append(annonce.getId());
		sb.append(',');
		sb.append(annonce.getPrix());
		sb.append(',');
		sb.append(annonce.getSuperficie());
		sb.append(',');
		sb.append(annonce.getPieces());
		return sb.toString();
	}

	private boolean canRetryRequest(Throwable cause) {
		return cause.getMessage().startsWith("Could not fetch URL:");
	}
}
