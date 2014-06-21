package fr.krachimmo.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import fr.krachimmo.core.html.DocumentLoader;
import fr.krachimmo.core.html.cleaner.HtmlCleanerDocumentLoader;
import fr.krachimmo.core.scrap.DocumentMapper;

/**
 *
 * @author Sébastien Chatel
 * @since 24 October 2013
 */
public class AnnonceSearchResultsDocumentMapperTest {

	private DocumentMapper<AnnonceSearchResults> documentMapper = new AnnonceSearchResultsDocumentMapper();

	private DocumentLoader documentLoader = new HtmlCleanerDocumentLoader();

	@Test
	public void parseAnnonces() throws Exception {
		Number[][] expected = new Number[][] {
				{86720249l, 6d, 44000, 1}, // 6m² pour 44000€ et 1 pièce
				{88568119l, 6d, 44000, 1},
				{89002299l, 6.48d, 44800, 1},
				{88169703l, 7d, 44500, 1},
				{86908799l, 6.36d, 45000, 1},
				{86288199l, 5d, 45000, 1},
				{87543623l, 7d, 45000, 1},
				{85282181l, 6d, 45000, 1},
				{89817785l, 5.21d, 45000, 1}};

		AnnonceSearchResults results =
				this.documentMapper.mapDocument(
						this.documentLoader.loadDocument(
								new ClassPathInputSource("/seloger.html", "utf-8")));
		assertNotNull(results);
		List<Annonce> annonces = results.getAnnonces();
		assertEquals(expected.length, annonces.size());
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i][0], annonces.get(i).getId());
			assertEquals(expected[i][1].doubleValue(), annonces.get(i).getSuperficie(), 0);
			assertEquals(expected[i][2].intValue(), annonces.get(i).getPrix());
			assertEquals(expected[i][3].intValue(), annonces.get(i).getPieces());
		}
	}
}
