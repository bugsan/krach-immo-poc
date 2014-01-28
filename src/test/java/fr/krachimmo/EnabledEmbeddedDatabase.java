package fr.krachimmo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/**
 *
 * @author Sébastien Chatel
 * @since 27 January 2014
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(EmbeddedDatabaseBeanDefinitionRegistrar.class)
public @interface EnabledEmbeddedDatabase {

	String value() default "";

	EmbeddedDatabaseType type() default EmbeddedDatabaseType.HSQL;
}
