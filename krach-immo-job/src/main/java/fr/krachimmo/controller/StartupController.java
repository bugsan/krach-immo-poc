package fr.krachimmo.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Sébastien Chatel
 * @since 6 July 2014
 */
@Controller
public class StartupController {
	private static final Log log = LogFactory.getLog(StartupController.class);
	@RequestMapping("/_ah/start")
	@ResponseStatus(HttpStatus.OK)
	public void start() {
		log.info("Backend started");
	}
}
