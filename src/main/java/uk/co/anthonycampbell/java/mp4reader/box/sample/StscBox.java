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
 * Class to encapsulate the MP4 media sample frame blocks box (stsc).
 * 
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public class StscBox extends AbstractBox implements Box {
	
	// Log
	private static final Logger log = LoggerFactory.getLogger(StscBox.class.getName());

	// Declare box properties
	protected final short version;
	protected final long flags;
	protected final long numberOfBlocks;
	protected final List<Sample> samples;
	protected final long sampleDescriptionId;
	
	/**
	 * Constructor.
	 * 
	 * @param reader - MP4 file reader.
	 * @param remainingOffset - remaining bytes to be read from the stream.
	 * @param boxName - name of the box type.
	 * @param boxType - box type ENUM.
	 * @throws IOException Unable read remaining bytes from the stream.
	 */
	public StscBox(final MP4Reader reader, final long remainingOffset, final String boxName,
			final BoxType boxType) throws IOException {
		super(reader, remainingOffset, boxName, boxType);
		
		this.version = reader.readUnsignedByte();
		this.flags = reader.readHex();
		this.numberOfBlocks = reader.readUnsignedInt();
		this.samples = new ArrayList<StscBox.Sample>();
		
		// Get frame timings
		for (int i = 0; i < this.numberOfBlocks; ++i) {
			final long block = reader.readUnsignedInt();
			final long numberOfFrames = reader.readUnsignedInt();

			log.trace("- block: " + block);
			log.trace("- numberOfFrames: " + numberOfFrames);
			
			this.samples.add(new Sample(block, numberOfFrames));
		}
		
		this.sampleDescriptionId = reader.readUnsignedInt();
		
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
	 * @return the number of blocks.
	 */
	public long getNumberOfBlocks() {
		return this.numberOfBlocks;
	}
	
	/**
	 * @return the samples list.
	 */
	public List<Sample> getSamples() {
		return this.samples;
	}
	
	/**
	 * @return the sample description ID.
	 */
	public long getSampleDescriptionId() {
		return this.sampleDescriptionId;
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
		private final long block;
		private final long numberOfFrames;
		
		/**
		 * Constructor.
		 * 
		 * @param frameCount - sample frame count.
		 * @param duration - sample duration.
		 */
		public Sample(final long frameCount, final long duration) {
			this.block = frameCount;
			this.numberOfFrames = duration;
		}

		/**
		 * @return the block.
		 */
		public long getBlock() {
			return this.block;
		}

		/**
		 * @return the number of frames.
		 */
		public long getNumberOfFrames() {
			return this.numberOfFrames;
		}

		@Override
		public String toString() {
			final StringBuilder builder = new StringBuilder();
			builder.append(Util.printFields(this));
			
			return builder.toString();
		}
	}
}
