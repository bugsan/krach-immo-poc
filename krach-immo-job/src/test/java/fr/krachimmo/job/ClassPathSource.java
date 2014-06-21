package fr.krachimmo.job;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.xml.transform.stream.StreamSource;

/**
 * 
 * @author Sébastien Chatel
 * @since 28 October 2013
 */
public class ClassPathSource extends StreamSource {

	public ClassPathSource(String path) {
		super(getResourceAsStream(path));
	}

	public ClassPathSource(String path, String encoding) {
		super(new InputStreamReader(
				getResourceAsStream(path),
				Charset.forName(encoding)));
	}

	private static InputStream getResourceAsStream(String path) {
		InputStream inputStream = ClassPathSource.class.getResourceAsStream(path);
		if (inputStream == null) {
			throw new IllegalArgumentException("Classpath resource not found: " + path);
		}
		return inputStream;
	}
}
