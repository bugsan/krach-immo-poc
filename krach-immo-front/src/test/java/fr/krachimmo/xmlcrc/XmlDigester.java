package fr.krachimmo.xmlcrc;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.codec.binary.Hex;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlDigester {

	private SAXParserFactory spf = SAXParserFactory.newInstance();

	private final String digestMethod;

	public XmlDigester() {
		this("md5");
	}

	public XmlDigester(String digestMethod) {
		this.digestMethod = digestMethod;
	}

	public String digest(String xml) {
		try {
			return digest(new StringReader(xml));
		}
		catch (IOException ex) {
			throw new IllegalStateException(ex);
		}
	}

	public String digest(Reader in) throws IOException {
		return digest(new InputSource(in));
	}

	public String digest(InputStream in) throws IOException {
		return digest(new InputSource(in));
	}

	protected String digest(InputSource in) throws IOException {
		try {
			ChecksumContentHandler handler = new ChecksumContentHandler(this.digestMethod);
			this.spf.newSAXParser().parse(in, handler);
			return handler.getHashString();
		}
		catch (ParserConfigurationException ex) {
			throw new IllegalStateException(ex);
		}
		catch (SAXException ex) {
			throw new IllegalStateException(ex);
		}
	}

	private static class ChecksumContentHandler extends DefaultHandler {
		private final MessageDigest messageDigest;
		private String hashString;
		public ChecksumContentHandler(String method) {
			try {
				this.messageDigest = MessageDigest.getInstance(method);
			}
			catch (NoSuchAlgorithmException ex) {
				throw new IllegalStateException(method + " non supporté", ex);
			}
		}
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			this.messageDigest.update("<".getBytes());
			this.messageDigest.update(localName.getBytes());
			if (attributes.getLength() > 0) {
				Set<String> attrs = new TreeSet<String>();
				for (int i = 0; i < attributes.getLength(); i++) {
					attrs.add(attributes.getQName(i) + "=" + attributes.getValue(i));
				}
				for (String attr : attrs) {
					this.messageDigest.update(attr.getBytes());
				}
			}
			this.messageDigest.update(">".getBytes());
		}
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			this.messageDigest.update("</".getBytes());
			this.messageDigest.update(localName.getBytes());
			this.messageDigest.update(">".getBytes());
		}
		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			String originalData = new String(ch, start, length);
			String trimedData = originalData.trim();
			if (trimedData.length() > 0) {
				this.messageDigest.update(originalData.getBytes());
			}
		}

		@Override
		public void endDocument() throws SAXException {
			this.hashString = Hex.encodeHexString(this.messageDigest.digest());
		}
		public String getHashString() {
			return this.hashString;
		}
	}
}
