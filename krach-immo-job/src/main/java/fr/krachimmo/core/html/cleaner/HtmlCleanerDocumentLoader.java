package fr.krachimmo.core.html.cleaner;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import fr.krachimmo.core.html.DocumentLoader;

/**
 *
 * @author Sébastien Chatel
 * @since 06 November 2012
 */
public class HtmlCleanerDocumentLoader extends CleanerProperties implements DocumentLoader {

	private HtmlCleaner htmlCleaner;

	public void setHtmlCleaner(HtmlCleaner htmlCleaner) {
		this.htmlCleaner = htmlCleaner;
	}

	public HtmlCleaner getHtmlCleaner() {
		return this.htmlCleaner;
	}

	public HtmlCleanerDocumentLoader() {
		setNamespacesAware(false);
		this.htmlCleaner = new HtmlCleaner(this);
	}

	@Override
	public Document loadDocument(InputSource inputSource) throws IOException {
		return convertToDom(loadHtml(inputSource));
	}

	/**
	 * Parse la source HTML en utilisant HtmlCleaner afin d'obtenir un arbre de noeuds. HtmlCleaner
	 * va ignorer les erreurs du à la non conformité d'html 4 par rapport à xml.
	 * @param inputSource
	 * @return
	 * @throws IOException
	 */
	private TagNode loadHtml(InputSource inputSource) throws IOException {
		if (inputSource.getByteStream() != null) {
			if (inputSource.getEncoding() != null) {
				return this.htmlCleaner.clean(inputSource.getByteStream(), inputSource.getEncoding());
			}
			return this.htmlCleaner.clean(inputSource.getByteStream());
		}
		else if (inputSource.getCharacterStream() != null) {
			return this.htmlCleaner.clean(inputSource.getCharacterStream());
		}
		else {
			throw new IllegalArgumentException("Inputsource doesnt contains any stream");
		}
	}

	/**
	 * Convertit le node HtmlCleaner en Node W3C standard.
	 * @param node
	 * @return
	 */
	private Document convertToDom(TagNode node) {
		try {
			return new DomSerializer(this.htmlCleaner.getProperties()).createDOM(node);
		}
		catch (ParserConfigurationException ex) {
			throw new IllegalStateException("HtmlCleaner failed to start", ex);
		}
	}
}
