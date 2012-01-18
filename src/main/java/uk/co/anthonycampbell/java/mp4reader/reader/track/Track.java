package uk.co.anthonycampbell.java.mp4reader.reader.track;

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

import java.util.Date;

/**
 * Interface to encapsulate MP4 tracks.
 * 
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public interface Track {

	/**
	 * Return the track ID.
	 * 
	 * @return - the track ID.
	 */
	public long getId();
	
	/**
	 * Return the track name.
	 * 
	 * @return - the track name.
	 */
	public String getName();
	
	/**
	 * Return the track creation date.
	 * 
	 * @return - the track creation date.
	 */
	public Date getCreationDate();
	
	/**
	 * Return the track modification date.
	 * 
	 * @return - the track modification date.
	 */
	public Date getModificationDate();
	
	/**
	 * Return the track duration.
	 * 
	 * @return - the track duration.
	 */
	public long getDuration();
}
