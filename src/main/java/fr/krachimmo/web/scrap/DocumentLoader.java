package fr.krachimmo.web.scrap;

import java.io.InputStream;

import org.w3c.dom.Document;

public interface DocumentLoader {

	Document loadDocument(InputStream inputStream, String charset) throws Exception;
}
