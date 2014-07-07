package fr.krachimmo.repository;

import java.util.Date;
import java.util.List;

/**
 *
 * @author S�bastien Chatel
 * @since 6 July 2014
 */
public interface Repository {

	String findLatestDataLocation();

	List<Quote> findAllQuotesFromDate(Date date);
}