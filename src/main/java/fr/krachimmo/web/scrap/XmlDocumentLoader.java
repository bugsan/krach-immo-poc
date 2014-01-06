package fr.krachimmo.web.scrap;

import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;

public class XmlDocumentLoader implements DocumentLoader {

	private TransformerFactory transformerFactory = TransformerFactory.newInstance();

	@Override
	public Document loadDocument(InputStream inputStream, String charset) throws Exception {
		StreamSource source = charset != null ?
				new StreamSource(new InputStreamReader(inputStream, charset)) :
				new StreamSource(inputStream);
		DOMResult result = new DOMResult();
		this.transformerFactory.newTransformer().transform(source, result);
		return (Document) result.getNode();
	}
}
