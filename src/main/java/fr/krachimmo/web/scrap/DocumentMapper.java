package fr.krachimmo.web.scrap;

import org.w3c.dom.Document;

public interface DocumentMapper<T> {

	T map(Document document) throws Exception;
}
