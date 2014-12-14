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
define (['jquery', 'backbone', 'model/cache-craftbukkit', 'model/cache-spigot'], function ($, Backbone, CacheCraftBukkit, CacheSpigot) {
	"use strict";

	// constants
	var VERSION_PATTERN = /(\s|^)git-Spigot-([A-F0-9]{7})-([A-F0-9]{7})(\s|$)/i;
	var OLD_VERSION_PATTERN = /(\s|^)git-Spigot-(\"([A-F0-9]{7})\"|([A-F0-9]{7}))(\s|$)/i;
	var MAX_COMMITS = 20;

	// helper variable for delayed router start
	var cacheLoaded = 0;

	/**
	 * Enables the router.
	 */
	function enableRouter () {
		// increase counter
		cacheLoaded++;

		// check
		if (cacheLoaded >= 2) console.log ('History start: ' + Backbone.history.start ());
	}

	// fetch caches from backend (see Java part)
	var cacheCraftBukkit = new CacheCraftBukkit ();
	var cacheSpigot = new CacheSpigot ();

	cacheCraftBukkit.fetch ({ success: enableRouter });
	cacheSpigot.fetch ({ success: enableRouter });

	/**
	 * Walks the commit tree.
	 * @param startHash The start hash.
	 * @param cache The cache to read from.
	 * @param commitsOut The output array.
	 */
	function walkTree (startHash, cache, commitsOut) {
		var commits = [];
		var searchQueue = [ startHash ];

		// verify start hash against known commits
		if (!cache.get ('commits')[startHash]) return -2;

		// collect known parents of start
		var knownParents = [ startHash ];

		(function () {
			var searchQueue = [ startHash ];

			do {
				var key = searchQueue[0];

				// get commit
				var commit = cache.get ('commits')[key];

				// remove from search queue
				searchQueue.splice (0, 1);

				// skip if commit is unknown to the system (too old)
				if (!commit) return true;

				// get parents for current element
				var parents = commit['parent'];

				// add to list of known parents
				$(parents).each (function (index, element) {
					if (knownParents.indexOf (element) > -1) return true;
					knownParents.push (element.substr (0, 7));
					searchQueue.push (element.substr (0, 7));
				});
			} while (searchQueue.length > 0);
		}) ();

		// search in parent map
		do {
			var key = searchQueue[0];

			// remove element from queue
			searchQueue.splice (0, 1);

			// skip iteration if already processed
			if (commits.indexOf (key) > -1) continue;

			// get children for current element
			var children = cache.get ('parents')[key];

			// check for current version
			if (!children && key == startHash) return -1;

			// add to commit list
			if (key != startHash) {
				commits.push (key);

				// add commit to output list
				commitsOut.push (cache.get ('commits')[key]);

				// check parents (support for merging)
				var parents = cache.get ('commits')[key]['parent'];

				$ (parents).each (function (index, element) {
					// fix length (bring down to Spigot format)
					element = element.substr (0, 7);

					// check whether commit is already queued
					if (searchQueue.indexOf (element) > -1 || commits.indexOf (element) > -1 || element == startHash || knownParents.indexOf (element) > -1) return true;

					// add to queue
					searchQueue.push (element);
				});
			}

			// append children to queue
			if (!children) continue;
			$(children).each (function (index, element) {
				// check whether commit is already queued
				if (searchQueue.indexOf (element) > -1 || commits.indexOf (element) > 0) return true;

				// add to queue
				searchQueue.push (element);
			});
		} while (searchQueue.length > 0);

		// reverse commits (we're running down the tree backwards)
		commitsOut.reverse ();

		// remove exhaust (limit to X commits on display)
		commitsOut.splice (MAX_COMMITS, (commitsOut.length - MAX_COMMITS));

		// return absolute length (!= commits displayed)
		return commits.length;
	}

	/**
	 * Checks a version.
	 */
	return function (versionString) {
		// check for empty values
		if (!versionString) return {
			error:		'invalid'
		};

		// check for regular version (current pattern)
		var matches = versionString.match (VERSION_PATTERN);

		// check for old version (major Buildtools fuckup)
		if (versionString.match (OLD_VERSION_PATTERN) && !matches) return {
			error:		'old'
		};

		// check whether current pattern matches
		if (!matches) return {
			error:		'invalid'
		};

		// extract versions
		var spigotHash = matches[2].toLowerCase ();
		var craftbukkitHash = matches[3].toLowerCase ();

		// initialize return value with defaults
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

		// walk down commit trees to receive commit counts and commits
		result.versions.craftbukkitCount = walkTree (craftbukkitHash, cacheCraftBukkit, result.commits.craftbukkit);
		result.versions.spigotCount = walkTree (spigotHash, cacheSpigot, result.commits.spigot);

		// handle invalid versions
		if (result.versions.craftbukkitCount == -2 || result.versions.spigotCount == -2) return {
			error:				"unknown"
		};

		// update helper variables (thanks Handlebars)
		result.versions.craftbukkit = (result.versions.craftbukkitCount > 0);
		result.versions.spigot = (result.versions.spigotCount > 0);

		// return result
		return result;
	}
});