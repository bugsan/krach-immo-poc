package fr.krachimmo.controller;

/**
 *
 * @author Sébastien Chatel
 * @since 24 June 2014
 */
public class SearchQuery {

	private String ci;
	private String idtypebien = "1,2";
	private String tri = "d_dt_crea";
	private Integer fraicheur;
	private int page = 1;

	public String getCi() {
		return this.ci;
	}
	public void setCi(String ci) {
		this.ci = ci;
	}
	public String getIdtypebien() {
		return this.idtypebien;
	}
	public void setIdtypebien(String idtypebien) {
		this.idtypebien = idtypebien;
	}
	public String getTri() {
		return this.tri;
	}
	public void setTri(String tri) {
		this.tri = tri;
	}
	public Integer getFraicheur() {
		return this.fraicheur;
	}
	public void setFraicheur(Integer fraicheur) {
		this.fraicheur = fraicheur;
	}
	public int getPage() {
		return this.page;
	}
	public void setPage(int page) {
		this.page = page;
	}
}
