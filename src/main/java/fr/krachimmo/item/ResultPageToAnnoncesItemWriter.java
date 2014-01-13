package fr.krachimmo.item;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.ItemWriter;

/**
 *
 * @author Sébastien Chatel
 * @since 13 January 2014
 */
public class ResultPageToAnnoncesItemWriter implements ItemStreamWriter<ResultPage> {

	private ItemWriter<Annonce> delegate;

	public void setDelegate(ItemWriter<Annonce> delegate) {
		this.delegate = delegate;
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		if (this.delegate instanceof ItemStreamWriter) {
			((ItemStreamWriter<Annonce>) this.delegate).open(executionContext);
		}
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		if (this.delegate instanceof ItemStreamWriter) {
			((ItemStreamWriter<Annonce>) this.delegate).update(executionContext);
		}
	}

	@Override
	public void close() throws ItemStreamException {
		if (this.delegate instanceof ItemStreamWriter) {
			((ItemStreamWriter<Annonce>) this.delegate).close();
		}
	}

	@Override
	public void write(List<? extends ResultPage> items) throws Exception {
		List<Annonce> aggregate = new ArrayList<Annonce>();
		for (ResultPage resultPage : items) {
			aggregate.addAll(resultPage.getAnnonces());
		}
		this.delegate.write(aggregate);
	}
}
