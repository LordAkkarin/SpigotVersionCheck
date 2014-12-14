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
package rocks.spud.spigot.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rocks.spud.spigot.component.ApplicationDetail;

/**
 * @author Johannes Donath <johannesd@torchmind.com>
 * @copyright Copyright (C) 2014 Torchmind <http://www.torchmind.com>
 */
@RestController
@RequestMapping ({ "/rest/api/2.0", "/rest/api/latest" })
public class ApplicationAPI {

	/**
	 * See {@link rocks.spud.spigot.component.ApplicationDetail}
	 */
	@Autowired
	private ApplicationDetail applicationDetail;

	/**
	 * Provides an endpoint to retrieve application information.
	 * @return The information object.
	 */
	@RequestMapping (value = "/")
	public ApplicationDetail information () {
		return this.applicationDetail;
	}
}
