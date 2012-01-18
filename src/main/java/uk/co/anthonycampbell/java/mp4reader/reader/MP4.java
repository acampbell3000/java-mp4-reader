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
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.anthonycampbell.java.mp4reader.box.common.Box;
import uk.co.anthonycampbell.java.mp4reader.box.item.IlstBox;
import uk.co.anthonycampbell.java.mp4reader.box.movie.MoovBox;
import uk.co.anthonycampbell.java.mp4reader.box.track.TrakBox;
import uk.co.anthonycampbell.java.mp4reader.box.type.FtypBox;
import uk.co.anthonycampbell.java.mp4reader.util.Util;

/**
 * Class to encapsulate meta data read from a MP4 file.
 *
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public class MP4 implements Comparable<MP4> {
	
	// Log
	private static final Logger log = LoggerFactory.getLogger(MP4.class.getName());
	
	// Declare properties
	protected String containerType;
	protected Path filePath;

	protected String title;
	protected String show;
	protected Date releaseDate;
	protected Rating rating;
	protected String genre;
	protected byte[] cover;
	protected String description;
	protected String synopsis;
	protected MediaType mediaType;
	protected List<String> actors;
	protected List<String> directors;
	protected List<String> producers;
	protected List<String> screenWriters;
	protected String tvShow;
	protected String tvEpisodeId;
	protected int tvSeason;
	protected int tvEpisode;
	protected String tvNetworkName;
	protected String copyright;
	protected int trackNumber;
	protected int trackTotal;
	protected int diskNumber;
	protected int diskTotal;
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
					this.containerType = ((FtypBox) box).getMajorBrand();
					
				} else if (box instanceof MoovBox && BoxType.MOVIE_HEADER == boxType) {
					final MoovBox moovBox = (MoovBox) box;

					this.creationDate = moovBox.getCreationDate();
					this.modifiedDate = moovBox.getModifiedDate();				
					this.duration = moovBox.getDuration();
					moovBox.getTimeScale();
					
					final IlstBox ilstBox = moovBox.getMetaData();
					if (ilstBox != null) {
						this.title = ilstBox.getTitle();
						this.show = ilstBox.getShow();
						this.releaseDate = ilstBox.getDate();
						this.genre = ilstBox.getGenre();
						this.cover = ilstBox.getCover();
						this.description = ilstBox.getDescription();
						this.synopsis = ilstBox.getSynopsis();
						this.actors = ilstBox.getActors();
						this.directors = ilstBox.getDirectors();
						this.producers = ilstBox.getProducers();
						this.screenWriters = ilstBox.getScreenWriters();
						this.tvShow = ilstBox.getTvShow();
						this.tvEpisodeId = ilstBox.getTvEpisodeId();
						this.tvSeason = ilstBox.getTvSeason();
						this.tvEpisode = ilstBox.getTvEpisode();
						this.tvNetworkName = ilstBox.getTvNetworkName();
						this.copyright = ilstBox.getCopyright();
						this.trackNumber = ilstBox.getTrackNumber();
						this.trackTotal = ilstBox.getTrackTotal();
						this.diskNumber = ilstBox.getDiskNumber();
						this.diskTotal = ilstBox.getDiskTotal();
						this.encodingTool = ilstBox.getEncodingTool();
						this.sortName = ilstBox.getSortName();
						this.sortAlbum = ilstBox.getSortAlbum();
						this.sortAlbumArtist = ilstBox.getSortAlbumArtist();
						this.sortArtist = ilstBox.getSortArtist();
						this.sortShow = ilstBox.getSortShow();
						
						final IlstBox.Rating rating = ilstBox.getRating();
						switch (rating) {
							case UNIVERSAL:
								this.rating = Rating.UNIVERSAL;
								break;
							case PARENTAL_GUIDANCE:
								this.rating = Rating.PARENTAL_GUIDANCE;
								break;
							case TWELVE:
								this.rating = Rating.TWELVE;
								break;
							case TWELVE_A:
								this.rating = Rating.TWELVE_A;
								break;
							case FIFTHTEEN:
								this.rating = Rating.FIFTHTEEN;
								break;
							case EIGHTEEN:
								this.rating = Rating.EIGHTEEN;
								break;
							default:
								this.rating = Rating.UNKNOWN;
								break;
						}
						
						final IlstBox.MediaType mediaType = ilstBox.getMediaType();
						switch (mediaType) {
							case MOVIE:
								this.mediaType = MediaType.MOVIE;
								break;
							case NORMAL:
								this.mediaType = MediaType.NORMAL;
								break;
							case AUDIOBOOK:
								this.mediaType = MediaType.AUDIOBOOK;
								break;
							case MUSIC_VIDEO:
								this.mediaType = MediaType.MUSIC_VIDEO;
								break;
							case SHORT_FILM:
								this.mediaType = MediaType.SHORT_FILM;
								break;
							case TV_SHOW:
								this.mediaType = MediaType.TV_SHOW;
								break;
							case BOOKLET:
								this.mediaType = MediaType.BOOKLET;
								break;
							case RINGTONE:
								this.mediaType = MediaType.RINGTONE;
								break;
							default:
								this.mediaType = MediaType.UNKNOWN;
								break;
						}
					}

					final Set<TrakBox> trackSet = moovBox.getTrackSet();
					for (final TrakBox trakBox : trackSet) {
						trakBox.getCreationDate();
						trakBox.getModifiedDate();
						trakBox.getAudioSample();
						trakBox.getVideoSample();
						trakBox.getTextSample();
						trakBox.getDuration();
						trakBox.getMetaData();
						trakBox.getTrackId();
						trakBox.getType();
						trakBox.getTrackName();
						trakBox.getTotalBlockSize();
					}
				}
			}
		} else {
			throw new IllegalArgumentException("Unable to update " + this.getClass().getSimpleName() +
					" instance, the provided box is invalid! (box=" + box + ")");
		}
	}

	@Override
	public int compareTo(final MP4 mp4) {
		// Validate
		if (mp4 != null) {
//			mp4
			
		} else {
			return 1;
		}
		
		return 0;
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName());
		builder.append(" -> ");
		builder.append(Util.printFields(this));
		
		return builder.toString();
	}
	
	/**
	 * ENUM to represent the UK movie ratings.
	 * 
	 * @author Anthony Campbell - anthonycampbell.co.uk
	 */
	public enum Rating {
		UNKNOWN("", -1),
		UNIVERSAL("U", 0),
		PARENTAL_GUIDANCE("PG", 1),
		TWELVE("12", 2),
		TWELVE_A("12A", 3),
		FIFTHTEEN("15", 4),
		EIGHTEEN("18", 5);
		
		// Declare properties
		private final String rating;
		private final int weighting;
		
		/**
		 * Constructor.
		 * 
		 * @param rating - the rating.
		 * @param weighting - the rating weight.
		 */
		private Rating(final String rating, final int weighting) {
			this.rating = rating;
			this.weighting = weighting;
		}

		/**
		 * @return the rating.
		 */
		public String getRating() {
			return this.rating;
		}
		
		/**
		 * @return the rating weight.
		 */
		public int getWeighting() {
			return this.weighting;
		}
	}

	/**
	 * ENUM to represent the container media type.
	 * 
	 * @author Anthony Campbell - anthonycampbell.co.uk
	 */
	public enum MediaType {
		MOVIE("Movie"),
		NORMAL("Music"),
		AUDIOBOOK("Audio"),
		MUSIC_VIDEO("Music Video"),
		SHORT_FILM("Short Film"),
		TV_SHOW("TV Show"),
		BOOKLET("Booklet"),
		RINGTONE("Ringtone"),
		UNKNOWN("Unknown");
		
		// Declare properties
		private final String name;
		
		/**
		 * Constructor.
		 * 
		 * @param name - the media type name.
		 */
		private MediaType(final String name) {
			this.name = name;
		}

		/**
		 * @return the name.
		 */
		public String getName() {
			return this.name;
		}
	}
}
