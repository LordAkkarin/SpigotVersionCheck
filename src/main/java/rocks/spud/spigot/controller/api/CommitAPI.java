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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rocks.spud.spigot.service.version.CraftBukkitVersionCache;
import rocks.spud.spigot.service.version.SpigotVersionCache;

import java.util.Map;

/**
 * @author Johannes Donath <johannesd@torchmind.com>
 * @copyright Copyright (C) 2014 Torchmind <http://www.torchmind.com>
 */
@RestController
@RequestMapping ({ "/rest/api/2.0/commits", "/rest/api/latest/commits" })
public class CommitAPI {
	@Autowired
	private CraftBukkitVersionCache craftBukkitVersionCache;

	@Autowired
	private SpigotVersionCache spigotVersionCache;

	/**
	 * Provides access to the CraftBukkit version cache.
	 * @return The cache.
	 */
	@RequestMapping (value = "/craftbukkit", method = RequestMethod.GET)
	public Map<String, Object> craftBukkit () {
		return this.craftBukkitVersionCache.getResponseCache ();
	}

	/**
	 * Provides access to the Spigot version cache.
	 * @return The cache.
	 */
	@RequestMapping (value = "/spigot", method = RequestMethod.GET)
	public Map<String, Object> spigot () {
		return this.spigotVersionCache.getResponseCache ();
	}
}
