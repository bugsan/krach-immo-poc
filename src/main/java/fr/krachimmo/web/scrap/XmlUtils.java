package fr.krachimmo.web.scrap;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public final class XmlUtils {

	private static XPath xpath = XPathFactory.newInstance().newXPath();

	private XmlUtils() {
	}

	public static XPathExpression xpath(String expr) {
		try {
			return xpath.compile(expr);
		}
		catch (XPathExpressionException ex) {
			throw new IllegalArgumentException("invalid xpath expression '" + expr + "'", ex);
		}
	}
}
