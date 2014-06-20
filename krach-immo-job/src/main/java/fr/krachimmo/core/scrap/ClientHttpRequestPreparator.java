package fr.krachimmo.core.scrap;

import java.io.IOException;

import fr.krachimmo.core.http.client.AsyncClientHttpRequest;
import fr.krachimmo.core.http.client.AsyncClientHttpRequestFactory;

/**
 *
 * @author Sébastien Chatel
 * @since 21 October 2013
 */
public interface ClientHttpRequestPreparator {

	AsyncClientHttpRequest prepare(AsyncClientHttpRequestFactory factory) throws IOException;
}
