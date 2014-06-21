package fr.krachimmo.job;

import org.xml.sax.InputSource;

/**
 * 
 * @author Sébastien Chatel
 * @since 24 October 2013
 */
public class ClassPathInputSource extends InputSource {

	public ClassPathInputSource(String path) {
		this(path, null);
	}

	public ClassPathInputSource(String path, String encoding) {
		setByteStream(getClass().getResourceAsStream(path));
		if (encoding != null) {
			setEncoding(encoding);
		}
	}
}
