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
import uk.co.anthonycampbell.java.mp4reader.box.sample.EsdsBox.ObjectType;
import uk.co.anthonycampbell.java.mp4reader.box.sample.EsdsBox.StreamType;
import uk.co.anthonycampbell.java.mp4reader.reader.BoxType;
import uk.co.anthonycampbell.java.mp4reader.reader.MP4InputStream;
import uk.co.anthonycampbell.java.mp4reader.reader.MP4Reader;
import uk.co.anthonycampbell.java.mp4reader.util.Util;

/**
 * Class to encapsulate the MP4 media audio sample boxes (e.g. mp4a).
 * 
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public class AudioBox extends AbstractBox implements Box {
	
	// Log
	private static final Logger log = LoggerFactory.getLogger(AudioBox.class.getName());

	// Declare box properties
	protected final int dataReferenceIndex;
	protected final long encodingVersion;
	protected final long encodingRevision;
	protected final String encodingVendor;
	protected final double channels;
	protected final int sampleSize;
	protected final short compressionId;
	protected final short packetSize;
	protected final long sampleRate;
	protected final long averageBitRate;
	protected final ObjectType objectType;
	protected final StreamType streamType;
	
	/**
	 * Constructor.
	 * 
	 * @param reader - MP4 file reader.
	 * @param remainingOffset - remaining bytes to be read from the stream.
	 * @param boxName - name of the box type.
	 * @param boxType - box type ENUM.
	 * @throws IOException Unable read remaining bytes from the stream.
	 */
	public AudioBox(final MP4Reader reader, final long remainingOffset, final String boxName,
			final BoxType boxType) throws IOException {
		super(reader, remainingOffset, boxName, boxType);
		skip(6);

		this.dataReferenceIndex = reader.readUnsignedShort(); 
		this.encodingVersion = reader.readHex(MP4InputStream.SIXTEEN_BIT_BYTE_LENGTH);
		this.encodingRevision = reader.readHex(MP4InputStream.SIXTEEN_BIT_BYTE_LENGTH);
		this.encodingVendor = reader.readString();
		double channels = reader.readUnsignedShort();
		this.sampleSize = reader.readUnsignedShort();
		this.compressionId = reader.readShort();
		this.packetSize = reader.readShort();
		long sampleRate = reader.readUnsignedInt();
		
		// Inner box defaults
		long averageBitRate = 0;
		ObjectType objectType = ObjectType.UNKNOWN;
		StreamType streamType = StreamType.UNKNOWN;
		
		// Parse inner boxes
		while (bytesRemaining() > 0) {
			final Box nextBox = reader.nextBox();
			
			// Validate
			if (nextBox != null) {
				if (nextBox instanceof EsdsBox &&
						BoxType.SAMPLE_DESCRIPTOR == nextBox.getBoxType() ||
						BoxType.SAMPLE_M4_DESCRIPTOR == nextBox.getBoxType()) {
					final EsdsBox esdsBox = (EsdsBox) nextBox;
					
					averageBitRate = esdsBox.getAverageBitRate();
					objectType = esdsBox.getObjectType();
					streamType = esdsBox.getStreamType();
	
				} else if (nextBox instanceof Dac3Box &&
						BoxType.SAMPLE_DAC3_DESCRIPTOR == nextBox.getBoxType()) {
					final Dac3Box dac3Box = (Dac3Box) nextBox;

					channels = dac3Box.getChannels();
					sampleRate = dac3Box.getSampleRate();
					averageBitRate = dac3Box.getBitRate();
					objectType = ObjectType.DOLBY_V3_AC3_AUDIO;
					streamType = StreamType.AUDIO;
				}
				
				log.debug("- '" + boxName + "' -> " + nextBox);
			}
		}
		
		this.channels = channels;
		this.sampleRate = sampleRate;
		this.averageBitRate = averageBitRate;
		this.objectType = objectType;
		this.streamType = streamType;
		
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
	public String getEncodingVersionString() {
		return Integer.toHexString((int) this.encodingVersion);
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
		return Integer.toHexString((int) this.encodingRevision);
	}

	/**
	 * @return the encoding vendor.
	 */
	public String getEncodingVendor() {
		return this.encodingVendor;
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
	 * @return the compression ID.
	 */
	public short getCompressionId() {
		return this.compressionId;
	}

	/**
	 * @return the packet size.
	 */
	public short getPacketSize() {
		return this.packetSize;
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

	/**
	 * @return the object type.
	 */
	public ObjectType getObjectType() {
		return this.objectType;
	}

	/**
	 * @return the stream type.
	 */
	public StreamType getStreamType() {
		return this.streamType;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append(Util.printFields(this));
		
		return builder.toString();
	}
}
