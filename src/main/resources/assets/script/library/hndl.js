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

/*
 * Copyright (C) 2014 Johannes Donath <johannesd@evil-co.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
define (['handlebars'], function (Handlebars) {
	'use strict';

	// constants
	var defaultExtension = 'mustache';

	/**
	 * Returns the template filename.
	 * @param name The template name.
	 * @param config The plugin configuration.
	 * @returns {string}
	 */
	var getFileName = function (name, config) {
		return (name + '.' + (!!config.extension ? config.extension : defaultExtension));
	};

	/**
	 * The RequireJS plugin.
	 */
	return {

		/**
		 * Handles resource loading.
		 * @param name The resource name.
		 * @param require The parent require instance.
		 * @param onLoad Plugin callback.
		 * @param config The plugin configuration.
		 * @todo RequireJS currently cannot execute async operations during optimization.
		 */
		load:			function (name, require, onLoad, config) {
			// stop optimizer
			if (config.isBuild) return onLoad ();

			// download template source
			require (['text!' + getFileName (name, config)], function (source) {
				// precompile
				// if (config.isBuild) buildMap[name] = Handlebars.precompile (source);

				// compile
				return onLoad (Handlebars.compile (source));
			});
		}

		/**
		 * Writes a precompiled template.
		 * @param pluginName The plugin name.
		 * @param moduleName The module name.
		 * @param write The write callback.
		 */
		/* write:			function (pluginName, moduleName, write) {
		 // skip unknown modules
		 if (!(moduleName in buildMap)) return;

		 // initialize output
		 var output = [ ];

		 // add AMD header
		 output.push ('define ([\'handlebars.runtime\'], function (Handlebars) {\n\tHandlebars = Handlebars[\'default\'];\n\t');

		 // add base definitions
		 output.push ('var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || { };\n\t');

		 // add template
		 output.push ('return templates[\'' + moduleName + '\'] = template (' + buildMap[moduleName] + ');\n');

		 // add end
		 output.push ('});');

		 // write to file
		 write (output.join (''));
		 }*/
	};
});