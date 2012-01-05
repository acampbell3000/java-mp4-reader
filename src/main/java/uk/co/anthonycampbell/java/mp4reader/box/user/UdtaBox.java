package uk.co.anthonycampbell.java.mp4reader.box.user;

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

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.anthonycampbell.java.mp4reader.box.common.AbstractBox;
import uk.co.anthonycampbell.java.mp4reader.box.common.Box;
import uk.co.anthonycampbell.java.mp4reader.box.item.AdditionalInfoBox;
import uk.co.anthonycampbell.java.mp4reader.box.movie.MetaBox;
import uk.co.anthonycampbell.java.mp4reader.reader.BoxType;
import uk.co.anthonycampbell.java.mp4reader.reader.MP4Reader;
import uk.co.anthonycampbell.java.mp4reader.util.Util;

/**
 * Class to encapsulate the MP4 user data box (udta).
 * 
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public class UdtaBox extends AbstractBox implements Box {
	
	// Log
	private static final Logger log = LoggerFactory.getLogger(UdtaBox.class.getName());

	// Declare box properties
	protected final String name;
	
	/**
	 * Constructor.
	 * 
	 * @param reader - MP4 file reader.
	 * @param remainingOffset - remaining bytes to be read from the stream.
	 * @param boxName - name of the box type.
	 * @param boxType - box type ENUM.
	 * @throws IOException Unable read remaining bytes from the stream.
	 */
	public UdtaBox(final MP4Reader reader, final long remainingOffset, final String boxName,
			final BoxType boxType) throws IOException {
		super(reader, remainingOffset, boxName, boxType);
		
		// Initialise defaults
		String name = "";
		
		// Parse inner boxes
		while (bytesRemaining() > 0) {
			final Box nextBox = reader.nextBox();
			
			// Validate
			if (nextBox != null) {
				if (nextBox instanceof AdditionalInfoBox &&
						BoxType.APPLE_ITEM_ADDITIONAL_NAME == nextBox.getBoxType()) {
					final AdditionalInfoBox additionalInfoBox = (AdditionalInfoBox) nextBox;

					name = additionalInfoBox.getText();
					
				} else if (nextBox instanceof MetaBox &&
						BoxType.MOVIE_PRESENTATION_META_DATA == nextBox.getBoxType()) {
					final MetaBox metaBox = (MetaBox) nextBox;

//					name = metaBox;				
				}
				
				log.debug("- '" + boxName + "' -> " + nextBox);	
			}
		}
		
		this.name = name;

		// Clean up
		skip();
	}
	
	/**
	 * @return the name.
	 */
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append(Util.printFields(this));
		
		return builder.toString();
	}
}
