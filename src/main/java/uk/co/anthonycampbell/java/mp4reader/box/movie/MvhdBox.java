package uk.co.anthonycampbell.java.mp4reader.box.movie;

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
import uk.co.anthonycampbell.java.mp4reader.reader.MP4Reader;
import uk.co.anthonycampbell.java.mp4reader.util.Util;

/**
 * Class to encapsulate the MP4 movie presentation header box (mvhd).
 * 
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public class MvhdBox extends AbstractBox implements Box {

	// Declare box properties
	protected final short version;
	protected final long flags;
	protected final BigInteger creationDate;
	protected final BigInteger modifiedDate;
	protected final long timeScale;
	protected final BigInteger duration;
	protected final float playbackSpeed;
	protected final float userVolume;
	protected final float windowWidthScale;
	protected final float windowWidthRotate;
	protected final float windowWidthAngle;
	protected final float windowHeightRotate;
	protected final float windowHeightScale;
	protected final float windowHeightAngle;
	protected final float windowPositionLeft;
	protected final float windowPositionTop;
	protected final float windowDividerScale;
	protected final long previewStart;
	protected final long previewLength;
	protected final long previewFrame;
	protected final long selectionStart;
	protected final long selectionLength;
	protected final long currentTime;
	protected final int nextTrack;
	
	/**
	 * Constructor.
	 * 
	 * @param reader - MP4 file reader.
	 * @param remainingOffset - remaining bytes to be read from the stream.
	 * @param boxName - name of the box type.
	 * @param boxType - box type ENUM.
	 * @throws IOException Unable read remaining bytes from the stream.
	 */
	public MvhdBox(final MP4Reader reader, final long remainingOffset, final String boxName,
			final BoxType boxType) throws IOException {
		super(reader, remainingOffset, boxName, boxType);
		
		this.version = reader.readUnsignedByte();
		this.flags = reader.readHex();
		
		if (remainingOffset >= 108) {
			this.creationDate = reader.readLong();
			this.modifiedDate = reader.readLong();	
		} else {
			this.creationDate = new BigInteger("" + reader.readUnsignedInt());
			this.modifiedDate = new BigInteger("" + reader.readUnsignedInt());	
		}
		
		this.timeScale = reader.readUnsignedInt();
		
		if (remainingOffset == 104 || remainingOffset == 112) {
			this.duration = reader.readLong();	
		} else {
			this.duration = new BigInteger("" + reader.readUnsignedInt());
		}

		this.playbackSpeed = reader.readFloat();
		this.userVolume = reader.readShortFloat();
		
		reader.skip(10);
		this.windowWidthScale = reader.readFloat();
		this.windowWidthRotate = reader.readFloat();
		this.windowWidthAngle = reader.readFloat();
		this.windowHeightRotate = reader.readFloat();
		this.windowHeightScale = reader.readFloat();
		this.windowHeightAngle = reader.readFloat();
		this.windowPositionLeft = reader.readFloat();
		this.windowPositionTop = reader.readFloat();
		this.windowDividerScale = reader.readFloat();
		this.previewStart = reader.readUnsignedInt();
		this.previewLength = reader.readUnsignedInt();
		this.previewFrame = reader.readUnsignedInt();
		this.selectionStart = reader.readUnsignedInt();
		this.selectionLength = reader.readUnsignedInt();
		this.currentTime = reader.readUnsignedInt();
		this.nextTrack = reader.readInt();

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
	 * @return the time scale.
	 */
	public long getTimeScale() {
		return this.timeScale;
	}

	/**
	 * @return the duration.
	 */
	public BigInteger getDuration() {
		return this.duration;
	}
	
	/**
	 * @return the playback speed.
	 */
	public float getPlaybackSpeed() {
		return this.playbackSpeed;
	}
	
	/**
	 * @return the user volume.
	 */
	public float getUserVolume() {
		return this.userVolume;
	}
	
	/**
	 * @return the window width scale.
	 */
	public float getWindowWidthScale() {
		return this.windowWidthScale;
	}
	
	/**
	 * @return the window width rotate.
	 */
	public float getWindowWidthRotate() {
		return this.windowWidthRotate;
	}
	
	/**
	 * @return the window width angle.
	 */
	public float getWindowWidthAngle() {
		return this.windowWidthAngle;
	}
	
	/**
	 * @return the window height rotate.
	 */
	public float getWindowHeightRotate() {
		return this.windowHeightRotate;
	}
	
	/**
	 * @return the window height scale.
	 */
	public float getWindowHeightScale() {
		return this.windowHeightScale;
	}
	
	/**
	 * @return the window height angle.
	 */
	public float getWindowHeightAngle() {
		return this.windowHeightAngle;
	}
	
	/**
	 * @return the window position left.
	 */
	public float getWindowPositionLeft() {
		return this.windowPositionLeft;
	}
	
	/**
	 * @return the window position top.
	 */
	public float getWindowPositionTop() {
		return this.windowPositionTop;
	}
	
	/**
	 * @return the window divider scale.
	 */
	public float getWindowDividerScale() {
		return this.windowDividerScale;
	}
	
	/**
	 * @return the preview start.
	 */
	public long getPreviewStart() {
		return this.previewStart;
	}
	
	/**
	 * @return the preview length.
	 */
	public long getPreviewLength() {
		return this.previewLength;
	}
	
	/**
	 * @return the preview frame.
	 */
	public long getPreviewFrame() {
		return this.previewFrame;
	}
	
	/**
	 * @return the selection start.
	 */
	public long getSelectionStart() {
		return this.selectionStart;
	}
	
	/**
	 * @return the selection length.
	 */
	public long getSelectionLength() {
		return this.selectionLength;
	}
	
	/**
	 * @return the current time.
	 */
	public long getCurrentTime() {
		return this.currentTime;
	}

	/**
	 * @return the next track.
	 */
	public int getNextTrack() {
		return this.nextTrack;
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append(Util.printFields(this));
		
		return builder.toString();
	}
}
