package uk.co.anthonycampbell.java.mp4reader.reader;

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

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to encapsulate the reading of a MP4 input stream.
 *
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public class MP4InputStream {
	
	// Log
	private static final Logger log = LoggerFactory.getLogger(MP4InputStream.class.getName());
	
	/** Number of bytes available in a 64-bit long value. */
	public static final byte SIXTY_FOUR_BIT_BYTE_LENGTH = 8;
	
	/** Number of bytes available in a 32-bit long value. */
	public static final byte THIRTY_TWO_BIT_BYTE_LENGTH = 4;
	
	/** Number of bytes available in a 24-bit long value. */
	public static final byte TWENTY_FOUR_BIT_BYTE_LENGTH = 3;
	
	/** Number of bytes available in a 16-bit long value. */
	public static final byte SIXTEEN_BIT_BYTE_LENGTH = 2;
	
	/** Supported character set. */
	public static final String ASCII_CHARSET = "US-ASCII";
	
	// Reader properties
	private final InputStream inputStream;
	private final BufferedInputStream bufferedInputStream;
	private final DataInputStream dataInputStream;
	private long bytesRead;
	protected MP4 mp4Instance;
	
	/**
	 * Constructor.
	 * 
	 * @param file - the file to read.
	 * @throws IllegalArgumentException - Provided file reference is invalid.
	 * @throws IOException - Unable to read provided file reference.
	 */
	public MP4InputStream(final File file) throws IllegalArgumentException, IOException {
		log.trace("Initialise MP4 reader...");
		
		// Validate
		if (file != null && file.isFile() && file.canRead()) {
			log.trace("- file: " + file.getPath());
			
			// Prepare last modified date
			final Date lastModified = new Date();
			lastModified.setTime(file.lastModified());			
			
			// Initialise stream
			this.inputStream = FileUtils.openInputStream(file);
			this.bufferedInputStream = new BufferedInputStream(this.inputStream);
			this.dataInputStream = new DataInputStream(this.bufferedInputStream);
			this.mp4Instance = new MP4(file.getAbsolutePath(), lastModified);
			this.bytesRead = 0;

			log.trace("- size: " + available());
		} else {
			throw new IllegalArgumentException("Provided file reference is invalid! (file=" +
					file +")");
		}
	}
	
	/**
	 * Method to read the next byte from the input stream.
	 * 
	 * @return one byte.
	 * @throws IOException Unable to read the next byte from the stream.
	 */
	public byte readByte() throws IOException {
		final byte b = this.dataInputStream.readByte();
		incrementReadCount(1);
		return b;
	}
	
	/**
	 * Method to read the next unsigned byte from the input stream.
	 * 
	 * @return one unsigned byte.
	 * @throws IOException Unable to read the next byte from the stream.
	 */
	public short readUnsignedByte() throws IOException {
		return (short) (readByte() & 0xFF);
	}
	
	/**
	 * Method to read the next 32-bits from the input stream.
	 * 
	 * @return byte array.
	 * @throws IOException Unable to read the next 32-bits from the stream.
	 */
	public byte[] readBytes() throws IOException {
		return readBytes(THIRTY_TWO_BIT_BYTE_LENGTH);
	}
	
	/**
	 * Method to read the next number of provided bytes from the MP4 reader stream.
	 * 
	 * @return byte array.
	 * @throws IOException Unable to read the provided number of bytes from the stream.
	 */
	public byte[] readBytes(final int numberOfBytes) throws IOException {
		final byte[] bytes = new byte[numberOfBytes];
		incrementReadCount(this.dataInputStream.read(bytes));
		return bytes;
	}
	
	/**
	 * Method to read the next 16-bit integer from the input stream.
	 * 
	 * @return 16-bit integer.
	 * @throws IOException Unable to read the next 16-bit integer from the stream.
	 */
	public short readShort() throws IOException {
		final short numeric = this.dataInputStream.readShort();
		incrementReadCount(SIXTEEN_BIT_BYTE_LENGTH);
		return numeric;
	}
	
	/**
	 * Method to read the next unsigned short from the input stream.
	 * 
	 * @return one unsigned short.
	 * @throws IOException Unable to read the next short from the stream.
	 */
	public int readUnsignedShort() throws IOException {
		return (int) (readShort() & 0xFFFF);
	}
	
	/**
	 * Method to read the next 24-bit hex value from the input stream.
	 * 
	 * @return 24-bit hex value.
	 * @throws IOException Unable to read the next 24-bit hex value from the stream.
	 */
	public long readHex() throws IOException {
		return readHex(TWENTY_FOUR_BIT_BYTE_LENGTH);
	}
	
	/**
	 * Method to read the next provided number of hex bytes from the input stream.
	 * 
	 * @return hex value.
	 * @throws IOException Unable to read the next hex values from the stream.
	 */
	public long readHex(final int numberOfBytes) throws IOException {
		int hex = 0;
		
		for (int i = 0; i < numberOfBytes; i++) {
			hex = (hex << 8) + (readByte() & 0xFF);
		}
		
		return hex;
	}
	
	/**
	 * Method to read the next 32-bit integer from the input stream.
	 * 
	 * @return 32-bit integer.
	 * @throws IOException Unable to read the next 32-bit integer from the stream.
	 */
	public int readInt() throws IOException {
		final int numeric = this.dataInputStream.readInt();
		incrementReadCount();
		return numeric;
	}
	
	/**
	 * Method to read the next unsigned int from the input stream.
	 * 
	 * @return one unsigned int.
	 * @throws IOException Unable to read the next int from the stream.
	 */
	public long readUnsignedInt() throws IOException {
		return (long) (readInt() & 0xFFFFFFFFL);
	}
	
	/**
	 * Method to read the next 64-bit long from the input stream.
	 * 
	 * @return 64-bit long.
	 * @throws IOException Unable to read the next 64-bit long from the stream.
	 */
	public BigInteger readLong() throws IOException {
		final BigInteger numeric = new BigInteger("" + this.dataInputStream.readLong());
		incrementReadCount(THIRTY_TWO_BIT_BYTE_LENGTH * 2);
		return numeric;
	}
	
	/**
	 * Method to read the next 16-bit floating point from the input stream.
	 * 
	 * @return 16-bit floating point.
	 * @throws IOException Unable to read the next 16-bit floating point from the stream.
	 */
	public float readShortFloat() throws IOException {
		return Float.parseFloat(readByte() + "." + readUnsignedByte());
	}
	
	/**
	 * Method to read the next 32-bit floating point from the input stream.
	 * 
	 * @return 32-bit floating point.
	 * @throws IOException Unable to read the next 32-bit floating point from the stream.
	 */
	public float readFloat() throws IOException {
		return Float.parseFloat(readShort() + "." + readUnsignedShort());
	}
	
	/**
	 * Method to read the next 64-bit floating point from the input stream.
	 * 
	 * @return 64-bit floating point.
	 * @throws IOException Unable to read the next 64-bit floating point from the stream.
	 */
	public double readDouble() throws IOException {
		return Double.parseDouble(readInt() + "." + readUnsignedInt());
	}
	
	/**
	 * Method to read the next 32-bit string from the input stream.
	 * 
	 * @return string representation of the read bytes.
	 * @throws IOException Unable to read the next 32-bit string from the stream.
	 */
	public String readString() throws IOException {
		return readString(THIRTY_TWO_BIT_BYTE_LENGTH);
	}
	
	/**
	 * Method to read a string from the provided next number of bytes in the input stream.
	 * 
	 * @param byteLength - the number of bytes to read from the stream.
	 * @return string representation of the read bytes.
	 * @throws IOException Unable to read the provided number of bytes from the input stream.
	 */
	public String readString(final int byteLength) throws IOException {
		// Initialise result and byte array
		String result = null;
		final byte[] stringByte = new byte[byteLength];
		
		// Validate
		if (this.dataInputStream != null && this.dataInputStream.available() > 0) {
			// Read
			final int read = this.dataInputStream.read(stringByte);
			incrementReadCount(read);
			
			result = new String(stringByte, ASCII_CHARSET);
		}
		
		return result;
	}
	
	/**
	 * Method to read all the 32-bit strings up until the provided total.
	 * 
	 * @param offsetTotal - total number of bytes to read.
	 * @return list containing the number of 32-bit strings available up until the provided offset.
	 * @throws IOException Unable to read the 32-bit strings from the input stream.
	 */
	public List<String> readStringOffset(final long offsetTotal) throws IOException {
		return readStringOffset(offsetTotal, THIRTY_TWO_BIT_BYTE_LENGTH);
	}
	
	/**
	 * Method to read all of the provided byte length strings up until the provided total.
	 * 
	 * @param offsetTotal - total number of bytes to read.
	 * @param byteLength - number of bytes per string.
	 * @return list containing the number of provided byte length strings available up
	 * 		until the provided offset.
	 * @throws IOException Unable to read the strings from the input stream.
	 */
	public List<String> readStringOffset(final long offsetTotal, final int byteLength)
			throws IOException {
		// Initialise result and count
		final List<String> stringList = new ArrayList<String>();
		long byteCount = 0;
		
		while (byteCount < offsetTotal) {
			stringList.add(readString(byteLength));
			byteCount = byteCount + byteLength;
		}
		
		return stringList;
	}
	
	/**
	 * Method to read all of the 32-bit integers up until the provided total.
	 * 
	 * @param offsetTotal - total number of bytes to read.
	 * @return list containing the number of 32-bit integers up until the provided offset.
	 * @throws IOException Unable to read the integers from the input stream.
	 */
	public List<Integer> readIntOffset(final long offsetTotal)
			throws IOException {
		// Initialise result and count
		final List<Integer> integerList = new ArrayList<Integer>();
		long byteCount = 0;
		
		while (byteCount < offsetTotal) {
			integerList.add(readInt());
			byteCount = byteCount + THIRTY_TWO_BIT_BYTE_LENGTH;
		}
		
		return integerList;
	}
	
	/**
	 * Method to read all of the 32-bit unsigned integers up until the provided total.
	 * 
	 * @param offsetTotal - total number of bytes to read.
	 * @return list containing the number of 32-bit unsigned integers up until the
	 * 		provided offset.
	 * @throws IOException Unable to read the unsigned integers from the input stream.
	 */
	public List<Long> readUnsignedIntOffset(final long offsetTotal)
			throws IOException {
		// Initialise result and count
		final List<Long> integerList = new ArrayList<Long>();
		long byteCount = 0;
		
		while (byteCount < offsetTotal) {
			integerList.add(readUnsignedInt());
			byteCount = byteCount + THIRTY_TWO_BIT_BYTE_LENGTH;
		}
		
		return integerList;
	}
	
	/**
	 * @return the MP4 instance.
	 */
	public MP4 getMp4Instance() {
		return this.mp4Instance;
	}

	/**
	 * Return the number of bytes remaining in the current file stream.
	 * 
	 * @return - the number of bytes available.
	 * @throws IOException Unable to retrieve the remaining number of bytes from the
	 * 		input stream.
	 */
	public int available() throws IOException {
		if (this.dataInputStream != null) {
			return this.dataInputStream.available();
		} else {
			return 0;
		}
	}
	
	/**
	 * Return the number of bytes read with this MP4 reader.
	 * 
	 * @return - the bytes read.
	 */
	public long bytesRead() {
		return this.bytesRead;
	}
	
	/**
	 * Skip the provided number of bytes in this input stream.
	 * 
	 * @param numberOfBytes - the number of bytes to skip.
	 * @throws IOException Unable to skip the provided number of bytes.
	 */
	public void skip(final long numberOfBytes) throws IOException {
		// Initialise count
		long remainingBytes = numberOfBytes;
		
		// Unfortunately skip bytes only supports integers
		if (remainingBytes > Integer.MAX_VALUE) {
			while (remainingBytes > Integer.MAX_VALUE) {
				this.dataInputStream.skipBytes(Integer.MAX_VALUE);
				this.bytesRead = this.bytesRead + Integer.MAX_VALUE;
				remainingBytes -= Integer.MAX_VALUE;
			}
		}
		
		this.dataInputStream.skipBytes((int) remainingBytes);
		this.bytesRead = this.bytesRead + remainingBytes;
	}
	
	/**
	 * Reset the current input stream's pointer and byte counter.
	 * 
	 * @throws IOException - Unable to reset MP4 input stream. 
	 */
	public void reset() throws IOException {
		this.dataInputStream.reset();
		this.bytesRead = 0;
	}
	
	/**
	 * Close the current input stream.
	 */
	public void close() {
		IOUtils.closeQuietly(this.dataInputStream);
		IOUtils.closeQuietly(this.bufferedInputStream);	
		IOUtils.closeQuietly(this.inputStream);	
	}
	
	/**
	 * Helper method to increment the byte read count by 32-bits.
	 */
	private void incrementReadCount() {
		incrementReadCount(THIRTY_TWO_BIT_BYTE_LENGTH);
	}
	
	/**
	 * Helper method to increment the byte read count.
	 * 
	 * @param byteCount - the number of bytes read.
	 */
	private void incrementReadCount(final int byteCount) {
		this.bytesRead = this.bytesRead + byteCount;
	}
}
