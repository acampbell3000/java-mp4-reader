package uk.co.anthonycampbell.java.mp4reader.box.type;

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
import java.util.List;

import uk.co.anthonycampbell.java.mp4reader.box.common.AbstractBox;
import uk.co.anthonycampbell.java.mp4reader.box.common.Box;
import uk.co.anthonycampbell.java.mp4reader.reader.BoxType;
import uk.co.anthonycampbell.java.mp4reader.reader.MP4Reader;
import uk.co.anthonycampbell.java.mp4reader.util.Util;

/**
 * Class to encapsulate the MP4 file type box (ftyp).
 * 
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public class FtypBox extends AbstractBox implements Box {
	
	// Declare box properties
	protected final String majorBrand;
	protected final int majorBrandVersion;
	protected final List<String> compatibleBrands;
	
	/**
	 * Constructor.
	 * 
	 * @param reader - MP4 file reader.
	 * @param remainingOffset - remaining bytes to be read from the stream.
	 * @param boxName - name of the box type.
	 * @param boxType - box type ENUM.
	 * @throws IOException Unable read remaining bytes from the stream.
	 */
	public FtypBox(final MP4Reader reader, final long remainingOffset, final String boxName,
			final BoxType boxType) throws IOException {
		super(reader, remainingOffset, boxName, boxType);
		
		// Validate
		this.majorBrand = reader.readString();
		this.majorBrandVersion = reader.readInt();
		this.compatibleBrands = reader.readStringOffset(remainingOffset - 8);
		
		// Clean up
		skip();
	}

	/**
	 * @return the major brand.
	 */
	public String getMajorBrand() {
		return this.majorBrand;
	}

	/**
	 * @return the major brand version.
	 */
	public int getMajorBrandVersion() {
		return this.majorBrandVersion;
	}

	/**
	 * @return the list of compatible brands.
	 */
	public List<String> getCompatibleBrands() {
		return this.compatibleBrands;
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append(Util.printFields(this));
		
		return builder.toString();
	}
}
