package fr.krachimmo.data;


/**
 *
 * @author Sébastien Chatel
 * @since 21 June 2014
 */
public interface DataStore {

	String findLatestDataLocation();

	void saveLatestDataLocation(String location);
}
