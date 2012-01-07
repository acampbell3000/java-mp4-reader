package uk.co.anthonycampbell.java.mp4reader.box.media;

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
 * Class to encapsulate the MP4 initial object descriptor box (iods).
 * 
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public class IodsBox extends AbstractBox implements Box {
	
	// Declare box properties
	protected final short hexVersion;
	protected final long flags;
	protected final long fileType;
	protected final long descriptorTypeTags;
	protected final short descriptorTypeLength;
	protected final int descriptorId;
	protected final short descriptorProfile;
	protected final short sceneProfileLevel;
	protected final short audioProfileLevel;
	protected final short videoProfileLevel;
	protected final short graphicsProfileLevel;
	
	/**
	 * Constructor.
	 * 
	 * @param reader - MP4 file reader.
	 * @param remainingOffset - remaining bytes to be read from the stream.
	 * @param boxName - name of the box type.
	 * @param boxType - box type ENUM.
	 * @throws IOException Unable read remaining bytes from the stream.
	 */
	public IodsBox(final MP4Reader reader, final long remainingOffset, final String boxName,
			final BoxType boxType) throws IOException {
		super(reader, remainingOffset, boxName, boxType);
		
		// Meta box specific properties
		this.hexVersion = reader.readUnsignedByte();
		this.flags = reader.readHex();
		this.fileType = reader.readHex(1);
		this.descriptorTypeTags = reader.readHex();
		this.descriptorTypeLength = reader.readUnsignedByte();
		
		if (this.descriptorTypeLength == 7) {
			this.descriptorId = reader.readUnsignedShort();
			this.descriptorProfile = reader.readUnsignedByte();
			this.sceneProfileLevel = reader.readUnsignedByte();
			this.audioProfileLevel = reader.readUnsignedByte();
			this.videoProfileLevel = reader.readUnsignedByte();
			this.graphicsProfileLevel = reader.readUnsignedByte();
		} else {
			this.descriptorId = 0;
			this.descriptorProfile = 0;
			this.sceneProfileLevel = 0;
			this.audioProfileLevel = 0;
			this.videoProfileLevel = 0;
			this.graphicsProfileLevel = 0;
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
	 * @return the IOD file type.
	 */
	public long getFileType() {
		return this.fileType;
	}
	
	/**
	 * @return the descriptor type tags.
	 */
	public long getDescriptorTypeTags() {
		return this.descriptorTypeTags;
	}
	
	/**
	 * @return the descriptor type length.
	 */
	public long getDescriptorTypeLength() {
		return this.descriptorTypeLength;
	}
	
	/**
	 * @return the descriptor ID.
	 */
	public long getDescriptorId() {
		return this.descriptorId;
	}
	
	/**
	 * @return the descriptor profile.
	 */
	public short getDescriptorProfile() {
		return this.descriptorProfile;
	}

	/**
	 * @return the scene profile level.
	 */
	public short getSceneProfileLevel() {
		return this.sceneProfileLevel;
	}

	/**
	 * @return the audio profile level.
	 */
	public short getAudioProfileLevel() {
		return this.audioProfileLevel;
	}

	/**
	 * @return the video profile level.
	 */
	public short getVideoProfileLevel() {
		return this.videoProfileLevel;
	}

	/**
	 * @return the graphics profile level.
	 */
	public short getGraphicsProfileLevel() {
		return this.graphicsProfileLevel;
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append(Util.printFields(this));
		
		return builder.toString();
	}
}
