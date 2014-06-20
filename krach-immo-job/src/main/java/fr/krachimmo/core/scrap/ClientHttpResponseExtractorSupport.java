package fr.krachimmo.core.scrap;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.springframework.http.client.ClientHttpResponse;

/**
 *
 * @author Sébastien Chatel
 * @since 25 October 2013
 */
public abstract class ClientHttpResponseExtractorSupport<T> implements ClientHttpResponseExtractor<T> {

	private static final String DEFAULT_HTTP_CHARSET = "iso-8859-1";

	private static final String CONTENT_TYPE_HEADER = "Content-Type";

	private static final String CONTENT_ENCODING_HEADER = "Content-Encoding";

	private static final String GZIP_ENCODING = "gzip";

	protected InputStream getResponseInputStream(ClientHttpResponse response) throws IOException {
		InputStream inputStreamToUse = response.getBody();
		return isCompressedResponse(response) ? new GZIPInputStream(inputStreamToUse) : inputStreamToUse;
	}

	protected boolean isCompressedResponse(ClientHttpResponse response) {
		String contentEncoding = response.getHeaders().getFirst(CONTENT_ENCODING_HEADER);
		return contentEncoding != null && contentEncoding.equals(GZIP_ENCODING);
	}

	protected String getResponseCharset(ClientHttpResponse response) {
		String charsetToUse = DEFAULT_HTTP_CHARSET;
		String contentType = response.getHeaders().getFirst(CONTENT_TYPE_HEADER);
		if (contentType != null) {
			int charsetPos = contentType.indexOf("charset=");
			if (charsetPos >= 0) {
				charsetToUse = contentType.substring(charsetPos + 8);
			}
		}
		return charsetToUse;
	}
}
