package fr.krachimmo.job;

import fr.krachimmo.seloger.Annonce;

/**
 *
 * @author Sébastien Chatel
 * @since 24 June 2014
 */
public class AnnonceCsvLineAggregator implements LineAggregator<Annonce> {

	@Override
	public String aggregate(Annonce item) {
		StringBuilder sb = new StringBuilder(128);
		sb.append(item.getId());
		sb.append(',');
		sb.append(item.getPrix());
		sb.append(',');
		sb.append(item.getSuperficie());
		sb.append(',');
		sb.append(item.getPieces());
		return sb.toString();
	}
}
