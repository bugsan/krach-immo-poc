package fr.krachimmo.core.file.store;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.GSFileOptions;
import com.google.appengine.api.files.GSFileOptions.GSFileOptionsBuilder;

import fr.krachimmo.core.file.CloudFile;
import fr.krachimmo.core.file.CloudFileOptions;
import fr.krachimmo.core.file.CloudFileStore;

/**
 *
 * @author Sébastien Chatel
 * @since 29 June 2014
 */
@SuppressWarnings("deprecation")
public class GoogleCloudFileStore implements CloudFileStore {

	@Autowired
	private FileService fileService;
	private String bucket;
	private String acl = "public_read";
	private boolean gzip = true;

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}
	public void setAcl(String acl) {
		this.acl = acl;
	}
	public void setGzip(boolean gzip) {
		this.gzip = gzip;
	}

	@PostConstruct
	public void postConstruct() {
		Assert.hasText(this.bucket, "property 'bucket' is required");
	}

	@Override
	public CloudFile create(CloudFileOptions options) throws IOException {
		return new GoogleCloudFile(this.fileService.createNewGSFile(createFileOptions(options)), this, options);
	}

	OutputStream openForWriting(AppEngineFile file) throws IOException {
		OutputStream os = new AppengineFileOutputStream(this.fileService, file, true);
		os = new BufferedOutputStream(os);
		os = this.gzip ? new GZIPOutputStream(os) : os;
		return os;
	}

	GSFileOptions createFileOptions(CloudFileOptions options) {
		GSFileOptionsBuilder builder = new GSFileOptionsBuilder()
			.setBucket(this.bucket)
			.setKey(options.getFilename())
			.setMimeType(options.getMimeType())
			.setAcl(this.acl);
		if (this.gzip) {
			builder.setContentEncoding("gzip");
		}
		return builder.build();
	}

	String getPathForFile(String filename) {
		return "http://storage.googleapis.com/" + this.bucket + "/" + filename;
	}
}
