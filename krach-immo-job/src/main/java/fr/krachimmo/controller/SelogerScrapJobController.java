package fr.krachimmo.controller;

import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.appengine.api.files.FileService;

import fr.krachimmo.data.DataStore;
import fr.krachimmo.job.CloudStorageFile;
import fr.krachimmo.job.Config;
import fr.krachimmo.job.SearchCriteria;
import fr.krachimmo.job.SelogerScrapJob;
import fr.krachimmo.job.TypeBien;

/**
 *
 * @author Sébastien Chatel
 * @since 20 June 2014
 */
@Controller
@SuppressWarnings("deprecation")
public class SelogerScrapJobController {

	private static final Log log = LogFactory.getLog(SelogerScrapJobController.class);

	@Autowired
	SelogerScrapJob selogerScrapJob;

	@Autowired
	DataStore dataStore;

	@Autowired
	FileService fileService;

	@RequestMapping("/run-scrap-job")
	public ResponseEntity<?> run() throws Exception {
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		df.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
		String filename = "paris-" + df.format(new Date()) + ".csv";
		CloudStorageFile file = new CloudStorageFile("krach-immo", filename);
		this.selogerScrapJob.run(new Config(
				new SearchCriteria()
				.commune("750056") // paris
				.typeBien(TypeBien.Appartement),
				file));
		this.dataStore.saveLatestDataLocation(file.getLocation());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(URI.create(file.getLocation()));
		return new ResponseEntity<String>(headers, HttpStatus.FOUND);
	}

	@RequestMapping("/paris-latest")
	public ResponseEntity<Resource> latestData() throws IOException {
		Resource resource = this.dataStore.findLatestData();
		if (resource == null) {
			return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "text/plain; charset=utf-8");
		return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
	}

	@RequestMapping("/_ah/start")
	@ResponseStatus(HttpStatus.OK)
	public void start() {
		log.info("Backend started");
	}
}
