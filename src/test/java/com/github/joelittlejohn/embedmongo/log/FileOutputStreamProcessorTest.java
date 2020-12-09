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
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FileOutputStreamProcessorTest {

	private static String TEXT;
	private static Path logFile;
	private static String encoding;

	@BeforeClass
	public static void init() throws IOException {
		TEXT = "Log";
		logFile = Paths.get(System.getProperty("user.home")).resolve(".embedmongo/embedmongo.log");
		Files.createFile(logFile);
		encoding = "utf-8";
	}

	@AfterClass
	public static void finish() {
		try {
			if (Files.exists(logFile)) {
				if (FileOutputStreamProcessor.getStream() != null) {
					FileOutputStreamProcessor.getStream().close();
				}
				Files.delete(logFile);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testProcess() {
		FileInputStream inputStream = null;
		StringBuilder resultStringBuilder = new StringBuilder();
		try {
			FileOutputStreamProcessor fileOutputStreamProcessor = new FileOutputStreamProcessor(logFile.toString(),
					encoding);
			fileOutputStreamProcessor.process(TEXT);
			inputStream = new FileInputStream(logFile.toFile());
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			while ((line = br.readLine()) != null) {
				resultStringBuilder.append(line).append("\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		assertEquals(TEXT, resultStringBuilder.toString().trim());
	}

	@Test
	public void testProcessErro() {
		try {
			new FileOutputStreamProcessor(".?%4233.", encoding).process("log");
		} catch (Exception e) {
			assertTrue("RuntimeException esperdo ", e instanceof RuntimeException);
		}
	}

	@Test
	public void testProcessLogFileNullErro() {
		try {
			new FileOutputStreamProcessor(null, encoding).process("log");
		} catch (Exception e) {
			assertTrue("IllegalArgumentException esperdo ", e instanceof IllegalArgumentException);
		}
	}

	@Test
	public void testProcessLogFileAndEncoding() {
		String outPutTest = "FileOutputStreamProcessor [logFile=" + logFile.toString() + ", encoding=" + encoding + "]";
		assertEquals(outPutTest, new FileOutputStreamProcessor(logFile.toString(), encoding).toString());
	}

	@Test
	public void testProcessEcodinNullErro() {
		try {
			new FileOutputStreamProcessor(logFile.toString(), null).process("log");
		} catch (Exception e) {

			assertTrue("IllegalArgumentException esperdo ", e instanceof IllegalArgumentException);
		}
	}

	@Test
	public void testOnProcessed() {
		FileOutputStreamProcessor fileOutputStreamProcessor = new FileOutputStreamProcessor(logFile.toString(),
				encoding);
		fileOutputStreamProcessor.onProcessed();
		assertNotNull(FileOutputStreamProcessor.getStream());
	}

	@Test
	public void testProcessIOException() {
		try {
			new FileOutputStreamProcessor("https://localhost.demo.test", encoding).process("log");
		} catch (Exception e) {
			assertTrue(e instanceof RuntimeException);
		}
	}

}
