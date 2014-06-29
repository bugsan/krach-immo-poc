package fr.krachimmo.core.file;

import java.io.IOException;

/**
 *
 * @author Sébastien Chatel
 * @since 29 June 2014
 */
public interface CloudFileStore {

	CloudFile create(CloudFileOptions options) throws IOException;
}
