package fr.krachimmo.web.scrap;

import static fr.krachimmo.web.scrap.XmlUtils.xpath;

import java.net.URI;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;

/**
 *
 * @author Sébastien Chatel
 * @since 07 January 2014
 */
public class SelogerUriItemReader implements ItemStreamReader<URI> {

	private static final Log log = LogFactory.getLog(SelogerUriItemReader.class);

	private static final String SEARCH_URL = "http://www.seloger.com/recherche.htm?";

	private static final int ANNONCES_PER_PAGE = 10;

	private static final int MAX_PAGES = 200;

	private int currentPage;

	private int lastPage = -1;

	private int minPrice;

	private int maxPrice;

	private String query = "";

	private ScrapOperations scrapTemplate = new ScrapTemplate();

	public void setQuery(String query) {
		this.query = query;
	}

	public void setScrapTemplate(ScrapOperations scrapOperations) {
		this.scrapTemplate = scrapOperations;
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		if (executionContext.containsKey("seloger.currentPage")) {
			this.currentPage = executionContext.getInt("seloger.currentPage");
			this.lastPage = executionContext.getInt("seloger.lastPage");
			this.minPrice = executionContext.getInt("seloger.minPrice");
			this.maxPrice = executionContext.getInt("seloger.maxPrice");
		}
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		executionContext.putInt("seloger.currentPage", this.currentPage);
		executionContext.putInt("seloger.lastPage", this.lastPage);
		executionContext.putInt("seloger.minPrice", this.minPrice);
		executionContext.putInt("seloger.maxPrice", this.maxPrice);
	}

	@Override
	public void close() throws ItemStreamException {
	}

	@Override
	public URI read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		if (this.lastPage == -1) {
			updateLastPage();
		}
		if (this.currentPage == this.lastPage && this.lastPage == MAX_PAGES) {
			this.minPrice = this.maxPrice + 1;
			updateLastPage();
			this.currentPage = 0;
		}
		if (this.currentPage == this.lastPage && this.lastPage < MAX_PAGES) {
			return null;
		}

		this.currentPage++;
		return getNextRequest();
	}

	protected void updateLastPage() {
		this.lastPage = Math.min(MAX_PAGES, fetchPageCount(this.query, this.minPrice));
		if (this.lastPage == MAX_PAGES) {
			this.maxPrice = fetchMaxPrice(this.query, this.minPrice, this.maxPrice) - 1;
		}
		else {
			this.maxPrice = 0;
		}
		log.info("Update range [lastPage=" + this.lastPage
				+ ", minPrice=" + this.minPrice + ", maxPrice=" + this.maxPrice + "]");
	}

	protected int fetchPageCount(String query, int minPrice) {
		int annonceCount = this.scrapTemplate.getObject(
				SEARCH_URL + query + "&tri=a_px" + "&pxmin=" + minPrice,
				new CountPageHtmlDocumentMapper());
		return (int) Math.ceil((double) annonceCount / ANNONCES_PER_PAGE);
	}

	protected Integer fetchMaxPrice(String query, int minPrice, int maxPrice) {
		return this.scrapTemplate.getObject(
				SEARCH_URL + query + "&tri=a_px"
					+ (minPrice > 0 ? ("&pxmin=" + minPrice) : "")
					+ "&ANNONCEpg=" + MAX_PAGES,
				new MaxPriceHtmlDocumentMapper());
	}

	protected URI getNextRequest() {
		return URI.create(
				"http://www.seloger.com/recherche.htm?" + this.query + "&tri=a_px"
						+ (this.minPrice > 0 ? ("&pxmin=" + this.minPrice) : "")
						+ (this.maxPrice > 0 ? ("&pxmax=" + this.maxPrice) : "")
						+ "&ANNONCEpg=" + this.currentPage
				);
	}
}
//class SimpleHttpRequest implements HttpRequest {
//	private final URI uri;
//	public SimpleHttpRequest(URI uri) {
//		this.uri = uri;
//	}
//	@Override
//	public HttpHeaders getHeaders() {
//		return null;
//	}
//	@Override
//	public HttpMethod getMethod() {
//		return null;
//	}
//	@Override
//	public URI getURI() {
//		return this.uri;
//	}
//}
class CountPageHtmlDocumentMapper implements DocumentMapper<Integer> {
	XPathExpression count = xpath("//span[@class='title_nbresult']/text()");
	@Override
	public Integer map(Document document) throws Exception {
		return parseCount((String) this.count.evaluate(document, XPathConstants.STRING));
	}
	protected int parseCount(String value) {
		return StringUtils.hasText(value) ? Integer.parseInt(value.trim().replaceAll("&nbsp;", "")) : 0;
	}
}
class MaxPriceHtmlDocumentMapper implements DocumentMapper<Integer> {
	XPathExpression highestPrice = xpath(
			"(/html/body/div/div/section[@class='liste_resultat l-grid__cell75']/article[@class='annonce'])" +
			"[last()]/div[3]/span[1]/text()");
	@Override
	public Integer map(Document document) throws Exception {
		return parsePrice((String) this.highestPrice.evaluate(document, XPathConstants.STRING));
	}
	protected Integer parsePrice(String value) {
		if (!StringUtils.hasText(value)) {
			return null;
		}
		value = value.trim().replaceAll("&nbsp;", "");
		value = value.substring(0, value.indexOf(' '));
		return Integer.parseInt(value);
	}
}