package fr.krachimmo;

import java.util.Map;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactoryBean;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.util.StringUtils;

/**
 *
 * @author Sébastien Chatel
 * @since 27 January 2014
 */
public class EmbeddedDatabaseBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		RootBeanDefinition definition = new RootBeanDefinition(EmbeddedDatabaseFactoryBean.class);
		Map<String, Object> attrs = importingClassMetadata.getAnnotationAttributes(EnabledEmbeddedDatabase.class.getName());
		String script = (String) attrs.get("script");
		if (StringUtils.hasText(script)) {
			ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
			databasePopulator.addScript(new StringResource(script));
			definition.setAttribute("databasePopulator", databasePopulator);
		}
		definition.setAttribute("databaseType", attrs.get("type"));
		definition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		registry.registerBeanDefinition("embeddedDataSource", definition);
	}
}
class StringResource extends ByteArrayResource {
	public StringResource(String value) {
		super(value.getBytes());
	}
}
