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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import rocks.spud.spigot.data.ICommit;
import rocks.spud.spigot.data.jira.JiraIssue;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a representation for stash commits.
 * @author Johannes Donath <johannesd@torchmind.com>
 * @copyright Copyright (C) 2014 Torchmind <http://www.torchmind.com>
 */
@AllArgsConstructor
@JsonIgnoreProperties (ignoreUnknown = true)
public class StashCommit implements ICommit {
	@Getter (AccessLevel.PROTECTED)
	private static final Logger logger = LogManager.getLogger (StashCommit.class);

	/**
	 * Stores the author (e.g. the Git name).
	 */
	@JsonProperty (value = "author", required = true)
	@Getter
	private StashAuthor author;

	/**
	 * Stores the display identifier (e.g. the shortened git hash).
	 */
	private String displayIdentifier;

	/**
	 * Stores the identifier (e.g. the full git hash).
	 */
	private String identifier;

	/**
	 * Stores a list of linked JIRA issues.
	 */
	private List<JiraIssue> linkedIssues = new ArrayList<> ();

	/**
	 * Stores the git commit message.
	 */
	@JsonProperty (value = "message", required = true)
	@Getter
	private String message;

	/**
	 * Stores a list of known direct parents.
	 */
	private List<String> parent;

	/**
	 * Jackson Utility Method
	 */
	private StashCommit () { }

	/**
	 * {@inheritDoc}
	 */
	@Override
	@JsonProperty ("displayIdentifier")
	public String getDisplayIdentifier () {
		return this.displayIdentifier;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@JsonProperty ("identifier")
	public String getIdentifier () {
		return this.identifier;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@JsonProperty ("linkedIssues")
	public List<JiraIssue> getLinkedIssues () {
		return this.linkedIssues;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@JsonProperty ("parent")
	public List<String> getParentCommits () {
		return this.parent;
	}

	/**
	 * Jackson Utility Method
	 * @param attributes The attributes.
	 */
	@JsonProperty ("attributes")
	private void processAttributes (@NonNull StashCommitAttributes attributes) {
		// process keys
		for (String key : attributes.getJiraKeys ()) {
			try {
				this.linkedIssues.add (JiraIssue.getIssue (key));
			} catch (Exception ex) {
				getLogger ().warn ("Could not load information for JIRA issue " + key + ": " + ex.getMessage ());
			}
		}
	}

	/**
	 * Jackson Utility Method
	 * @param displayIdentifier The identifier.
	 */
	@JsonProperty (value = "displayId", required = true)
	private void processDisplayIdentifier (@NonNull String displayIdentifier) {
		this.displayIdentifier = displayIdentifier;
	}

	/**
	 * Jackson Utility Method
	 * @param identifier The identifier.
	 */
	@JsonProperty (value = "id", required = true)
	private void processIdentifier (@NonNull String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Jackson Utility Method
	 * @param commits The commit list.
	 */
	@JsonProperty (value = "parents", required = true)
	private void processParents (@NonNull List<StashCommitParentCommit> commits) {
		this.parent = new ArrayList<> ();
		for (StashCommitParentCommit commit : commits) this.parent.add (commit.getIdentifier ());
	}

	/**
	 * Represents the commit attributes within the original JSON response.
	 */
	@JsonIgnoreProperties (ignoreUnknown = true)
	private static class StashCommitAttributes {

		/**
		 * Stores a list of referenced JIRA keys.
		 */
		@Getter
		private List<String> jiraKeys = new ArrayList<> ();

		/**
		 * Jackson Utility Method
		 * @param keys The key list.
		 */
		@JsonProperty (value = "jira-key", required = false)
		private void processKeys (@NonNull List<String> keys) {
			this.jiraKeys = keys;
		}
	}

	/**
	 * Represents the parent commit.
	 */
	@JsonIgnoreProperties (ignoreUnknown = true)
	public static class StashCommitParentCommit {
		@Getter
		@JsonProperty (value = "id", required = true)
		private String identifier;
	}
}
