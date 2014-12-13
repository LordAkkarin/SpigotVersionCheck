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
package rocks.spud.spigot.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import rocks.spud.spigot.service.version.CraftBukkitVersionCache;
import rocks.spud.spigot.service.version.SpigotVersionCache;

import javax.annotation.PostConstruct;

/**
 * @author Johannes Donath <johannesd@torchmind.com>
 * @copyright Copyright (C) 2014 Torchmind <http://www.torchmind.com>
 */
@Configuration
@EnableScheduling
public class ScheduleConfiguration {

	/**
	 * Stores an internal logger instance.
	 */
	@Getter (AccessLevel.PROTECTED)
	private static final Logger logger = LogManager.getLogger (ScheduleConfiguration.class);

	/**
	 * Stores the CraftBukkit version cache.
	 */
	@Autowired
	private CraftBukkitVersionCache craftBukkitVersionCache;

	/**
	 * Stores the Spigot version cache.
	 */
	@Autowired
	private SpigotVersionCache spigotVersionCache;

	/**
	 * Initializes the instance.
	 */
	@PostConstruct
	private void initialize () {
		this.poll ();
	}

	/**
	 * Polls an update.
	 */
	@Scheduled (cron = "0 */15 * * * *")
	private void poll () {
		// log
		getLogger ().info ("Starting version cache update ...");

		// poll
		this.craftBukkitVersionCache.poll ();

		// log
		getLogger ().info ("Found " + this.craftBukkitVersionCache.getCommits ().size () + " commits for CraftBukkit.");

		// poll
		this.spigotVersionCache.poll ();

		// log
		getLogger ().info ("Found " + this.spigotVersionCache.getCommits ().size () + " commits for Spigot.");
		getLogger ().info ("Version cache update finished.");
	}
}
