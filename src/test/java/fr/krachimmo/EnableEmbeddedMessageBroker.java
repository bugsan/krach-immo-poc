package fr.krachimmo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 *
 * @author S�bastien Chatel
 * @since 27 January 2014
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(EmbeddedMessageBrokerConfig.class)
public @interface EnableEmbeddedMessageBroker {

}