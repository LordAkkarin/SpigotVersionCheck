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
define (['model/commits-craftbukkit', 'model/commits-spigot'], function (CommitsCraftBukkit, CommitsSpigot) {
	"use strict";

	// constants
	var VERSION_PATTERN = /(\s|^)git-Spigot-([A-F0-9]{7})-([A-F0-9]{7})(\s|$)/i;
	var OLD_VERSION_PATTERN = /(\s|^)git-Spigot-(\"([A-F0-9]{7})\"|([A-F0-9]{7}))(\s|$)/i;
	var MAX_COMMITS = 20;

	// fetch commits
	var commitsCraftBukkit = new CommitsCraftBukkit ();
	commitsCraftBukkit.fetch ();

	var commitsSpigot = new CommitsSpigot ();
	commitsSpigot.fetch ();

	/**
	 * Checks a version.
	 */
	return function (versionString) {
		// check value
		if (!versionString) return {
			error:		'invalid'
		};

		// check for regular version
		var matches = versionString.match (VERSION_PATTERN);

		// check for old version
		if (versionString.match (OLD_VERSION_PATTERN) && !matches) return {
			error:		'old'
		};

		// check for errors
		if (!matches) return {
			error:		'invalid'
		};

		// extract versions
		var spigotHash = matches[2].toLowerCase ();
		var craftbukkitHash = matches[3].toLowerCase ();

		// initialize return value
		var result = {
			versions: {
				craftbukkit:		false,
				spigot:			false,
				craftbukkitCount:	-1,
				spigotCount:		-1
			},
			commits: {
				craftbukkit:		[],
				spigot:			[]
			}
		};

		// search CraftBukkit version
		commitsCraftBukkit.find (function (element, index) {
			// increase counter
			result.versions.craftbukkitCount++;

			// extract commit ID
			var commitID = element.get ('identifier').substr (0, 7).toLowerCase();

			// check
			if (commitID == craftbukkitHash) return true;

			// add commit to list
			if (result.commits.craftbukkit.length < MAX_COMMITS) result.commits.craftbukkit.push (element);

			// sanitize end result
			if (index == (commitsCraftBukkit.length - 1)) result = {
				error:		'unknown'
			};

			// continue
			return false;
		});

		// check for errors
		if (!!result.error) return result;

		// search Spigot version
		commitsSpigot.find (function (element, index) {
			// increase counter
			result.versions.spigotCount++;

			// extract commit ID
			var commitID = element.get ('identifier').substr (0, 7).toLowerCase();

			// check
			if (commitID == spigotHash) return true;

			// add commit to list
			if (result.commits.spigot.length < MAX_COMMITS) result.commits.spigot.push (element);

			// sanitize end result
			if (index == (commitsSpigot.length - 1)) result = {
				error:		'unknown'
			};

			// continue
			return false;
		});

		// set booleans
		if (!result.error && result.versions.spigotCount > 0) result.versions.spigot = true;
		if (!result.error && result.versions.craftbukkitCount > 0) result.versions.craftbukkit = true;

		// return result
		return result;
	}
});