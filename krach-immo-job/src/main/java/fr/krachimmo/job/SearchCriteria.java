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
	private Integer fraicheur;

	public SearchCriteria typeBien(TypeBien typeBien) {
		this.typeBiens.add(typeBien);
		return this;
	}
	public SearchCriteria commune(String commune) {
		this.communes.add(commune);
		return this;
	}
	public SearchCriteria tri(Tri tri) {
		this.tri = tri;
		return this;
	}
	public SearchCriteria fraicheur(int days) {
		this.fraicheur = days;
		return this;
	}

	public String buildUri(int page) {
		StringBuilder sb = new StringBuilder(256);
		sb.append("http://www.seloger.com/list.htm");
		sb.append("?idtt=2");
		sb.append("&ci=");
		appendChoices(sb, this.communes);
		sb.append("&idtypebien=");
		appendChoices(sb, this.typeBiens);
		sb.append("&tri=").append(this.tri);
		sb.append(this.fraicheur != null ? "&fraicheur=" + this.fraicheur : "");
		sb.append("&LISTING-LISTpg=").append(page);
		return sb.toString();
	}

	private void appendChoices(StringBuilder sb, List<?> choices) {
		boolean first = true;
		for (Object choice : choices) {
			if (!first) {
				sb.append(',');
			}
			sb.append(choice.toString());
			first = false;
		}
	}
}
