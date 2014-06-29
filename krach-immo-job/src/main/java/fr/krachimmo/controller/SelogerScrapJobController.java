package fr.krachimmo.controller;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import fr.krachimmo.data.DataStore;
import fr.krachimmo.job.AnnonceCsvLineAggregator;
import fr.krachimmo.job.Config;
import fr.krachimmo.job.FileOptions;
import fr.krachimmo.job.LineAggregator;
import fr.krachimmo.job.SearchCriteria;
import fr.krachimmo.job.SelogerScrapJob;
import fr.krachimmo.job.Tri;
import fr.krachimmo.job.TypeBien;
import fr.krachimmo.seloger.Annonce;
import fr.krachimmo.seloger.AnnonceSearchResults;
import fr.krachimmo.seloger.SelogerSearchService;

/**
 *
 * @author Sébastien Chatel
 * @since 20 June 2014
 */
@Controller
public class SelogerScrapJobController {

	private static final Log log = LogFactory.getLog(SelogerScrapJobController.class);

	@Autowired
	SelogerScrapJob selogerScrapJob;
	@Autowired
	DataStore dataStore;
	@Autowired
	SelogerSearchService selogerSearchService;

	LineAggregator<Annonce> lineAggregator = new AnnonceCsvLineAggregator();

	@RequestMapping("/run-scrap-job")
	@ResponseStatus(HttpStatus.CREATED)
	public void run() throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		df.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
		FileOptions fileOptions = new FileOptions()
				.bucket("krach-immo")
				.filename("paris-" + df.format(new Date()) + ".csv")
				.charset("utf-8")
				.gzip(true);
		SearchCriteria criteria = new SearchCriteria()
				.commune("750101,750102,750103,750104,750105,750106," +
						"750107,750108,750109,750110,750111,750112," +
						"750113,750114,750115,750116,750117") // paris
				.typeBien(TypeBien.Appartement)
				.tri(Tri.Modification)
				.fraicheur(5);
		this.selogerScrapJob.run(new Config(criteria, fileOptions));
		this.dataStore.saveLatestDataLocation(fileOptions.getLocation());
	}

	@RequestMapping("/latest")
	public ResponseEntity<?> latestData() throws IOException {
		String location = this.dataStore.findLatestDataLocation();
		if (location == null) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(URI.create(location));
		return new ResponseEntity<Object>(headers, HttpStatus.FOUND);
	}

	@RequestMapping("/search")
	public ResponseEntity<String> search(SearchQuery query) throws Exception {
		AnnonceSearchResults results = this.selogerSearchService.search(
				"http://www.seloger.com/list.htm?idtt=2" +
				(query.getCi() != null ? "&ci=" + query.getCi() : "") +
				"&idtypebien=" + query.getIdtypebien() +
				"&tri=" + query.getTri() +
				(query.getFraicheur() != null ? "&fraicheur=" + query.getFraicheur() : "") +
				"&LISTING-LISTpg=" + query.getPage())
				.get();
		StringBuilder sb = new StringBuilder();
		for (Annonce annonce : results.getAnnonces()) {
			sb.append(this.lineAggregator.aggregate(annonce));
			sb.append("\n");
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);
		return new ResponseEntity<String>(sb.toString(), headers, HttpStatus.OK);
	}

	@RequestMapping("/_ah/start")
	@ResponseStatus(HttpStatus.OK)
	public void start() {
		log.info("Backend started");
	}
}
