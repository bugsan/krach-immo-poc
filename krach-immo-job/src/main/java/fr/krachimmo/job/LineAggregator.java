package fr.krachimmo.job;

/**
 *
 * @author S�bastien Chatel
 * @since 24 June 2014
 */
public interface LineAggregator<T> {

	String aggregate(T item);
}