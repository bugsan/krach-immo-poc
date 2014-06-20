package fr.krachimmo.core.http.client;

import java.io.IOException;
import java.net.URI;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequestFactory;

/**
 *
 * @author Sébastien Chatel
 * @since 10 May 2013
 */
public interface AsyncClientHttpRequestFactory extends ClientHttpRequestFactory {

	@Override
	public AsyncClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException;
}
