package fr.krachimmo.core.scrap;

import java.util.concurrent.Future;

import org.springframework.http.HttpHeaders;


/**
 *
 * @author Sébastien Chatel
 * @since 20 June 2014
 */
public interface ScrapOperations {

	<T> Future<T> scrapForObject(ClientHttpRequestPreparator preparator, ClientHttpResponseExtractor<T> extractor);
	<T> Future<T> scrapForObject(String uri, HttpHeaders headers, DocumentMapper<T> documentMapper);
	<T> Future<T> scrapForObject(String uri, DocumentMapper<T> documentMapper);
}
