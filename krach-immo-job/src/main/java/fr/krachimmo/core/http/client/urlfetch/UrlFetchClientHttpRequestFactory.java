package fr.krachimmo.core.http.client.urlfetch;

import java.io.IOException;
import java.net.URI;

import org.springframework.http.HttpMethod;

import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

import fr.krachimmo.core.http.client.AsyncClientHttpRequest;
import fr.krachimmo.core.http.client.AsyncClientHttpRequestFactory;

/**
 *
 * @author Sébastien Chatel
 * @since 10 May 2013
 */
public class UrlFetchClientHttpRequestFactory implements AsyncClientHttpRequestFactory {

	private URLFetchService urlFetchService;

	boolean validateCertificate = false;

	double deadline = 10;

	public void setValidateCertificate(boolean validateCertificate) {
		this.validateCertificate = validateCertificate;
	}

	public void setUrlFetchService(URLFetchService urlFetchService) {
		this.urlFetchService = urlFetchService;
	}

	public void setDeadline(double deadline) {
		this.deadline = deadline;
	}

	public URLFetchService getUrlFetchService() {
		if (this.urlFetchService == null) {
			this.urlFetchService = URLFetchServiceFactory.getURLFetchService();
		}
		return this.urlFetchService;
	}

	public UrlFetchClientHttpRequestFactory() {
	}

	public UrlFetchClientHttpRequestFactory(URLFetchService urlFetchService) {
		this.urlFetchService = urlFetchService;
	}

	@Override
	public AsyncClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
		return new UrlFetchClientHttpRequest(uri, httpMethod, this);
	}
}
