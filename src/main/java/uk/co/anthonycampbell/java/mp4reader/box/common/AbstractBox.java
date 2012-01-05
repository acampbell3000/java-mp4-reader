package uk.co.anthonycampbell.java.mp4reader.box.common;

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

import uk.co.anthonycampbell.java.mp4reader.reader.BoxType;
import uk.co.anthonycampbell.java.mp4reader.reader.MP4Reader;
import uk.co.anthonycampbell.java.mp4reader.util.Util;

/**
 * Abstract box to provide common properties for MP4 boxes.
 * 
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public abstract class AbstractBox implements Box {
	
	// Log
	private static final Logger log = LoggerFactory.getLogger(AbstractBox.class.getName());

	// Declare box properties
	protected final MP4Reader reader;
	protected final long startPosition;
	protected final long totalSize;
	protected final String boxName;
	protected final BoxType boxType;
	
	/**
	 * Constructor.
	 * 
	 * @parqm reader - Instance of the MP4 file reader.
	 * @param totalSize - Total box size, including box header (8 bytes).
	 * @param boxName - name of the box type.
	 * @param boxType - box type ENUM.
	 * @throws IOException Unable to read from provided stream.
	 */
	public AbstractBox(final MP4Reader reader, final long totalSize, final String boxName,
			final BoxType boxType) throws IOException {
		// Validate
		if (reader == null || reader.available() == 0) {
			throw new IllegalArgumentException("Provided MP4 file reader is invalid! (reader=" +
					reader + ")");
		}
		
		log.debug("Creating box '" + boxName + "'...");
		
		// Persist box properties
		this.reader = reader;
		this.startPosition = reader.bytesRead() - 8;
		this.totalSize = totalSize + 8;
		this.boxName = boxName;
		this.boxType = boxType;
	}

	@Override
	public long getStartPosition() {
		return this.startPosition;
	}

	@Override
	public long getTotalSize() {
		return this.totalSize;
	}

	@Override
	public String getBoxName() {
		return this.boxName;
	}

	@Override
	public BoxType getBoxType() {
		return this.boxType;
	}
	
	/**
	 * Utility method to check whether the current box still has bytes available
	 * from the MP4 reader stream.
	 * 
	 * @return number of bytes available from the MP4 reader stream for this box.
	 */
	protected long bytesRemaining() {
		final long bytesRemaining = (this.startPosition + this.totalSize) - this.reader.bytesRead();		
		log.trace("- '" + boxName + "' " + bytesRemaining + " bytes remaining...");		
		return bytesRemaining;
	}
	
	/**
	 * Helper method to make sure all remaining bytes in the box offset are skipped.
	 * 
	 * @throws IOException - Unable to skip bytes in the stream reader.
	 */
	protected void skip() throws IOException {
		skip(bytesRemaining());
	}
	
	/**
	 * Helper method to skip the provided number of bytes in the MP4 stream.
	 * 
	 * @param numberOfBytes - number of bytes to skip.
	 * @throws IOException - Unable to skip bytes in the MP4 reader.
	 */
	protected void skip(final long numberOfBytes) throws IOException {
		if (numberOfBytes > 0) {
			log.trace("Skipping box " + numberOfBytes + " bytes...");
			this.reader.skip(numberOfBytes);
		}
	}

	@Override
	public String toString() {
		return Util.printFields(this, AbstractBox.class);
	}
}
