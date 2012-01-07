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
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.anthonycampbell.java.mp4reader.box.common.AbstractBox;
import uk.co.anthonycampbell.java.mp4reader.box.common.Box;
import uk.co.anthonycampbell.java.mp4reader.box.item.IlstBox;
import uk.co.anthonycampbell.java.mp4reader.box.track.TrakBox;
import uk.co.anthonycampbell.java.mp4reader.box.user.UdtaBox;
import uk.co.anthonycampbell.java.mp4reader.reader.BoxType;
import uk.co.anthonycampbell.java.mp4reader.reader.MP4Reader;
import uk.co.anthonycampbell.java.mp4reader.util.Util;

/**
 * Class to encapsulate the MP4 movie presentation box (moov).
 * 
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public class MoovBox extends AbstractBox implements Box {
	
	// Log
	private static final Logger log = LoggerFactory.getLogger(MoovBox.class.getName());
	
	// Declare box properties
	protected final Date creationDate;
	protected final Date modifiedDate;
	protected final long timeScale;
	protected final BigInteger duration;
	protected final SortedSet<TrakBox> trackSet;
	protected final IlstBox metaData;
	
	/**
	 * Constructor.
	 * 
	 * @param reader - MP4 file reader.
	 * @param remainingOffset - remaining bytes to be read from the stream.
	 * @param boxName - name of the box type.
	 * @param boxType - box type ENUM.
	 * @throws IOException Unable read remaining bytes from the stream.
	 */
	public MoovBox(final MP4Reader reader, final long remainingOffset, final String boxName,
			final BoxType boxType) throws IOException {
		super(reader, remainingOffset, boxName, boxType);
			
		// Prepare defaults
		Date creationDate = new Date();
		Date modifiedDate = new Date();
		long timeScale = 0;
		BigInteger duration = new BigInteger("0");
		this.trackSet = new TreeSet<>();
		IlstBox metaData = null;
		
		// Parse inner boxes
		while (bytesRemaining() > 0) {
			final Box nextBox = reader.nextBox();
			
			// Validate
			if (nextBox != null) {
				if (nextBox instanceof MvhdBox &&
						BoxType.MOVIE_PRESENTATION_HEADER == nextBox.getBoxType()) {
					final MvhdBox mvhdBox = (MvhdBox) nextBox;

					final BigInteger bigCreationDate = mvhdBox.getCreationDate();
					final BigInteger bigModifiedDate = mvhdBox.getModifiedDate();
					if (bigCreationDate != null) {
						creationDate = Util.generateDate(bigCreationDate);
					}
					if (bigModifiedDate != null) {
						modifiedDate = Util.generateDate(bigModifiedDate);
					}

					timeScale = mvhdBox.getTimeScale();
					duration = mvhdBox.getDuration();
					
				} else if (nextBox instanceof TrakBox && BoxType.TRACK == nextBox.getBoxType()) {
					this.trackSet.add((TrakBox) nextBox);
					
				} else if (nextBox instanceof UdtaBox && BoxType.USER_DATA == nextBox.getBoxType()) {
					metaData = ((UdtaBox) nextBox).getMetaData();
				}
				
				log.debug("- '" + boxName + "' -> " + nextBox);	
			}
		}

		this.creationDate = creationDate;
		this.modifiedDate = modifiedDate;
		this.timeScale = timeScale;
		this.duration = duration;
		this.metaData = metaData;
		
		// Clean up
		skip();
	}
	
	/**
	 * @return the creation date.
	 */
	public Date getCreationDate() {
		return this.creationDate;
	}
	
	/**
	 * @return the modified date.
	 */
	public Date getModifiedDate() {
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
	 * @return the track set.
	 */
	public SortedSet<TrakBox> getTrackSet() {
		return this.trackSet;
	}
	
	/**
	 * @return the meta data.
	 */
	public IlstBox getMetaData() {
		return this.metaData;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append(Util.printFields(this));
		
		return builder.toString();
	}
}
