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

	/**
	 * Stores an internal logger instance.
	 */
	@Getter (AccessLevel.PROTECTED)
	private static final Logger logger = LogManager.getLogger (StashCommit.class);

	/**
	 * Stores the author.
	 */
	@JsonProperty (value = "author", required = true)
	@Getter
	private StashAuthor author;

	/**
	 * Stores the display identifier.
	 */
	private String displayIdentifier;

	/**
	 * Stores the identifier.
	 */
	private String identifier;

	/**
	 * Stores the issue list.
	 */
	private List<JiraIssue> linkedIssues = new ArrayList<> ();

	/**
	 * Stores the message.
	 */
	@JsonProperty (value = "message", required = true)
	@Getter
	private String message;

	/**
	 * Internal Constructor
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
	 * Processes the commit attributes.
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
	 * Processes the display identifier.
	 * @param displayIdentifier The identifier.
	 */
	@JsonProperty (value = "displayId", required = true)
	private void processDisplayIdentifier (@NonNull String displayIdentifier) {
		this.displayIdentifier = displayIdentifier;
	}

	/**
	 * Processes the identifier.
	 * @param identifier The identifier.
	 */
	@JsonProperty (value = "id", required = true)
	private void processIdentifier (@NonNull String identifier) {
		this.identifier = identifier;
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
		 * Processes the JIRA issue keys.
		 * @param keys The key list.
		 */
		@JsonProperty (value = "jira-key", required = false)
		private void processKeys (@NonNull List<String> keys) {
			this.jiraKeys = keys;
		}
	}
}
