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
	 * Defines the CraftBukkit release commit to look for.
	 */
	public static final String RELEASE_COMMIT = "24557bc2b37deb6a0edf497d547471832457b1dd";

	/**
	 * Stores the commit list.
	 */
	private Map<String, ? extends ICommit> commits = new HashMap<> ();

	/**
	 * Stores the parent map.
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
	 * Polls an update.
	 */
	public synchronized void poll () {
		StashCommitResponse response = StashCommitResponse.getStashCommitResponse ("SPIGOT", "craftbukkit", RELEASE_COMMIT);

		// synchronize & store values
		synchronized (this) {
			this.commits = response.getCommitMap ();
			this.parents = response.getParentMap ();
		}
	}
}
