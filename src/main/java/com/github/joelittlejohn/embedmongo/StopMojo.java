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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import com.github.joelittlejohn.embedmongo.configuration.GlobalConfiguration;

import de.flapdoodle.embed.mongo.MongodProcess;

/**
 * When invoked, this goal stops an instance of mojo that was started by this
 * plugin.
 */
@Mojo(name="stop", defaultPhase = LifecyclePhase.POST_INTEGRATION_TEST)
public class StopMojo extends AbstractEmbeddedMongoMojo {

	@Override
    public void executeStart() throws MojoExecutionException, MojoFailureException {
        MongodProcess mongod = (MongodProcess) getPluginContext().get(StartMojo.MONGOD_CONTEXT_PROPERTY_NAME);

        if (mongod != null) {
			GlobalConfiguration.mapMongoController.get(StartMojo.MONGOD_CONTEXT_PROPERTY_NAME).cleanup();
            mongod.stop();
        } else {
            throw new MojoFailureException("No mongod process found, it appears embedmongo:start was not called");
        }
    }

}
