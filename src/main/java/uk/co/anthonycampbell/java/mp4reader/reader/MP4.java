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
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.anthonycampbell.java.mp4reader.box.common.Box;
import uk.co.anthonycampbell.java.mp4reader.box.movie.MoovBox;
import uk.co.anthonycampbell.java.mp4reader.box.type.FtypBox;
import uk.co.anthonycampbell.java.mp4reader.util.Util;

/**
 * Class to encapsulate meta data read from a MP4 file.
 *
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public class MP4 {
	
	// Log
	private static final Logger log = LoggerFactory.getLogger(MP4.class.getName());
	
	// Declare properties
	protected String type;
	protected Path filePath;

	protected String title;
	protected String show;
	protected String releaseDate;
	protected String rating;
	protected String genre;
	protected byte[] cover;
	protected String description;
	protected String synopsis;
	protected String mediaType;
	protected List<String> actors;
	protected List<String> directors;
	protected List<String> producers;
	protected List<String> screenWriters;
	protected String tvShow;
	protected String tvEpisodeId;
	protected String tvSeason;
	protected String tvEpisode;
	protected String tvNetworkName;
	protected String copyright;
	protected String encodingTool;
	protected String sortName;
	protected String sortAlbum;
	protected String sortAlbumArtist;
	protected String sortArtist;
	protected String sortShow;

	protected Date creationDate;
	protected Date modifiedDate;
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
	 * @param filePath - MP4 file path.
	 */
	public MP4(final Path filePath) {
		this.filePath = filePath;
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
				log.debug("Adding box '" + box.getBoxName() + "' to " +
						this.getClass().getSimpleName() + "\n");
				
				if (box instanceof FtypBox && BoxType.FILE_TYPE == boxType) {
					this.type = ((FtypBox) box).getMajorBrand();
					
				} else if (box instanceof MoovBox && BoxType.MOVIE_HEADER == boxType) {
					final MoovBox moovBox = (MoovBox) box;

					this.creationDate = moovBox.getCreationDate();
					this.modifiedDate = moovBox.getModifiedDate();				
					this.duration = moovBox.getDuration();
					
					moovBox.getTimeScale();
					moovBox.getTrackSet();
					moovBox.getMetaData();
				}
			}
		} else {
			throw new IllegalArgumentException("Unable to update " + this.getClass().getSimpleName() +
					" instance, the provided box is invalid! (box=" + box + ")");
		}
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
