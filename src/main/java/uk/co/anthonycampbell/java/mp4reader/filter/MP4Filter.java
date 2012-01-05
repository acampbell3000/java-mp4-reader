package uk.co.anthonycampbell.java.mp4reader.filter;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.lang3.StringUtils;

/**
 * IO file filter to match valid MP4 files.
 * 
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public class MP4Filter implements IOFileFilter {
	
	// Supported extensions
	final private static String[] extensions = { "m4v", "mp4", "m4a" };
	
	@Override
	public boolean accept(final File file) {
		if (file != null && supportedExtension(FilenameUtils.getExtension(file.getAbsolutePath()))) {
			return true;
		}
		
		return false;
	}

	@Override
	public boolean accept(final File directory, final String fileName) {
		if (supportedExtension(fileName)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Utility method to check whether the provided extension is supported.
	 * 
	 * @param extension - the extension to validate.
	 * @return whether the extension is supported.
	 */
	private static boolean supportedExtension(final String extension) {
		if (StringUtils.isNotEmpty(extension)) {
			for (final String supportedExtension : extensions) {
				if (extension.equalsIgnoreCase(supportedExtension)) {
					return true;
				}
			}
		}
		
		return false;
	}
}