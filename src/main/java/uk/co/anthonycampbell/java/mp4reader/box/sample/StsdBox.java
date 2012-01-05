package uk.co.anthonycampbell.java.mp4reader.box.sample;

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
 * Class to encapsulate the MP4 media sample description box (stsd).
 * 
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public class StsdBox extends AbstractBox implements Box {
	
	// Log
	private static final Logger log = LoggerFactory.getLogger(StsdBox.class.getName());

	// Declare box properties
	protected final short version;
	protected final long flags;
	protected final long numberOfDescriptions;
	protected final VideoBox videoBox;
	protected final AudioBox audioBox;
	protected final TextBox textBox;
	
	/**
	 * Constructor.
	 * 
	 * @param reader - MP4 file reader.
	 * @param remainingOffset - remaining bytes to be read from the stream.
	 * @param boxName - name of the box type.
	 * @param boxType - box type ENUM.
	 * @throws IOException Unable read remaining bytes from the stream.
	 */
	public StsdBox(final MP4Reader reader, final long remainingOffset, final String boxName,
			final BoxType boxType) throws IOException {
		super(reader, remainingOffset, boxName, boxType);
		
		this.version = reader.readUnsignedByte();
		this.flags = reader.readHex();
		this.numberOfDescriptions = reader.readUnsignedInt();
		
		VideoBox videoBox = null;
		AudioBox audioBox = null;
		TextBox textBox = null;
		
		// Iterate through descriptions
		for (int i = 0; i < this.numberOfDescriptions; ++i) {
			final Box nextBox = reader.nextBox();
			
			// Validate
			if (nextBox != null) {
				if (nextBox instanceof VideoBox &&
						BoxType.SAMPLE_VIDEO_AVC1 == nextBox.getBoxType()) {
					videoBox = (VideoBox) nextBox;
					
				} else if (nextBox instanceof AudioBox &&
						BoxType.SAMPLE_AUDIO_AC3 == nextBox.getBoxType() ||
						BoxType.SAMPLE_AUDIO_MP4A == nextBox.getBoxType()) {
					audioBox = (AudioBox) nextBox;
					
				} else if (nextBox instanceof TextBox &&
						BoxType.SAMPLE_TEXT == nextBox.getBoxType()) {
					textBox = (TextBox) nextBox;	
				}
				
				log.debug("- '" + boxName + "' -> " + nextBox);	
			}
		}

		this.videoBox = videoBox;
		this.audioBox = audioBox;
		this.textBox = textBox;
		
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
	 * @return the number of descriptions.
	 */
	public long getNumberOfDescriptions() {
		return this.numberOfDescriptions;
	}
	
	/**
	 * @return the video box.
	 */
	public VideoBox getVideoBox() {
		return this.videoBox;
	}

	/**
	 * @return the audio box.
	 */
	public AudioBox getAudioBox() {
		return this.audioBox;
	}

	/**
	 * @return the textBox.
	 */
	public TextBox getTextBox() {
		return textBox;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append(Util.printFields(this));
		
		return builder.toString();
	}
}
