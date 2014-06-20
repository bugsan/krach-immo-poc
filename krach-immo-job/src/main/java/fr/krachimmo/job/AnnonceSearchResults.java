package fr.krachimmo.job;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente une page de résultats de recherche SeLoger.
 * @author Sébastien Chatel
 * @since 24 October 2013
 */
public class AnnonceSearchResults {

	private final List<Annonce> annonces = new ArrayList<Annonce>();

	public List<Annonce> getAnnonces() {
		return annonces;
	}
	public void addAnnonce(Annonce annonce) {
		this.annonces.add(annonce);
	}
	@Override
	public String toString() {
		return "AnnonceSearchResults [annonces=" + annonces + "]";
	}
}
