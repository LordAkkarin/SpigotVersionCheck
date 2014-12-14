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
import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import rocks.spud.spigot.data.IAuthor;

/**
 * Provides an author implementation for the Stash API.
 * @author Johannes Donath <johannesd@torchmind.com>
 * @copyright Copyright (C) 2014 Torchmind <http://www.torchmind.com>
 */
@AllArgsConstructor
@JsonIgnoreProperties (ignoreUnknown = true)
public class StashAuthor implements IAuthor {

	/**
	 * Stores the avatar identifier (e.g. Gravatar hash).
	 */
	private String avatarIdentifier;

	/**
	 * Stores the author name (Jira/Stash Username).
	 */
	private String name;

	/**
	 * Jackson Utility Method
	 */
	private StashAuthor () { }

	/**
	 * {@inheritDoc}
	 */
	@Override
	@JsonProperty ("avatarIdentifier")
	public String getAvatarIdentifier () {
		return this.avatarIdentifier;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@JsonProperty ("name")
	public String getName () {
		return this.name;
	}

	/**
	 * Jackson Utility Method
	 * @param emailAddress The email address.
	 */
	@JsonProperty (value = "emailAddress", required = true)
	private void processEmailAddress (@NonNull String emailAddress) {
		// get md5 hash algorithm
		HashFunction function = Hashing.md5 ();

		// create hash code
		HashCode hashCode = function.newHasher ().putString (emailAddress.toLowerCase (), Charsets.UTF_8).hash ();

		// store hex representation
		this.avatarIdentifier = hashCode.toString ();
	}

	/**
	 * Jackson Utility Method
	 * @param name The name.
	 */
	@JsonProperty (value = "name", required = true)
	private void processName (@NonNull String name) {
		this.name = name;
	}
}
