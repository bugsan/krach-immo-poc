package fr.krachimmo.core.html;

import java.io.IOException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 *
 * @author Sébastien Chatel
 * @since 06 November 2012
 */
public interface DocumentLoader {

	Document loadDocument(InputSource inputSource) throws IOException;
}
