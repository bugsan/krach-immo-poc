package fr.krachimmo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import fr.krachimmo.job.SelogerScrapJob;

/**
 *
 * @author Sébastien Chatel
 * @since 20 June 2014
 */
@Controller
public class SelogerScrapJobController {

	@Autowired
	SelogerScrapJob selogerScrapJob;

	@RequestMapping("/seloger-scrap-job")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void start() {
		this.selogerScrapJob.execute();
	}
}
