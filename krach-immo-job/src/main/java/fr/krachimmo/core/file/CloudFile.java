package fr.krachimmo.core.file;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author Sébastien Chatel
 * @since 29 June 2014
 */
public interface CloudFile {

	OutputStream openForWriting() throws IOException;

	String getPath();
}
