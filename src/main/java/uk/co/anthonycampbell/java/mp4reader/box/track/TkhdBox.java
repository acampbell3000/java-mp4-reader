package uk.co.anthonycampbell.java.mp4reader.box.track;

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
import java.math.BigInteger;

import uk.co.anthonycampbell.java.mp4reader.box.common.AbstractBox;
import uk.co.anthonycampbell.java.mp4reader.box.common.Box;
import uk.co.anthonycampbell.java.mp4reader.reader.BoxType;
import uk.co.anthonycampbell.java.mp4reader.reader.MP4InputStream;
import uk.co.anthonycampbell.java.mp4reader.reader.MP4Reader;
import uk.co.anthonycampbell.java.mp4reader.util.Util;

/**
 * Class to encapsulate the MP4 track header box (tkhd).
 * 
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public class TkhdBox extends AbstractBox implements Box {

	// Declare box properties
	protected final short version;
	protected final long flags;
	protected final BigInteger creationDate;
	protected final BigInteger modifiedDate;
	protected final int trackId;
	protected final BigInteger duration;
	protected final short videoLayer;
	protected final short quicktimeTrackId;
	protected final float trackAudioVolume;
	protected final float videoWidthScale;
	protected final float videoWidthRotate;
	protected final float videoWidthAngle;
	protected final float videoHeightRotate;
	protected final float videoHeightScale;
	protected final float videoHeightAngle;
	protected final float videoPositionLeft;
	protected final float videoPositionTop;
	protected final float videoDividerScale;
	protected final int videoFrameWidth;
	protected final int videoFrameHeight;
	
	/**
	 * Constructor.
	 * 
	 * @param reader - MP4 file reader.
	 * @param remainingOffset - remaining bytes to be read from the stream.
	 * @param boxName - name of the box type.
	 * @param boxType - box type ENUM.
	 * @throws IOException Unable read remaining bytes from the stream.
	 */
	public TkhdBox(final MP4Reader reader, final long remainingOffset, final String boxName,
			final BoxType boxType) throws IOException {
		super(reader, remainingOffset, boxName, boxType);
		
		this.version = reader.readUnsignedByte();
		this.flags = reader.readHex();
		
		if (remainingOffset >= 92) {
			this.creationDate = reader.readLong();
			this.modifiedDate = reader.readLong();
		} else {
			this.creationDate = new BigInteger("" + reader.readUnsignedInt());
			this.modifiedDate = new BigInteger("" + reader.readUnsignedInt());	
		}
		
		this.trackId = reader.readInt();
		skip(MP4InputStream.THIRTY_TWO_BIT_BYTE_LENGTH * 2);
		
		if (remainingOffset == 88 || remainingOffset == 96) {
			this.duration = reader.readLong();	
		} else {
			this.duration = new BigInteger("" + reader.readUnsignedInt());
		}
		skip(MP4InputStream.THIRTY_TWO_BIT_BYTE_LENGTH);
		
		this.videoLayer = reader.readShort();
		this.quicktimeTrackId = reader.readShort();
		this.trackAudioVolume = reader.readShortFloat();
		skip(MP4InputStream.SIXTEEN_BIT_BYTE_LENGTH);
		
		this.videoWidthScale = reader.readFloat();
		this.videoWidthRotate = reader.readFloat();
		this.videoWidthAngle = reader.readFloat();
		this.videoHeightRotate = reader.readFloat();
		this.videoHeightScale = reader.readFloat();
		this.videoHeightAngle = reader.readFloat();
		this.videoPositionLeft = reader.readFloat();
		this.videoPositionTop = reader.readFloat();
		this.videoDividerScale = reader.readFloat();
		this.videoFrameWidth = reader.readInt();
		this.videoFrameHeight = reader.readInt();
		
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
	 * @return the creation date.
	 */
	public BigInteger getCreationDate() {
		return this.creationDate;
	}
	
	/**
	 * @return the modified date.
	 */
	public BigInteger getModifiedDate() {
		return this.modifiedDate;
	}
	
	/**
	 * @return the track ID.
	 */
	public int getTrackId() {
		return this.trackId;
	}
	
	/**
	 * @return the duration.
	 */
	public BigInteger getDuration() {
		return this.duration;
	}
	
	/**
	 * @return the video layer.
	 */
	public short getVideoLayer() {
		return this.videoLayer;
	}
	
	/**
	 * @return the QUICKTIME track ID.
	 */
	public short getQuicktimeTrackId() {
		return this.quicktimeTrackId;
	}
	
	/**
	 * @return the track audio volume.
	 */
	public float getTrackAudioVolume() {
		return this.trackAudioVolume;
	}
	
	/**
	 * @return the video width scale.
	 */
	public float getVideoWidthScale() {
		return this.videoWidthScale;
	}
	
	/**
	 * @return the video width rotate.
	 */
	public float getVideoWidthRotate() {
		return this.videoWidthRotate;
	}
	
	/**
	 * @return the video width angle.
	 */
	public float getVideoWidthAngle() {
		return this.videoWidthAngle;
	}
	
	/**
	 * @return the video height rotate.
	 */
	public float getVideoHeightRotate() {
		return this.videoHeightRotate;
	}
	
	/**
	 * @return the video height scale.
	 */
	public float getVideoHeightScale() {
		return this.videoHeightScale;
	}
	
	/**
	 * @return the video height angle.
	 */
	public float getVideoHeightAngle() {
		return this.videoHeightAngle;
	}
	
	/**
	 * @return the video position left.
	 */
	public float getVideoPositionLeft() {
		return this.videoPositionLeft;
	}
	
	/**
	 * @return the video position top.
	 */
	public float getVideoPositionTop() {
		return this.videoPositionTop;
	}
	
	/**
	 * @return the video divider scale.
	 */
	public float getVideoDividerScale() {
		return this.videoDividerScale;
	}
	
	/**
	 * @return the video frame width.
	 */
	public int getVideoFrameWidth() {
		return this.videoFrameWidth;
	}
	
	/**
	 * @return the video frame height.
	 */
	public int getVideoFrameHeight() {
		return this.videoFrameHeight;
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append(Util.printFields(this));
		
		return builder.toString();
	}
}
