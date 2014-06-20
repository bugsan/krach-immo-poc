package fr.krachimmo.job;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.krachimmo.core.scrap.DocumentMapper;
import static fr.krachimmo.core.scrap.DocumentMapperUtils.*;

/**
 *
 * @author Sébastien Chatel
 * @since 24 October 2013
 */
public class AnnonceSearchResultsDocumentMapper implements DocumentMapper<AnnonceSearchResults> {

	private static final NumberFormat numberFormat = new DecimalFormat("0000000,00");
	private static final XPathExpression itemsExpr = xpath(
			"//div[@class='rech_headerann']");
//	private static final XPathExpression id = xpath(
//			"//span[@class='mea1']/a");
	private static final XPathExpression titreExpr = xpath(
			"div/span[@class='mea1']/a");
	private static final XPathExpression prixExpr = xpath(
			"div/div[@class='rech_box_prix']/span[@class='mea2']");

	@Override
	public AnnonceSearchResults mapDocument(Node document) throws Exception {
		AnnonceSearchResults results = new AnnonceSearchResults();
		NodeList annonceNodes = (NodeList) itemsExpr.evaluate(document, XPathConstants.NODESET);
		for (int i = 0; i < annonceNodes.getLength(); i++) {
			Node annonceNode = annonceNodes.item(i);
			String titre = titreExpr.evaluate(annonceNode);
			int pieces = parsePieces(titre);
			Number superficie = parseSuperficie(titre);
			Number prix = parsePrix(prixExpr.evaluate(annonceNode));
			if (superficie != null && prix != null) {
				Annonce annonce = new Annonce();
				annonce.setSuperficie(superficie.doubleValue());
				annonce.setPrix(prix.doubleValue());
				annonce.setPieces(pieces);
				results.addAnnonce(annonce);
			}
		}
		return results;
	}

	private Number parseSuperficie(String value) throws ParseException {
		String valueToUse = value.trim();
		int m2pos = valueToUse.indexOf("m²");
		if (m2pos == -1) {
			return null;
		}
		int start = valueToUse.substring(0, m2pos - 2).lastIndexOf(' ') + 1;
		int end = m2pos - 1;
		return numberFormat.parse(valueToUse.substring(start, end));
	}

	private Number parsePrix(String value) throws ParseException {
		String valueToUse = value.replaceAll("[\\s\\xa0]", "");
		return numberFormat.parse(valueToUse.substring(0, valueToUse.indexOf('€')));
	}

	private int parsePieces(String value) {
		if (value.contains("Studio")) {
			return 1;
		}
		else if (value.contains("pièces")) {
			String part = value.substring(0, value.indexOf("pièces") - 1);
			return Integer.parseInt(part.substring(part.lastIndexOf(" ") + 1));
		}
		else {
			return 0;
		}
	}
}
