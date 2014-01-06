package fr.krachimmo.web.scrap;

import java.io.IOException;

import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;

public interface HttpRequestCreator {

	ClientHttpRequest createRequest(ClientHttpRequestFactory factory) throws IOException;
}
