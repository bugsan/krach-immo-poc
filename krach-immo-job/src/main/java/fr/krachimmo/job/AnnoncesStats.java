package fr.krachimmo.job;

import fr.krachimmo.seloger.Annonce;

/**
 *
 * @author Sébastien Chatel
 * @since 29 June 2014
 */
public class AnnoncesStats {

	private int totalPrice = 0;
	private double totalSurface = 0d;
	private int totalPrice1Piece = 0;
	private double totalSurface1Piece = 0d;
	private int totalPrice2Pieces = 0;
	private double totalSurface2Pieces = 0d;
	private int totalPrice3Pieces = 0;
	private double totalSurface3Pieces = 0d;
	private int totalPrice4PlusPieces = 0;
	private double totalSurface4PlusPieces = 0d;

	public void add(Annonce annonce) {
		this.totalPrice += annonce.getPrix();
		this.totalSurface += annonce.getSuperficie();
		switch (annonce.getPieces()) {
		case 1: totalPrice1Piece += annonce.getPrix();
				totalSurface1Piece += annonce.getSuperficie();
				break;
		case 2: totalPrice2Pieces += annonce.getPrix();
				totalSurface2Pieces += annonce.getSuperficie();
				break;
		case 3: totalPrice3Pieces += annonce.getPrix();
				totalSurface3Pieces += annonce.getSuperficie();
				break;
		default: totalPrice4PlusPieces += annonce.getPrix();
				totalSurface4PlusPieces += annonce.getSuperficie();
		}
	}

	public int getPricePerSquareMeter() {
		return (int) Math.round(this.totalPrice / this.totalSurface);
	}
	public int getPricePerSquareMeter1Piece() {
		return (int) Math.round(this.totalPrice1Piece / this.totalSurface1Piece);
	}
	public int getPricePerSquareMeter2Pieces() {
		return (int) Math.round(this.totalPrice2Pieces / this.totalSurface2Pieces);
	}
	public int getPricePerSquareMeter3Pieces() {
		return (int) Math.round(this.totalPrice3Pieces / this.totalSurface3Pieces);
	}
	public int getPricePerSquareMeter4PlusPieces() {
		return (int) Math.round(this.totalPrice4PlusPieces / this.totalSurface4PlusPieces);
	}
}
