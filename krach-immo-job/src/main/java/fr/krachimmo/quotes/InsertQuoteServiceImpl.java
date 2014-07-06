package fr.krachimmo.quotes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;

import fr.krachimmo.core.file.store.AppengineFileInputStream;
import fr.krachimmo.job.AnnoncesStats;
import fr.krachimmo.seloger.Annonce;

/**
 *
 * @author Sébastien Chatel
 * @since 6 July 2014
 */
@Service
@SuppressWarnings("deprecation")
public class InsertQuoteServiceImpl implements InsertQuoteService {

	@Autowired
	FileService fileService;
	@Autowired
	DatastoreService datastoreService;

	@Override
	public void insertFromFile(String filename, String date) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new GZIPInputStream(
						new AppengineFileInputStream(
								this.fileService, new AppEngineFile(
										"/gs/krach-immo/" + filename))),
				"utf-8"));
		AnnoncesStats stats = new AnnoncesStats();
		String line;
		while ((line = reader.readLine()) != null) {
			String[] parts = line.split(",");
			Annonce annonce = new Annonce();
			annonce.setPrix(Integer.parseInt(parts[1]));
			annonce.setSuperficie(Double.parseDouble(parts[2]));
			annonce.setPieces(Integer.parseInt(parts[3]));
			stats.add(annonce);
		}
		reader.close();
		Entity entity = new Entity(KeyFactory.createKey("Quote", date));
		entity.setUnindexedProperty("studio", stats.getPricePerSquareMeter1Piece());
		entity.setUnindexedProperty("2pieces", stats.getPricePerSquareMeter2Pieces());
		entity.setUnindexedProperty("3pieces", stats.getPricePerSquareMeter3Pieces());
		entity.setUnindexedProperty("4+pieces", stats.getPricePerSquareMeter4PlusPieces());
		entity.setUnindexedProperty("total", stats.getPricePerSquareMeter());
		this.datastoreService.put(entity);
	}
}
