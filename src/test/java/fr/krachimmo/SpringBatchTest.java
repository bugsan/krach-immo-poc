package fr.krachimmo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={BatchConfig.class})
public class SpringBatchTest {
	@Autowired
	JobLauncher jobLauncher;
	@Autowired @Qualifier("seloger")
	Job job;
	@Test
	public void execute() throws Exception {
		this.jobLauncher.run(this.job, new JobParametersBuilder().toJobParameters());
	}
}
@Configuration
@ImportResource("classpath:/spring-batch.xml")
class BatchConfig {
	@Bean
	MapJobRepositoryFactoryBean jobRepository() {
		return new MapJobRepositoryFactoryBean();
	}
	@Bean
	JobRegistry jobRegistry() {
		return new MapJobRegistry();
	}
	@Bean
	PlatformTransactionManager transactionManager() {
		return new ResourcelessTransactionManager();
	}
	@Bean
	JobLauncher jobLauncher(JobRepository jobRepository) {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(jobRepository);
		return jobLauncher;
	}
}
