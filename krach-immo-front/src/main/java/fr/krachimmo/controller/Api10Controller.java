package fr.krachimmo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Sébastien Chatel
 * @since 04 March 2014
 */
//@Controller
@RequestMapping(headers="X-Api-Version=1.0", produces="application/json")
public class Api10Controller {

	@RequestMapping("/api")
	public ResponseEntity<String> welcome() {
		return new ResponseEntity<String>("{\"version\":\"1.0\"}", HttpStatus.OK);
	}
}
