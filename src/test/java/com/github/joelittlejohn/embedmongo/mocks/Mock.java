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
package com.github.joelittlejohn.embedmongo.mocks;

import java.util.HashMap;

import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;

import com.github.joelittlejohn.embedmongo.ImportDataConfig;
import com.github.joelittlejohn.embedmongo.MongoImportMojo;
import com.github.joelittlejohn.embedmongo.StartMojo;
import com.github.joelittlejohn.embedmongo.StopMojo;

public class Mock {

	private Mock() {

	}

	public static StartMojo getStartMojo(String downloadPath, String version) {
		StartMojo startMojo = new StartMojo();
		startMojo.setProject(new MavenProject());
		startMojo.setDownloadPath(downloadPath);
		startMojo.setSettings(new Settings());
		startMojo.setPort(27017);
		startMojo.setVersion(version);
		startMojo.setPluginContext(new HashMap<>());
		return startMojo;
	}

	public static StopMojo getStopMojo(StartMojo startMojo, String version) {
		StopMojo stopMojo = new StopMojo();
		stopMojo.setProject(new MavenProject());
		stopMojo.setPort(27017);
		stopMojo.setVersion(version);
		stopMojo.setPluginContext(startMojo.getPluginContext());
		return stopMojo;
	}

	public static MongoImportMojo getMongoImportMojo(String pathMockJsonFile, String collection, boolean parallel,
			String dataBase, String version) {
		MongoImportMojo mongoImportMojo = new MongoImportMojo();
		ImportDataConfig config = new ImportDataConfig(dataBase, collection, pathMockJsonFile, false, false, 1000);
		ImportDataConfig[] configs = new ImportDataConfig[1];
		configs[0] = config;
		mongoImportMojo.setVersion(version);
		mongoImportMojo.setPort(27017);
		mongoImportMojo.setProject(new MavenProject());
		mongoImportMojo.setImports(configs);
		mongoImportMojo.setParallel(parallel);
		return mongoImportMojo;
	}

	public static MongoImportMojo getMongoImportMojoEmptyConfiguration(boolean parallel, String version) {
		MongoImportMojo mongoImportMojo = new MongoImportMojo();
		mongoImportMojo.setVersion(version);
		mongoImportMojo.setPort(27017);
		mongoImportMojo.setProject(new MavenProject());
		mongoImportMojo.setParallel(parallel);
		return mongoImportMojo;
	}

	public static StartMojo getStartMojoErro(String downloadPath, String version) {
		StartMojo startMojo = new StartMojo();
		startMojo.setPluginContext(new HashMap<>());
		return startMojo;
	}

	public static StopMojo getStopMojoErro(StartMojo startMojo, String version) {
		StopMojo stopMojo = new StopMojo();
		stopMojo.setProject(new MavenProject());
		stopMojo.setPort(27017);
		stopMojo.setVersion(version);
		return stopMojo;
	}

}
