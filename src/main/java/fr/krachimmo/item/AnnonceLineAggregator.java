package fr.krachimmo.item;

import org.springframework.batch.item.file.transform.LineAggregator;

/**
 *
 * @author Sébastien Chatel
 * @since 13 January 2014
 */
public class AnnonceLineAggregator implements LineAggregator<Annonce> {

	@Override
	public String aggregate(Annonce item) {
		return new StringBuilder(64)
			.append(item.getPrice())
			.append(',')
			.append(item.getSurface())
			.toString();
	}
}
