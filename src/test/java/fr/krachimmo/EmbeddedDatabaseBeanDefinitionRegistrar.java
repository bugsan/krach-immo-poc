package fr.krachimmo;

import java.util.Map;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.jdbc.core.JdbcTemplate;
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
		BeanDefinitionBuilder bean = BeanDefinitionBuilder.rootBeanDefinition(EmbeddedDatabaseFactoryBean.class);
		Map<String, Object> attrs = importingClassMetadata.getAnnotationAttributes(EnableEmbeddedDatabase.class.getName());
		String sqlScript = (String) attrs.get("value");
		if (StringUtils.hasText(sqlScript)) {
			ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
			databasePopulator.addScript(new StringResource(sqlScript));
			bean.addPropertyValue("databasePopulator", databasePopulator);
		}
		bean.addPropertyValue("databaseType", attrs.get("type"));
		registry.registerBeanDefinition("embeddedDataSource", bean.getBeanDefinition());

		bean = BeanDefinitionBuilder.rootBeanDefinition(JdbcTemplate.class);
		bean.addPropertyReference("dataSource", "embeddedDataSource");
		registry.registerBeanDefinition("jdbcTemplate", bean.getBeanDefinition());
	}
}
class StringResource extends ByteArrayResource {
	public StringResource(String value) {
		super(value.getBytes());
	}
}
