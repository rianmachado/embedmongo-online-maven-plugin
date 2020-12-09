/**
 * Copyright © 2015 Pablo Diaz
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

import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import de.flapdoodle.embed.mongo.distribution.Feature;
import de.flapdoodle.embed.mongo.distribution.IFeatureAwareVersion;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.distribution.Versions;

/**
 * Created by pablo on 28/03/15.
 */
public abstract class AbstractEmbeddedMongoMojo extends AbstractMojo {
	@Parameter(property = "embedmongo.skip", defaultValue = "false")
	private boolean skip;

	/**
	 * The port MongoDB should run on.
	 *
	 * @since 0.1.0
	 */
	@Parameter(property = "embedmongo.port", defaultValue = "27017")
	private int port;

	/**
	 * Whether a random free port should be used for MongoDB instead of the one
	 * specified by {@code port}. If {@code randomPort} is {@code true}, the random
	 * port chosen will be available in the Maven project property
	 * {@code embedmongo.port}.
	 *
	 * @since 0.1.8
	 */
	@Parameter(property = "embedmongo.randomPort", defaultValue = "false")
	private boolean randomPort;

	/**
	 * The version of MongoDB to run e.g. 2.1.1, 1.6 v1.8.2, V2_0_4,
	 *
	 * @since 0.1.0
	 */
	@Parameter(property = "embedmongo.version", defaultValue = "2.2.1")
	private String version;

	/**
	 * The flapdoodle features required for download e.g.
	 * sync_delay,no_http_interface_arg
	 *
	 * @since 0.4.2
	 */
	@Parameter(property = "embedmongo.features")
	private String features;

	/**
	 * Block immediately and wait until MongoDB is explicitly stopped (eg:
	 * {@literal <ctrl-c>}). This option makes this goal similar in spirit to
	 * something like jetty:run, useful for interactive debugging.
	 *
	 * @since 0.1.2
	 */
	@Parameter(property = "embedmongo.wait", defaultValue = "false")
	private boolean wait;

	@Parameter(defaultValue = "${project}", readonly = true)
	protected MavenProject project;

	@Override
	public final void execute() throws MojoExecutionException, MojoFailureException {
		if (skip) {
			onSkip();
		} else {
			executeStart();
		}
	}

	protected void onSkip() {
		// Nothing to do, this is just to allow do things if mojo is skipped
	}

	protected IFeatureAwareVersion getVersion() {

		StringBuilder versionEnumName = new StringBuilder();
		versionEnumName.append(this.version.toUpperCase().replace("\\.", "_"));

		if (versionEnumName.charAt(0) != 'V') {
			versionEnumName.append('V');
			versionEnumName.append(versionEnumName);
		}

		Feature[] loadedFeatures = new Feature[0];
		if (features != null) {
			try {
				loadedFeatures = Stream.of(features.split(",")).map(String::trim).map(String::toUpperCase)
						.map(Feature::valueOf).toArray(Feature[]::new);
			} catch (IllegalArgumentException e) {
				getLog().warn("Unrecognised feature '" + features + ". Attempting download anyway...");
			}
		}

		try {
			return Versions.withFeatures(Version.valueOf(versionEnumName.toString()), loadedFeatures);
		} catch (IllegalArgumentException e) {
			getLog().warn("Unrecognised MongoDB version '" + this.version
					+ "', this might be a new version that we don't yet know about. Attempting download anyway...");
			return Versions.withFeatures(() -> version, loadedFeatures);
		}
	}

	public String getFeatures() {
		return features;
	}

	protected Integer getPort() {
		String portStr = project.getProperties().getProperty("embedmongo.port");

		if (StringUtils.isNotBlank(portStr)) {
			return Integer.valueOf(portStr);
		} else {
			return port;
		}
	}

	public abstract void executeStart() throws MojoExecutionException, MojoFailureException;

	/**
	 * Saves port to the {@link MavenProject#getProperties()} (with the property
	 * name {@code embedmongo.port}) to allow others (plugins, tests, etc) to find
	 * the randomly allocated port.
	 *
	 * @param port the port.
	 */
	protected void savePortToProjectProperties(int port) {
		project.getProperties().put("embedmongo.port", String.valueOf(port));
	}

	public boolean isSkip() {
		return skip;
	}

	public boolean isRandomPort() {
		return randomPort;
	}

	public MavenProject getProject() {
		return project;
	}

	public void setProject(MavenProject project) {
		this.project = project;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setRandomPort(boolean randomPort) {
		this.randomPort = randomPort;
	}

}
