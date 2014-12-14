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
 * Provides a version cache for CraftBukkit commits.
 * @author Johannes Donath <johannesd@torchmind.com>
 * @copyright Copyright (C) 2014 Torchmind <http://www.torchmind.com>
 */
@Component ("craftBukkitVersionCache")
public class CraftBukkitVersionCache extends AbstractVersionCache {

	/**
	 * Defines the initial CraftBukkit release commit to look for.
	 * Note: The application will not load any commits beyond that point.
	 */
	public static final String RELEASE_COMMIT = "24557bc2b37deb6a0edf497d547471832457b1dd";

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
	public synchronized void poll () {
		StashCommitResponse response = StashCommitResponse.getStashCommitResponse ("SPIGOT", "craftbukkit", RELEASE_COMMIT);

		// synchronize with other threads to ensure no data is corrupted when updating the values
		synchronized (this) {
			this.commits = response.getCommitMap ();
			this.parents = response.getParentMap ();
		}

		// delete cache (serve new contents on page reload)
		this.deleteResponseCache ();
	}
}
