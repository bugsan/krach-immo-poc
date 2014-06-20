package fr.krachimmo.job;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
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

	private static final int CONCURRENT_REQUESTS = 20;

	private static final int LAST_PAGE = 200;

	@Autowired
	ScrapOperations scrap;

	@Autowired
	FileService fileService;

	DocumentMapper<AnnonceSearchResults> documentMapper = new AnnonceSearchResultsDocumentMapper();

	@Override
	public void execute() throws Exception {
		final AppEngineFile file = this.fileService.createNewGSFile(new GSFileOptionsBuilder()
			.setBucket("krach-immo")
			.setKey("paris.json")
			.setMimeType("application/json; charset=utf-8")
			.setAcl("public_read")
			.build());

		PrintWriter out = openAppengineWriter(file);
		out.println("[");
		boolean moreThanOneAnnonce = false;

		int page = 0;
		while (page <= LAST_PAGE) {
			int concurrentRequestCount = 0;
			List<Future<AnnonceSearchResults>> futurePool = new ArrayList<Future<AnnonceSearchResults>>();
			while (concurrentRequestCount++ < CONCURRENT_REQUESTS && page <= LAST_PAGE) {
				futurePool.add(executeAsyncRequest(page));
				page++;
			}
			for (Future<AnnonceSearchResults> future : futurePool) {
				for (Annonce annonce : future.get().getAnnonces()) {
					if (moreThanOneAnnonce) {
						out.print(",");
					}
					writeAnnonce(annonce, out);
					moreThanOneAnnonce = true;
				}
			}
		}
		out.println("]");
		out.close();
	}

	private PrintWriter openAppengineWriter(AppEngineFile file) throws IOException {
		return new PrintWriter(new BufferedWriter(new OutputStreamWriter(
				new AppengineFileOutputStream(this.fileService, file, true), "utf-8")));
	}

	private Future<AnnonceSearchResults> executeAsyncRequest(int page) {
		return this.scrap.scrapForObject(
				"http://www.seloger.com/recherche.htm?" +
				"org=engine&" +
				"idtt=2&" +
				"ci=950641&" +
				"idtypebien=1,2&" +
				"tri=d_dt_crea&" +
				"BCLANNpg=" + page,
				this.documentMapper);
	}

	private void writeAnnonce(Annonce annonce, PrintWriter out) {
		int pieces = annonce.getPieces();
		int prixm2 = (int) Math.round((annonce.getPrix() / annonce.getSuperficie()) / 50) * 50;
		int superficie = (int) Math.round(annonce.getSuperficie());
		out.println("[" + superficie
				+ ", " + (pieces==1||pieces==0&&superficie<=50?prixm2:null)
				+ ", " + (pieces==2?prixm2:null)
				+ ", " + (pieces==3?prixm2:null)
				+ ", " + (pieces==4?prixm2:null)
				+ ", " + (pieces>=5||pieces==0&&superficie>50?prixm2:null)
				+ "]");
	}
}
