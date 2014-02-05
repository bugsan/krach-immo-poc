package fr.krachimmo.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import fr.krachimmo.annotation.EnableAppEngine;

/**
 *
 * @author Sébastien Chatel
 * @since 20 January 2014
 */
@Configuration
@ComponentScan(basePackages="fr.krachimmo")
@EnableWebMvc
@EnableAppEngine
public class WebConfig {

}
