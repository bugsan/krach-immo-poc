package fr.krachimmo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 *
 * @author Sébastien Chatel
 * @since 17 January 2014
 */
public class AsyncItemProcessorBeanPostProcessor implements BeanPostProcessor {

	private static final Log log = LogFactory.getLog(AsyncItemProcessorBeanPostProcessor.class);

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		log.info("post processing bean '" + beanName + "' " + bean);
		return bean;
	}
}
