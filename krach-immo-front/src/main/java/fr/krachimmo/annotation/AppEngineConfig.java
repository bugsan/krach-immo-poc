package fr.krachimmo.annotation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.search.SearchService;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;

/**
 *
 * @author Sébastien Chatel
 * @since 05 February 2014
 */
@Configuration
public class AppEngineConfig {
	@Bean
	GcsService storageService() {
		return GcsServiceFactory.createGcsService();
	}
	@Bean
	DatastoreService datastoreService() {
		return DatastoreServiceFactory.getDatastoreService();
	}
	@Bean
	MemcacheService memcacheService() {
		return MemcacheServiceFactory.getMemcacheService();
	}
	@Bean
	SearchService searchService() {
		return SearchServiceFactory.getSearchService();
	}
}
