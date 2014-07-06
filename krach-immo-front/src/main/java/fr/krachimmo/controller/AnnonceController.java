package fr.krachimmo.controller;

import java.io.IOException;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.krachimmo.repository.Repository;

/**
 *
 * @author Sébastien Chatel
 * @since 6 July 2014
 */
@Controller
public class AnnonceController {
	@Autowired
	Repository repository;

	@RequestMapping("/api/annonces/latest")
	public ResponseEntity<?> latestData() throws IOException {
		String location = this.repository.findLatestDataLocation();
		if (location == null) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(URI.create(location));
		return new ResponseEntity<Object>(headers, HttpStatus.FOUND);
	}
}
