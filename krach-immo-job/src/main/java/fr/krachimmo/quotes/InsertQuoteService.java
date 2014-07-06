package fr.krachimmo.quotes;

import java.io.IOException;

/**
 *
 * @author Sébastien Chatel
 * @since 6 July 2014
 */
public interface InsertQuoteService {

	void insertFromFile(String filename, String date) throws IOException;
}
