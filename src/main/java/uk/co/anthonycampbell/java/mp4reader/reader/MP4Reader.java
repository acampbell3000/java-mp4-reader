package uk.co.anthonycampbell.java.mp4reader.reader;

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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.anthonycampbell.java.mp4reader.box.common.Box;
import uk.co.anthonycampbell.java.mp4reader.box.free.UnknownBox;

/**
 * Factory class to create box instances.
 *
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public class MP4Reader extends MP4InputStream {
	
	// Log
	private static final Logger log = LoggerFactory.getLogger(MP4Reader.class.getName());
	
	/**
	 * Constructor.
	 * 
	 * @param file - the MP4 file to read.
	 * @throws IOException - Unable to read MP4 file.
	 * @throws IllegalArgumentException - Provided argument was invalid!
	 */
	public MP4Reader(final File file) throws IllegalArgumentException, IOException {
		super(file);
	}

	/**
	 * Parse the MP4 file reference and construct a {@link MP4} instance.
	 * 
	 * @return MP4 instance.
	 * @throws IOException - Unable to parse MP4 file.
	 */
	public MP4 parse() throws IOException {
		// If we've read before we need to reset.
		if (bytesRead() > 0) {
			reset();
			this.mp4Instance = new MP4(null);
		}
		
		// Validate
		while (available() >= 8) {
			final Box nextBox = nextBox();
			
			log.debug(">>>");
			log.debug("" + nextBox);
			log.debug("<<<\n");
			
			this.mp4Instance.add(nextBox);
		}
		
		return this.mp4Instance;
	}
	
	/**
	 * Read the next MP4 box from the MP4 input stream.
	 * 
	 * @return the next box read from the input stream.
	 * @throws IOException Unable to next read box from the input stream.
	 */
	public Box nextBox() throws IOException {
		// Declare result
		final Box nextBox;

		// Validate
		if (available() > 0) {
			final long offset = readUnsignedInt();
			final byte[] boxTypeBytes = readBytes();
			final String boxType;
			
			// Validate
			final String firstHex = Integer.toHexString(boxTypeBytes[0]);
			if (StringUtils.isNotEmpty(firstHex) && firstHex.toUpperCase().equals("FFFFFFA9")) {
				boxType = new String(boxTypeBytes, 1, boxTypeBytes.length - 1);
			} else {
				boxType = new String(boxTypeBytes);
			}
			
			// Read next box
			nextBox = read(offset - 8, boxType);
		} else {
			nextBox = null;
		}
		
		return nextBox;
	}

	/**
	 * Initialise the provided MP4 box type.
	 * 
	 * @param remainingOffset - remaining offset.
	 * @param boxName - name of the box type.
	 * @param boxType - box type ENUM.
	 * @return initialised box type.
	 * @throws IOException Unable to create box from remaining input stream.
	 */
	private Box read(final long remainingOffset, final String boxName) throws IOException {
		// Validate
		if (StringUtils.isNotEmpty(boxName) && available() >= remainingOffset) {
			final BoxType[] boxTypesEnums = BoxType.values();
			
			for (final BoxType boxTypeEnum : boxTypesEnums) {
				if (boxName.equals(boxTypeEnum.getName())) {
					Class<? extends Box> boxTypeClass = boxTypeEnum.getClazz();
					Constructor<? extends Box> boxTypeConstructor;
					
					try {
						boxTypeConstructor =
								boxTypeClass.getDeclaredConstructor(
										MP4Reader.class, long.class, String.class, BoxType.class);
						return boxTypeConstructor.newInstance(this, remainingOffset, boxName, boxTypeEnum);
					}
					catch (SecurityException se) { }
					catch (NoSuchMethodException nsme) { }
					catch (IllegalArgumentException iae) { }
					catch (InstantiationException ie) { }
					catch (IllegalAccessException iae) { }
					catch (InvocationTargetException ite) { }
				}				
			}
			
			return new UnknownBox(this, remainingOffset, boxName, null);
		}
		
		return null;
	}
}
