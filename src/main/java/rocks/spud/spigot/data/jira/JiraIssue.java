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
package rocks.spud.spigot.data.jira;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.web.client.RestTemplate;
import rocks.spud.spigot.data.IIssue;

/**
 * Provides an issue implementation for the JIRA API.
 * @author Johannes Donath <johannesd@torchmind.com>
 * @copyright Copyright (C) 2014 Torchmind <http://www.torchmind.com>
 */
@AllArgsConstructor
@JsonIgnoreProperties (ignoreUnknown = true)
public class JiraIssue implements IIssue {

	/**
	 * Stores the issue key.
	 */
	@Getter
	@JsonProperty (value = "key", required = true)
	private String key;

	/**
	 * Stores the issue summary.
	 */
	private String summary;

	/**
	 * Internal Constructor
	 */
	private JiraIssue () { }

	/**
	 * {@inheritDoc}
	 */
	@Override
	@JsonProperty ("summary")
	public String getSummary () {
		return this.summary;
	}

	/**
	 * Processes the field object.
	 * @param fields The field object.
	 */
	@JsonProperty (value = "fields", required = true)
	private void processFields (@NonNull Fields fields) {
		this.summary = fields.getSummary ();
	}

	/**
	 * Requests information about a JIRA issue.
	 * @param key The issue key.
	 * @return The issue.
	 */
	public static JiraIssue getIssue (@NonNull String key) {
		// get new template
		RestTemplate template = new RestTemplate ();

		// request key
		return template.getForObject ("https://hub.spigotmc.org/jira/rest/api/2/issue/" + key, JiraIssue.class);
	}

	/**
	 * Provides a representation of the fields object within the API response.
	 */
	@JsonIgnoreProperties (ignoreUnknown = true)
	private static class Fields {

		/**
		 * Stores the summary.
		 */
		@Getter
		@JsonProperty (value = "summary", required = true)
		private String summary;
	}
}
