package fr.krachimmo.job;


/**
 *
 * @author Sébastien Chatel
 * @since 21 June 2014
 */
public class Config {

	private SearchCriteria criteria;
	private FileOptions fileOptions;

	public Config(SearchCriteria criteria, FileOptions fileOptions) {
		this.criteria = criteria;
		this.fileOptions = fileOptions;
	}
	public SearchCriteria getCriteria() {
		return this.criteria;
	}
	public FileOptions getFileOptions() {
		return this.fileOptions;
	}
}
