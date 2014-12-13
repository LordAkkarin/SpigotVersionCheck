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
package rocks.spud.spigot.component;

import lombok.AccessLevel;
import lombok.Getter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Provides environment information.
 * @author Johannes Donath <johannesd@torchmind.com>
 * @copyright Copyright (C) 2014 Torchmind <http://www.torchmind.com>
 */
@Component
public class ApplicationDetail {

	/**
	 * Stores an internal logger instance.
	 */
	@Getter (AccessLevel.PROTECTED)
	private static final Logger logger = LogManager.getLogger (ApplicationDetail.class);

	/**
	 * Stores the application title.
	 */
	@Getter
	private final String title;

	/**
	 * Stores the application version.
	 */
	@Getter
	private final String version;

	/**
	 * Stores the application vendor.
	 */
	@Getter
	private final String vendor;

	/**
	 * Constructs a new ApplicationDetail instance.
	 */
	public ApplicationDetail () {
		// get package
		Package p = ApplicationDetail.class.getPackage ();

		// declare default values
		String title = "Development Server";
		String version = "(Development Snapshot)";
		String vendor = "Unknown Vendor";

		// read manifest values
		if (p != null) {
			title = (p.getImplementationTitle () != null ? p.getImplementationTitle () : title);
			version = (p.getImplementationVersion () != null ? p.getImplementationVersion () : version);
			vendor = (p.getImplementationVendor () != null ? p.getImplementationVendor () : vendor);
		}

		// read custom values
		try {
			// open manifest
			Manifest manifest = new Manifest (this.getClass ().getResourceAsStream ("/META-INF/MANIFEST.MF"));

			// get attributes
			Attributes attributes = manifest.getMainAttributes ();

			// get value
			String buildNumber = attributes.getValue ("Implementation-Build");

			if (buildNumber == null)
				getLogger ().warn ("No build number found in application manifest.");
			else
				version += "-" + buildNumber;
		} catch (IOException ex) {
			getLogger ().warn ("Could not load manifest: " + ex.getMessage ());
		}

		// store version
		this.title = title;
		this.version = version;
		this.vendor = vendor;
	}
}
