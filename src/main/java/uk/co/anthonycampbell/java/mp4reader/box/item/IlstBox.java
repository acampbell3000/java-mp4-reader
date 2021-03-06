package uk.co.anthonycampbell.java.mp4reader.box.item;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.anthonycampbell.java.mp4reader.box.common.AbstractBox;
import uk.co.anthonycampbell.java.mp4reader.box.common.Box;
import uk.co.anthonycampbell.java.mp4reader.reader.BoxType;
import uk.co.anthonycampbell.java.mp4reader.reader.MP4Reader;
import uk.co.anthonycampbell.java.mp4reader.util.Util;

/**
 * Class to encapsulate the MP4 Apple item list box (ilst).
 * 
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public class IlstBox extends AbstractBox implements Box {
	
	// Log
	private static final Logger log = LoggerFactory.getLogger(IlstBox.class.getName());

	// Declare box properties
	protected final String title;
	protected final String show;
	protected final Date date;
	protected final Rating rating;
	protected final String genre;
	protected final byte[] cover;
	protected final String description;
	protected final String synopsis;
	protected final MediaType mediaType;
	protected final List<String> actors;
	protected final List<String> directors;
	protected final List<String> producers;
	protected final List<String> screenWriters;
	protected final String tvShow;
	protected final String tvEpisodeId;
	protected final int tvSeason;
	protected final int tvEpisode;
	protected final String tvNetworkName;
	protected final String copyright;
	protected final int trackNumber;
	protected final int trackTotal;
	protected final int diskNumber;
	protected final int diskTotal;
	protected final String encodingTool;
	protected final String sortName;
	protected final String sortAlbum;
	protected final String sortAlbumArtist;
	protected final String sortArtist;
	protected final String sortShow;
	
	/**
	 * Constructor.
	 * 
	 * @param reader - MP4 file reader.
	 * @param remainingOffset - remaining bytes to be read from the stream.
	 * @param boxName - name of the box type.
	 * @param boxType - box type ENUM.
	 * @throws IOException Unable read remaining bytes from the stream.
	 */
	public IlstBox(final MP4Reader reader, final long remainingOffset, final String boxName,
			final BoxType boxType) throws IOException {
		super(reader, remainingOffset, boxName, boxType);
		
		// Set defaults
		String title = "";
		String show = "";
		Date date = new Date();
		Rating rating = Rating.UNKNOWN;
		String genre = "";
		byte[] cover = new byte[0];
		String description = "";
		String synopsis = "";
		MediaType mediaType = MediaType.UNKNOWN;
		List<String> actors = new ArrayList<>();
		List<String> directors = new ArrayList<>();
		List<String> producers = new ArrayList<>();
		List<String> screenWriters = new ArrayList<>();
		String tvShow = "";
		String tvEpisodeId = "";	 
		int tvSeason = 0; 
		int tvEpisode = 0;	 
		String tvNetworkName = "";
		String copyright = "";
		int trackNumber = 0;
		int trackTotal = 0;
		int diskNumber = 0;
		int diskTotal = 0;
		String encodingTool = "";
		String sortName = "";
		String sortAlbum = "";
		String sortAlbumArtist = "";
		String sortArtist = "";
		String sortShow = "";
		
		// Parse inner boxes
		while (bytesRemaining() >= 8) {
			final Box nextBox = reader.nextBox();
			
			// Validate
			if (nextBox != null && nextBox instanceof ItemBox) {
				final ItemBox itemBox = (ItemBox) nextBox;
				final byte[] data = itemBox.getData();

				if (data != null && data.length > 0) {
					switch (nextBox.getBoxType()) {
						case APPLE_ITEM_NAME:
							title = new String(data);
							break;
							
						case APPLE_ITEM_ARTIST:
							show = new String(data);
							break;
							
						case APPLE_ITEM_CREATION:
							final String creationDateString = new String(data);
							try {
								date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(creationDateString);
							} catch (final ParseException pe) {
								log.error("Creation date value is invalid! (creationDateString=" +
										creationDateString + ")");
							}
							break;
							
						case APPLE_ITEM_GENRE:
						case APPLE_ITEM_GENRE2:
							genre = new String(data);
							break;
							
						case APPLE_ITEM_COVER:
							cover = data;
							break;
							
						case APPLE_ITEM_DESCRIPTION:
							description = new String(data);
							break;
							
						case APPLE_ITEM_SYNOPSIS:
							synopsis = new String(data);
							break;
							
						case APPLE_ITEM_MEDIA_TYPE:
							final int mediaTypeInt = (data[0] & 0xFF);
							final MediaType[] mediaTypeValues = MediaType.values();
							
							for (final MediaType mediaTypeValue : mediaTypeValues) {
								if (mediaTypeValue.getId() == mediaTypeInt) {
									mediaType = mediaTypeValue;
									break;
								}
							}
							break;
							
						case APPLE_ITEM_ADDITIONAL_NAME:
							final String itemBoxName = itemBox.getBoxName();
							final ITunes[] iTunesValues = ITunes.values();
							
							ITunes iTunesProperty = ITunes.UNKNOWN;
							for (final ITunes iTunesValue : iTunesValues) {
								if (iTunesValue.getName().equals(itemBoxName)) {
									iTunesProperty = iTunesValue;
									break;
								}
							}
							
							switch (iTunesProperty) {
								case RATING:
									final String ratingString = new String(data);

									log.trace(ratingString);
									
									if (StringUtils.isNotEmpty(ratingString)) {
										final String[] ratingSplit = ratingString.split("\\|");
										
										if (ratingSplit.length >= 3 && Rating.KEY.equals(ratingSplit[0])) {
											final Rating[] ratings = Rating.values();
											
											for (final Rating ratingValue : ratings) {
												if (ratingValue.getRating().equals(ratingSplit[1])) {
													rating = ratingValue;
													break;
												}
											}
										}
									}
									break;

								case META:
									final String metaXml = new String(data);

									log.trace(metaXml);
									
									if (StringUtils.isNotEmpty(metaXml)) {
										final Map<String, List<String>> iTunesMetaData = Util.parseITunesMeta(metaXml);
										
										if (iTunesMetaData != null && !iTunesMetaData.isEmpty()) {
											for (final String key: iTunesMetaData.keySet()) {
												if (StringUtils.isNotEmpty(key)) {
													switch (key) {
														case "cast":
															actors.addAll(iTunesMetaData.get(key));
															break;
															
														case "directors":
															directors.addAll(iTunesMetaData.get(key));
															break;
															
														case "producers":
															producers.addAll(iTunesMetaData.get(key));
															break;
															
														case "screenwriters":
															screenWriters.addAll(iTunesMetaData.get(key));
															break;
													}
												}
											}
										}
									}
									break;
							}
							
							break;
							
						case APPLE_ITEM_TV_SHOW:
							tvShow = new String(data);
							break;
							
						case APPLE_ITEM_TV_EPISODE_ID:
							tvEpisodeId = new String(data);
							break;
							
						case APPLE_ITEM_TV_SEASON:
							tvSeason = Util.convertToUint16(Arrays.copyOfRange(data, 2, 4));
							break;
							
						case APPLE_ITEM_TV_EPISODE:
							tvEpisode = Util.convertToUint16(Arrays.copyOfRange(data, 2, 4));
							break;
							
						case APPLE_ITEM_TV_NETWORK_NAME:
							tvNetworkName = new String(data);
							break;
							
						case APPLE_ITEM_COPYRIGHT:
							copyright = new String(data);
							break;
							
						case APPLE_ITEM_ENCODING_TOOL:
							encodingTool = new String(data);
							break;
							
						case APPLE_ITEM_TRACK_NUMBER:
							trackNumber = Util.convertToUint16(Arrays.copyOfRange(data, 2, 4));
							trackTotal = Util.convertToUint16(Arrays.copyOfRange(data, 4, 6));
							break;
							
						case APPLE_ITEM_DISK:
							diskNumber = Util.convertToUint16(Arrays.copyOfRange(data, 2, 4));
							diskTotal = Util.convertToUint16(Arrays.copyOfRange(data, 4, 6));
							break;
							
						case APPLE_ITEM_SORT_NAME:
							sortName = new String(data);
							break;
							
						case APPLE_ITEM_SORT_ALBUM:
							sortAlbum = new String(data);
							break;
							
						case APPLE_ITEM_SORT_ALBUM_ARTIST:
							sortAlbumArtist = new String(data);
							break;
							
						case APPLE_ITEM_SORT_ARTIST:
							sortArtist = new String(data);
							break;
							
						case APPLE_ITEM_SORT_SHOW:
							sortShow = new String(data);
							break;
					}
				}
				
				log.debug("- '" + boxName + "' -> " + nextBox);	
			}
		}

		this.title = title;
		this.show = show;
		this.date = date;
		this.rating = rating;
		this.genre = genre;
		this.cover = cover;
		this.description = description;
		this.synopsis = synopsis;
		this.mediaType = mediaType;
		this.actors = actors;
		this.directors = directors;
		this.producers = producers;
		this.screenWriters = screenWriters;
		this.tvShow = tvShow;
		this.tvEpisodeId = tvEpisodeId;
		this.tvSeason = tvSeason;
		this.tvEpisode = tvEpisode;
		this.tvNetworkName = tvNetworkName;
		this.copyright = copyright;
		this.trackNumber = trackNumber;
		this.trackTotal = trackTotal;
		this.diskNumber = diskNumber;
		this.diskTotal = diskTotal;
		this.encodingTool = encodingTool;
		this.sortName = sortName;
		this.sortAlbum = sortAlbum;
		this.sortAlbumArtist = sortAlbumArtist;
		this.sortArtist = sortArtist;
		this.sortShow = sortShow;
		
		// Clean up
		skip();
	}
	
	/**
	 * @return the title.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * @return the show.
	 */
	public String getShow() {
		return this.show;
	}

	/**
	 * @return the creation date.
	 */
	public Date getDate() {
		return this.date;
	}

	/**
	 * @return the rating.
	 */
	public Rating getRating() {
		return this.rating;
	}

	/**
	 * @return the GENRE.
	 */
	public String getGenre() {
		return this.genre;
	}
	
	/**
	 * @return the cover.
	 */
	public byte[] getCover() {
		return this.cover;
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
	 * @return the media type.
	 */
	public MediaType getMediaType() {
		return this.mediaType;
	}
	
	/**
	 * @return list of actors.
	 */
	public List<String> getActors() {
		return this.actors;
	}

	/**
	 * @return list of directors.
	 */
	public List<String> getDirectors() {
		return this.directors;
	}

	/**
	 * @return list of producers.
	 */
	public List<String> getProducers() {
		return this.producers;
	}

	/**
	 * @return list of screen writers.
	 */
	public List<String> getScreenWriters() {
		return this.screenWriters;
	}

	/**
	 * @return the TV show.
	 */
	public String getTvShow() {
		return this.tvShow;
	}

	/**
	 * @return the TV episode ID.
	 */
	public String getTvEpisodeId() {
		return this.tvEpisodeId;
	}

	/**
	 * @return the TV season.
	 */
	public int getTvSeason() {
		return this.tvSeason;
	}

	/**
	 * @return the TV episode.
	 */
	public int getTvEpisode() {
		return this.tvEpisode;
	}

	/**
	 * @return the TV network name.
	 */
	public String getTvNetworkName() {
		return this.tvNetworkName;
	}

	/**
	 * @return the copyright.
	 */
	public String getCopyright() {
		return this.copyright;
	}

	/**
	 * @return the track number.
	 */
	public int getTrackNumber() {
		return this.trackNumber;
	}

	/**
	 * @return the track total.
	 */
	public int getTrackTotal() {
		return this.trackTotal;
	}

	/**
	 * @return the disk number.
	 */
	public int getDiskNumber() {
		return this.diskNumber;
	}

	/**
	 * @return the disk total.
	 */
	public int getDiskTotal() {
		return this.diskTotal;
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
	 * @return the sort album.
	 */
	public String getSortAlbum() {
		return this.sortAlbum;
	}

	/**
	 * @return the sort album artist.
	 */
	public String getSortAlbumArtist() {
		return this.sortAlbumArtist;
	}

	/**
	 * @return the sort artist.
	 */
	public String getSortArtist() {
		return this.sortArtist;
	}

	/**
	 * @return the sort show.
	 */
	public String getSortShow() {
		return this.sortShow;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append(Util.printFields(this));
		
		return builder.toString();
	}
	
	/**
	 * ENUM for iTunes specific items.
	 * 
	 * @author Anthony Campbell - anthonycampbell.co.uk
	 */
	public enum ITunes {
		RATING("iTunEXTC"),
		META("iTunMOVI"),
		UNKNOWN("");
		
		// Declare properties
		private final String name;

		/**
		 * Constructor.
		 * 
		 * @param name - property name.
		 */
		ITunes(final String name) {
			this.name = name;
		}

		/**
		 * @return the name.
		 */
		public String getName() {
			return this.name;
		}
	}
	
	/**
	 * ENUM to represent the UK movie ratings.
	 * 
	 * @author Anthony Campbell - anthonycampbell.co.uk
	 */
	public enum Rating {
		UNKNOWN(""),
		UNIVERSAL("U"),
		PARENTAL_GUIDANCE("PG"),
		TWELVE("12"),
		TWELVE_A("12A"),
		FIFTHTEEN("15"),
		EIGHTEEN("18");
		
		// iTunes rating key
		public static final String KEY = "uk-movie";
		
		// Declare properties
		private final String rating;
		
		/**
		 * Constructor.
		 * 
		 * @param rating - the rating.
		 */
		private Rating(final String rating) {
			this.rating = rating;
		}

		/**
		 * @return the rating.
		 */
		public String getRating() {
			return this.rating;
		}
	}

	/**
	 * ENUM to represent the container media type.
	 * 
	 * @author Anthony Campbell - anthonycampbell.co.uk
	 */
	public enum MediaType {
		MOVIE(0, "Movie"),
		NORMAL(1, "Music"),
		AUDIOBOOK(2, "Audio"),
		MUSIC_VIDEO(6, "Music Video"),
		SHORT_FILM(9, "Short Film"),
		TV_SHOW(10, "TV Show"),
		BOOKLET(11, "Booklet"),
		RINGTONE(14, "Ringtone"),
		UNKNOWN(-1, "Unknown");
		
		// Declare properties
		private final int id;
		private final String name;
		
		/**
		 * Constructor.
		 * 
		 * @param id - the media type ID.
		 * @param name - the media type name.
		 */
		private MediaType(final int id, final String name) {
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
	}
}
