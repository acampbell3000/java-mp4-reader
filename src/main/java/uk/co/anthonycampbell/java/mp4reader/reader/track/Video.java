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
public class Video extends AbstractTrack {
	
	// Declare properties
	protected final String encodingName;
	protected final int resolutionWidth;
	protected final int resolutionHeight;
	protected final double horizontalDpi;
	protected final double verticalDpi;
	protected final long averageBitRate;
	
	/**
	 * Constructor.
	 * 
	 * @param id - the track ID.
	 * @param name - the track name.
	 * @param creationDate - the creation date.
	 * @param modifiedDate - the modified date.
	 * @param duration - the duration.
	 * @param resolutionWidth - the resolution width.
	 * @param resolutionHeight - the resolution height.
	 * @param horizontalDpi - the horizontal DPI.
	 * @param verticalDpi - the vertical DPI.
	 * @param encodingName - the encoding name.
	 * @param averageBitRate - the average bit rate.
	 */
	private Video(final long id, final String name, final Date creationDate, final Date modifiedDate,
			final long duration, final String encodingName, final int resolutionWidth,
			final int resolutionHeight, final double horizontalDpi, final double verticalDpi,
			final long averageBitRate) {
		super(id, name, creationDate, creationDate, duration);
		this.encodingName = encodingName;
		this.resolutionWidth = resolutionWidth;
		this.resolutionHeight = resolutionHeight;
		this.horizontalDpi = horizontalDpi;
		this.verticalDpi = verticalDpi;
		this.averageBitRate = averageBitRate;
	}
	
	/**
	 * @return the encoding name.
	 */
	public String getEncodingName() {
		return this.encodingName;
	}
	
	/**
	 * @return the resolution width.
	 */
	public int getResolutionWidth() {
		return this.resolutionWidth;
	}

	/**
	 * @return the resolution height.
	 */
	public int getResolutionHeight() {
		return this.resolutionHeight;
	}

	/**
	 * @return the horizontal DPI.
	 */
	public double getHorizontalDpi() {
		return this.horizontalDpi;
	}

	/**
	 * @return the vertical DPI.
	 */
	public double getVerticalDpi() {
		return this.verticalDpi;
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
