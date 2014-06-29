package fr.krachimmo.core.file;

/**
 *
 * @author Sébastien Chatel
 * @since 29 June 2014
 */
public class CloudFileOptions {

	private String filename;
	private String mimeType;

	public String getFilename() {
		return this.filename;
	}
	public CloudFileOptions filename(String filename) {
		this.filename = filename;
		return this;
	}
	public String getMimeType() {
		return this.mimeType;
	}
	public CloudFileOptions mimeType(String mimeType) {
		this.mimeType = mimeType;
		return this;
	}
}
