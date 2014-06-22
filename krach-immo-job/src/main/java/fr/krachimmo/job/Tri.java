package fr.krachimmo.job;

/**
 *
 * @author Sébastien Chatel
 * @since 21 June 2014
 */
public enum Tri {

	Creation("d_dt_crea"),Modification("d_dt_maj");

	private final String code;

	private Tri(String code) {
		this.code = code;
	}
	@Override
	public String toString() {
		return this.code;
	}
}
