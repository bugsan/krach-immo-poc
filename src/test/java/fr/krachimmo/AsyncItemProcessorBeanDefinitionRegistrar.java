package fr.krachimmo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 *
 * @author Sébastien Chatel
 * @since 17 January 2014
 */
public class AsyncItemProcessorBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

	private static final Log log = LogFactory.getLog(AsyncItemProcessorBeanDefinitionRegistrar.class);

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		log.info("Registering AsyncItemProcessorBeanPostProcessor");
		RootBeanDefinition definition = new RootBeanDefinition(AsyncItemProcessorBeanPostProcessor.class);
		registry.registerBeanDefinition(AsyncItemProcessorBeanPostProcessor.class.getSimpleName(), definition);
	}
}
