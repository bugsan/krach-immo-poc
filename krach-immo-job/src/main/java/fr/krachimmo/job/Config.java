package fr.krachimmo.job;

/**
 *
 * @author Sébastien Chatel
 * @since 21 June 2014
 */
public class Config {

	private SearchCriteria criteria;
	private CloudStorageFile file;

	public Config(SearchCriteria criteria, CloudStorageFile file) {
		this.criteria = criteria;
		this.file = file;
	}
	public SearchCriteria getCriteria() {
		return this.criteria;
	}
	public CloudStorageFile getFile() {
		return this.file;
	}
}
