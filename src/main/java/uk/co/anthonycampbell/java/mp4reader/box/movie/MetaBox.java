package uk.co.anthonycampbell.java.mp4reader.box.movie;

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
import uk.co.anthonycampbell.java.mp4reader.box.item.IlstBox;
import uk.co.anthonycampbell.java.mp4reader.reader.BoxType;
import uk.co.anthonycampbell.java.mp4reader.reader.MP4Reader;
import uk.co.anthonycampbell.java.mp4reader.util.Util;

/**
 * Class to encapsulate the MP4 movie presentation meta data box (meta).
 * 
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public class MetaBox extends AbstractBox implements Box {
	
	// Log
	private static final Logger log = LoggerFactory.getLogger(MetaBox.class.getName());

	// Declare box properties
	protected final short version;
	protected final long flags;
	protected final IlstBox metaData;
	
	/**
	 * Constructor.
	 * 
	 * @param reader - MP4 file reader.
	 * @param remainingOffset - remaining bytes to be read from the stream.
	 * @param boxName - name of the box type.
	 * @param boxType - box type ENUM.
	 * @throws IOException Unable read remaining bytes from the stream.
	 */
	public MetaBox(final MP4Reader reader, final long remainingOffset, final String boxName,
			final BoxType boxType) throws IOException {
		super(reader, remainingOffset, boxName, boxType);
		
		// Meta box specific properties
		this.version = reader.readUnsignedByte();
		this.flags = reader.readHex();
		
		// Set defaults
		IlstBox metaData = null;
		
		// Parse inner boxes
		while (bytesRemaining() > 0) {
			final Box nextBox = reader.nextBox();
			
			// Validate
			if (nextBox != null) {
				if (nextBox instanceof IlstBox && BoxType.APPLE_ITEM_LIST == nextBox.getBoxType()) {
					metaData = (IlstBox) nextBox;			
				}
				
				log.debug("- '" + boxName + "' -> " + nextBox);	
			}
		}
		
		this.metaData = metaData;

		// Clean up
		skip();
	}
	
	/**
	 * @return the version.
	 */
	public short getVersion() {
		return this.version;
	}
	
	/**
	 * @return the hex flags.
	 */
	public long getFlags() {
		return this.flags;
	}
	
	/**
	 * @return the hex flags string.
	 */
	public String getFlagsHexString() {
		return Long.toHexString(this.flags);
	}
	
	/**
	 * @return the meta data.
	 */
	public IlstBox getMetaData() {
		return this.metaData;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append(Util.printFields(this));
		
		return builder.toString();
	}
}
