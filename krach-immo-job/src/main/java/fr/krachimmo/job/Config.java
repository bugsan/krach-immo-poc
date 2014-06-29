package fr.krachimmo.job;

/**
 *
 * @author Sébastien Chatel
 * @since 21 June 2014
 */
public class Config {

	private SearchCriteria criteria;
	private FileOptions fileOptions;
	private ReportConfig reportConfig;

	public Config(SearchCriteria criteria, FileOptions fileOptions, ReportConfig reportConfig) {
		this.criteria = criteria;
		this.fileOptions = fileOptions;
		this.reportConfig = reportConfig;
	}
	public SearchCriteria getCriteria() {
		return this.criteria;
	}
	public FileOptions getFileOptions() {
		return this.fileOptions;
	}
	public ReportConfig getReportConfig() {
		return this.reportConfig;
	}
}
