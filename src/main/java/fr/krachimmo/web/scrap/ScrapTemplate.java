package fr.krachimmo.web.scrap;

import java.net.URI;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

public class ScrapTemplate implements ScrapOperations {

	private ClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();

	private DocumentLoader documentLoader = new HtmlDocumentLoader();

	@Override
	public <T> T getObject(String url, DocumentMapper<T> mapper) {
		ClientHttpResponse response = null;
		try {
			ClientHttpRequest request = this.clientHttpRequestFactory.createRequest(URI.create(url), HttpMethod.GET);
			request.getHeaders().add("Accept-Encoding", "gzip");
			response = request.execute();
			if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
				return null;
			}
			if (response.getStatusCode() != HttpStatus.OK) {
				throw new IllegalStateException("Invalid http status: " + response.getStatusText());
			}
			return mapper.map(
					this.documentLoader.loadDocument(
							HttpUtils.getInputStream(response),
							HttpUtils.getCharset(response)));
		}
		catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
		finally {
			if (response != null) {
				response.close();
			}
		}
	}
}
