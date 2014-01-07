package fr.krachimmo.web.scrap;

import java.io.InputStream;

import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;

public class HtmlDocumentLoader implements DocumentLoader {

	@Override
	public Document loadDocument(InputStream inputStream, String charset) throws Exception {
		HtmlCleaner cleaner = new HtmlCleaner();
		cleaner.getProperties().setNamespacesAware(false);
		TagNode rootNode = (charset != null) ?
				cleaner.clean(inputStream, charset) :
				cleaner.clean(inputStream);
		return new DomSerializer(cleaner.getProperties()).createDOM(rootNode);
	}
}
