package fr.krachimmo.core.scrap;

import org.w3c.dom.Node;

/**
 *
 * @author Sébastien Chatel
 * @since 31 October 2012
 */
public interface DocumentMapper<T> {

	T mapDocument(Node document) throws Exception;
}
