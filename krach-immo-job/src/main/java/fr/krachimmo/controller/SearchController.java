package fr.krachimmo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.krachimmo.job.AnnonceCsvLineAggregator;
import fr.krachimmo.job.LineAggregator;
import fr.krachimmo.seloger.Annonce;
import fr.krachimmo.seloger.AnnonceSearchResults;
import fr.krachimmo.seloger.SelogerSearchService;

/**
 *
 * @author Sébastien Chatel
 * @since 6 July 2014
 */
@Controller
public class SearchController {

	@Autowired
	SelogerSearchService selogerSearchService;
	LineAggregator<Annonce> lineAggregator = new AnnonceCsvLineAggregator();

	@RequestMapping("/search")
	public ResponseEntity<String> search(SearchQuery query) throws Exception {
		AnnonceSearchResults results = this.selogerSearchService.search(
				"http://www.seloger.com/list.htm?idtt=2" +
				(query.getCi() != null ? "&ci=" + query.getCi() : "") +
				"&idtypebien=" + query.getIdtypebien() +
				"&tri=" + query.getTri() +
				(query.getFraicheur() != null ? "&fraicheur=" + query.getFraicheur() : "") +
				"&LISTING-LISTpg=" + query.getPage())
				.get();
		StringBuilder sb = new StringBuilder();
		for (Annonce annonce : results.getAnnonces()) {
			sb.append(this.lineAggregator.aggregate(annonce));
			sb.append("\n");
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);
		return new ResponseEntity<String>(sb.toString(), headers, HttpStatus.OK);
	}
}
