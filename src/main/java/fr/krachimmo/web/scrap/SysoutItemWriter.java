package fr.krachimmo.web.scrap;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

/**
 *
 * @author Sébastien Chatel
 * @since 07 January 2014
 */
public class SysoutItemWriter implements ItemWriter<Object> {
	@Override
	public void write(List<? extends Object> items) throws Exception {
		for (Object item : items) {
			System.out.println(item);
		}
	}
}
