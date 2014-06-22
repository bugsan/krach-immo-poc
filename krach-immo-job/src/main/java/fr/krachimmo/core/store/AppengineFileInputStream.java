package fr.krachimmo.core.store;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;

import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.apphosting.api.ApiProxy.ApplicationException;

@SuppressWarnings("deprecation")
public class AppengineFileInputStream extends InputStream {

	private static final int FILE_CLOSED = 10;

	private final FileService fileService;

	private final AppEngineFile file;

	private InputStream inputStream;

	public AppengineFileInputStream(FileService fileService, AppEngineFile file) throws IOException {
		this.fileService = fileService;
		this.file = file;
		openChannel();
	}

	@Override
	public int read() throws IOException {
		try {
			return this.inputStream.read();
		}
		catch (IOException ex) {
			if (fileHasBeenClosedByTimeout(ex)) {
				openChannel();
				return this.inputStream.read();
			}
			else {
				throw ex;
			}
		}
	}

	@Override
	public int read(byte[] b) throws IOException {
		try {
			return this.inputStream.read(b);
		}
		catch (IOException ex) {
			if (fileHasBeenClosedByTimeout(ex)) {
				openChannel();
				return this.inputStream.read(b);
			}
			else {
				throw ex;
			}
		}
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		try {
			return this.inputStream.read(b, off, len);
		}
		catch (IOException ex) {
			if (fileHasBeenClosedByTimeout(ex)) {
				openChannel();
				return this.inputStream.read(b, off, len);
			}
			else {
				throw ex;
			}
		}
	}

	@Override
	public int available() throws IOException {
		try {
			return this.inputStream.available();
		}
		catch (IOException ex) {
			if (fileHasBeenClosedByTimeout(ex)) {
				openChannel();
				return this.inputStream.available();
			}
			else {
				throw ex;
			}
		}
	}

	@Override
	public void close() throws IOException {
		try {
			this.inputStream.close();
		}
		catch (IOException ex) {
			if (!fileHasBeenClosedByTimeout(ex)) {
				throw ex;
			}
		}
	}

	private void openChannel() throws IOException {
		this.inputStream = Channels.newInputStream(this.fileService.openReadChannel(this.file, false));
	}

	private boolean fileHasBeenClosedByTimeout(IOException exception) {
		Throwable cause = exception.getCause();
		return cause != null && cause instanceof ApplicationException
			&& ((ApplicationException) cause).getApplicationError() == FILE_CLOSED;
	}
}
