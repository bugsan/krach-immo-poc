package fr.krachimmo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

/**
 *
 * @author Sébastien Chatel
 * @since 17 January 2014
 */
@Configuration
public class AsyncItemProcessorConfiguration {

	private static final Log log = LogFactory.getLog(AsyncItemProcessorConfiguration.class);

	@Bean
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	public AsyncItemProcessorBeanPostProcessor asyncItemProcessorBeanPostProcessor() {
		log.info("Registering AsyncItemProcessorBeanPostProcessor");
		return new AsyncItemProcessorBeanPostProcessor();
	}
}
