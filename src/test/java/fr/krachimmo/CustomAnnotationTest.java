package fr.krachimmo;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author Sébastien Chatel
 * @since 17 January 2014
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class CustomAnnotationTest {
	@Autowired
	JmsTemplate jmsTemplate;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Before
	public void setup() {
		jmsTemplate.convertAndSend("test.queue", "Hello World!");
	}
	@Test
	public void should_be_setup_correctly() {
		assertEquals("Hello World!", jmsTemplate.receiveAndConvert("test.queue"));
		assertEquals("test", jdbcTemplate.queryForObject("select name from torrent where id=?", String.class, 1));
	}
	@Configuration
	@EnableAsyncItemProcessor
	@EnableEmbeddedMessageBroker
	@EnabledEmbeddedDatabase(
			"create table torrent (id integer not null, name varchar(128) not null);\n" +
			"alter table torrent add constraint torrent_pk primary key (id);\n" +
			"create index torrent_name_idx on torrent (name);\n" +
			"insert into torrent (id, name) values (1, 'test');")
	static class CustomConfig {
	}
}
