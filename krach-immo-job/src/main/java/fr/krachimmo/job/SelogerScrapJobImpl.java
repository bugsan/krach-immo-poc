package fr.krachimmo.job;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

	private static final int MAX_CONCURRENT_REQUESTS = 15;

	private static final int LAST_PAGE = 200;

	@Autowired
	ScrapOperations scrap;

	@Autowired
	FileService fileService;

	DocumentMapper<AnnonceSearchResults> documentMapper = new AnnonceSearchResultsDocumentMapper();

	@Override
	public void run(Config config) throws Exception {
		log.info("Starting seloger scrap job");
		log.info("Opening file for writing " + config.getFile().getLocation());

		PrintWriter out = openWriter(config.getFile());

		int page = 1;
		while (page <= LAST_PAGE) {
			int concurrentRequestCount = 0;
			List<ScrapTask<AnnonceSearchResults>> taskPool = new ArrayList<ScrapTask<AnnonceSearchResults>>();
			while (concurrentRequestCount++ < MAX_CONCURRENT_REQUESTS && page <= LAST_PAGE) {
				String uri = buildSearchUri(config.getCriteria(), page);
//				log.info("Scrap de la page seloger " + uri);
				taskPool.add(new ScrapTask<AnnonceSearchResults>(
						uri, this.scrap.scrapForObject(uri, this.documentMapper)));
				page++;
			}
			for (ScrapTask<AnnonceSearchResults> task : taskPool) {
				try {
					AnnonceSearchResults results = task.getFuture().get();
					for (Annonce annonce : results.getAnnonces()) {
						out.println(buildCsvLine(annonce));
					}
				}
				catch (ExecutionException ex) {
					throw new IllegalStateException(
							"Could not scrap resource " + task.getUrl(), ex);
				}
			}
		}

		out.close();

		log.info("Seloger scrap job finished successfuly");
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
		sb.append("http://www.seloger.com/list.htm?org=engine&idtt=2");
		sb.append("&ci=");
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
}
