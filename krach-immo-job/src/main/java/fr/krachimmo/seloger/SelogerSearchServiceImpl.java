package fr.krachimmo.seloger;

import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import fr.krachimmo.core.scrap.DocumentMapper;
import fr.krachimmo.core.scrap.ScrapOperations;

/**
 *
 * @author Sébastien Chatel
 * @since 29 June 2014
 */
@Component
public class SelogerSearchServiceImpl implements SelogerSearchService {

	@Autowired
	ScrapOperations scrap;

	DocumentMapper<AnnonceSearchResults> documentMapper = new AnnonceSearchResultsDocumentMapper();

	@Override
	public Future<AnnonceSearchResults> search(String uri) {
		HttpHeaders headers = new HttpHeaders();
		populateCommonsHeaders(headers);
		return this.scrap.scrapForObject(uri, headers, this.documentMapper);
	}

	private void populateCommonsHeaders(HttpHeaders headers) {
		headers.set("Accept-Encoding", "gzip");
		headers.set("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		headers.set("Accept-Language", "fr-fr,fr;q=0.8,en-us;q=0.5,en;q=0.3");
		headers.set("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:30.0) Gecko/20100101 Firefox/30.0");
	}
}
