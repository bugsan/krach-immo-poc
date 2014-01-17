package fr.krachimmo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 *
 * @author Sébastien Chatel
 * @since 17 January 2014
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(AsyncItemProcessorBeanDefinitionRegistrar.class)
public @interface EnableAsyncItemProcessor {

}
