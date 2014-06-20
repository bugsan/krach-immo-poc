package fr.krachimmo.core.scrap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 *
 * @author Sébastien Chatel
 * @since 31 October 2012
 */
public class DocumentMapperUtils {

	private static final XPath xpath = XPathFactory.newInstance().newXPath();

	public static XPath newXPath(final String... namespaces) {
		XPath xpath = XPathFactory.newInstance().newXPath();
		if (namespaces.length > 0) {
			xpath.setNamespaceContext(new NamespaceContext() {
				private Map<String, String> prefixToURI = new HashMap<String, String>();
				private Map<String, String> uriToPrefix = new HashMap<String, String>();
				{
					for (String namespace : namespaces) {
						int equal = namespace.indexOf("=");
						String prefix = namespace.substring(0, equal);
						String uri = namespace.substring(equal + 1);
						this.prefixToURI.put(prefix, uri);
						this.uriToPrefix.put(uri, prefix);
					}
				}
				public Iterator<?> getPrefixes(String namespaceURI) {
					return this.prefixToURI.keySet().iterator();
				}
				public String getPrefix(String namespaceURI) {
					return this.uriToPrefix.get(namespaceURI);
				}
				public String getNamespaceURI(String prefix) {
					return this.prefixToURI.get(prefix);
				}
			});
		}
		return xpath;
	}

	public static List<String> nodeListToList(NodeList nodeList) {
		List<String> values = new ArrayList<String>(nodeList.getLength());
		for (int i = 0; i < nodeList.getLength(); i++) {
			values.add(nodeList.item(i).getTextContent());
		}
		return values;
	}

	public static XPathExpression xpath(String expression) {
		return xpath(xpath, expression);
	}

	public static XPathExpression xpath(XPath xpath, String expression) {
		try {
			return xpath.compile(expression);
		}
		catch (XPathExpressionException ex) {
			throw new IllegalStateException(ex);
		}
	}

	public static String evaluate(XPathExpression expression, Document document) {
		try {
			return expression.evaluate(document);
		}
		catch (XPathExpressionException ex) {
			throw new IllegalStateException(ex);
		}
	}

	public static Object evaluateNodes(XPathExpression expression, Document document) {
		try {
			return expression.evaluate(document, XPathConstants.NODESET);
		}
		catch (XPathExpressionException ex) {
			throw new IllegalStateException(ex);
		}
	}

	public static String between(String value, String before, String after) {
		int startPos = (before != null && before.length() > 0) ? value.indexOf(before) : -1;
		startPos = (startPos != -1) ? startPos + before.length() : 0;
		int endPos = (after != null && after.length() > 0) ? value.indexOf(after, startPos) : -1;
		endPos = (endPos != -1) ? endPos : value.length();
		return value.substring(startPos, endPos);
	}
}
