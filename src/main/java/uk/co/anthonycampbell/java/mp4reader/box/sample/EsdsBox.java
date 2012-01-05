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
 * Class to encapsulate the MP4 media sample descriptor box (esds).
 * 
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public class EsdsBox extends AbstractBox implements Box {

	// Declare box properties
	protected final short version;
	protected final long flags;
	protected final long descriptorType;
	protected final long extendedDescriptorType;
	protected final short descriptorTypeLength;
	protected final int esId;
	protected final short streamPriority;
	protected final long decoderConfigDescriptorType;
	protected final long decoderConfigExtendedDescriptorType;
	protected final short decoderConfigDescriptorTypeLength;
	protected final short objectTypeId;
	protected final int streamType;
	protected final int upStreamFlag;
	protected final long bufferSize;
	protected final long maximumBitRate;
	protected final long averageBitRate;
	protected final long decoderSpecificDescriptorType;
	protected final long decoderSpecificExtendedDescriptorType;
	protected final short decoderSpecificDescriptorTypeLength;
	protected final long decoderSpecificDescriptorHexDump;
	protected final long slConfigDescriptorType;
	protected final long slConfigExtendedDescriptorType;
	protected final short slConfigDescriptorTypeLength;
	protected final long slValue;
	
	/**
	 * Constructor.
	 * 
	 * @param reader - MP4 file reader.
	 * @param remainingOffset - remaining bytes to be read from the stream.
	 * @param boxName - name of the box type.
	 * @param boxType - box type ENUM.
	 * @throws IOException Unable read remaining bytes from the stream.
	 */
	public EsdsBox(final MP4Reader reader, final long remainingOffset, final String boxName,
			final BoxType boxType) throws IOException {
		super(reader, remainingOffset, boxName, boxType);

		this.version = reader.readUnsignedByte();
		this.flags = reader.readHex();
		this.descriptorType = reader.readHex(1);
		this.extendedDescriptorType = reader.readHex();
		this.descriptorTypeLength = reader.readUnsignedByte();
		this.esId = reader.readUnsignedShort();
		this.streamPriority = reader.readUnsignedByte();
		
		this.decoderConfigDescriptorType = reader.readHex(1);
		this.decoderConfigExtendedDescriptorType = reader.readHex();
		this.decoderConfigDescriptorTypeLength = reader.readUnsignedByte();

		this.objectTypeId = reader.readUnsignedByte();
		
		final int streamType = reader.readUnsignedByte();
		this.streamType = (streamType >> 2);
		this.upStreamFlag = ((streamType << 30) >> 31);
		
		this.bufferSize = reader.readHex();
		this.maximumBitRate = reader.readUnsignedInt();
		this.averageBitRate = reader.readUnsignedInt();
		
		this.decoderSpecificDescriptorType = reader.readHex(1);
		this.decoderSpecificExtendedDescriptorType = reader.readHex();
		this.decoderSpecificDescriptorTypeLength = reader.readUnsignedByte();
		this.decoderSpecificDescriptorHexDump = reader.readHex(this.decoderSpecificDescriptorTypeLength);
		
		this.slConfigDescriptorType = reader.readHex(1);
		this.slConfigExtendedDescriptorType = reader.readHex();
		this.slConfigDescriptorTypeLength = reader.readUnsignedByte();
		this.slValue = reader.readHex(1);
		
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
	 * @return the descriptor type.
	 */
	public long getDescriptorType() {
		return this.descriptorType;
	}
	
	/**
	 * @return the descriptor type string.
	 */
	public String getDescriptorTypeString() {
		return Long.toHexString(this.descriptorType);
	}

	/**
	 * @return the extended descriptor type.
	 */
	public long getExtendedDescriptorType() {
		return this.extendedDescriptorType;
	}

	/**
	 * @return the extended descriptor type string.
	 */
	public String getExtendedDescriptorTypeString() {
		return Long.toHexString(this.extendedDescriptorType);
	}
	
	/**
	 * @return the descriptor type length.
	 */
	public short getDescriptorTypeLength() {
		return this.descriptorTypeLength;
	}

	/**
	 * @return the ES ID.
	 */
	public int getEsId() {
		return this.esId;
	}

	/**
	 * @return the stream priority.
	 */
	public short getStreamPriority() {
		return this.streamPriority;
	}
	
	/**
	 * @return the decoder configuration descriptor type.
	 */
	public long getDecoderConfigDescriptorType() {
		return this.decoderConfigDescriptorType;
	}

	/**
	 * @return the decoder configuration descriptor type.
	 */
	public String getDecoderConfigDescriptorTypeString() {
		return Long.toHexString(this.decoderConfigDescriptorType);
	}

	/**
	 * @return the decoder configuration extended descriptor type.
	 */
	public long getDecoderConfigExtendedDescriptorType() {
		return this.decoderConfigExtendedDescriptorType;
	}
	
	/**
	 * @return the decoder configuration extended descriptor type string.
	 */
	public String getDecoderConfigExtendedDescriptorTypeString() {
		return Long.toHexString(this.decoderConfigExtendedDescriptorType);
	}
	
	/**
	 * @return the decoder configuration descriptor type length.
	 */
	public short getDecoderConfigDescriptorTypeLength() {
		return this.decoderConfigDescriptorTypeLength;
	}

	/**
	 * @return the object type ID.
	 */
	public short getObjectTypeId() {
		return this.objectTypeId;
	}

	/**
	 * @return the object type ENUM.
	 */
	public ObjectType getObjectType() {
		ObjectType objectType = ObjectType.UNKNOWN;
		
		final ObjectType[] types = ObjectType.values();		
		if (types != null && types.length > 0) {
			for (final ObjectType type : types) {
				if (type != null && this.objectTypeId == type.getId()) {
					objectType = type;
				}
			}
		}
		
		return objectType;
	}

	/**
	 * @return the stream type ENUM.
	 */
	public StreamType getStreamType() {
		StreamType streamType = StreamType.UNKNOWN;
		
		final StreamType[] types = StreamType.values();		
		if (types != null && types.length > 0) {
			for (final StreamType type : types) {
				if (type != null && this.streamType == type.getId()) {
					streamType = type;
				}
			}
		}
		
		return streamType;
	}
	
	/**
	 * @return the up stream flag.
	 */
	public int getUpStreamFlag() {
		return this.upStreamFlag;
	}

	/**
	 * @return the buffer size.
	 */
	public long getBufferSize() {
		return this.bufferSize;
	}

	/**
	 * @return the maximum bit rate.
	 */
	public long getMaximumBitRate() {
		return this.maximumBitRate;
	}

	/**
	 * @return the average bit rate.
	 */
	public long getAverageBitRate() {
		return this.averageBitRate;
	}

	/**
	 * @return the decoder specific descriptor type.
	 */
	public long getDecoderSpecificDescriptorType() {
		return this.decoderSpecificDescriptorType;
	}

	/**
	 * @return the decoder specific extended descriptor type.
	 */
	public long getDecoderSpecificExtendedDescriptorType() {
		return this.decoderSpecificExtendedDescriptorType;
	}
	
	/**
	 * @return the decoder specific extended descriptor type string.
	 */
	public String getDecoderSpecificExtendedDescriptorTypeString() {
		return Long.toHexString(this.decoderConfigExtendedDescriptorType);
	}

	/**
	 * @return the decoder specific descriptor type length.
	 */
	public short getDecoderSpecificDescriptorTypeLength() {
		return this.decoderSpecificDescriptorTypeLength;
	}

	/**
	 * @return the decoder specific descriptor hex dump.
	 */
	public long getDecoderSpecificDescriptorHexDump() {
		return this.decoderSpecificDescriptorHexDump;
	}

	/**
	 * @return the decoder specific descriptor hex dump string.
	 */
	public String getDecoderSpecificDescriptorHexDumpString() {
		return Long.toHexString(this.decoderSpecificDescriptorHexDump);
	}
	
	/**
	 * @return the SL configuration descriptor type.
	 */
	public long getSlConfigDescriptorType() {
		return this.slConfigDescriptorType;
	}
	
	/**
	 * @return the SL configuration descriptor type string.
	 */
	public String getSlConfigDescriptorTypeString() {
		return Long.toHexString(this.slConfigDescriptorType);
	}

	/**
	 * @return the SL configuration extended descriptor type.
	 */
	public long getSlConfigExtendedDescriptorType() {
		return this.slConfigExtendedDescriptorType;
	}

	/**
	 * @return the SL configuration extended descriptor type string.
	 */
	public String getSlConfigExtendedDescriptorTypeString() {
		return Long.toHexString(this.slConfigExtendedDescriptorType);
	}

	/**
	 * @return the SL configuration descriptor type length.
	 */
	public short getSlConfigDescriptorTypeLength() {
		return this.slConfigDescriptorTypeLength;
	}

	/**
	 * @return the SL value.
	 */
	public long getSlValue() {
		return this.slValue;
	}
	
	/**
	 * @return the SL value string.
	 */
	public String getSlValueString() {
		return Long.toHexString(this.slValue);
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(super.toString());	
		builder.append(Util.printFields(this));
		
		return builder.toString();
	}
	
	/**
	 * Object types.
	 * 
	 * @author Anthony Campbell - anthonycampbell.co.uk
	 */
	public enum ObjectType {
		SYSTEM_V1(1, "System V1"),
		SYSTEM_V2(2, "System V2"),
		MPEG4_VIDEO(32, "MPEG-4 Video"),
		MPEG4_AVC_SPS(33, "MPEG-4 AVC SPS"),
		MPEG4_AVC_PPS(34, "MPEG-4 AVC PPS"),
		MPEG4_AUDIO(64, "MPEG-4 Audio"),
		MPEG2_SIMPLE_VIDEO(96, "MPEG-2 Simple Video"),
		MPEG2_MAIN_VIDEO(97, "MPEG-2 Main Video"),
		MPEG2_SNR_VIDEO(98, "MPEG-2 SNR Video"),
		MPEG2_SPATIAL_VIDEO(99, "MPEG-2 Spatial Video"),
		MPEG2_HIGH_VIDEO(100, "MPEG-2 High Video"),
		MPEG2_422_VIDEO(101, "MPEG-2 4:2:2 Video"),
		MPEG4_ADTS_MAIN(102, "MPEG-4 ADTS Main"),
		MPEG4_ADTS_LOW_COMPLEXITY(103, "MPEG-4 ADTS Low Complexity"),
		MPEG4_ADTS_SCALABLE_SAMPLING_RATE(104, "MPEG-4 ADTS Scalable Sampling Rate"),
		MPEG2_ADTS(105, "MPEG-2 ADTS"),
		MPEG1_VIDEO(106, "MPEG-1 Video"),
		MPEG1_ADTS(107, "MPEG-1 ADTS"),
		JPEG_VIDEO(108, "JPEG Video"),
		PRIVATE_AUDIO(192, "Private Audio"),
		PRIVATE_VIDEO(208, "Private Video"),
		PCM_LE_AUDIO(224, "16-bit PCM LE Audio"),
		VORBIS_AUDIO(225, "Vorbis Audio"),
		DOLBY_V3_AC3_AUDIO(226, "Dolby V3 (AC3) Audio"),
		ALAW_AUDIO(227, "Alaw Audio"),
		MULAW_AUDIO(228, "Mulaw Audio"),
		G723_ADPCM_AUDIO(229, "G723 ADPCM Audio"),
		PCM_BIG_ENDIAN_AUDIO(230, "16-bit PCM Big Endian Audio"),
		YV12_420_AUDIO(240, "Y'CbCr 4:2:0 (YV12) Video"),
		H264_VIDEO(241, "H264 Video"),
		H263_VIDEO(242, "H263 Video"),
		H261_VIDEO(243, "H261 Video"),		
		UNKNOWN(-1, "Unknown");
		
		// Declare properties
		private final int id;
		private final String name;
		
		/**
		 * Constructor.
		 * 
		 * @param id - the type ID.
		 * @param name - the type name.
		 */
		ObjectType(final int id, final String name) {
			this.id = id;
			this.name = name;
		}

		/**
		 * @return the ID.
		 */
		public int getId() {
			return this.id;
		}

		/**
		 * @return the name.
		 */
		public String getName() {
			return this.name;
		}
		
		@Override
		public String toString() {
			return this.name;
		}
	}
	
	/**
	 * Stream types.
	 * 
	 * @author Anthony Campbell - anthonycampbell.co.uk
	 */
	public enum StreamType {
		OBJECT_DESCRIPTOR(1, "Object Descriptor"),
		CLOCK_REFERENCE(2, "Clock Reference"),
		SCENE_DESCRIPTOR(3, "Scene Descriptor"),
		VISUAL(4, "Visual"),
		AUDIO(5, "Audio"),
		MPEG7(6, "MPEG-7"),
		IPMP(7, "IPMP"),
		OCI(8, "OCI"),
		MPEG_JAVA(9, "MPEG Java"),
		USER_PRIVATE(32, "User Private"),
		UNKNOWN(-1, "Unknown");
		
		// Declare properties
		private final int id;
		private final String name;
		
		/**
		 * Constructor.
		 * 
		 * @param id - the type ID.
		 * @param name - the type name.
		 */
		StreamType(final int id, final String name) {
			this.id = id;
			this.name = name;
		}

		/**
		 * @return the ID.
		 */
		public int getId() {
			return this.id;
		}

		/**
		 * @return the name.
		 */
		public String getName() {
			return this.name;
		}
		
		@Override
		public String toString() {
			return this.name;
		}
	}
}
