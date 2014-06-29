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

	public void add(Annonce annonce) {
		this.totalPrice += annonce.getPrix();
		this.totalSurface += annonce.getSuperficie();
	}

	public int getPricePerSquareMeter() {
		return (int) Math.round(this.totalPrice / this.totalSurface);
	}
}
