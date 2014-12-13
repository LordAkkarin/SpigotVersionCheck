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
package rocks.spud.spigot.data;

import java.util.List;

/**
 * Represents a commit.
 * @author Johannes Donath <johannesd@torchmind.com>
 * @copyright Copyright (C) 2014 Torchmind <http://www.torchmind.com>
 */
public interface ICommit {

	/**
	 * Returns the commit author.
	 * @return The author.
	 */
	public IAuthor getAuthor ();

	/**
	 * Returns the display identifier.
	 * @return The identifier.
	 */
	public String getDisplayIdentifier ();

	/**
	 * Returns the commit identifier.
	 * @return The identifier.
	 */
	public String getIdentifier ();

	/**
	 * Returns a list of linked issues.
	 * @return The list.
	 */
	public List<? extends IIssue> getLinkedIssues ();

	/**
	 * Returns the commit message.
	 * @return The message.
	 */
	public String getMessage ();
}
