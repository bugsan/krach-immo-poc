package fr.krachimmo.controller;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import fr.krachimmo.data.DataStore;
import fr.krachimmo.job.CloudStorageFile;
import fr.krachimmo.job.Config;
import fr.krachimmo.job.SearchCriteria;
import fr.krachimmo.job.SelogerScrapJob;
import fr.krachimmo.job.Tri;
import fr.krachimmo.job.TypeBien;

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

	@RequestMapping("/run-scrap-job")
	@ResponseStatus(HttpStatus.OK)
	public void run() throws Exception {
		CloudStorageFile file = new CloudStorageFile("krach-immo",
				"paris-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".csv",
				"utf-8", true);
		SearchCriteria criteria = new SearchCriteria()
				.commune("750101,750102,750103,750104,750105,750106," +
						"750107,750108,750109,750110,750111,750112," +
						"750113,750114,750115,750116,750117") // paris
				.typeBien(TypeBien.Appartement)
				.tri(Tri.Modification);
		this.selogerScrapJob.run(new Config(criteria, file));
		this.dataStore.saveLatestDataLocation(file.getLocation());
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

	@RequestMapping("/_ah/start")
	@ResponseStatus(HttpStatus.OK)
	public void start() {
		log.info("Backend started");
	}
}
