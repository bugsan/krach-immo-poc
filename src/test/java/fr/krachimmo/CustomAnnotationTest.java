package fr.krachimmo;

import static org.junit.Assert.assertEquals;

import javax.jms.ConnectionFactory;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author Sébastien Chatel
 * @since 17 January 2014
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={CustomConfig.class})
public class CustomAnnotationTest {
	private static final Log log = LogFactory.getLog(CustomAnnotationTest.class);
	@Autowired
	ConnectionFactory connectionFactory;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Test
	public void blah() {
		log.info("Using connectionFactory: " + connectionFactory);
		assertEquals("test", jdbcTemplate.queryForObject("select name from torrent where id=?", String.class, 1));
	}
}
@Configuration
@EnableAsyncItemProcessor
@EnableEmbeddedMessageBroker
@EnabledEmbeddedDatabase(script =
		"create table torrent (id integer not null, name varchar(128) not null);\n" +
		"alter table torrent add constraint torrent_pk primary key (id);\n" +
		"create index torrent_name_idx on torrent (name);\n" +
		"insert into torrent (id, name) values (1, 'test');")
class CustomConfig {
	@Bean
	JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
}
