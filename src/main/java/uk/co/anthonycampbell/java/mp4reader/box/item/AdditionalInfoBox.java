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

import uk.co.anthonycampbell.java.mp4reader.box.common.AbstractBox;
import uk.co.anthonycampbell.java.mp4reader.box.common.Box;
import uk.co.anthonycampbell.java.mp4reader.reader.BoxType;
import uk.co.anthonycampbell.java.mp4reader.reader.MP4Reader;
import uk.co.anthonycampbell.java.mp4reader.util.Util;

/**
 * Class to encapsulate the MP4 Apple item data box.
 * 
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public class AdditionalInfoBox extends AbstractBox implements Box {

	// Declare box properties
	protected final String text;
	
	/**
	 * Constructor.
	 * 
	 * @param reader - MP4 file reader.
	 * @param remainingOffset - remaining bytes to be read from the stream.
	 * @param boxName - name of the box type.
	 * @param boxType - box type ENUM.
	 * @throws IOException Unable read remaining bytes from the stream.
	 */
	public AdditionalInfoBox(final MP4Reader reader, final long remainingOffset, final String boxName,
			final BoxType boxType) throws IOException {
		super(reader, remainingOffset, boxName, boxType);

		// Any text available
		if (bytesRemaining() > 0) {
			this.text = reader.readString(Long.valueOf(bytesRemaining()).intValue());
		} else {
			this.text = "";
		}
		
		// Clean up
		skip();
	}
	
	/**
	 * @return data text.
	 */
	public String getText() {
		return this.text;
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append(Util.printFields(this));
		
		return builder.toString();
	}
}
