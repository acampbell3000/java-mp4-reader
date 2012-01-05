package uk.co.anthonycampbell.java.mp4reader.box.stream;

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
import java.math.BigInteger;

import uk.co.anthonycampbell.java.mp4reader.box.common.AbstractBox;
import uk.co.anthonycampbell.java.mp4reader.box.common.Box;
import uk.co.anthonycampbell.java.mp4reader.reader.BoxType;
import uk.co.anthonycampbell.java.mp4reader.reader.MP4Reader;
import uk.co.anthonycampbell.java.mp4reader.util.Util;

/**
 * Class to encapsulate the MP4 media stream header box (mdhd).
 * 
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public class MdhdBox extends AbstractBox implements Box {
	
	// Declare box properties
	protected final short version;
	protected final long flags;
	protected final BigInteger creationDate;
	protected final BigInteger modifiedDate;
	protected final long timeScale;
	protected final BigInteger duration;
	protected final short language;
	protected final short quicktimeQuality;
	
	/**
	 * Constructor.
	 * 
	 * @param reader - MP4 file reader.
	 * @param remainingOffset - remaining bytes to be read from the stream.
	 * @param boxName - name of the box type.
	 * @param boxType - box type ENUM.
	 * @throws IOException Unable read remaining bytes from the stream.
	 */
	public MdhdBox(final MP4Reader reader, final long remainingOffset, final String boxName,
			final BoxType boxType) throws IOException {
		super(reader, remainingOffset, boxName, boxType);
		
		this.version = reader.readUnsignedByte();
		this.flags = reader.readHex();
		
		if (remainingOffset >= 108) {
			this.creationDate = reader.readLong();
			this.modifiedDate = reader.readLong();	
		} else {
			this.creationDate = new BigInteger("" + reader.readUnsignedInt());
			this.modifiedDate = new BigInteger("" + reader.readUnsignedInt());
		}
		
		this.timeScale = reader.readUnsignedInt();
		
		if (remainingOffset == 104 || remainingOffset == 112) {
			this.duration = reader.readLong();	
		} else {
			this.duration = new BigInteger("" + reader.readUnsignedInt());
		}
		
		this.language = reader.readShort();
		this.quicktimeQuality = reader.readShort();
		
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
	 * @return the creation date.
	 */
	public BigInteger getCreationDate() {
		return this.creationDate;
	}

	/**
	 * @return the modified date.
	 */
	public BigInteger getModifiedDate() {
		return this.modifiedDate;
	}

	/**
	 * @return the time scale.
	 */
	public long getTimeScale() {
		return this.timeScale;
	}

	/**
	 * @return the duration.
	 */
	public BigInteger getDuration() {
		return this.duration;
	}

	/**
	 * @return the language.
	 */
	public short getLanguage() {
		return this.language;
	}

	/**
	 * @return the QUICKTIME quality.
	 */
	public short getQuicktimeQuality() {
		return this.quicktimeQuality;
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append(Util.printFields(this));
		
		return builder.toString();
	}
}
