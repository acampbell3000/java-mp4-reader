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
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.anthonycampbell.java.mp4reader.box.common.AbstractBox;
import uk.co.anthonycampbell.java.mp4reader.box.common.Box;
import uk.co.anthonycampbell.java.mp4reader.box.sample.AudioBox;
import uk.co.anthonycampbell.java.mp4reader.box.sample.TextBox;
import uk.co.anthonycampbell.java.mp4reader.box.sample.VideoBox;
import uk.co.anthonycampbell.java.mp4reader.reader.BoxType;
import uk.co.anthonycampbell.java.mp4reader.reader.MP4Reader;
import uk.co.anthonycampbell.java.mp4reader.util.Util;

/**
 * Class to encapsulate the MP4 media stream box (mdia).
 * 
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public class MdiaBox extends AbstractBox implements Box {
	
	// Log
	private static final Logger log = LoggerFactory.getLogger(MdiaBox.class.getName());

	// Declare properties
	protected final Date creationDate;
	protected final Date modifiedDate;
	protected final long timeScale;
	protected final long totalBlockSize;
	protected final BigInteger duration;
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
	public MdiaBox(final MP4Reader reader, final long remainingOffset, final String boxName,
			final BoxType boxType) throws IOException {
		super(reader, remainingOffset, boxName, boxType);
		
		// Set defaults
		Date creationDate = new Date();
		Date modifiedDate = new Date();
		long timeScale = 0;
		long totalBlockSize = 0;
		BigInteger duration = new BigInteger("0");
		VideoBox videoSample = null;
		AudioBox audioSample = null;
		TextBox textSample = null;
		
		// Parse inner boxes
		while (bytesRemaining() > 0) {
			final Box nextBox = reader.nextBox();
			
			// Validate
			if (nextBox != null) {
				if (nextBox instanceof MdhdBox && BoxType.MEDIA_STREAM_HEADER == nextBox.getBoxType()) {
					final MdhdBox mdhdBox = (MdhdBox) nextBox;

					final BigInteger bigCreationDate = mdhdBox.getCreationDate();
					final BigInteger bigModifiedDate = mdhdBox.getModifiedDate();
					if (bigCreationDate != null) {
						creationDate = Util.generateDate(bigCreationDate);
					}
					if (bigModifiedDate != null) {
						modifiedDate = Util.generateDate(bigModifiedDate);
					}

					timeScale = mdhdBox.getTimeScale();
					duration = mdhdBox.getDuration();
					
				} else if (nextBox instanceof MinfBox && BoxType.MEDIA_STREAM_INFORMATION == nextBox.getBoxType()) {
					final MinfBox minfBox = (MinfBox) nextBox;
					
					videoSample = minfBox.getVideoSample();
					audioSample = minfBox.getAudioSample();
					textSample = minfBox.getTextSample();
					totalBlockSize = minfBox.getTotalBlockSize();
				}
				
				log.debug("- '" + boxName + "' -> " + nextBox);	
			}
		}

		// Update class members
		this.creationDate = creationDate;
		this.modifiedDate = modifiedDate;
		this.timeScale = timeScale;
		this.totalBlockSize = totalBlockSize;
		this.duration = duration;
		this.videoSample = videoSample;
		this.audioSample = audioSample;
		this.textSample = textSample;

		// Clean up
		skip();
	}
	
	/**
	 * @return the creation date.
	 */
	public Date getCreationDate() {
		return this.creationDate;
	}
	
	/**
	 * @return the modified date.
	 */
	public Date getModifiedDate() {
		return this.modifiedDate;
	}

	/**
	 * @return the time scale.
	 */
	public long getTimeScale() {
		return this.timeScale;
	}

	/**
	 * @return the total block size.
	 */
	public long getTotalBlockSize() {
		return this.totalBlockSize;
	}
	
	/**
	 * @return the duration.
	 */
	public BigInteger getDuration() {
		return this.duration;
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
