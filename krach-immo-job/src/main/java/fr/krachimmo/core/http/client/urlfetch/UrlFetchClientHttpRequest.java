package fr.krachimmo.core.http.client.urlfetch;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.Future;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.utils.FutureWrapper;

import fr.krachimmo.core.http.client.AsyncClientHttpRequest;

/**
 *
 * @author Sébastien Chatel
 * @since 10 May 2013
 */
public class UrlFetchClientHttpRequest implements AsyncClientHttpRequest {

	private final UrlFetchClientHttpRequestFactory clientHttpRequestFactory;

	private final URLFetchService urlFetchService;

	private final URI uri;

	private final HttpMethod httpMethod;

	private final HttpHeaders headers = new HttpHeaders();

	private boolean executed = false;

	public UrlFetchClientHttpRequest(URI uri, HttpMethod httpMethod, UrlFetchClientHttpRequestFactory clientHttpRequestFactory) {
		this.uri = uri;
		this.httpMethod = httpMethod;
		this.clientHttpRequestFactory = clientHttpRequestFactory;
		this.urlFetchService = clientHttpRequestFactory.getUrlFetchService();
	}

	@Override
	public ClientHttpResponse execute() throws IOException {
		checkExecuted();
		HTTPRequest request = prepareHTTPRequest();
		HTTPResponse response = this.urlFetchService.fetch(request);
		this.executed = true;
		return new UrlFetchClientHttpResponse(response);
	}

	@Override
	public Future<ClientHttpResponse> executeAsync() {
		checkExecuted();
		final HTTPRequest request = prepareHTTPRequest();
//		printHeaders(request.getHeaders());
		Future<HTTPResponse> future = this.urlFetchService.fetchAsync(request);
		this.executed = true;
		return new FutureWrapper<HTTPResponse, ClientHttpResponse>(future) {
			@Override
			protected ClientHttpResponse wrap(HTTPResponse response) throws Exception {
				return new UrlFetchClientHttpResponse(response);
			}
			@Override
			protected Throwable convertException(Throwable cause) {
				return cause;
			}
		};
	}

	@Override
	public HttpMethod getMethod() {
		return this.httpMethod;
	}

	@Override
	public URI getURI() {
		return this.uri;
	}

	@Override
	public OutputStream getBody() throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpHeaders getHeaders() {
		return this.headers;
	}

	protected URL getUrl() {
		try {
			return this.uri.toURL();
		}
		catch (MalformedURLException ex) {
			throw new IllegalStateException("Could not get URL: " + ex.getMessage(), ex);
		}
	}

	private HTTPRequest prepareHTTPRequest() {
		HTTPRequest request = new HTTPRequest(getUrl(), HTTPMethod.valueOf(getMethod().name()));
		for (Entry<String, List<String>> e : getHeaders().entrySet()) {
			for (String value : e.getValue()) {
				request.addHeader(new HTTPHeader(e.getKey(), value));
			}
		}
		if (!this.clientHttpRequestFactory.validateCertificate) {
			request.getFetchOptions().doNotValidateCertificate();
		}
		request.getFetchOptions().setDeadline(this.clientHttpRequestFactory.deadline);
		return request;
	}

	private void checkExecuted() {
		Assert.state(!this.executed, "ClientHttpRequest already executed");
	}

	protected void printHeaders(List<HTTPHeader> headers) {
		for (HTTPHeader header : headers) {
			System.out.println(header.getName() + ": " + header.getValue());
		}
	}
}
