package uk.co.anthonycampbell.java.mp4reader.util;

/**
 * Copyright 2011 Anthony Campbell (anthonycampbell.co.uk)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.anthonycampbell.java.mp4reader.Main;

/**
 * Small test suite to test Util XML parsing.
 * 
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public class UtilTest {
	
	// Log
	private static final Logger log = LoggerFactory.getLogger(Main.class.getName());

	// Test data
	private static String xml;
	
	/**
	 * Prepare test XML for Util parser.
	 * 
	 * @throws Exception - Unable to prepare test XML.
	 */
	@BeforeClass
	public static void setUp() throws Exception {
		final StringBuilder builder = new StringBuilder();
		final BufferedReader reader = new BufferedReader(
				new FileReader("./src/main/resources/documents/plist.xml"));

		// Read test file contents
		String line = null;
		while ((line = reader.readLine()) != null) {
		    builder.append(line + "\n");
		}
		
		xml = builder.toString();
		log.info(xml);
	}

	@Test
	public void testParse() {
		final Map<String, List<String>> result = Util.parseITunesMeta(xml);

		assertThat(result.isEmpty(), not(equalTo(true)));
		assertThat(result.keySet().size(), equalTo(4));
	}
}
