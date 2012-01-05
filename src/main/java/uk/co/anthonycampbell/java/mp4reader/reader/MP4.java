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

import java.math.BigInteger;
import java.util.Date;

import uk.co.anthonycampbell.java.mp4reader.box.common.Box;
import uk.co.anthonycampbell.java.mp4reader.box.item.ItemBox;
import uk.co.anthonycampbell.java.mp4reader.box.sample.VideoBox;
import uk.co.anthonycampbell.java.mp4reader.box.track.TrakBox;
import uk.co.anthonycampbell.java.mp4reader.box.track.TrakBox.Type;
import uk.co.anthonycampbell.java.mp4reader.util.Util;

/**
 * Class to encapsulate meta data read from a MP4 file.
 *
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public class MP4 {
	
	// Declare properties
	protected String title;
	protected String filePath;
	protected Date creationDate;
	protected Date modifiedDate;
	protected String show;
	protected String actors;
	protected String director;
	protected String producer;
	protected String screenWriter;
	protected String releaseDate;
	protected String rating;
	protected String genre;
	protected String description;
	protected String synopsis;
	protected byte[] cover;
	protected String copyright;
	protected long track;
	protected String encodingTool;
	protected String sortName;
	protected String sortArtist;
	protected BigInteger duration;
 	protected String bitrate; 	// bitrate (kbps)  = ( filesize * framerate ) / num_frames;
	protected int pixelFrameWidth;
	protected int pixelFrameHeight;

//	videoSample.getFrameCount();
//	videoSample.getPixelFrameWidth();
//	videoSample.getPixelFrameHeight();
	
//	Stream
//	 - type
//	 - Resolution
//	 - fps
//	--
//	 - Hz
//	 - number of channels
//	 - bitrate
//	Subtitles
	
	/**
	 * Constructor.
	 * 
	 * @param absolutePath - absolute file path.
	 * @param lastModifiedDate - last modified date.
	 */
	public MP4(final String absoluteFilePath, final Date lastModifiedDate) {
		this.filePath = absoluteFilePath;
		this.modifiedDate = lastModifiedDate;
	}

	/**
	 * Add the provided MP4 box to this MP4 instance.
	 * 
	 * @param mp4Instance - the MP4 box to add.
	 */
	public void add(final Box box) {
		if (box != null) {
			final BoxType boxType = box.getBoxType();
			
			if (boxType != null) {
				// User meta data
				if (box instanceof ItemBox) {
					final ItemBox itemBox = (ItemBox) box;
					
					switch (boxType) {
						case APPLE_ITEM_COVER:
							this.cover = itemBox.getData();
							break;
					}
				}
				
				// Sample stream information
				if (box instanceof TrakBox) {
					final TrakBox trakBox = (TrakBox) box;
					final Type trakType = trakBox.getType();
					
					this.duration = trakBox.getDuration();
					this.creationDate = trakBox.getCreationDate();
					
					if (trakType != null) {
						switch (trakType) {
							case VIDEO:
								final VideoBox videoSample = trakBox.getVideoSample();
								
								if (videoSample != null) {
									videoSample.getFrameCount();
									this.pixelFrameWidth = videoSample.getPixelFrameWidth();
									this.pixelFrameHeight = videoSample.getPixelFrameHeight();
								}
								
								break;
						}
					}
				}
			}
		} else {
			throw new IllegalArgumentException("Unable to update MP4 instance, the provided box " +
					"is invalid! (box=" + box + ")");
		}
	}
	
	/**
	 * @return the title.
	 */
	public String getTitle() {
		return this.title;
	}
	
	/**
	 * @return the file path.
	 */
	public String getFilePath() {
		return this.filePath;
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
	 * @return the show.
	 */
	public String getShow() {
		return this.show;
	}
	
	/**
	 * @return the actors.
	 */
	public String getActors() {
		return this.actors;
	}
	
	/**
	 * @return the director.
	 */
	public String getDirector() {
		return this.director;
	}
	
	/**
	 * @return the producer.
	 */
	public String getProducer() {
		return this.producer;
	}
	
	/**
	 * @return the screen writer.
	 */
	public String getScreenWriter() {
		return this.screenWriter;
	}
	
	/**
	 * @return the release date.
	 */
	public String getReleaseDate() {
		return this.releaseDate;
	}
	
	/**
	 * @return the rating.
	 */
	public String getRating() {
		return this.rating;
	}
	
	/**
	 * @return the genre.
	 */
	public String getGenre() {
		return this.genre;
	}
	
	/**
	 * @return the description.
	 */
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * @return the synopsis.
	 */
	public String getSynopsis() {
		return this.synopsis;
	}
	
	/**
	 * @return the cover.
	 */
	public byte[] getCover() {
		return this.cover;
	}
	
	/**
	 * @return the copyright.
	 */
	public String getCopyright() {
		return this.copyright;
	}
	
	/**
	 * @return the track.
	 */
	public long getTrack() {
		return this.track;
	}
	
	/**
	 * @return the encoding tool.
	 */
	public String getEncodingTool() {
		return this.encodingTool;
	}
	
	/**
	 * @return the sort name.
	 */
	public String getSortName() {
		return this.sortName;
	}
	
	/**
	 * @return the sort artist.
	 */
	public String getSortArtist() {
		return this.sortArtist;
	}
	
	/**
	 * @return the duration.
	 */
	public String getDuration() {
		return this.duration.toString();
	}

	/**
	 * @return the bit rate.
	 */
	public String getBitrate() {
		return this.bitrate;
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

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName());
		builder.append(" -> ");
		builder.append(Util.printFields(this));
		
		return builder.toString();
	}
}
