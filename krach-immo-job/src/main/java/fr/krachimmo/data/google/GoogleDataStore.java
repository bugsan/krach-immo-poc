package fr.krachimmo.data.google;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.files.FileService;

import fr.krachimmo.data.DataStore;

/**
 *
 * @author Sébastien Chatel
 * @since 21 June 2014
 */
@Component
@SuppressWarnings("deprecation")
public class GoogleDataStore implements DataStore {

	@Autowired
	DatastoreService datastoreService;

	@Autowired
	FileService fileService;

	@Override
	public String findLatestDataLocation() {
		try {
			Entity latest = this.datastoreService.get(KeyFactory.createKey("LatestData", 1L));
			return (String) latest.getProperty("location");
		}
		catch (EntityNotFoundException ex) {
			return null;
		}
	}

	@Override
	public void saveLatestDataLocation(String location) {
		Entity latest = new Entity("LatestData", 1L);
		latest.setUnindexedProperty("location", location);
		this.datastoreService.put(latest);
	}

//	@Override
//	public Resource findLatestData() {
//		String path = findLatestDataLocation();
//		if (path == null) {
//			return null;
//		}
//		String filepath = "/gs/" + path.substring(path.indexOf(".com/") + 5);
//		return new CloudStorageResource(filepath, this.fileService);
//	}
}
