package fr.krachimmo.job;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sébastien Chatel
 * @since 20 June 2014
 */
public class SearchCriteria {

	private List<TypeBien> typeBiens = new ArrayList<TypeBien>();
	private List<String> communes = new ArrayList<String>();
	private Tri tri = Tri.Creation;

	public List<TypeBien> getTypeBiens() {
		return this.typeBiens;
	}
	public SearchCriteria typeBien(TypeBien typeBien) {
		this.typeBiens.add(typeBien);
		return this;
	}
	public List<String> getCommunes() {
		return this.communes;
	}
	public SearchCriteria commune(String commune) {
		this.communes.add(commune);
		return this;
	}
	public Tri getTri() {
		return this.tri;
	}
	public SearchCriteria tri(Tri tri) {
		this.tri = tri;
		return this;
	}
}
