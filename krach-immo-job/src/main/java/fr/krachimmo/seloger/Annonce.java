package fr.krachimmo.seloger;

/**
 * Représente une annonce SeLoger.
 * @author Sébastien Chatel
 * @since 24 October 2013
 */
public class Annonce {

	private long id;
	private double superficie;
	private int prix;
	private int pieces;

	public long getId() {
		return this.id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public double getSuperficie() {
		return this.superficie;
	}
	public void setSuperficie(double superficie) {
		this.superficie = superficie;
	}
	public int getPrix() {
		return this.prix;
	}
	public void setPrix(int prix) {
		this.prix = prix;
	}
	public int getPieces() {
		return this.pieces;
	}
	public void setPieces(int pieces) {
		this.pieces = pieces;
	}
	@Override
	public String toString() {
		return "Annonce [id=" + this.id
				+ ", superficie=" + this.superficie
				+ ", prix=" + this.prix
				+ ", pieces=" + this.pieces + "]";
	}
}
