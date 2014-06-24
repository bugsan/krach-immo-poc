package fr.krachimmo.job;

import static fr.krachimmo.core.scrap.DocumentMapperUtils.xpath;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.krachimmo.core.scrap.DocumentMapper;

/**
 * Note: Le format décimal français doit être utilisé pour parser les superficies.
 * Il faut le déclarer explicitement car ce code pourrait être exécuté sur une
 * machine non française.
 * @author Sébastien Chatel
 * @since 24 October 2013
 */
public class AnnonceSearchResultsDocumentMapper implements DocumentMapper<AnnonceSearchResults> {

	private static final NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.FRANCE);

	private static final XPathExpression itemsExpr = xpath(
			"//div[@class='listing_infos']");
//	private static final XPathExpression id = xpath(
//			"//span[@class='mea1']/a");
	private static final XPathExpression titreExpr = xpath(
			"h2[1]/a[1]");
	private static final XPathExpression prixExpr = xpath(
			"div[1]/a[@class='amount']");
	private static final XPathExpression superficieExpr = xpath(
			"ul[@class='property_list']/li[contains(text(),' m²')]");

	@Override
	public AnnonceSearchResults mapDocument(Node document) throws Exception {
		AnnonceSearchResults results = new AnnonceSearchResults();
		NodeList annonceNodes = (NodeList) itemsExpr.evaluate(document, XPathConstants.NODESET);
		for (int i = 0; i < annonceNodes.getLength(); i++) {
			Annonce annonce = parseAnnonce(annonceNodes.item(i));
			if (annonce != null) {
				results.addAnnonce(annonce);
			}
		}
		return results;
	}

	private Annonce parseAnnonce(Node node) throws Exception {
		Node titreNode = (Node) titreExpr.evaluate(node, XPathConstants.NODE);
		String href = ((Element) titreNode).getAttribute("href");
		if (href.startsWith("http://www.selogerneuf.com")) {
			return null;
		}
		long id = parseId(href);
		Number pieces = parsePieces(titreNode.getTextContent());
		Number superficie = parseSuperficie(superficieExpr.evaluate(node));
		Number prix = parsePrix(prixExpr.evaluate(node));
		if (superficie == null || prix == null || pieces == null) {
			return null;
		}
		Annonce annonce = new Annonce();
		annonce.setId(id);
		annonce.setSuperficie(superficie.doubleValue());
		annonce.setPrix(prix.intValue());
		annonce.setPieces(pieces.intValue());
		return annonce;
	}

	private long parseId(String value) {
		int start = value.lastIndexOf('/');
		int end = value.indexOf('.', start);
		return Long.parseLong(value.substring(start + 1, end));
	}

	private Number parseSuperficie(String value) throws ParseException {
		if (value == null) {
			return null;
		}
		String valueToUse = value.trim();
		int m2pos = valueToUse.indexOf(" m²");
		if (m2pos == -1) {
			return null;
		}
		return numberFormat.parse(valueToUse.substring(0, m2pos));
	}

	private Number parsePrix(String value) throws ParseException {
		String valueToUse = value.replaceAll("[\\s\\xa0]|(&nbsp;)", "");
		return numberFormat.parse(valueToUse.substring(0, valueToUse.indexOf("€")));
	}

	private Number parsePieces(String value) {
		if (value.contains("Studio")) {
			return 1;
		}
		else if (value.contains("pièce")) {
			String part = value.substring(0, value.indexOf("pièce") - 1);
			return Integer.parseInt(part.substring(part.lastIndexOf(" ") + 1));
		}
		else {
			return null;
		}
	}
}
