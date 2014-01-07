package fr.krachimmo.web.scrap;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.springframework.http.client.ClientHttpResponse;

public final class HttpUtils {
	private HttpUtils() {
	}

	public static String getCharset(ClientHttpResponse response) {
		String charsetToUse = "iso-8859-1";
		List<String> contentTypes = response.getHeaders().get("Content-Type");
		if (contentTypes != null && contentTypes.size() > 0) {
			String contentType = contentTypes.get(0);
			int pos = contentType.indexOf("charset");
			if (pos != -1) {
				charsetToUse = contentType.substring(pos + 8);
			}
		}
		return charsetToUse;
	}

	public static InputStream getInputStream(ClientHttpResponse response) throws IOException {
		return isCompressedResponse(response) ?
				new GZIPInputStream(response.getBody()) :
				response.getBody();
	}

	public static boolean isCompressedResponse(ClientHttpResponse response) {
		List<String> contentEncodings = response.getHeaders().get("Content-Encoding");
		if (contentEncodings != null && contentEncodings.size() > 0) {
			return contentEncodings.get(0).equals("gzip");
		}
		return false;
	}
}
