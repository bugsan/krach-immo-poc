package fr.krachimmo.core.scrap;

import java.io.IOException;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.client.ClientHttpResponse;

import fr.krachimmo.core.http.client.AsyncClientHttpRequest;
import fr.krachimmo.core.http.client.AsyncClientHttpRequestFactory;

/**
 *
 * @author Sébastien Chatel
 * @since 25 October 2013
 */
public class ScrapUtils {

	private static final Log log = LogFactory.getLog(ScrapUtils.class);

	public static Future<ClientHttpResponse> executeRequest(ClientHttpRequestPreparator preparator, AsyncClientHttpRequestFactory requestFactory) {
		if (log.isDebugEnabled()) {
			log.debug("Executing Http request");
		}
		try {
			AsyncClientHttpRequest request = preparator.prepare(requestFactory);
			return request.executeAsync();
		}
		catch (IOException ex) {
			throw new IllegalStateException("Cannot execute http request", ex);
		}
	}

	public static <T> T extractResponse(ClientHttpResponse response, ClientHttpResponseExtractor<T> extractor) throws Exception {
		try {
			if (response.getRawStatusCode() >= 500) {
				throw new IllegalStateException("Invalid http status code " + response.getRawStatusCode());
			}
			else if (response.getRawStatusCode() >= 300) {
				return null;
			}
			return extractor.extract(response);
		}
		finally {
			if (log.isDebugEnabled()) {
				log.debug("Closing Http response");
			}
			try {
				response.getBody().close();
			}
			catch (IOException ex) {
				// ignore
			}
			response.close();
		}
	}
}
