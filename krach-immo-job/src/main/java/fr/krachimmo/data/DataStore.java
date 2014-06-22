package fr.krachimmo.data;

import org.springframework.core.io.Resource;

/**
 *
 * @author S�bastien Chatel
 * @since 21 June 2014
 */
public interface DataStore {

	String findLatestDataLocation();

	void saveLatestDataLocation(String location);

	Resource findLatestData();
}