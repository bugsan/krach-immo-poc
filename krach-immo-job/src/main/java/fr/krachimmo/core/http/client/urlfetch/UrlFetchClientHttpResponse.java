package fr.krachimmo.core.http.client.urlfetch;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPResponse;

/**
 *
 * @author Sébastien Chatel
 * @since 10 May 2013
 */
public class UrlFetchClientHttpResponse implements ClientHttpResponse {

	private final HTTPResponse response;

	private HttpHeaders headers;

	private HttpStatus httpStatus;

	public UrlFetchClientHttpResponse(HTTPResponse response) {
		this.response = response;
	}

	@Override
	public HttpStatus getStatusCode() throws IOException {
		if (this.httpStatus == null) {
			this.httpStatus = HttpStatus.valueOf(this.response.getResponseCode());
		}
		return this.httpStatus;
	}

	@Override
	public int getRawStatusCode() throws IOException {
		return this.response.getResponseCode();
	}

	@Override
	public String getStatusText() throws IOException {
		return getStatusCode().name();
	}

	@Override
	public HttpHeaders getHeaders() {
		if (this.headers == null) {
			this.headers = new HttpHeaders();
			for (HTTPHeader header : this.response.getHeadersUncombined()) {
				this.headers.add(header.getName(), header.getValue());
			}
		}
		return this.headers;
	}

	@Override
	public InputStream getBody() throws IOException {
		return new ByteArrayInputStream(this.response.getContent());
	}

	@Override
	public void close() {
	}
}
