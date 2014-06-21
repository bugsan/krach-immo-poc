package fr.krachimmo.job;

/**
 *
 * @author Sébastien Chatel
 * @since 21 June 2014
 */
public enum TypeBien {

	Appartement("1"),Maison("2");

	private final String code;

	private TypeBien(String code) {
		this.code = code;
	}
	@Override
	public String toString() {
		return this.code;
	}
}
