package fr.krachimmo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

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
	public ScrapOperations scrapOperations(URLFetchService urlFetchService) {
		return new ScrapTemplate(new UrlFetchClientHttpRequestFactory(urlFetchService));
	}
	@Bean
	public URLFetchService urlFetchService() {
		return URLFetchServiceFactory.getURLFetchService();
	}
	@Bean
	public FileService fileService() {
		return FileServiceFactory.getFileService();
	}
}
