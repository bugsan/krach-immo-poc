package fr.krachimmo.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import fr.krachimmo.job.Config;
import fr.krachimmo.job.FileOptions;
import fr.krachimmo.job.ReportConfig;
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

	@Autowired
	SelogerScrapJob selogerScrapJob;

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
				.tri(Tri.Creation)
				.fraicheur(5);
		ReportConfig reportConfig = new ReportConfig()
				.mailTo("bugsan@gmail.com");
		this.selogerScrapJob.run(new Config(criteria, fileOptions, reportConfig));
	}
}
