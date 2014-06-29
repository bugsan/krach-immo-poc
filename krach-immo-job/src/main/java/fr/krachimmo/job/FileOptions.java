package fr.krachimmo.job;

/**
 *
 * @author Sébastien Chatel
 * @since 21 June 2014
 */
public class FileOptions {

	private String bucket;
	private String filename;
	private String charset;
	private boolean gzip = false;

	public FileOptions bucket(String bucket) {
		this.bucket = bucket;
		return this;
	}
	public String getBucket() {
		return this.bucket;
	}
	public FileOptions filename(String filename) {
		this.filename = filename;
		return this;
	}
	public String getFilename() {
		return this.filename;
	}
	public FileOptions charset(String charset) {
		this.charset = charset;
		return this;
	}
	public String getCharset() {
		return this.charset;
	}
	public FileOptions gzip(boolean gzip) {
		this.gzip = gzip;
		return this;
	}
	public boolean isGzip() {
		return this.gzip;
	}
	public String getLocation() {
		return "http://storage.googleapis.com/" + this.bucket + "/" + this.filename;
	}
}
