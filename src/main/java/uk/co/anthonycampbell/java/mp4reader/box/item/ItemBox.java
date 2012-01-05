package uk.co.anthonycampbell.java.mp4reader.box.item;

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
import uk.co.anthonycampbell.java.mp4reader.reader.BoxType;
import uk.co.anthonycampbell.java.mp4reader.reader.MP4Reader;
import uk.co.anthonycampbell.java.mp4reader.util.Util;

/**
 * Class to encapsulate the MP4 Apple item box.
 * 
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public class ItemBox extends AbstractBox implements Box {
	
	// Log
	private static final Logger log = LoggerFactory.getLogger(ItemBox.class.getName());
	
	// Declare properties
	protected final byte[] data;
	protected final String boxName;
	protected final BoxType boxType;
	
	/**
	 * Constructor.
	 * 
	 * @param reader - MP4 file reader.
	 * @param remainingOffset - remaining bytes to be read from the stream.
	 * @param boxName - name of the box type.
	 * @param boxType - box type ENUM.
	 * @throws IOException Unable read remaining bytes from the stream.
	 */
	public ItemBox(final MP4Reader reader, final long remainingOffset, final String boxName,
			final BoxType boxType) throws IOException {
		super(reader, remainingOffset, boxName, boxType);
		
		// Set defaults
		String boxNameValue = super.getBoxName();
		BoxType boxTypeValue = super.getBoxType();
		byte[] data = null;
		
		// Parse data box
		if (bytesRemaining() >= 8) {
			final Box nextBox = reader.nextBox();

			log.debug("- '" + boxType + "' -> " + nextBox);
			
			// Validate
			if (nextBox != null) {
				if (nextBox instanceof DataBox) {
					data = ((DataBox) nextBox).getData();
					
				} else if (nextBox instanceof AdditionalInfoBox) {
					// iTunes specific pay load?
					switch (boxType) {
						case APPLE_ITEM_ITUNES:
							final Box key = reader.nextBox();
							final Box value = reader.nextBox();
							
							// Validate
							if (key != null && value != null) {
								boxNameValue = ((AdditionalInfoBox) key).getText().trim();
								boxTypeValue = ((AdditionalInfoBox) key).getBoxType();
								data = ((DataBox) value).getData();	
							}
							break;
							
						default:
							data = ((AdditionalInfoBox) nextBox).getText().getBytes();		
							break;
					}					
				}
			}
		}

		this.boxName = boxNameValue;
		this.boxType = boxTypeValue;
		this.data = data;
		
		// Clean up
		skip();
	}
	
	@Override
	public String getBoxName() {
		return this.boxName;
	}

	@Override
	public BoxType getBoxType() {
		return this.boxType;
	}
	
	/**
	 * @return the item data.
	 */
	public byte[] getData() {
		return this.data;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append(Util.printFields(this));
		
		return builder.toString();
	}
}
