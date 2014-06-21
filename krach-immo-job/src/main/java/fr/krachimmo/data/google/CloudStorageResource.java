package fr.krachimmo.data.google;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.AbstractResource;

import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;

import fr.krachimmo.core.store.AppengineFileInputStream;

/**
 *
 * @author Sébastien Chatel
 * @since 21 June 2014
 */
@SuppressWarnings("deprecation")
class CloudStorageResource extends AbstractResource {

	private final String path;
	private final FileService fileService;

	public CloudStorageResource(String path, FileService fileService) {
		this.path = path;
		this.fileService = fileService;
	}

	@Override
	public String getDescription() {
		return this.path;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new AppengineFileInputStream(
				this.fileService,
				new AppEngineFile(this.path));
	}

	@Override
	public long contentLength() throws IOException {
		return 0;
	}

	@Override
	public boolean exists() {
		return true;
	}
}
