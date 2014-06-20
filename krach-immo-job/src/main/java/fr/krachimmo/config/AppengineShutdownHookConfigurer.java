package fr.krachimmo.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

import com.google.appengine.api.LifecycleManager;
import com.google.appengine.api.LifecycleManager.ShutdownHook;

/**
 *
 * @author Sébastien Chatel
 * @since 21 May 2013
 */
public class AppengineShutdownHookConfigurer implements ApplicationContextAware, InitializingBean {

	private static final Log log = LogFactory.getLog(AppengineShutdownHookConfigurer.class);

	private ConfigurableApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = (ConfigurableApplicationContext) applicationContext;
	}

	@Override
	public void afterPropertiesSet() {
		Assert.notNull(applicationContext, "property 'applicationContext' is required");
		LifecycleManager.getInstance().setShutdownHook(new ShutdownHook() {
			public void shutdown() {
				log.info("AppEngine is requesting context shutdown");
				applicationContext.close();
			}
		});
	}
}
