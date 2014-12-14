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
define (['handlebars', 'translation'], function (Handlebars, translation) {
	"use strict";

	/**
	 * Provides a simple condition helper.
	 * Usage: {#condition variable1 "operator" variable2}
	 */
	Handlebars.registerHelper('condition', function (v1, operator, v2, options) {
		var value = false;

		switch (operator) {
			case '==':	value = (v1 == v2); break;
			case '===':	value = (v1 === v2); break;
			case '!=':	value = (v1 != v2); break;
			case '<':	value = (v1 < v2); break;
			case '<=':	value = (v1 <= v2); break;
			case '>':	value = (v1 > v2); break;
			case '>=':	value = (v1 >= v2); break;
			case '&&':	value = (v1 && v2); break;
			case '||':	value = (v1 || v2); break;
		}

		// compile contents (fn => display, inverse => removal)
		return (value ? options.fn (this) : options.inverse (this));
	});

	/**
	 * Provides a simple translation helper.
	 * Usage: {{translation "this.is.a.stupid.translation.name"}}
	 */
	Handlebars.registerHelper ('translation', function (arg) {
		return (translation[arg] != undefined ? translation[arg] : arg);
	});

	// return our modified version
	return Handlebars;
});