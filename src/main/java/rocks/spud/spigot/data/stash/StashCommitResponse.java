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
package rocks.spud.spigot.data.stash;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;
import org.apache.log4j.LogManager;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides a representation for Stash commit API responses.
 * @author Johannes Donath <johannesd@torchmind.com>
 * @copyright Copyright (C) 2014 Torchmind <http://www.torchmind.com>
 */
@JsonIgnoreProperties (ignoreUnknown = true)
public class StashCommitResponse {

	/**
	 * Defines the default commit limit.
	 */
	public static final int DEFAULT_LIMIT = 8192;

	/**
	 * Stores the commit map.
	 */
	@Getter
	private Map<String, StashCommit> commitMap;

	/**
	 * Stores the parent map.
	 */
	@Getter
	private Map<String, List<String>> parentMap;

	/**
	 * Internal Constructor
	 */
	private StashCommitResponse () { }

	/**
	 * Requests a commit set from Stash.
	 * @param organization The organization.
	 * @param repository The repository.
	 * @param limit The commit limit.
	 * @param since The first commit to display.
	 * @return The commit response.
	 */
	public static StashCommitResponse getStashCommitResponse (@NonNull String organization, @NonNull String repository, int limit, String since) {
		// create rest template
		RestTemplate template = new RestTemplate ();

		// get object
		return template.getForObject ("https://hub.spigotmc.org/stash/rest/api/1.0/projects/" + organization + "/repos/" + repository + "/commits?limit=" + limit + (since != null ? "&since=" + since : ""), StashCommitResponse.class);
	}

	/**
	 * Requests a commit set from Stash.
	 * @param organization The organization.
	 * @param repository The repository.
	 * @param since The first commit to display.
	 * @return The commit response.
	 */
	public static StashCommitResponse getStashCommitResponse (String organization, String repository, String since) {
		return getStashCommitResponse (organization, repository, DEFAULT_LIMIT, since);
	}

	/**
	 * Processes the value list.
	 * @param commitList The commit list.
	 */
	@JsonProperty (value = "values", required = true)
	private void processValues (@NonNull List<StashCommit> commitList) {
		this.commitMap = new HashMap<> ();
		this.parentMap = new HashMap<> ();

		// collect list of commits
		for (StashCommit commit : commitList) {
			this.commitMap.put (commit.getIdentifier ().substring (0, 7).toLowerCase (), commit);
		}

		// build parent index
		for (Map.Entry<String, StashCommit> commitEntry : this.commitMap.entrySet ()) {
			for (String parent : commitEntry.getValue ().getParentCommits ()) {
				// get parent name
				parent = parent.substring (0, 7);

				// create new list
				if (!this.parentMap.containsKey (parent)) this.parentMap.put (parent, new ArrayList<String> ());

				// add to list
				this.parentMap.get (parent).add (commitEntry.getValue ().getIdentifier ().substring (0, 7));
			}
		}
	}
}
