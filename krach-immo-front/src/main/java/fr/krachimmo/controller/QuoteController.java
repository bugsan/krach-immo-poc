package fr.krachimmo.controller;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fr.krachimmo.repository.Quote;
import fr.krachimmo.repository.Repository;

/**
 *
 * @author Sébastien Chatel
 * @since 6 July 2014
 */
@Controller
public class QuoteController {

	@Autowired
	Repository repository;

	@RequestMapping("/api/quotes")
	public ResponseEntity<String> findLatestQuotes(@RequestParam(defaultValue="30") int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -days);
		List<Quote> quotes = this.repository.findAllQuotesFromDate(calendar.getTime());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setCacheControl("public, max-age=3600");
		return new ResponseEntity<String>(toJsonQuotes(quotes), headers, HttpStatus.OK);
	}

	private String toJsonQuotes(List<Quote> quotes) {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"quotes\":[");
		boolean first = true;
		for (Quote quote : quotes) {
			if (!first) {
				sb.append(',');
			}
			sb.append('{')
				.append("\"date\":\"").append(quote.getDate()).append("\",")
				.append("\"prices\":{")
				.append("\"studio\":").append(quote.getPriceStudio()).append(",")
				.append("\"deux\":").append(quote.getPrice2Pieces()).append(",")
				.append("\"trois\":").append(quote.getPrice3Pieces()).append(",")
				.append("\"more\":").append(quote.getPrice4PlusPieces()).append(",")
				.append("\"total\":").append(quote.getPriceTotal())
				.append("}}");
			first = false;
		}
		sb.append("]}");
		return sb.toString();
	}
}
