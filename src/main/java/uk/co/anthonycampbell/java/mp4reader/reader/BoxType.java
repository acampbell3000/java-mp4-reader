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

import uk.co.anthonycampbell.java.mp4reader.box.common.Box;
import uk.co.anthonycampbell.java.mp4reader.box.common.DrefBox;
import uk.co.anthonycampbell.java.mp4reader.box.common.HdlrBox;
import uk.co.anthonycampbell.java.mp4reader.box.free.FreeBox;
import uk.co.anthonycampbell.java.mp4reader.box.free.SkipBox;
import uk.co.anthonycampbell.java.mp4reader.box.free.WideBox;
import uk.co.anthonycampbell.java.mp4reader.box.item.AdditionalInfoBox;
import uk.co.anthonycampbell.java.mp4reader.box.item.DataBox;
import uk.co.anthonycampbell.java.mp4reader.box.item.IlstBox;
import uk.co.anthonycampbell.java.mp4reader.box.item.ItemBox;
import uk.co.anthonycampbell.java.mp4reader.box.media.IodsBox;
import uk.co.anthonycampbell.java.mp4reader.box.media.MdatBox;
import uk.co.anthonycampbell.java.mp4reader.box.media.MdraBox;
import uk.co.anthonycampbell.java.mp4reader.box.movie.MetaBox;
import uk.co.anthonycampbell.java.mp4reader.box.movie.MoovBox;
import uk.co.anthonycampbell.java.mp4reader.box.movie.MvhdBox;
import uk.co.anthonycampbell.java.mp4reader.box.sample.AudioBox;
import uk.co.anthonycampbell.java.mp4reader.box.sample.Dac3Box;
import uk.co.anthonycampbell.java.mp4reader.box.sample.EsdsBox;
import uk.co.anthonycampbell.java.mp4reader.box.sample.StblBox;
import uk.co.anthonycampbell.java.mp4reader.box.sample.StscBox;
import uk.co.anthonycampbell.java.mp4reader.box.sample.StsdBox;
import uk.co.anthonycampbell.java.mp4reader.box.sample.SttsBox;
import uk.co.anthonycampbell.java.mp4reader.box.sample.TextBox;
import uk.co.anthonycampbell.java.mp4reader.box.sample.VideoBox;
import uk.co.anthonycampbell.java.mp4reader.box.stream.DinfBox;
import uk.co.anthonycampbell.java.mp4reader.box.stream.MdhdBox;
import uk.co.anthonycampbell.java.mp4reader.box.stream.MdiaBox;
import uk.co.anthonycampbell.java.mp4reader.box.stream.MinfBox;
import uk.co.anthonycampbell.java.mp4reader.box.track.ChapBox;
import uk.co.anthonycampbell.java.mp4reader.box.track.TkhdBox;
import uk.co.anthonycampbell.java.mp4reader.box.track.TrakBox;
import uk.co.anthonycampbell.java.mp4reader.box.track.TrefBox;
import uk.co.anthonycampbell.java.mp4reader.box.type.FtypBox;
import uk.co.anthonycampbell.java.mp4reader.box.user.UdtaBox;

/**
 * Enum to hold all of the MP4 supported box types.
 *
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public enum BoxType {

	// Supported types
	FILE_TYPE("ftyp", FtypBox.class),
	TRACK("trak", TrakBox.class),
	TRACK_HEADER("tkhd", TkhdBox.class),
	TRACK_REFERENCE("tref", TrefBox.class),
	TRACK_CHAPTER_LIST("chap", ChapBox.class),
	MEDIA_STREAM("mdia", MdiaBox.class),
	MEDIA_STREAM_HEADER("mdhd", MdhdBox.class),
	MEDIA_STREAM_INFORMATION("minf", MinfBox.class),
	MEDIA_DATA("mdat", MdatBox.class),
	MOVIE_HEADER("moov", MoovBox.class),
	MOVIE_DATA_REFERENCE("mdra", MdraBox.class),
	MOVIE_PRESENTATION_HEADER("mvhd", MvhdBox.class),
	MOVIE_PRESENTATION_META_DATA("meta", MetaBox.class),
	INITIAL_OBJECT_DESCRIPTOR("iods", IodsBox.class),
	USER_DATA("udta", UdtaBox.class),
	DATA_REFERENCE("dref", DrefBox.class),
	DATA_INFORMATION("dinf", DinfBox.class),
	SAMPLE_TABLE("stbl", StblBox.class),
	SAMPLE_DESCRIPTION("stsd", StsdBox.class),
	SAMPLE_DESCRIPTOR("esds", EsdsBox.class),
	SAMPLE_M4_DESCRIPTOR("m4ds", EsdsBox.class),
	SAMPLE_DAC3_DESCRIPTOR("dac3", Dac3Box.class),
	SAMPLE_FRAMING_TIMING("stts", SttsBox.class),
	SAMPLE_FRAME_BLOCKS("stsc", StscBox.class),
	HANDLER_REFERNECE("hdlr", HdlrBox.class),
	APPLE_ITEM_LIST("ilst", IlstBox.class),	
	APPLE_ITEM_NAME("nam", ItemBox.class),
	APPLE_ITEM_COMMENT("cmt", ItemBox.class),
	APPLE_ITEM_CREATION("day", ItemBox.class),
	APPLE_ITEM_ARTIST("ART", ItemBox.class),
	APPLE_ITEM_TRACK("trk", ItemBox.class),
	APPLE_ITEM_ALBUM("alb", ItemBox.class),
	APPLE_ITEM_COMPOSER("com", ItemBox.class),
	APPLE_ITEM_ENCODER("too", ItemBox.class),
	APPLE_ITEM_GENRE("gnre", ItemBox.class),
	APPLE_ITEM_GENRE2("gen", ItemBox.class),
	APPLE_ITEM_DISK("disk", ItemBox.class),
	APPLE_ITEM_TRACK_NUMBER("trkn", ItemBox.class),
	APPLE_ITEM_BEATS_PER_MINUTE("tmpo", ItemBox.class),
	APPLE_ITEM_COMPILATION("cpil", ItemBox.class),
	APPLE_ITEM_COVER("covr", ItemBox.class),
	APPLE_ITEM_ITUNES("----", ItemBox.class),
	APPLE_ITEM_DESCRIPTION("desc", ItemBox.class),
	APPLE_ITEM_SYNOPSIS("ldes", ItemBox.class),
	APPLE_ITEM_MEDIA_TYPE("stik", ItemBox.class),
	APPLE_ITEM_TV_SHOW("tvsh", ItemBox.class),
	APPLE_ITEM_TV_EPISODE_ID("tven", ItemBox.class),
	APPLE_ITEM_TV_SEASON("tvsn", ItemBox.class),
	APPLE_ITEM_TV_EPISODE("tves", ItemBox.class),
	APPLE_ITEM_TV_NETWORK_NAME("tvnn", ItemBox.class),
	APPLE_ITEM_COPYRIGHT("cprt", ItemBox.class),
	APPLE_ITEM_SORT_NAME("sonm", ItemBox.class),
	APPLE_ITEM_SORT_ALBUM("soal", ItemBox.class),
	APPLE_ITEM_SORT_ALBUM_ARTIST("soaa", ItemBox.class),
	APPLE_ITEM_SORT_ARTIST("soar", ItemBox.class),
	APPLE_ITEM_SORT_SHOW("sosn", ItemBox.class),
	APPLE_ITEM_DATA("data", DataBox.class),
	APPLE_ITEM_ADDITIONAL_APPLICATION_NAME("mean", AdditionalInfoBox.class),
	APPLE_ITEM_ADDITIONAL_NAME("name", AdditionalInfoBox.class),
	SAMPLE_VIDEO_AVC1("avc1", VideoBox.class),
	SAMPLE_AUDIO_MP4A("mp4a", AudioBox.class),
	SAMPLE_AUDIO_AC3("ac-3", AudioBox.class),
	SAMPLE_TEXT("text", TextBox.class),
	FREE("free", FreeBox.class),
	SKIP("skip", SkipBox.class),
	WIDE("wide", WideBox.class);
	
	// Declare properties
	private final String name;
	private final Class<? extends Box> clazz;
	
	/**
	 * Constructor.
	 * 
	 * @param name - box name.
	 */
	BoxType(final String name, final Class<? extends Box> clazz) {
		this.name = name;
		this.clazz = clazz;
	}

	/**
	 * @return the box name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the class.
	 */
	public Class<? extends Box> getClazz() {
		return this.clazz;
	}
}
