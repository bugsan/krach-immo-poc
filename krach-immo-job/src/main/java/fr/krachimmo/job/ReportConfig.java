package fr.krachimmo.job;

/**
 *
 * @author Sébastien Chatel
 * @since 29 June 2014
 */
public class ReportConfig {

	private String mailTo;

	public ReportConfig mailTo(String to) {
		this.mailTo = to;
		return this;
	}
	public String getMailTo() {
		return this.mailTo;
	}
}
