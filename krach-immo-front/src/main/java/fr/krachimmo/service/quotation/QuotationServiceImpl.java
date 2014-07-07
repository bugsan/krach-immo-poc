package fr.krachimmo.service.quotation;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;

/**
 *
 * @author Sébastien Chatel
 * @since 6 July 2014
 */
@Service
public class QuotationServiceImpl implements QuotationService {
	private static final Log log = LogFactory.getLog(QuotationServiceImpl.class);
	@Autowired
	URLFetchService urlFetchService;
	@Autowired
	DatastoreService datastoreService;

	final DocumentBuilder documentBuilder;
	final XPath xpath;
	final XPathExpression prices;
	final XPathExpression surfaces;

	public QuotationServiceImpl() throws Exception {
		documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		xpath = XPathFactory.newInstance().newXPath();
		prices = xpath.compile("/recherche/annonces/annonce[prix>0 and surface>0]/prix/text()");
		surfaces = xpath.compile("/recherche/annonces/annonce[prix>0 and surface>0]/surface/text()");
	}

	@Override
	public void emitQuote() throws Exception {
		log.info("Emitting new quote");
		Date now = new Date();
		long pricePerMeterCumul = 0;
		long annonceTotalCount = 0;
		for (int page = 1; page <= 4; page++) {
			log.info("Fetching page " + page);
			HTTPRequest request = new HTTPRequest(new URL(
					"http://ws.seloger.com/search.xml" +
					"?cp=75&idtt=2&idtypebien=1&tri=d_dt_crea&SEARCHpg=" + page));
			request.setHeader(new HTTPHeader("Accept-Encoding", "gzip"));
			request.getFetchOptions().setDeadline(20d);
			HTTPResponse response = urlFetchService.fetch(request);
			InputStream in = new GZIPInputStream(new ByteArrayInputStream(response.getContent()));
			Document document = documentBuilder.parse(in);
			NodeList priceNodes = (NodeList) prices.evaluate(document, XPathConstants.NODESET);
			NodeList surfaceNodes = (NodeList) surfaces.evaluate(document, XPathConstants.NODESET);
			for (int i = 0; i < priceNodes.getLength(); i++) {
				pricePerMeterCumul +=
						Integer.parseInt(priceNodes.item(i).getTextContent()) /
						Double.parseDouble(surfaceNodes.item(i).getTextContent());
				annonceTotalCount++;
			}
			in.close();
		}
		int quote = (int) (pricePerMeterCumul / annonceTotalCount);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
		Entity entity = new Entity("Quote", sdf.format(now));
		entity.setProperty("value", quote);
		datastoreService.put(entity);
		log.info("Emitted new quote " + quote + " from " + annonceTotalCount + " annonces");
	}
}
