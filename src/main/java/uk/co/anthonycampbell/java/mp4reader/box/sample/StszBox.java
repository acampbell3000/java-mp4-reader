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

import uk.co.anthonycampbell.java.mp4reader.box.common.AbstractBox;
import uk.co.anthonycampbell.java.mp4reader.box.common.Box;
import uk.co.anthonycampbell.java.mp4reader.reader.BoxType;
import uk.co.anthonycampbell.java.mp4reader.reader.MP4Reader;
import uk.co.anthonycampbell.java.mp4reader.util.Util;

/**
 * Class to encapsulate the MP4 media sample block size box (stsz).
 * 
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public class StszBox extends AbstractBox implements Box {

	// Declare box properties
	protected final short version;
	protected final long flags;
	protected final long fixedBlockSize;
	protected final long numberOfBlocks;
	protected final long totalBlockSize;
	protected final long[][] blockSizes;
	
	/**
	 * Constructor.
	 * 
	 * @param reader - MP4 file reader.
	 * @param remainingOffset - remaining bytes to be read from the stream.
	 * @param boxName - name of the box type.
	 * @param boxType - box type ENUM.
	 * @throws IOException Unable read remaining bytes from the stream.
	 */
	public StszBox(final MP4Reader reader, final long remainingOffset, final String boxName,
			final BoxType boxType) throws IOException {
		super(reader, remainingOffset, boxName, boxType);

		this.version = reader.readUnsignedByte();
		this.flags = reader.readHex();
		this.fixedBlockSize = reader.readUnsignedInt();
		this.numberOfBlocks = reader.readUnsignedInt();
		
		// Set defaults
		long totalBlockSize = 0;
		
		// If not fixed we need to read all of the variable block sizes
		if (this.fixedBlockSize == 0) {
			if (this.numberOfBlocks > Integer.MAX_VALUE) {
				this.blockSizes = new long[(int) Math.ceil((this.numberOfBlocks / Integer.MAX_VALUE))][Integer.MAX_VALUE];
			} else {
				this.blockSizes = new long[1][(int) this.numberOfBlocks];
			}
			
			// Read block sizes
			int i = 0;
			int j = 0;
			for (long l = 0; l < this.numberOfBlocks; ++l) {
				final long blockSize = reader.readUnsignedInt();
				this.blockSizes[i][j] = blockSize;
				totalBlockSize += blockSize;
				
				j++;
				if (j > Integer.MAX_VALUE) {
					j = 0; i++;
				}
			}
		} else {
			totalBlockSize = this.fixedBlockSize * this.numberOfBlocks;
			this.blockSizes = new long[0][0];
		}
		
		this.totalBlockSize = totalBlockSize;
		
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
	 * @return the fixed block size.
	 */
	public long getFixedBlockSize() {
		return this.fixedBlockSize;
	}

	/**
	 * @return the number of blocks.
	 */
	public long getNumberOfBlocks() {
		return this.numberOfBlocks;
	}

	/**
	 * @return the total block size.
	 */
	public long getTotalBlockSize() {
		return this.totalBlockSize;
	}

	/**
	 * @return the block sizes.
	 */
	public long[][] getBlockSizes() {
		return this.blockSizes;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append(Util.printFields(this));
		
		return builder.toString();
	}
}
