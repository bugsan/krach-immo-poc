package fr.krachimmo.item;

import static fr.krachimmo.web.scrap.XmlUtils.xpath;

import java.net.URI;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemProcessor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.krachimmo.web.scrap.DocumentMapper;
import fr.krachimmo.web.scrap.ScrapOperations;
import fr.krachimmo.web.scrap.ScrapTemplate;

/**
 *
 * @author Sébastien Chatel
 * @since 13 January 2014
 */
public class SearchUriItemProcessor implements ItemProcessor<URI, ResultPage> {

	private static final Log log = LogFactory.getLog(SearchUriItemProcessor.class);

	private ScrapOperations scrapTemplate = new ScrapTemplate();

	public void setScrapTemplate(ScrapOperations scrapTemplate) {
		this.scrapTemplate = scrapTemplate;
	}

	@Override
	public ResultPage process(URI item) throws Exception {
		if (log.isInfoEnabled()) {
			log.info("Fetching " + item);
		}
		return this.scrapTemplate.getObject(item.toString(), new ResultPageHtmlDocumentMapper());
	}
}
class ResultPageHtmlDocumentMapper implements DocumentMapper<ResultPage> {
	XPathExpression annoncesExpr = xpath("/html/body/div/div/section[@class='liste_resultat l-grid__cell75']/article[@class='annonce']");
	XPathExpression priceExpr = xpath("div[3]/span[1]/text()");
	XPathExpression surfaceExpr = xpath("div[2]/a/span/text()");
	@Override
	public ResultPage map(Document document) throws Exception {
		ResultPage resultPage = new ResultPage();
		NodeList nodeList = (NodeList) this.annoncesExpr.evaluate(document, XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			Integer surface = parseSurface((String) this.surfaceExpr.evaluate(node, XPathConstants.STRING));
			if (surface != null) {
				Annonce annonce = new Annonce();
				annonce.setSurface(surface);
				annonce.setPrice(parsePrice((String) this.priceExpr.evaluate(node, XPathConstants.STRING)));
				resultPage.addAnnonce(annonce);
			}
		}
		return resultPage;
	}
	protected Integer parsePrice(String value) {
		value = value.trim().replaceAll("&nbsp;", "");
		value = value.substring(0, value.indexOf(' '));
		return Integer.parseInt(value);
	}
	protected Integer parseSurface(String value) {
		value = value.trim().replaceAll(" ", "");;
		int beg = value.indexOf(',');
		int end = value.indexOf('m', beg);
		if (beg < 0 || end < 0) {
			return null;
		}
		try {
			return Integer.parseInt(value.substring(beg + 1, end));
		}
		catch (NumberFormatException ex) {
			return null;
		}
	}
}