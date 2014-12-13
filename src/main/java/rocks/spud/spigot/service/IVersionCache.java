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
package rocks.spud.spigot.service;

import rocks.spud.spigot.data.ICommit;

import java.util.List;
import java.util.Map;

/**
 * Provides a version cache.
 * @author Johannes Donath <johannesd@torchmind.com>
 * @copyright Copyright (C) 2014 Torchmind <http://www.torchmind.com>
 */
public interface IVersionCache {

	/**
	 * Returns a map of commits.
	 * @return The commit map.
	 */
	public Map<String, ? extends ICommit> getCommits ();

	/**
	 * Returns the parent map.
	 * @return The parent map.
	 */
	public Map<String, List<String>> getParents ();

	/**
	 * Returns the response cache.
	 * @return The cache.
	 */
	public Map<String, Object> getResponseCache ();
}
