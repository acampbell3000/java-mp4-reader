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
import uk.co.anthonycampbell.java.mp4reader.reader.MP4Reader;
import uk.co.anthonycampbell.java.mp4reader.util.Util;

/**
 * Class to encapsulate the MP4 media sample AC3 descriptor box (dac3).
 * 
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public class Dac3Box extends AbstractBox implements Box {
	
	// Log
	private static final Logger log = LoggerFactory.getLogger(Dac3Box.class.getName());

	// Declare box properties
	protected final int sampleRateCode;
	protected final int bitStreamIdentification;
	protected final int bitStreamMode;
	protected final int audioCodingMode;
	protected final int lowFrequencyEffectsChannel;
	protected final int bitRateCode;
	
	/**
	 * Constructor.
	 * 
	 * @param reader - MP4 file reader.
	 * @param remainingOffset - remaining bytes to be read from the stream.
	 * @param boxName - name of the box type.
	 * @param boxType - box type ENUM.
	 * @throws IOException Unable read remaining bytes from the stream.
	 */
	public Dac3Box(final MP4Reader reader, final long remainingOffset, final String boxName,
			final BoxType boxType) throws IOException {
		super(reader, remainingOffset, boxName, boxType);

		final int dac3Bytes = (((int) reader.readHex()) << 8);
		
		log.trace("- dac3 bytes: " + Integer.toBinaryString(dac3Bytes));
		
		this.sampleRateCode = (dac3Bytes >>> 30);
		this.bitStreamIdentification = ((dac3Bytes << 2) >>> 27);
		this.bitStreamMode = ((dac3Bytes << 7) >>> 29);
		this.audioCodingMode = ((dac3Bytes << 10) >>> 29);
		this.lowFrequencyEffectsChannel = ((dac3Bytes << 13) >>> 31);
		this.bitRateCode = ((dac3Bytes << 14) >>> 27);

		log.trace("- sampleRateCode: " + Integer.toBinaryString(this.sampleRateCode));
		log.trace("- bitStreamIdentification: " + Integer.toBinaryString(this.bitStreamIdentification));
		log.trace("- bitStreamMode: " + Integer.toBinaryString(this.bitStreamMode));
		log.trace("- audioCodingMode: " + Integer.toBinaryString(this.audioCodingMode));
		log.trace("- lowFrequencyEffectsChannel: " + Integer.toBinaryString(this.lowFrequencyEffectsChannel));
		log.trace("- bitRateCode: " + Integer.toBinaryString(this.bitRateCode));
		log.trace("- dac3Box: " + toString());
		
		// Clean up
		skip();
	}
	
	/**
	 * @return the sample rate code.
	 */
	public int getSampleRateCode() {
		return this.sampleRateCode;
	}
	
	/**
	 * @return the sample rate.
	 */
	public int getSampleRate() {
		int result = SampleRate.RESERVED.getRate();
		
		final SampleRate[] sampleRates = SampleRate.values();
		for (final SampleRate sampleRate : sampleRates) {
			if (this.sampleRateCode == sampleRate.getCode()) {
				result = sampleRate.getRate();
				break;
			}
		}
		
		return result;
	}
	
	/**
	 * @return the bit stream identification.
	 */
	public int getBitStreamIdentification() {
		return this.bitStreamIdentification;
	}
	
	/**
	 * @return the bit stream mode.
	 */
	public int getBitStreamMode() {
		return this.bitStreamMode;
	}
	
	/**
	 * @return the audio coding mode.
	 */
	public int getAudioCodingMode() {
		return this.audioCodingMode;
	}
	
	/**
	 * @return the low frequency effects channel.
	 */
	public int getLowFrequencyEffectsChannel() {
		return this.lowFrequencyEffectsChannel;
	}
	
	/**
	 * @return the channel.
	 */
	public double getChannels() {
		double result = Channel.UNKNOWN.getChannels();
		
		final Channel[] channels = Channel.values();
		for (final Channel channel : channels) {
			if (this.audioCodingMode == channel.getCode() &&
					this.lowFrequencyEffectsChannel == channel.hasSubWooferChannel()) {
				result = channel.getChannels();
				break;
			}
		}
		
		return result;
	}
	
	/**
	 * @return the bit rate code.
	 */
	public int getBitRateCode() {
		return this.bitRateCode;
	}
	
	/**
	 * @return the bit rate.
	 */
	public int getBitRate() {
		int result = BitRate.UNKNOWN.getRate();
		
		final BitRate[] bitRates = BitRate.values();
		for (final BitRate bitRate : bitRates) {
			if (this.bitRateCode == bitRate.getCode()) {
				result = bitRate.getRate();
				break;
			}
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(super.toString());	
		builder.append(Util.printFields(this));
		
		return builder.toString();
	}
	
	/**
	 * ENUM for the 'fscod' sample rate code
	 * 
	 * @author Anthony Campbell - anthonycampbell.co.uk
	 */
	public enum SampleRate {
		FOURTY_EIGHT(0, 48),
		FOURTY_FOUR(1, 44),
		THIRTY_TWO(2, 32),
		RESERVED(3, 00);
		
		// Declare properties
		private final int code;
		private final int rate;
		
		/**
		 * Constructor.
		 * 
		 * @param code - the code.
		 * @param rate - the rate.
		 */
		SampleRate(final int code, final int rate) {
			this.code = code;
			this.rate = rate;
		}

		/**
		 * @return the code.
		 */
		public int getCode() {
			return this.code;
		}

		/**
		 * @return the sample rate.
		 */
		public int getRate() {
			return this.rate;
		}
	}
	
	/**
	 * ENUM for the AC3 audio channels.
	 * 
	 * @author Anthony Campbell - anthonycampbell.co.uk
	 */
	public enum Channel {
		ONE_ONE_NO(0, 1, 0),
		ONE_ZERO_NO(1, 1, 0),
		TWO_ZERO_NO(2, 2, 0),
		THREE_ZERO_NO(3, 3, 0),
		TWO_ONE_NO(4, 3, 0),
		THREE_ONE_NO(5, 4, 0),
		TWO_TWO_NO(6, 4, 0),
		THREE_TWO_NO(7, 5, 0),		
		ONE_ONE_YES(0, 1, 1),
		ONE_ZERO_YES(1, 1, 1),
		TWO_ZERO_YES(2, 2, 1),
		THREE_ZERO_YES(3, 3, 1),
		TWO_ONE_YES(4, 3, 1),
		THREE_ONE_YES(5, 4, 1),
		TWO_TWO_YES(6, 4, 1),
		THREE_TWO_YES(7, 5, 1),
		UNKNOWN(-1, 0, 0); 
		
		// Declare properties
		private final int code;
		private final int numberOfChannels;
		private final int hasSubWooferChannel;
		
		/**
		 * Constructor.
		 * 
		 * @param code
		 * @param numberOfChannels - the number of audio channels.
		 * @param hasSubWooferChannel - whether this stream has a sub woofer channel.
		 */
		Channel(final int code, final int numberOfChannels, final int hasSubWooferChannel) {
			this.code = code;
			this.numberOfChannels = numberOfChannels;
			this.hasSubWooferChannel = hasSubWooferChannel;
		}

		/**
		 * @return the code.
		 */
		public int getCode() {
			return this.code;
		}

		/**
		 * @return the number of audio channels.
		 */
		public int getNumberOfChannels() {
			return this.numberOfChannels;
		}

		/**
		 * @return whether the ENUM has a sub woofer channel.
		 */
		public int hasSubWooferChannel() {
			return this.hasSubWooferChannel;
		}
		
		/**
		 * Get channel representation for this ENUM.
		 * 
		 * @return - ENUM channels.
		 */
		public double getChannels() {
			double result = this.numberOfChannels;
			
			if (this.hasSubWooferChannel > 0) {
				result += ((double) this.hasSubWooferChannel / 10);	
			}
			
			return result;
		}
		
		@Override
		public String toString() {
			final StringBuilder builder = new StringBuilder();
			builder.append(this.numberOfChannels);
			
			if (this.hasSubWooferChannel > 0) {
				builder.append(".");
				builder.append(this.hasSubWooferChannel);	
			}
			
			return builder.toString();
		}
	}
	
	/**
	 * ENUM for the bit rate code.
	 * 
	 * @author Anthony Campbell - anthonycampbell.co.uk
	 */
	public enum BitRate {
		THIRTY_TWO(0, 32),
		FOURTY(1, 40),
		FOURTY_EIGHT(2, 48),
		FIFTY_SIX(3, 56),
		SIXTY_FOUR(4, 64),
		EIGHTY(5, 80),
		NINETY_SIX(6, 96),
		ONE_HUNDRED_TWELVE(7, 112),
		ONE_HUNDRED_TWENTY_EIGHT(8, 128),
		ONE_HUNDRED_SIXTY(9, 160),
		ONE_HUNDRED_NINETY_TWO(10, 192),
		TWO_HUNDRED_TWENTY_FOUR(11, 224),
		TWO_HUNDRED_FIFTY_SIX(12, 256),
		THREE_HUNDRED_TWENTY(13, 320),
		THREE_HUNDRED_EIGHTY_FOUR(14, 384),
		FOUR_HUNDRED_FOURTY_EIGHT(15, 448),
		FIVE_HUNDRED_TWELVE(16, 512),
		FIVE_HUNDRED_SEVENTY_SIX(17, 576),
		SIX_HUNDRED_FOURTY(18, 640),		
		UNKNOWN(-1, 00);
		
		// Declare properties
		private final int code;
		private final int rate;
		
		/**
		 * Constructor.
		 * 
		 * @param code - the code.
		 * @param rate - the rate.
		 */
		BitRate(final int code, final int rate) {
			this.code = code;
			this.rate = rate;
		}

		/**
		 * @return the code.
		 */
		public int getCode() {
			return this.code;
		}

		/**
		 * @return the sample rate.
		 */
		public int getRate() {
			return this.rate;
		}
		
		@Override
		public String toString() {
			final StringBuilder builder = new StringBuilder();
			builder.append(this.rate);
			
			return builder.toString();
		}
	}
}
