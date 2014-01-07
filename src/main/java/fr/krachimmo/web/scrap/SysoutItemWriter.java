package fr.krachimmo.web.scrap;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;

/**
 *
 * @author Sébastien Chatel
 * @since 07 January 2014
 */
public class SysoutItemWriter implements ItemWriter<Object> {
	private static final Log log = LogFactory.getLog(SysoutItemWriter.class);
	@Override
	public void write(List<? extends Object> items) throws Exception {
		for (Object item : items) {
			log.info(item);
		}
	}
}
