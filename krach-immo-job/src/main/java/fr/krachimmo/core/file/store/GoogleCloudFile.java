package fr.krachimmo.core.file.store;

import java.io.IOException;
import java.io.OutputStream;

import com.google.appengine.api.files.AppEngineFile;

import fr.krachimmo.core.file.CloudFile;
import fr.krachimmo.core.file.CloudFileOptions;

/**
 *
 * @author Sébastien Chatel
 * @since 29 June 2014
 */
@SuppressWarnings("deprecation")
public class GoogleCloudFile implements CloudFile {

	final GoogleCloudFileStore filestore;
	final CloudFileOptions options;
	final AppEngineFile appengineFile;
	private String path;

	public GoogleCloudFile(AppEngineFile appengineFile, GoogleCloudFileStore filestore, CloudFileOptions options) {
		this.filestore = filestore;
		this.options = options;
		this.appengineFile = appengineFile;
	}

	@Override
	public OutputStream openForWriting() throws IOException {
		return this.filestore.openForWriting(this.appengineFile);
	}

	@Override
	public String getPath() {
		if (this.path == null) {
			this.path =this.filestore.getPathForFile(this.options.getFilename());
		}
		return this.path;
	}
}
