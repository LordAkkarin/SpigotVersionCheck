/*
 * Copyright 2014 Johannes Donath <johannesd@torchmind.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rocks.spud.spigot.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring4.resourceresolver.SpringResourceResourceResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

/**
 * Configures the web framework.
 * @author Johannes Donath <johannesd@torchmind.com>
 * @copyright Copyright (C) 2014 Torchmind <http://www.torchmind.com>
 */
@Configuration
@EnableWebMvc
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

	/**
	 * Stores an internal logger instance.
	 */
	@Getter (AccessLevel.PROTECTED)
	private static final Logger logger = LogManager.getLogger (WebMvcConfiguration.class);

	/**
	 * Indicates whether cache shall be disabled.
	 */
	@Value ("${cache.disable:false}")
	private boolean disableCache;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addResourceHandlers (ResourceHandlerRegistry registry) {
		registry.addResourceHandler ("/assets/**").addResourceLocations ("classpath:/assets/");
	}

	/**
	 * Provides a template resolver for Thymeleaf.
	 * @return The resolver.
	 */
	@Bean
	public ITemplateResolver defaultTemplateResolver () {
		// create new resolver
		TemplateResolver resolver = new TemplateResolver ();

		// set prefix & suffix
		resolver.setPrefix ("classpath:/template/");
		resolver.setSuffix (".html");

		// set additional properties
		resolver.setTemplateMode ("HTML5");
		resolver.setCharacterEncoding ("UTF-8");
		resolver.setCacheable (!this.disableCache);
		resolver.setResourceResolver (this.resourceResolver ());

		// log cache state
		if (this.disableCache) {
			getLogger ().warn ("==================================");
			getLogger ().warn ("   TEMPLATE CACHING IS DISABLED   ");
			getLogger ().warn ("   DO NOT DISABLE CACHING UNLESS  ");
			getLogger ().warn ("  YOU INTEND TO MODIFY TEMPLATES  ");
			getLogger ().warn (" FREQUENTLY WHILE THE APPLICATION ");
			getLogger ().warn ("            IS RUNNING            ");
			getLogger ().warn ("==================================");
		}

		// return prepared resolver
		return resolver;
	}

	/**
	 * Provides a resource resolver.
	 * @return The resource resolver.
	 */
	@Bean
	public SpringResourceResourceResolver resourceResolver () {
		return (new SpringResourceResourceResolver ());
	}
}
