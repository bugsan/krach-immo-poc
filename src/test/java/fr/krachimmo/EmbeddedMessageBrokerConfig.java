package fr.krachimmo;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

/**
 *
 * @author Sébastien Chatel
 * @since 27 January 2014
 */
@Configuration
public class EmbeddedMessageBrokerConfig {
	@Bean
	public ConnectionFactory connectionFactory(BrokerService brokerService) {
		return new ActiveMQConnectionFactory("vm://localhost");
	}
	@Bean
	public BrokerService brokerService() throws Exception {
		BrokerService brokerService = new BrokerService();
		brokerService.setPersistent(false);
		brokerService.setUseJmx(false);
		brokerService.addConnector("vm://localhost");
		brokerService.getSystemUsage().getTempUsage().setLimit(1024L * 1024L); // 1MB
		brokerService.getSystemUsage().getMemoryUsage().setLimit(1024L * 1024L * 64L); // 64MB
		return brokerService;
	}
	@Bean
	public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
		return new JmsTemplate(connectionFactory);
	}
}
