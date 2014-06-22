package fr.krachimmo.core.store;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.apphosting.api.ApiProxy.ApplicationException;

@SuppressWarnings("deprecation")
public class AppengineFileOutputStream extends OutputStream {

	private static final int FILE_CLOSED = 10;

	private final FileService fileService;

	private final AppEngineFile file;

	private WritableByteChannel channel;

	private final boolean finallyOnClose;

	public AppengineFileOutputStream(FileService fileService, AppEngineFile file) throws IOException {
		this(fileService, file, false);
	}

	public AppengineFileOutputStream(FileService fileService, AppEngineFile file, boolean finallyOnClose) throws IOException {
		this.fileService = fileService;
		this.file = file;
		this.finallyOnClose = finallyOnClose;
		openChannel();
	}

	@Override
	public void write(int b) throws IOException {
		write(new byte[] {(byte) (b & 0xff)});
	}

	@Override
	public void write(byte[] b) throws IOException {
		write(b, 0, b.length);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		try {
			doWrite(ByteBuffer.wrap(b, off, len));
		}
		catch (IOException ex) {
			if (fileHasBeenClosedByTimeout(ex)) {
				openChannel();
				doWrite(ByteBuffer.wrap(b, off, len));
			}
			else {
				throw ex;
			}
		}
	}

	private void doWrite(ByteBuffer buffer) throws IOException {
		this.channel.write(buffer);
	}

	@Override
	public void close() throws IOException {
		try {
			this.channel.close();
		}
		catch (IOException ex) {
			if (!fileHasBeenClosedByTimeout(ex)) {
				throw ex;
			}
		}
		if (this.finallyOnClose) {
			closeFinally();
		}
	}

	public void closeFinally() throws IOException {
		this.fileService.openWriteChannel(this.file, true).closeFinally();
	}

	private void openChannel() throws IOException {
		this.channel = this.fileService.openWriteChannel(this.file, false);
	}

	private boolean fileHasBeenClosedByTimeout(IOException exception) {
		Throwable cause = exception.getCause();
		return cause != null && cause instanceof ApplicationException
			&& ((ApplicationException) cause).getApplicationError() == FILE_CLOSED;
	}
}
