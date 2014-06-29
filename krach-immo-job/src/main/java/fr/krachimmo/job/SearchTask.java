package fr.krachimmo.job;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author Sébastien Chatel
 * @since 21 June 2014
 */
public class SearchTask<T> {

	private final String uri;
	private final Future<T> future;

	public SearchTask(String uri, Future<T> future) {
		this.uri = uri;
		this.future = future;
	}
	public String getUri() {
		return this.uri;
	}
	public T getResult() throws ExecutionException, TimeoutException, InterruptedException {
		return this.future.get();
	}
	public T getResult(long timeoutMillis) throws ExecutionException, TimeoutException, InterruptedException {
		return this.future.get(timeoutMillis, TimeUnit.MILLISECONDS);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.uri == null) ? 0 : this.uri.hashCode());
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SearchTask<T> other = (SearchTask<T>) obj;
		if (this.uri == null) {
			if (other.uri != null)
				return false;
		} else if (!this.uri.equals(other.uri))
			return false;
		return true;
	}
}
