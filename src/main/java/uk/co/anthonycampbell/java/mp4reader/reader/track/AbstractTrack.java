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

import uk.co.anthonycampbell.java.mp4reader.util.Util;

/**
 * Class to encapsulate common track properties.
 * 
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public abstract class AbstractTrack implements Track {

	// Declare properties
	protected final long id;
	protected final String name;
	protected final Date creationDate;
	protected final Date modifiedDate;
	protected final long duration;
	
	/**
	 * Constructor.
	 * 
	 * @param id - the ID.
	 * @param name - the name.
	 * @param creationDate - the creation date.
	 * @param modifiedDate - the modified date.
	 * @param duration - the duration.
	 */
	public AbstractTrack(final long id, final String name, final Date creationDate,
			final Date modifiedDate, final long duration) {
		super();
		this.id = id;
		this.name = name;
		this.creationDate = creationDate;
		this.modifiedDate = modifiedDate;
		this.duration = duration;
	}

	@Override
	public long getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Date getCreationDate() {
		return this.creationDate;
	}

	@Override
	public Date getModificationDate() {
		return this.modifiedDate;
	}

	@Override
	public long getDuration() {
		return this.duration;
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append(Util.printFields(this));
		
		return builder.toString();
	}
}
