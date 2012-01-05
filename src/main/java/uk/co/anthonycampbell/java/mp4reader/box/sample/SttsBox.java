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
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.anthonycampbell.java.mp4reader.box.common.AbstractBox;
import uk.co.anthonycampbell.java.mp4reader.box.common.Box;
import uk.co.anthonycampbell.java.mp4reader.reader.BoxType;
import uk.co.anthonycampbell.java.mp4reader.reader.MP4Reader;
import uk.co.anthonycampbell.java.mp4reader.util.Util;

/**
 * Class to encapsulate the MP4 media sample framing timing box (stts).
 * 
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public class SttsBox extends AbstractBox implements Box {
	
	// Log
	private static final Logger log = LoggerFactory.getLogger(SttsBox.class.getName());

	// Declare box properties
	protected final short version;
	protected final long flags;
	protected final long numberOfTimes;
	protected final boolean isVariable;
	protected final List<Sample> samples;
	
	/**
	 * Constructor.
	 * 
	 * @param reader - MP4 file reader.
	 * @param remainingOffset - remaining bytes to be read from the stream.
	 * @param boxName - name of the box type.
	 * @param boxType - box type ENUM.
	 * @throws IOException Unable read remaining bytes from the stream.
	 */
	public SttsBox(final MP4Reader reader, final long remainingOffset, final String boxName,
			final BoxType boxType) throws IOException {
		super(reader, remainingOffset, boxName, boxType);
		
		this.version = reader.readUnsignedByte();
		this.flags = reader.readHex();
		this.numberOfTimes = reader.readUnsignedInt();
		this.samples = new ArrayList<SttsBox.Sample>();

		// Is variable?
		if (this.numberOfTimes > 1) {
			this.isVariable = true;
		} else {
			this.isVariable = false;
		}
		
		// Get frame timings
		for (int i = 0; i < this.numberOfTimes; ++i) {
			final long frameCount = reader.readUnsignedInt();
			final long duration = reader.readUnsignedInt();

			log.trace("- frameCount: " + frameCount);
			log.trace("- duration: " + duration);
			
			this.samples.add(new Sample(frameCount, duration));
		}
		
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
	 * @return the number of times.
	 */
	public long getNumberOfTimes() {
		return this.numberOfTimes;
	}

	/**
	 * @return whether the frame timing is variable.
	 */
	public boolean isVariable() {
		return this.isVariable;
	}
	
	/**
	 * @return the samples list.
	 */
	public List<Sample> getSamples() {
		return this.samples;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append(Util.printFields(this));
		
		return builder.toString();
	}
	
	/**
	 * Inner class to encapsulate frame timing sample measurement. 
	 * 
	 * @author Anthony Campbell - anthonycampbell.co.uk
	 */
	public class Sample {
		
		// Declare properties
		private final long frameCount;
		private final long duration;
		
		/**
		 * Constructor.
		 * 
		 * @param frameCount - sample frame count.
		 * @param duration - sample duration.
		 */
		public Sample(final long frameCount, final long duration) {
			this.frameCount = frameCount;
			this.duration = duration;
		}

		/**
		 * @return the sample frame count.
		 */
		public long getFrameCount() {
			return this.frameCount;
		}

		/**
		 * @return the duration.
		 */
		public long getDuration() {
			return this.duration;
		}

		@Override
		public String toString() {
			final StringBuilder builder = new StringBuilder();
			builder.append(Util.printFields(this));
			
			return builder.toString();
		}
	}
}
