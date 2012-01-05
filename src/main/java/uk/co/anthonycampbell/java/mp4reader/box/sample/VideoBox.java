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
import uk.co.anthonycampbell.java.mp4reader.reader.MP4InputStream;
import uk.co.anthonycampbell.java.mp4reader.reader.MP4Reader;
import uk.co.anthonycampbell.java.mp4reader.util.Util;

/**
 * Class to encapsulate the MP4 media video sample boxes (e.g. avc1).
 * 
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public class VideoBox extends AbstractBox implements Box {
	
	// Log
	private static final Logger log = LoggerFactory.getLogger(VideoBox.class.getName());

	// Declare box properties
	protected final int dataReferenceIndex;
	protected final long encodingVersion;
	protected final long encodingRevision;
	protected final String encodingVendor;
	protected final long temporalQuality;
	protected final long spatialQuality;
	protected final int pixelFrameWidth;
	protected final int pixelFrameHeight;
	protected final float resolutionHorizontalDpi;
	protected final float resolutionVerticalDpi;
	protected final int dataSize;
	protected final int frameCount;
	protected final short encodingNameLength;
	protected final String encodingName;
	protected final int pixelDepth;
	protected final short colourTableId;
	
	/**
	 * Constructor.
	 * 
	 * @param reader - MP4 file reader.
	 * @param remainingOffset - remaining bytes to be read from the stream.
	 * @param boxName - name of the box type.
	 * @param boxType - box type ENUM.
	 * @throws IOException Unable read remaining bytes from the stream.
	 */
	public VideoBox(final MP4Reader reader, final long remainingOffset, final String boxName,
			final BoxType boxType) throws IOException {
		super(reader, remainingOffset, boxName, boxType);
		skip(6);

		this.dataReferenceIndex = reader.readUnsignedShort(); 
		this.encodingVersion = reader.readHex(MP4InputStream.SIXTEEN_BIT_BYTE_LENGTH);
		this.encodingRevision = reader.readHex(MP4InputStream.SIXTEEN_BIT_BYTE_LENGTH);
		this.encodingVendor = reader.readString();
		this.temporalQuality = reader.readUnsignedInt();
		this.spatialQuality = reader.readUnsignedInt();
		this.pixelFrameWidth = reader.readUnsignedShort();
		this.pixelFrameHeight = reader.readUnsignedShort();		
		this.resolutionHorizontalDpi = reader.readFloat();
		this.resolutionVerticalDpi = reader.readFloat();
		this.dataSize = reader.readInt();
		this.frameCount = reader.readUnsignedShort();
		this.encodingNameLength = reader.readUnsignedByte();
		this.encodingName = reader.readString(this.encodingNameLength);
		skip(31 - this.encodingNameLength);
		this.pixelDepth = reader.readUnsignedShort();
		this.colourTableId = reader.readShort();
		
		// Iterate through additional inner boxes
		while (bytesRemaining() > 0) {
			final Box innerBox = reader.nextBox();
			
			log.debug("- '" + boxName + "' -> " + innerBox);
		}
		
		// Clean up
		skip();
	}
	
	/**
	 * @return the data reference index.
	 */
	public int getDataReferenceIndex() {
		return this.dataReferenceIndex;
	}

	/**
	 * @return the encoding version.
	 */
	public long getEncodingVersion() {
		return this.encodingVersion;
	}

	/**
	 * @return the encoding version string.
	 */
	public String getEncodingVendorString() {
		return Long.toHexString((int) this.encodingVersion);
	}
	
	/**
	 * @return the encoding revision.
	 */
	public long getEncodingRevision() {
		return this.encodingRevision;
	}

	/**
	 * @return the encoding revision string.
	 */
	public String getEncodingRevisionString() {
		return Long.toHexString((int) this.encodingRevision);
	}

	/**
	 * @return the encoding vendor.
	 */
	public String getEncodingVendor() {
		return this.encodingVendor;
	}

	/**
	 * @return the temporal quality.
	 */
	public long getTemporalQuality() {
		return this.temporalQuality;
	}

	/**
	 * @return the spatial quality.
	 */
	public long getSpatialQuality() {
		return this.spatialQuality;
	}

	/**
	 * @return the pixel frame width.
	 */
	public int getPixelFrameWidth() {
		return this.pixelFrameWidth;
	}

	/**
	 * @return the pixel frame height.
	 */
	public int getPixelFrameHeight() {
		return this.pixelFrameHeight;
	}
	
	/**
	 * @return the horizontal resolution DPI.
	 */
	public float getResolutionHorizontalDpi() {
		return this.resolutionHorizontalDpi;
	}

	/**
	 * @return the vertical resolution DPI.
	 */
	public float getResolutionVerticalDpi() {
		return this.resolutionVerticalDpi;
	}

	/**
	 * @return the data size.
	 */
	public int getDataSize() {
		return this.dataSize;
	}
	
	/**
	 * @return the frame count.
	 */
	public int getFrameCount() {
		return this.frameCount;
	}

	/**
	 * @return the encoding name length.
	 */
	public short getEncodingNameLength() {
		return this.encodingNameLength;
	}

	/**
	 * @return the encoding name.
	 */
	public String getEncodingName() {
		return this.encodingName;
	}

	/**
	 * Helper method to determine whether the video stream is H.264.
	 * 
	 * @return - whether the stream is H.264.
	 */
	public boolean isH264() {
		return "JVT/AVC Coding".equals(this.encodingName);
	}
	
	/**
	 * @return the pixel depth.
	 */
	public int getPixelDepth() {
		return this.pixelDepth;
	}

	/**
	 * @return the colour table ID.
	 */
	public short getColourTableId() {
		return this.colourTableId;
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append(Util.printFields(this));
		
		return builder.toString();
	}
}
