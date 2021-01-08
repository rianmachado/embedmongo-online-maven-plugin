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
import java.util.Map;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yaml.snakeyaml.Yaml;

import com.github.joelittlejohn.embedmongo.mocks.Mock;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class MongoImportMojoTest {

	private static String pathMockJsonFile;

	private static String downloadPath;

	private static String version;

	@BeforeAll
	public static void init() {
		pathMockJsonFile = MongoImportMojoTest.class.getClassLoader().getResource("demo-test.json").getPath();
		String osName = System.getProperty("os.name");
		if (osName.startsWith("Windows", 0)) {
			pathMockJsonFile = pathMockJsonFile.substring(1);
		}

		Yaml yaml = new Yaml();
		InputStream inputStream = MongoImportMojoTest.class.getClassLoader()
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
	public void testExecuteImportMojoDataBase() throws MojoExecutionException, MojoFailureException {
		StartMojo startMojo = Mock.getStartMojo(downloadPath, version);
		MongoImportMojo mongoImportMojo = Mock.getMongoImportMojo(pathMockJsonFile, "collection-demo", false, "demo",
				version);
		StopMojo stopMojo = Mock.getStopMojo(startMojo, version);
		startMojo.executeStart();
		mongoImportMojo.execute();
		stopMojo.execute();
		assertNotNull(mongoImportMojo.getLog());

	}

	@Test
	public void testExecuteImportMojoDataBaseNotStarted() {

		Mock.getStartMojo(downloadPath, version);
		MongoImportMojo mongoImportMojo = Mock.getMongoImportMojo(pathMockJsonFile, "", true, "demo", version);
		try {
			mongoImportMojo.execute();
		} catch (MojoExecutionException | MojoFailureException e) {
			String erro = e.getLocalizedMessage().substring(0, 15);
			assertEquals("Could not start", erro);
		}
	}

	@Test
	public void testExecuteImportMojoDataBaseRequired() {
		Mock.getStartMojo(downloadPath, version);
		MongoImportMojo mongoImportMojo = Mock.getMongoImportMojo(pathMockJsonFile, "", true, "", version);
		try {
			mongoImportMojo.execute();
		} catch (MojoExecutionException | MojoFailureException e) {
			assertTrue(e.getLocalizedMessage().startsWith("Database is required"));
		}
	}

	@Test
	public void testExecuteImportMojoDataBaseNotStartedConfigEmpty()
			throws MojoExecutionException, MojoFailureException {
		Mock.getStartMojo(downloadPath, version);
		MongoImportMojo mongoImportMojo = Mock.getMongoImportMojoEmptyConfiguration(true, version);
		mongoImportMojo.execute();
		assertNull(mongoImportMojo.getMongoImportProcess());
	}

	@Test
	public void testExecuteImportMojoDataBaseParallelTrue() throws MojoExecutionException, MojoFailureException {

		StartMojo startMojo = Mock.getStartMojo(downloadPath, version);
		MongoImportMojo mongoImportMojo = Mock.getMongoImportMojo(pathMockJsonFile, "collection-demo", false, "demo",
				version);
		StopMojo stopMojo = Mock.getStopMojo(startMojo, version);
		startMojo.executeStart();
		mongoImportMojo.execute();
		stopMojo.execute();
		assertNotNull(mongoImportMojo.getLog());

	}

}
