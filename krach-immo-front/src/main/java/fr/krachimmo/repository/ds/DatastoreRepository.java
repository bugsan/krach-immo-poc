package fr.krachimmo.repository.ds;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

import fr.krachimmo.repository.Quote;
import fr.krachimmo.repository.Repository;

/**
 *
 * @author Sébastien Chatel
 * @since 6 July 2014
 */
@Component
public class DatastoreRepository implements Repository {

	@Autowired
	DatastoreService datastoreService;

	@Override
	public String findLatestDataLocation() {
		try {
			Entity latest = this.datastoreService.get(KeyFactory.createKey("LatestData", 1L));
			return (String) latest.getProperty("location");
		}
		catch (EntityNotFoundException ex) {
			return null;
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public List<Quote> findAllQuotesFromDate(Date fromDate) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		df.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
		String dateToUse = df.format(fromDate);
		PreparedQuery query = this.datastoreService.prepare(
				new Query("Quote")
				.addFilter("__key__", FilterOperator.GREATER_THAN_OR_EQUAL,
						KeyFactory.createKey("Quote", dateToUse))
				.addSort("__key__"));
		List<Quote> quotes = new ArrayList<Quote>();
		for (Entity entity : query.asIterable()) {
			Quote quote = new Quote();
			quote.setDate(entity.getKey().getName());
			quote.setPriceStudio(((Number) entity.getProperty("studio")).intValue());
			quote.setPrice2Pieces(((Number) entity.getProperty("2pieces")).intValue());
			quote.setPrice3Pieces(((Number) entity.getProperty("3pieces")).intValue());
			quote.setPrice4PlusPieces(((Number) entity.getProperty("4+pieces")).intValue());
			quote.setPriceTotal(((Number) entity.getProperty("total")).intValue());
			quotes.add(quote);
		}
		return quotes;
	}
}
