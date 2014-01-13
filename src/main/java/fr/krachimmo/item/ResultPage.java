package fr.krachimmo.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Sébastien Chatel
 * @since 13 January 2014
 */
public class ResultPage {

	private List<Annonce> annonces = new ArrayList<Annonce>();

	public void addAnnonce(Annonce annonce) {
		this.annonces.add(annonce);
	}
	public List<Annonce> getAnnonces() {
		return Collections.unmodifiableList(this.annonces);
	}
}
