package fr.krachimmo.config;

import java.util.Properties;

import javax.mail.Session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

import fr.krachimmo.core.http.client.AsyncClientHttpRequestFactory;
import fr.krachimmo.core.http.client.urlfetch.UrlFetchClientHttpRequestFactory;
import fr.krachimmo.core.scrap.ScrapOperations;
import fr.krachimmo.core.scrap.ScrapTemplate;

/**
 *
 * @author Sébastien Chatel
 * @since 20 June 2014
 */
@Configuration
@ComponentScan("fr.krachimmo")
@EnableWebMvc
@SuppressWarnings("deprecation")
public class WebAppConfig {
	@Bean
	public AppengineShutdownHookConfigurer shutdownHookConfigurer() {
		return new AppengineShutdownHookConfigurer();
	}
	@Bean
	public AsyncClientHttpRequestFactory clientHttpRequestFactory(URLFetchService urlFetchService) {
		UrlFetchClientHttpRequestFactory factory = new UrlFetchClientHttpRequestFactory();
		factory.setUrlFetchService(urlFetchService);
		factory.setDeadline(20);
		return factory;
	}
	@Bean
	public ScrapOperations scrapOperations(AsyncClientHttpRequestFactory factory) {
		return new ScrapTemplate(factory);
	}
	@Bean
	public URLFetchService urlFetchService() {
		return URLFetchServiceFactory.getURLFetchService();
	}
	@Bean
	public FileService fileService() {
		return FileServiceFactory.getFileService();
	}
	@Bean
	public DatastoreService datastoreService() {
		return DatastoreServiceFactory.getDatastoreService();
	}
	@Bean
	public Session mailSession() {
		return Session.getDefaultInstance(new Properties());
	}
}
