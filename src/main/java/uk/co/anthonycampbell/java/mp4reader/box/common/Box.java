package uk.co.anthonycampbell.java.mp4reader.box.common;

import uk.co.anthonycampbell.java.mp4reader.reader.BoxType;


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

/**
 * Interface for the MP4 information boxes.
 *
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public interface Box {

	/**
	 * Start position in the reader stream.
	 * 
	 * @return - stream start position.
	 */
	public long getStartPosition();

	/**
	 * Total box size in bytes.
	 * 
	 * @return - box size in bytes.
	 */
	public long getTotalSize();

	/**
	 * Box type name.
	 * 
	 * @return - box type name.
	 */
	public String getBoxName();

	/**
	 * Box type.
	 * 
	 * @return - box type.
	 */
	public BoxType getBoxType();
}
