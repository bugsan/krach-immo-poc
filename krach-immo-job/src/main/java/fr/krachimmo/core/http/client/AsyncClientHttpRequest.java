package fr.krachimmo.core.http.client;

import java.util.concurrent.Future;

import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;

/**
 *
 * @author S�bastien Chatel
 * @since 10 May 2013
 */
public interface AsyncClientHttpRequest extends ClientHttpRequest {

	Future<ClientHttpResponse> executeAsync();
}