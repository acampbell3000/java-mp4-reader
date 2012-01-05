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
 * Class to encapsulate the MP4 media sample table box (stbl).
 * 
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public class StblBox extends AbstractBox implements Box {
	
	// Log
	private static final Logger log = LoggerFactory.getLogger(StblBox.class.getName());

	// Declare box properties
	protected final VideoBox videoSample;
	protected final AudioBox audioSample;
	protected final TextBox textSample;
	
	/**
	 * Constructor.
	 * 
	 * @param reader - MP4 file reader.
	 * @param remainingOffset - remaining bytes to be read from the stream.
	 * @param boxName - name of the box type.
	 * @param boxType - box type ENUM.
	 * @throws IOException Unable read remaining bytes from the stream.
	 */
	public StblBox(final MP4Reader reader, final long remainingOffset, final String boxName,
			final BoxType boxType) throws IOException {
		super(reader, remainingOffset, boxName, boxType);

		VideoBox videoSample = null;
		AudioBox audioSample = null;
		TextBox textSample = null;
		
		// Parse inner boxes
		while (bytesRemaining() > 0) {
			final Box nextBox = reader.nextBox();
			
			// Validate
			if (nextBox != null) {
				if (nextBox instanceof StsdBox && BoxType.SAMPLE_DESCRIPTION == nextBox.getBoxType()) {
					final StsdBox stsdBox = (StsdBox) nextBox;

					videoSample = stsdBox.getVideoBox();
					audioSample = stsdBox.getAudioBox();
					textSample = stsdBox.getTextBox();
					
				} else if (nextBox instanceof SttsBox &&
						BoxType.SAMPLE_FRAMING_TIMING == nextBox.getBoxType()) {
								
				}
				
				log.debug("- '" + boxName + "' -> " + nextBox);	
			}
		}
		
		this.videoSample = videoSample;
		this.audioSample = audioSample;
		this.textSample = textSample;

		// Clean up
		skip();
	}
	
	/**
	 * @return the video sample.
	 */
	public VideoBox getVideoSample() {
		return this.videoSample;
	}
	
	/**
	 * @return the audio sample.
	 */
	public AudioBox getAudioSample() {
		return this.audioSample;
	}
	
	/**
	 * @return the text sample.
	 */
	public TextBox getTextSample() {
		return this.textSample;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append(Util.printFields(this));
		
		return builder.toString();
	}
}
