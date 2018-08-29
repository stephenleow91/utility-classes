/**
 *
 */
package com.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * @author USER
 *
 */
public class FileUtils implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	private FileUtils() {
		super();
	}

	/**
	 *
	 * @param sourceLocation
	 * @param targetLocation
	 * @throws IOException
	 */
	public static void copyDirectory(File sourceLocation, File targetLocation) throws IOException {

		if (sourceLocation.isDirectory()) {
			if (!targetLocation.exists()) {
				targetLocation.mkdir();
			}

			String[] children = sourceLocation.list();
			for (String element : children) {
				copyDirectory(new File(sourceLocation, element), new File(targetLocation, element));
			}
		} else {

			InputStream in = new FileInputStream(sourceLocation);
			OutputStream out = new FileOutputStream(targetLocation);

			// Copy the bits from instream to outstream
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		}
	}
}
