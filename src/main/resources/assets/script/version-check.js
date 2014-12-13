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

	var cacheLoaded = 0;

	/**
	 * Enables the router.
	 */
	function enableRouter () {
		console.log ('A cache finished loading ...');

		// increase counter
		cacheLoaded++;

		// check
		if (cacheLoaded >= 2) console.log ('History start: ' + Backbone.history.start ());
	}

	// fetch caches
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

		// verify start hash
		if (!cache.get ('commits')[startHash]) return -2;

		// search in parent map
		do {
			var key = searchQueue[0];

			// remove
			searchQueue.splice (0, 1);

			// skip iteration if already processed
			if (commits.indexOf (key) > -1) continue;

			// get children
			var children = cache.get ('parents')[key];

			// check for current version
			if (!children && key == startHash) return -1;

			// add to commit list
			commits.push (key);

			// add commit
			commitsOut.push (cache.get ('commits')[key]);

			// skip if no children are defined
			if (!children) continue;

			// append children to queue
			$(children).each (function (index, element) {
				// check whether commit is already queued
				if (searchQueue.indexOf (element) > -1 || commits.indexOf (element) > 0) return true;

				// add to queue
				searchQueue.push (element);
			});
		} while (searchQueue.length > 0);

		// reverse array
		commitsOut.reverse ();

		// remove elements
		commitsOut.splice (MAX_COMMITS, (commitsOut.length - MAX_COMMITS));

		// return length
		return commits.length;
	}

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

		// walk commit trees
		result.versions.craftbukkitCount = walkTree (craftbukkitHash, cacheCraftBukkit, result.commits.craftbukkit);
		result.versions.spigotCount = walkTree (spigotHash, cacheSpigot, result.commits.spigot);

		// handle invalid versions
		if (result.versions.craftbukkitCount == -2 || result.versions.spigotCount == -2) return {
			error:				"unknown"
		};

		// update booleans
		result.versions.craftbukkit = (result.versions.craftbukkitCount > 0);
		result.versions.spigot = (result.versions.spigotCount > 0);

		console.log (result);

		// return result
		return result;
	}
});