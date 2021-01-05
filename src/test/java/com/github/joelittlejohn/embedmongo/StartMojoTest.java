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
package com.github.joelittlejohn.embedmongo;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yaml.snakeyaml.Yaml;
import com.github.joelittlejohn.embedmongo.log.Loggers;
import com.github.joelittlejohn.embedmongo.mocks.Mock;

/**
 * @author rianmachado@gmail.com
 */
@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class StartMojoTest {

	private static String downloadPath;

	private static String version;

	@BeforeAll
	public static void init() {
		Yaml yaml = new Yaml();
		InputStream inputStream = StartMojoTest.class.getClassLoader()
				.getResourceAsStream("applicationTest.yaml");
		Map<String, Object> objPropertie = yaml.load(inputStream);

		downloadPath = objPropertie.get("downloadpath-binary").toString();
		version = objPropertie.get("version-binary").toString();

		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testExecuteStartStop() {

		StartMojo startMojo = Mock.getStartMojo(downloadPath, version);
		startMojo.setLogging(Loggers.LoggingStyle.CONSOLE.name());
		startMojo.onSkip();
		StopMojo stopMojo = Mock.getStopMojo(startMojo, version);

		try {
			startMojo.executeStart();
			stopMojo.execute();
			assertNotNull(startMojo.getPluginContext().get(StartMojo.MONGOD_CONTEXT_PROPERTY_NAME));
		} catch (MojoExecutionException | MojoFailureException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testExecuteStartStopRandomPortTrue() {

		StartMojo startMojo = Mock.getStartMojo(downloadPath, version);
		startMojo.setLogging(Loggers.LoggingStyle.CONSOLE.name());
		startMojo.onSkip();
		startMojo.setRandomPort(true);

		StopMojo stopMojo = Mock.getStopMojo(startMojo, version);
		try {
			startMojo.executeStart();
			stopMojo.execute();
			assertNotNull(startMojo.getPluginContext().get(StartMojo.MONGOD_CONTEXT_PROPERTY_NAME));
		} catch (MojoExecutionException | MojoFailureException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testExecuteStartStopErro() {
		StartMojo startMojo = Mock.getStartMojoErro(downloadPath, version);
		startMojo.setLogging(Loggers.LoggingStyle.FILE.name());
		StopMojo stopMojo = Mock.getStopMojoErro(startMojo, version);

		HashMap<String, String> map = new HashMap<>();
		map.put(StartMojo.MONGOD_CONTEXT_PROPERTY_NAME, "");
		stopMojo.setPluginContext(startMojo.getPluginContext());
		startMojo.onSkip();
		try {
			stopMojo.execute();
		} catch (MojoExecutionException | MojoFailureException e) {
			assertNull(startMojo.getFeatures());
			assertNull(startMojo.getProject());
			assertTrue("No mongod process found, it appears embedmongo:start was not called"
					.equalsIgnoreCase(e.getLocalizedMessage()));
		}

	}

	@Test
	public void testExecuteStartStopPortNotEmpty() {
		StartMojo startMojo = Mock.getStartMojo(downloadPath, version);
		startMojo.setLogging(Loggers.LoggingStyle.CONSOLE.name());
		startMojo.onSkip();
		MavenProject mavenProject = new MavenProject();
		mavenProject.getProperties().put("embedmongo.port", "25118");
		startMojo.setProject(mavenProject);

		StopMojo stopMojo = Mock.getStopMojo(startMojo, version);
		stopMojo.setPort(25118);

		try {
			startMojo.executeStart();
			stopMojo.execute();
			assertEquals(25118, startMojo.getPort().intValue());
		} catch (MojoExecutionException | MojoFailureException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testExecuteStartStopLogFile() {

		int port = 0;
		try {
			port = NetworkUtils.allocateRandomPort();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		StartMojo startMojo = Mock.getStartMojo(downloadPath, version);
		startMojo.setLogFile("embedmongo.log");
		startMojo.setLogFileEncoding("utf-8");
		startMojo.setLogging(Loggers.LoggingStyle.FILE.name());
		startMojo.onSkip();
		startMojo.setPort(port);

		StopMojo stopMojo = Mock.getStopMojo(startMojo, version);
		stopMojo.setPort(port);

		try {
			startMojo.executeStart();
			stopMojo.execute();
			assertNotNull(startMojo.getPluginContext().get(StartMojo.MONGOD_CONTEXT_PROPERTY_NAME));
		} catch (MojoExecutionException | MojoFailureException e) {
			e.printStackTrace();
		}

	}

}
