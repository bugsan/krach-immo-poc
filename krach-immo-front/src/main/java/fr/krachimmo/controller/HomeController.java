package fr.krachimmo.controller;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;

/**
 *
 * @author Sébastien Chatel
 * @since 20 January 2014
 */
@Controller
public class HomeController {

	private static final Log log = LogFactory.getLog(HomeController.class);

	@Autowired
	GcsService storageService;
	@Autowired
	DatastoreService datastoreService;
	@Autowired
	MemcacheService memcacheService;

	@RequestMapping("/")
	public ResponseEntity<String> home(@RequestParam String filename) throws IOException {
		Entity entity = new Entity("Toto");
		entity.setUnindexedProperty("filename", filename);
		this.datastoreService.put(entity);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/plain");
		return new ResponseEntity<String>(entity.getKey().toString(), headers, HttpStatus.OK);
	}

//	@RequestMapping("/")
	public ResponseEntity<String> home2(@RequestParam String filename) throws IOException {
		log.info("storing file");
		GcsFileOptions options = new GcsFileOptions.Builder().mimeType("text/plain").build();
		GcsOutputChannel channel = this.storageService.createOrReplace(
				new GcsFilename("test-bucket", filename), options);
		channel.write(ByteBuffer.wrap("Hello World!".getBytes()));
		channel.close();

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/plain");
		return new ResponseEntity<String>(channel.getFilename().toString(), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/annonces", method = RequestMethod.GET)
	public ResponseEntity<String> getAllAnnonces() {
		PreparedQuery query = this.datastoreService.prepare(new Query("Annonce"));
		StringBuilder sb = new StringBuilder(256);
		sb.append('[');
		for (Entity entity : query.asIterable()) {
			if (sb.length() > 1) {
				sb.append(',');
			}
			sb.append('[')
				.append(entity.getProperty("surface")).append(',')
				.append(entity.getProperty("price"))
				.append(']');
		}
		sb.append(']');
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		return new ResponseEntity<String>(sb.toString(), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/annonces", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void createAnnonces() {
		List<Entity> entities = new ArrayList<Entity>();
		entities.add(createAnnonce(60, 3500));
		entities.add(createAnnonce(110, 3000));
		entities.add(createAnnonce(45, 4100));
		entities.add(createAnnonce(40, 4000));
		entities.add(createAnnonce(25, 5200));
		this.datastoreService.put(entities);
	}

	private Entity createAnnonce(int surface, int price) {
		Entity entity = new Entity("Annonce");
		entity.setUnindexedProperty("surface", surface);
		entity.setUnindexedProperty("price", price);
		return entity;
	}
}
