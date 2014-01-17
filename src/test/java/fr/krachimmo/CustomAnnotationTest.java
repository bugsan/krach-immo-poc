package fr.krachimmo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Configuration;
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
	@Test
	public void blah() {
	}
}
@Configuration
@EnableAsyncItemProcessor
class CustomConfig {
}