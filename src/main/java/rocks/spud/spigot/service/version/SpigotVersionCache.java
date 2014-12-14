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

import org.springframework.stereotype.Component;
import rocks.spud.spigot.data.ICommit;
import rocks.spud.spigot.data.stash.StashCommitResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides a version cache for Spigot commits.
 * @author Johannes Donath <johannesd@torchmind.com>
 * @copyright Copyright (C) 2014 Torchmind <http://www.torchmind.com>
 */
@Component ("spigotVersionCache")
public class SpigotVersionCache extends AbstractVersionCache {

	/**
	 * Defines the Spigot release commit to look for.
	 */
	public static final String RELEASE_COMMIT = "2e45f5d2fa44333b19ba591d20a7cec2d85b40cc";

	/**
	 * See {@link rocks.spud.spigot.service.IVersionCache#getCommits()}
	 */
	private Map<String, ? extends ICommit> commits = new HashMap<> ();

	/**
	 * See {@link rocks.spud.spigot.service.IVersionCache#getParents()}
	 */
	private Map<String, List<String>> parents = new HashMap<> ();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, ? extends ICommit> getCommits () {
		synchronized (this) {
			return this.commits;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, List<String>> getParents () {
		synchronized (this) {
			return this.parents;
		}
	}

	/**
	 * Requests an update from the upstream server.
	 */
	public void poll () {
		StashCommitResponse response = StashCommitResponse.getStashCommitResponse ("SPIGOT", "spigot", RELEASE_COMMIT);

		// synchronize with other threads to ensure no data is corrupted when updating the values
		synchronized (this) {
			this.commits = response.getCommitMap ();
			this.parents = response.getParentMap ();
		}

		// delete cache (serve new data on page reload)
		this.deleteResponseCache ();
	}
}
