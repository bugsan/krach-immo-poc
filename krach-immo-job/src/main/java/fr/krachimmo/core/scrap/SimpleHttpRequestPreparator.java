package fr.krachimmo.core.scrap;

import java.io.IOException;
import java.net.URI;

import org.springframework.http.HttpMethod;

import fr.krachimmo.core.http.client.AsyncClientHttpRequest;
import fr.krachimmo.core.http.client.AsyncClientHttpRequestFactory;

/**
 *
 * @author Sébastien Chatel
 * @since 25 October 2013
 */
public class SimpleHttpRequestPreparator implements ClientHttpRequestPreparator {

	private final String uri;

	private final HttpMethod method;

	public SimpleHttpRequestPreparator(String uri) {
		this(uri, HttpMethod.GET);
	}

	public SimpleHttpRequestPreparator(String uri, HttpMethod method) {
		this.uri = uri;
		this.method = method;
	}

	@Override
	public AsyncClientHttpRequest prepare(AsyncClientHttpRequestFactory requestFactory) throws IOException {
		AsyncClientHttpRequest request = requestFactory.createRequest(URI.create(this.uri), this.method);
		populateHeaders(request);
		return request;
	}

	protected void populateHeaders(AsyncClientHttpRequest request) {
		request.getHeaders().set("Accept-Encoding", "gzip");
	}
}
