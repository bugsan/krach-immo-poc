package fr.krachimmo.core.scrap;

import org.springframework.http.client.ClientHttpResponse;

/**
 *
 * @author Sébastien Chatel
 * @since 21 October 2013
 */
public interface ClientHttpResponseExtractor<T> {

	T extract(ClientHttpResponse response) throws Exception;
}
