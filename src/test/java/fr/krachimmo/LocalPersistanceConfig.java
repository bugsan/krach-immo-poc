package fr.krachimmo;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.derby.jdbc.ClientDriver;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import fr.krachimmo.item.Annonce;

/**
 *
 * @author Sébastien Chatel
 * @since 15 January 2014
 */
@Configuration
@Profile({"local","default"})
public class LocalPersistanceConfig {

	@Bean
	DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(ClientDriver.class.getName());
		dataSource.setUsername("app");
		dataSource.setPassword("app");
		dataSource.setUrl("jdbc:derby://localhost:1527/testdb;create=true");
		return dataSource;
//		return new EmbeddedDatabaseBuilder()
//			.setType(EmbeddedDatabaseType.HSQL)
//			.addScript("classpath:/META-INF/schemas/hsqldb-schema.sql")
//			.build();
	}

	@Bean
	DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(new ClassPathResource("/META-INF/schemas/hsqldb-schema.sql"));
		DataSourceInitializer initializer = new DataSourceInitializer();
		initializer.setDatabasePopulator(populator);
		initializer.setDataSource(dataSource);
		return initializer;
	}

	@Bean(name="annonceWriter") @Scope("step")
	JdbcBatchItemWriter<Annonce> itemWriter(DataSource dataSource/*, @Value("#{jobParameters['snapshot_id']}") final long snapshotId*/) {
		JdbcBatchItemWriter<Annonce> itemWriter = new JdbcBatchItemWriter<Annonce>();
		itemWriter.setDataSource(dataSource);
		itemWriter.setItemPreparedStatementSetter(new AnnoncePreparedStatementSetter(0));
		itemWriter.setSql("insert into annonce (prix, surface) values (?, ?)");
		return itemWriter;
	}
}
class AnnoncePreparedStatementSetter implements ItemPreparedStatementSetter<Annonce> {
	private final long snapshotId;
	public AnnoncePreparedStatementSetter(long snapshotId) {
		this.snapshotId = snapshotId;
	}
	@Override
	public void setValues(Annonce item, PreparedStatement ps) throws SQLException {
		ps.setInt(1, item.getPrice());
		ps.setInt(2, item.getSurface());
//		ps.setLong(3, snapshotId);
	}
}
