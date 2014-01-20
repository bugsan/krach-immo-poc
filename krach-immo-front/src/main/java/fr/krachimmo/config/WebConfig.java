package fr.krachimmo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;

/**
 *
 * @author Sébastien Chatel
 * @since 20 January 2014
 */
@Configuration
@ComponentScan(basePackages="fr.krachimmo")
@EnableWebMvc
public class WebConfig {
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
}
