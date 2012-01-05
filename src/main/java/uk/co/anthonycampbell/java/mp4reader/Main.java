package uk.co.anthonycampbell.java.mp4reader;

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

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.anthonycampbell.java.mp4reader.filter.MP4Filter;
import uk.co.anthonycampbell.java.mp4reader.reader.MP4;
import uk.co.anthonycampbell.java.mp4reader.reader.MP4Reader;

/**
 * Main class to run / test the MP4 reader utility.
 * 
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public class Main {
	
	// Log
	private static final Logger log = LoggerFactory.getLogger(Main.class.getName());

	/**
	 * Main method.
	 * 
	 * @param args - command line argument.
	 * @throws IOException Unexpected exception.
	 */
	public static void main(final String[] args) throws IOException {
		log.info("Begin...\n");
		
		final File directory = new File(".");				
		final Collection<File> files =
				FileUtils.listFiles(directory, new MP4Filter(), TrueFileFilter.TRUE);
		
		for (final File file : files) {
//			final long startTime = Calendar.getInstance().getTimeInMillis();

//			final MP4Reader reader = new MP4Reader(file);
//			final MP4Reader reader = new MP4Reader(new File("./Set Up.m4v"));
//			final MP4 mp4 = reader.parse();
//			reader.close();
			
//			log.info("- " + mp4);
			
//			break; // One file for now
		}
		
		final MP4Reader reader = new MP4Reader(new File("./src/main/resources/mp4/Green.m4v"));
		final MP4 mp4 = reader.parse();
		reader.close();
		log.info("- " + mp4);
		
		log.info("End");
	}
	
	/**
	 * Utility method to kill the parser if its taking too long!
	 * 
	 * @param startTime - parse start time.
	 * @return whether project is taking too long.
	 */
	private static boolean tooSlow(final long startTime) {
		return (Calendar.getInstance().getTimeInMillis() - startTime) > (1000 * 5);
	}
}
