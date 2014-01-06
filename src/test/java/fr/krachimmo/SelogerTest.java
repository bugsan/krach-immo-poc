package fr.krachimmo;

import static fr.krachimmo.web.scrap.XmlUtils.xpath;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;

import org.junit.Test;
import org.w3c.dom.Document;

import fr.krachimmo.web.scrap.DocumentMapper;
import fr.krachimmo.web.scrap.ScrapTemplate;

public class SelogerTest {

	@Test
	public void list() {
		ScrapTemplate scrapTemplate = new ScrapTemplate();
		Integer priceMin = 0;
		System.out.println("priceMin: " + priceMin);
		while (priceMin != null) {
			HighestPrice highestPrice = scrapTemplate.getObject(
				"http://www.seloger.com/recherche.htm?idtypebien=1&cp=75&idtt=2&tri=a_px&ANNONCEpg=200" +
					"&pxmin=" + priceMin,
//				"http://ws.seloger.com/search.xml?idtypebien=1&cp=75&idtt=2&tri=a_px&SEARCHpg=4",
				new HighestPriceHtmlDocumentMapper());
			priceMin = (highestPrice != null ? highestPrice.getValue() : null);
			System.out.println("priceMin: " + priceMin);
		}
	}
}
//class HighestPriceXmlDocumentMapper implements DocumentMapper<HighestPrice> {
//	XPathExpression highestPrice = xpath("/recherche/annonces/annonce[last()]/prix");
//	XPathExpression count = xpath("count(/recherche/annonces/annonce)");
//	@Override
//	public HighestPrice map(Document document) throws Exception {
//		System.out.println(((Number) count.evaluate(document, XPathConstants.NUMBER)).intValue());
//		Number hp = (Number) this.highestPrice.evaluate(document, XPathConstants.NUMBER);
//		return hp == null ? null : new HighestPrice(hp.intValue());
//	}
//}
class HighestPriceHtmlDocumentMapper implements DocumentMapper<HighestPrice> {
	XPathExpression highestPrice = xpath("/html/body/div/div/section[@class='liste_resultat l-grid__cell75']" +
			"/article[last()]/div[3]/span[1]/text()");
	@Override
	public HighestPrice map(Document document) throws Exception {
		String hp = (String) this.highestPrice.evaluate(document, XPathConstants.STRING);
		return (hp == null || hp.length() == 0) ? null : new HighestPrice(parsePrice(hp));
	}
	protected int parsePrice(String value) {
		value = value.trim().replaceAll("&nbsp;", "");
		value = value.substring(0, value.indexOf(' '));
		return Integer.parseInt(value);
	}
}
