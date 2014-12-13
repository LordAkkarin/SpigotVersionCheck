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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * Configures application localization.
 * @author Johannes Donath <johannesd@torchmind.com>
 * @copyright Copyright (C) 2014 Torchmind <http://www.torchmind.com>
 */
@Configuration
public class LocalizationConfiguration {

	/**
	 * Indicates whether caching shall be disabled.
	 */
	@Value ("${cache.disable:false}")
	private boolean disableCache;

	/**
	 * Provides the localization message source.
	 * @return The message source.
	 */
	@Bean
	public MessageSource messageSource () {
		// create message source
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource ();

		// set properties
		messageSource.setBasename ("classpath:/localization/frontend");
		messageSource.setUseCodeAsDefaultMessage (true);
		messageSource.setDefaultEncoding ("UTF-8");
		messageSource.setCacheSeconds ((this.disableCache ? 0 : -1));

		return messageSource;
	}
}
