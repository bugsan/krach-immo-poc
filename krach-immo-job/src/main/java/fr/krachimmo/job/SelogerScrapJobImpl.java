package fr.krachimmo.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.krachimmo.core.scrap.DocumentMapper;
import fr.krachimmo.core.scrap.ScrapOperations;

/**
 *
 * @author Sébastien Chatel
 * @since 20 June 2014
 */
@Component
public class SelogerScrapJobImpl implements SelogerScrapJob {

	@Autowired
	ScrapOperations scrap;

	DocumentMapper<AnnonceSearchResults> documentMapper = new AnnonceSearchResultsDocumentMapper();

	@Override
	public void execute() {
		this.scrap.scrapForObject("", this.documentMapper);
	}
}
