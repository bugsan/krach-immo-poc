package fr.krachimmo.job;

/**
 * Représente une annonce SeLoger.
 * @author Sébastien Chatel
 * @since 24 October 2013
 */
public class Annonce {

	private Long id;
	private double superficie;
	private double prix;
	private int pieces;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public double getSuperficie() {
		return superficie;
	}
	public void setSuperficie(double superficie) {
		this.superficie = superficie;
	}
	public double getPrix() {
		return prix;
	}
	public void setPrix(double prix) {
		this.prix = prix;
	}
	public int getPieces() {
		return pieces;
	}
	public void setPieces(int pieces) {
		this.pieces = pieces;
	}
	@Override
	public String toString() {
		return "Annonce [superficie=" + superficie + ", prix=" + prix + ", pieces=" + pieces + "]";
	}
}
