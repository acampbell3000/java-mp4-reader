package uk.co.anthonycampbell.java.mp4reader.box.track;

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
import uk.co.anthonycampbell.java.mp4reader.box.stream.MdiaBox;
import uk.co.anthonycampbell.java.mp4reader.box.user.UdtaBox;
import uk.co.anthonycampbell.java.mp4reader.reader.BoxType;
import uk.co.anthonycampbell.java.mp4reader.reader.MP4Reader;
import uk.co.anthonycampbell.java.mp4reader.util.Util;

/**
 * Class to encapsulate the MP4 track box (trak).
 * 
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public class TrakBox extends AbstractBox implements Box {
	
	// Log
	private static final Logger log = LoggerFactory.getLogger(TrakBox.class.getName());
	
	// Declare properties
	protected final long trackId;
	protected final String trackName;
	protected final Type type;
	protected final Date creationDate;
	protected final Date modifiedDate;
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
	public TrakBox(final MP4Reader reader, final long remainingOffset, final String boxName,
			final BoxType boxType) throws IOException {
		super(reader, remainingOffset, boxName, boxType);
		
		// Set defaults
		long trackId = 0;
		Date creationDate = new Date();
		Date modifiedDate = new Date();
		BigInteger duration = new BigInteger("0");
		VideoBox videoSample = null;
		AudioBox audioSample = null;
		TextBox textSample = null;
		String trackName = "";
		
		// Parse inner boxes
		while (bytesRemaining() > 0) {
			final Box nextBox = reader.nextBox();
			
			// Validate
			if (nextBox != null) {
				if (nextBox instanceof TkhdBox && BoxType.TRACK_HEADER == nextBox.getBoxType()) {
					final TkhdBox tkhdBox = (TkhdBox) nextBox;

					trackId = tkhdBox.getTrackId();
					
				} else if (nextBox instanceof MdiaBox && BoxType.MEDIA_STREAM == nextBox.getBoxType()) {
					final MdiaBox mdiaBox = (MdiaBox) nextBox;

					creationDate = mdiaBox.getCreationDate();
					modifiedDate = mdiaBox.getModifiedDate();
					duration = mdiaBox.getDuration();
					videoSample = mdiaBox.getVideoSample();
					audioSample = mdiaBox.getAudioSample();
					textSample = mdiaBox.getTextSample();
					
				} else if (nextBox instanceof UdtaBox && BoxType.USER_DATA == nextBox.getBoxType()) {
					final UdtaBox udtaBox = (UdtaBox) nextBox;

					trackName = udtaBox.getName();
				}
				
				log.debug("- '" + boxName + "' -> " + nextBox);	
			}
		}

		// Update class members
		this.trackId = trackId;
		this.trackName = trackName;
		this.creationDate = creationDate;
		this.modifiedDate = modifiedDate;
		this.duration = duration;
		this.videoSample = videoSample;
		this.audioSample = audioSample;
		this.textSample = textSample;
		
		// Determine track type
		if (this.videoSample != null) {
			this.type = Type.VIDEO;
			
		} else if (this.audioSample != null) {
			this.type = Type.AUDIO;
			
		} else if (this.textSample != null) {
			this.type = Type.TEXT;
			
		} else {
			this.type = Type.UNKNOWN;
		}

		// Clean up
		skip();
	}
	
	/**
	 * @return the type.
	 */
	public Type getType() {
		return this.type;
	}
	
	/**
	 * @return the track ID.
	 */
	public long getTrackId() {
		return this.trackId;
	}
	
	/**
	 * @return the track name.
	 */
	public String getTrackName() {
		return this.trackName;
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
	
	/**
	 * Supported track types.
	 *
	 * @author Anthony Campbell - anthonycampbell.co.uk
	 */
	public enum Type {
		VIDEO,
		AUDIO,
		TEXT,
		UNKNOWN;
	}
}
