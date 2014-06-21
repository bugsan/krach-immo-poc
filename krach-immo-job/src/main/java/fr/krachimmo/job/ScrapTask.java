package fr.krachimmo.job;

import java.util.concurrent.Future;

/**
 *
 * @author Sébastien Chatel
 * @since 21 June 2014
 */
public class ScrapTask<T> {

	private final String url;
	private final Future<T> future;

	public ScrapTask(String url, Future<T> future) {
		this.url = url;
		this.future = future;
	}
	public String getUrl() {
		return this.url;
	}
	public Future<T> getFuture() {
		return this.future;
	}
}
