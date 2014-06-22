package fr.krachimmo.job;

/**
 *
 * @author Sébastien Chatel
 * @since 21 June 2014
 */
public class CloudStorageFile {

	private final String bucket;
	private final String filename;
	private final String charset;
	private final boolean gzip;

	public CloudStorageFile(String bucket, String filename) {
		this(bucket, filename, "utf-8", false);
	}
	public CloudStorageFile(String bucket, String filename, String charset, boolean gzip) {
		this.bucket = bucket;
		this.filename = filename;
		this.charset = charset;
		this.gzip = gzip;
	}
	public String getBucket() {
		return this.bucket;
	}
	public String getFilename() {
		return this.filename;
	}
	public String getCharset() {
		return this.charset;
	}
	public boolean isGzip() {
		return this.gzip;
	}
	public String getLocation() {
		return "http://storage.googleapis.com/" + this.bucket + "/" + this.filename;
	}
}
