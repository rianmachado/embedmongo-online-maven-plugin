/**
 * Copyright © 2012 Joe Littlejohn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.joelittlejohn.embedmongo.log;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import de.flapdoodle.embed.process.config.io.ProcessOutput;

@RunWith(MockitoJUnitRunner.class)
public class LoggersTest {

	private String file;
	private String encoding;

	private static final String LOGFILE = "FILE";
	private static final String LOGCONSOLE = "CONSOLE";
	private static final String LOGNONE = "NONE";

	@Before
	public void init() {
		file = "embedmongo.log";
		encoding = "utf8";
	}

	@Test
	public void testFile() {
		ProcessOutput processOutput = Loggers.file(file, encoding);
		assertNotNull(processOutput.getCommands());
	}

	@Test
	public void testConsole() {
		assertNotNull(Loggers.console());
	}

	@Test
	public void testNone() {
		assertNotNull(Loggers.none());
	}

	@Test
	public void testLoggingStyleFile() {
		assertEquals(LOGFILE, com.github.joelittlejohn.embedmongo.log.Loggers.LoggingStyle.FILE.name());
	}

	@Test
	public void testLoggingStyleConsole() {
		assertEquals(LOGCONSOLE, com.github.joelittlejohn.embedmongo.log.Loggers.LoggingStyle.CONSOLE.name());
	}

	@Test
	public void testLoggingStyleNone() {
		assertEquals(LOGNONE, com.github.joelittlejohn.embedmongo.log.Loggers.LoggingStyle.NONE.name());
	}

}
