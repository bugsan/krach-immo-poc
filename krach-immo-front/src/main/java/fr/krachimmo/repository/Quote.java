package fr.krachimmo.repository;

/**
 *
 * @author Sébastien Chatel
 * @since 6 July 2014
 */
public class Quote {
	private String date;
	private int priceStudio;
	private int price2Pieces;
	private int price3Pieces;
	private int price4PlusPieces;
	private int priceTotal;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getPriceStudio() {
		return priceStudio;
	}
	public void setPriceStudio(int priceStudio) {
		this.priceStudio = priceStudio;
	}
	public int getPrice2Pieces() {
		return price2Pieces;
	}
	public void setPrice2Pieces(int price2Pieces) {
		this.price2Pieces = price2Pieces;
	}
	public int getPrice3Pieces() {
		return price3Pieces;
	}
	public void setPrice3Pieces(int price3Pieces) {
		this.price3Pieces = price3Pieces;
	}
	public int getPrice4PlusPieces() {
		return price4PlusPieces;
	}
	public void setPrice4PlusPieces(int price4PlusPieces) {
		this.price4PlusPieces = price4PlusPieces;
	}
	public void setPriceTotal(int priceTotal) {
		this.priceTotal = priceTotal;
	}
	public int getPriceTotal() {
		return priceTotal;
	}
}
