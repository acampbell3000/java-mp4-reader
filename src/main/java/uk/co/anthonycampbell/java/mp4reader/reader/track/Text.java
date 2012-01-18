package uk.co.anthonycampbell.java.mp4reader.reader.track;

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

import java.util.Date;

import uk.co.anthonycampbell.java.mp4reader.util.Util;

/**
 * Class to encapsulate MP4 video track.
 * 
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public class Text extends AbstractTrack {
	
	// Declare properties
	protected final String audioType;
	protected final double channels;
	protected final int sampleSize;
	protected final long sampleRate;
	protected final long averageBitRate;
	
	/**
	 * Constructor.
	 * 
	 * @param id - the track ID.
	 * @param name - the track name.
	 * @param creationDate - the creation date.
	 * @param modifiedDate - the modified date.
	 * @param duration - the duration.
	 * @param audioType - the audio type.
	 * @param channels - the audio channels.
	 * @param sampleSize - the audio sample size.
	 * @param sampleRate - the audio sample rate.
	 * @param averageBitRate - the audio bit rate.
	 */
	public Text(long id, String name, Date creationDate, Date modifiedDate,
			long duration, String audioType, double channels, int sampleSize,
			long sampleRate, long averageBitRate) {
		super(id, name, creationDate, modifiedDate, duration);
		this.audioType = audioType;
		this.channels = channels;
		this.sampleSize = sampleSize;
		this.sampleRate = sampleRate;
		this.averageBitRate = averageBitRate;
	}

	/**
	 * @return the audio type.
	 */
	public String getAudioType() {
		return this.audioType;
	}

	/**
	 * @return the channels.
	 */
	public double getChannels() {
		return this.channels;
	}

	/**
	 * @return the sample size.
	 */
	public int getSampleSize() {
		return this.sampleSize;
	}

	/**
	 * @return the sample rate.
	 */
	public long getSampleRate() {
		return this.sampleRate;
	}

	/**
	 * @return the average bit rate.
	 */
	public long getAverageBitRate() {
		return this.averageBitRate;
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append(Util.printFields(this));
		
		return builder.toString();
	}
}
