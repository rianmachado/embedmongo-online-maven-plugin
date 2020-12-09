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

import java.io.IOException;

import de.flapdoodle.embed.mongo.config.IMongoImportConfig;
import de.flapdoodle.embed.mongo.config.MongoImportConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.config.Timeout;
import de.flapdoodle.embed.mongo.distribution.IFeatureAwareVersion;

public class MongoImportMojoHelper {

	public IMongoImportConfig buildImpotConfig(ImportDataConfig importData, IFeatureAwareVersion version, Integer port,
			boolean networkUtils, String database) throws IOException {
		return new MongoImportConfigBuilder().version(version).net(new Net(port, networkUtils)).db(database)
				.collection(importData.getCollection()).upsert(importData.getUpsertOnImport())
				.dropCollection(importData.getDropOnImport()).importFile(importData.getFile()).jsonArray(true)
				.timeout(new Timeout(importData.getTimeout())).build();
	}

}
