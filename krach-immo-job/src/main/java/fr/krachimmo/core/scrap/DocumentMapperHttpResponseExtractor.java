package fr.krachimmo.core.scrap;

import java.io.IOException;

import org.springframework.http.client.ClientHttpResponse;
import org.xml.sax.InputSource;

import fr.krachimmo.core.html.DocumentLoader;

/**
 *
 * @author Sébastien Chatel
 * @since 25 October 2013
 */
public class DocumentMapperHttpResponseExtractor<T> extends ClientHttpResponseExtractorSupport<T> {

	private final DocumentLoader documentLoader;

	private final DocumentMapper<T> documentMapper;

	public DocumentMapperHttpResponseExtractor(DocumentLoader documentLoader, DocumentMapper<T> documentMapper) {
		this.documentLoader = documentLoader;
		this.documentMapper = documentMapper;
	}

	@Override
	public T extract(ClientHttpResponse response) throws Exception {
		return this.documentMapper.mapDocument(this.documentLoader.loadDocument(getInputSource(response)));
	}

	private InputSource getInputSource(ClientHttpResponse response) throws IOException {
		InputSource inputSource = new InputSource();
		inputSource.setByteStream(getResponseInputStream(response));
		inputSource.setEncoding(getResponseCharset(response));
		return inputSource;
	}
}
