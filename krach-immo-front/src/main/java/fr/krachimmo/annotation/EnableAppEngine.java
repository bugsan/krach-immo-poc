package fr.krachimmo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 *
 * @author Sébastien Chatel
 * @since 05 February 2014
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(AppEngineConfig.class)
public @interface EnableAppEngine {

}
