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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MongoImportMojoTest {

	private static String pathMockJsonFile;

	@BeforeClass
	public static void init() {
		pathMockJsonFile = MongoImportMojoTest.class.getClassLoader().getResource("demo-test.json").getPath();
		String osName = System.getProperty("os.name");
		if (osName.startsWith("Windows", 0)) {
			pathMockJsonFile = pathMockJsonFile.substring(1);
		}
	}

	@Test
	public void testExecuteImportMojoDataBase() {

		StartMojo startMojo = new StartMojo();
		startMojo.setProject(new MavenProject());
		startMojo.setDownloadPath("http://fastdl.mongodb.org/");
		startMojo.setSettings(new Settings());
		startMojo.setPort(27017);
		startMojo.setVersion("2.7.1");
		startMojo.setPluginContext(new HashMap<>());

		MongoImportMojo mongoImportMojo = new MongoImportMojo();
		ImportDataConfig config = new ImportDataConfig("demo", "collection-demo", pathMockJsonFile, false, false, 1000);
		ImportDataConfig[] configs = new ImportDataConfig[1];
		configs[0] = config;
		mongoImportMojo.setVersion("2.7.1");
		mongoImportMojo.setPort(27017);
		mongoImportMojo.setProject(new MavenProject());
		mongoImportMojo.setImports(configs);
		mongoImportMojo.setParallel(false);

		StopMojo stopMojo = new StopMojo();
		stopMojo.setProject(new MavenProject());
		stopMojo.setPort(27017);
		stopMojo.setVersion("2.7.1");
		stopMojo.setPluginContext(startMojo.getPluginContext());

		try {
			startMojo.executeStart();
			mongoImportMojo.execute();
			stopMojo.execute();
			assertNotNull(mongoImportMojo.getLog());
		} catch (MojoExecutionException | MojoFailureException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testExecuteImportMojoDataBaseNotStarted() {

		int port = 0;
		try {
			port = NetworkUtils.allocateRandomPort();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		StartMojo startMojo = new StartMojo();
		startMojo.setProject(new MavenProject());
		startMojo.setDownloadPath("http://fastdl.mongodb.org/");
		startMojo.setSettings(new Settings());
		startMojo.setPort(port);
		startMojo.setVersion("2.7.1");
		startMojo.setPluginContext(new HashMap<>());

		MongoImportMojo mongoImportMojo = new MongoImportMojo();
		ImportDataConfig config = new ImportDataConfig("demo", "", pathMockJsonFile, false, false, 800);
		ImportDataConfig[] configs = new ImportDataConfig[1];
		configs[0] = config;
		mongoImportMojo.setVersion("2.7.1");
		mongoImportMojo.setPort(27017);
		mongoImportMojo.setProject(new MavenProject());
		mongoImportMojo.setImports(configs);
		mongoImportMojo.setParallel(true);
		try {
			mongoImportMojo.execute();
		} catch (MojoExecutionException | MojoFailureException e) {
			String erro = e.getLocalizedMessage().substring(0, 15);
			assertEquals("Could not start", erro);
		}
	}

	@Test
	public void testExecuteImportMojoDataBaseRequired() {

		int port = 0;
		try {
			port = NetworkUtils.allocateRandomPort();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		StartMojo startMojo = new StartMojo();
		startMojo.setProject(new MavenProject());
		startMojo.setDownloadPath("http://fastdl.mongodb.org/");
		startMojo.setSettings(new Settings());
		startMojo.setPort(port);
		startMojo.setVersion("2.7.1");
		startMojo.setPluginContext(new HashMap<>());

		MongoImportMojo mongoImportMojo = new MongoImportMojo();
		ImportDataConfig config = new ImportDataConfig("", "", pathMockJsonFile, false, false, 800);
		ImportDataConfig[] configs = new ImportDataConfig[1];
		configs[0] = config;
		mongoImportMojo.setVersion("2.7.1");
		mongoImportMojo.setPort(27017);
		mongoImportMojo.setProject(new MavenProject());
		mongoImportMojo.setImports(configs);
		mongoImportMojo.setParallel(true);
		try {
			mongoImportMojo.execute();
		} catch (MojoExecutionException | MojoFailureException e) {
			assertTrue(e.getLocalizedMessage().startsWith("Database is required"));
		}
	}

	@Test
	public void testExecuteImportMojoDataBaseNotStartedConfigEmpty() {

		int port = 0;
		try {
			port = NetworkUtils.allocateRandomPort();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		StartMojo startMojo = new StartMojo();
		startMojo.setProject(new MavenProject());
		startMojo.setDownloadPath("http://fastdl.mongodb.org/");
		startMojo.setSettings(new Settings());
		startMojo.setPort(port);
		startMojo.setVersion("2.7.1");
		startMojo.setPluginContext(new HashMap<>());

		MongoImportMojo mongoImportMojo = new MongoImportMojo();
		mongoImportMojo.setVersion("2.7.1");
		mongoImportMojo.setPort(27017);
		mongoImportMojo.setProject(new MavenProject());
		mongoImportMojo.setParallel(true);
		try {
			mongoImportMojo.execute();
			assertNull(mongoImportMojo.getMongoImportProcess());
		} catch (MojoExecutionException | MojoFailureException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testExecuteImportMojoDataBaseParallelTrue() {

		StartMojo startMojo = new StartMojo();
		startMojo.setProject(new MavenProject());
		startMojo.setDownloadPath("http://fastdl.mongodb.org/");
		startMojo.setSettings(new Settings());
		startMojo.setPort(27017);
		startMojo.setVersion("2.7.1");
		startMojo.setPluginContext(new HashMap<>());

		MongoImportMojo mongoImportMojo = new MongoImportMojo();
		ImportDataConfig config = new ImportDataConfig("demo", "collection-demo", pathMockJsonFile, false, false, 1000);
		ImportDataConfig[] configs = new ImportDataConfig[1];
		configs[0] = config;
		mongoImportMojo.setVersion("2.7.1");
		mongoImportMojo.setPort(27017);
		mongoImportMojo.setProject(new MavenProject());
		mongoImportMojo.setImports(configs);
		mongoImportMojo.setParallel(true);

		StopMojo stopMojo = new StopMojo();
		stopMojo.setProject(new MavenProject());
		stopMojo.setPort(27017);
		stopMojo.setVersion("2.7.1");
		stopMojo.setPluginContext(startMojo.getPluginContext());

		try {
			startMojo.executeStart();
			mongoImportMojo.execute();
			stopMojo.execute();
			assertNotNull(mongoImportMojo.getLog());
		} catch (MojoExecutionException | MojoFailureException e) {
			e.printStackTrace();
		}
	}

}
