package uk.co.anthonycampbell.java.mp4reader.box.common;

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

import uk.co.anthonycampbell.java.mp4reader.reader.BoxType;
import uk.co.anthonycampbell.java.mp4reader.reader.MP4Reader;
import uk.co.anthonycampbell.java.mp4reader.util.Util;

/**
 * Class to encapsulate the MP4 handler reference box (hdlr).
 * 
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public class HdlrBox extends AbstractBox implements Box {

	// Declare box properties
	protected final short hexVersion;
	protected final long flags;
	protected final String quicktimeType;
	protected final String type;
	protected final String quicktimeManufacturer;
	protected final long quicktimeFlags;
	protected final long quicktimeFlagMask;
	protected final String componentType;
	
	/**
	 * Constructor.
	 * 
	 * @param reader - MP4 file reader.
	 * @param remainingOffset - remaining bytes to be read from the stream.
	 * @param boxName - name of the box type.
	 * @param boxType - box type ENUM.
	 * @throws IOException Unable read remaining bytes from the stream.
	 */
	public HdlrBox(final MP4Reader reader, final long remainingOffset, final String boxName,
			final BoxType boxType) throws IOException {
		super(reader, remainingOffset, boxName, boxType);
		
		// Meta box specific properties
		this.hexVersion = reader.readUnsignedByte();
		this.flags = reader.readHex();
		this.quicktimeType = reader.readString();
		this.type = reader.readString();
		this.quicktimeManufacturer = reader.readString();
		this.quicktimeFlags = reader.readHex(4);
		this.quicktimeFlagMask = reader.readHex(4);
		
		// Component type
		final long bytesRemaining = bytesRemaining();
		if (bytesRemaining() > 1 && bytesRemaining < Integer.MAX_VALUE) {
			this.componentType = reader.readString(Long.valueOf(bytesRemaining() + 1).intValue());
		} else {
			this.componentType = "";
		}

		// Clean up
		skip();
	}
	
	/**
	 * @return the hex version.
	 */
	public short getHexVersion() {
		return this.hexVersion;
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
	 * @return the QUICKTIME type.
	 */
	public String getQuicktimeType() {
		return this.quicktimeType;
	}

	/**
	 * @return the meta type.
	 */
	public String getMetaType() {
		return this.type;
	}

	/**
	 * @return the QUICKTIME manufacturer.
	 */
	public String getQuicktimeManufacturer() {
		return this.quicktimeManufacturer;
	}

	/**
	 * @return the QUICKTIME flags.
	 */
	public long getQuicktimeFlags() {
		return this.quicktimeFlags;
	}

	/**
	 * @return the QUICKTIME flag mask.
	 */
	public long getQuicktimeMaskFlag() {
		return this.quicktimeFlagMask;
	}

	/**
	 * @return the component type.
	 */
	public String getComponentType() {
		return this.componentType;
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append(Util.printFields(this));
		
		return builder.toString();
	}
}
