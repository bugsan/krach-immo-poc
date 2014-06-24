package fr.krachimmo.core.scrap;

import static fr.krachimmo.core.scrap.ScrapUtils.executeRequest;
import static fr.krachimmo.core.scrap.ScrapUtils.extractResponse;

import java.util.concurrent.Future;

import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpResponse;

import com.google.appengine.api.utils.FutureWrapper;

import fr.krachimmo.core.html.DocumentLoader;
import fr.krachimmo.core.html.cleaner.HtmlCleanerDocumentLoader;
import fr.krachimmo.core.http.client.AsyncClientHttpRequestFactory;
import fr.krachimmo.core.http.client.urlfetch.UrlFetchClientHttpRequestFactory;

/**
 *
 * @author Sébastien Chatel
 * @since 20 June 2014
 */
public class ScrapTemplate implements ScrapOperations {

	private DocumentLoader documentLoader;

	private AsyncClientHttpRequestFactory clientHttpRequestFactory;

	public void setClientHttpRequestFactory(AsyncClientHttpRequestFactory clientHttpRequestFactory) {
		this.clientHttpRequestFactory = clientHttpRequestFactory;
	}

	public void setDocumentLoader(DocumentLoader documentLoader) {
		this.documentLoader = documentLoader;
	}

	public ScrapTemplate() {
		this(new UrlFetchClientHttpRequestFactory(), new HtmlCleanerDocumentLoader());
	}

	public ScrapTemplate(AsyncClientHttpRequestFactory clientHttpRequestFactory) {
		this(clientHttpRequestFactory, new HtmlCleanerDocumentLoader());
	}

	public ScrapTemplate(AsyncClientHttpRequestFactory clientHttpRequestFactory, DocumentLoader documentLoader) {
		this.clientHttpRequestFactory = clientHttpRequestFactory;
		this.documentLoader = documentLoader;
	}

	@Override
	public <T> Future<T> scrapForObject(ClientHttpRequestPreparator preparator, ClientHttpResponseExtractor<T> extractor) {
		return new FutureHttpResponseExtractor<T>(executeRequest(preparator, this.clientHttpRequestFactory), extractor);
	}

	@Override
	public <T> Future<T> scrapForObject(String uri, DocumentMapper<T> documentMapper) {
		return scrapForObject(uri, new HttpHeaders(), documentMapper);
	}

	@Override
	public <T> Future<T> scrapForObject(String uri, HttpHeaders headers, DocumentMapper<T> documentMapper) {
		return scrapForObject(new SimpleHttpRequestPreparator(uri, headers), new DocumentMapperHttpResponseExtractor<>(this.documentLoader, documentMapper));
	}

	private static class FutureHttpResponseExtractor<T> extends FutureWrapper<ClientHttpResponse, T> {
		private final ClientHttpResponseExtractor<T> extractor;
		public FutureHttpResponseExtractor(Future<ClientHttpResponse> response, ClientHttpResponseExtractor<T> extractor) {
			super(response);
			this.extractor = extractor;
		}
		@Override
		protected T wrap(ClientHttpResponse response) throws Exception {
			return extractResponse(response, this.extractor);
		}
		@Override
		protected Throwable convertException(Throwable cause) {
			return cause;
		}
	}
}
