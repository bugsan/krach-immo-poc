package fr.krachimmo.job;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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

		PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
				new AppengineFileOutputStream(this.fileService, file, true), "utf-8")));

		for (int page = 0; page <= 200; page++) {
			Future<AnnonceSearchResults> resultAsync = this.scrap.scrapForObject("", this.documentMapper);
			for (Annonce annonce : resultAsync.get().getAnnonces()) {

			}
		}
		out.close();
	}
}
