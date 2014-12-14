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
define (['jquery', 'handlebars', 'hndl!../template/commit'], function ($, Handlebars, TemplateCommit) {
	"use strict";

	// define partials
	Handlebars.registerPartial ('commit', TemplateCommit);

	// Application Logic
	require (['version-check', 'router', 'hndl!../template/result-none', 'hndl!../template/result-latest', 'hndl!../template/result-outdated', 'hndl!../template/result-unknown', 'hndl!../template/result-releaseday'], function (VersionCheck, Router, TemplateNone, TemplateLatest, TemplateOutdated, TemplateUnknown, TemplateReleaseDay) {
		// initialize page (ensures users are warned when no JavaScript is available)
		// also simplifies translation slightly
		$('#version-check-result').html (TemplateNone ());

		/**
		 * Renders the version information on the page.
		 */
		function process () {
			// remove error
			$('#version-check').parent ().removeClass ('has-error');

			// hide previous elements
			$('#version-check-result').hide ('fast', function () {
				// check version against backend
				var result = VersionCheck ($('#version-check').val ());

				if (!!result.error) {
					switch (result.error) {
						default:
						case 'invalid':
							$ ('#version-check').parent ().addClass ('has-error');
							$ ('#version-check-result').html (TemplateNone ());
							break;
						case 'old':
							$ ('#version-check-result').html (TemplateReleaseDay ());
							break;
						case 'unknown':
							$ ('#version-check-result').html (TemplateUnknown ());
							break;
					}
				} else if (!result.versions.craftbukkit && !result.versions.spigot)
					$('#version-check-result').html (TemplateLatest ());
				else
					$('#version-check-result').html (TemplateOutdated (result));

				// re-enable root element
				$('#version-check-result').show ('fast');
			});
		}

		// register application router (enabled when the caches are fully loaded)
		var router = new Router ();
		router.on ('route:version', function (version) {
			// skip empty version
			if (!version) return;

			// force version check
			$('#version-check').val (version);
			process ();
		});

		// hook form submit
		$('#form-version-check').submit (function (e) {
			e.preventDefault ();

			// enforce navigation change & render out results
			router.navigate ($('#version-check').val ());
			process ();

			// cancel
			return false;
		});
	});
});