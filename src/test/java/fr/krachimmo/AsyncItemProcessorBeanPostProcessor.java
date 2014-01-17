package fr.krachimmo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 *
 * @author Sébastien Chatel
 * @since 17 January 2014
 */
public class AsyncItemProcessorBeanPostProcessor implements BeanPostProcessor, InitializingBean, DisposableBean {

	private static final Log log = LogFactory.getLog(AsyncItemProcessorBeanPostProcessor.class);

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("init");
	}

	@Override
	public void destroy() throws Exception {
		log.info("destroy");
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
}
