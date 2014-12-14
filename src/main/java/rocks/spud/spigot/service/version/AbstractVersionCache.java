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
package rocks.spud.spigot.service.version;

import lombok.AccessLevel;
import lombok.Getter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import rocks.spud.spigot.service.IVersionCache;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides common implementations.
 * @author Johannes Donath <johannesd@torchmind.com>
 * @copyright Copyright (C) 2014 Torchmind <http://www.torchmind.com>
 */
public abstract class AbstractVersionCache implements IVersionCache {
	@Getter (AccessLevel.PROTECTED)
	private static final Logger logger = LogManager.getLogger (AbstractVersionCache.class);

	/**
	 * Stores the response cache (a cached version of the API response).
	 */
	private Map<String, Object> responseCache = null;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Object> getResponseCache () {
		// build
		if (this.responseCache == null) {
			// log
			getLogger ().info ("Rebuilding response cache for " + this.getClass ().getCanonicalName () + ".");

			// rebuild
			this.responseCache = new HashMap<> ();

			// add elements
			this.responseCache.put ("commits", this.getCommits ());
			this.responseCache.put ("parents", this.getParents ());
		}

		// return
		return this.responseCache;
	}

	/**
	 * Deletes the response cache.
	 */
	protected void deleteResponseCache () {
		this.responseCache = null;
	}
}
