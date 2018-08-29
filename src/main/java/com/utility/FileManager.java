/**
 *
 */
package com.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author USER
 *
 */
public class FileManager implements Serializable {

	private static Logger logger = LoggerFactory.getLogger(FileManager.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String filename;
	private String filePath;

	/**
	 *
	 * @param directory
	 * @param name
	 */
	public FileManager(String directory, String name) {

		if ((name != null) && (name.trim().length() > 0)) {

			if ((directory != null) && (directory.trim().length() > 0)) {
				filePath = directory + name;

			} else {
				filePath = name;
			}
		} else {
			throw new NullPointerException();
		}
	}

	/**
	 *
	 * @return
	 */
	public String readFile() {
		String content = "";

		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			int i;

			do {
				i = br.read();
				if (i != -1) {
					content = content + (char) i;
				}
			} while (i != -1);

			br.close();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return content;
	}

	/**
	 *
	 * @return
	 */
	public List<String> readFileByList() {
		List<String> content = new ArrayList<String>();

		try {
			BufferedReader in = new BufferedReader(new FileReader(filePath));
			String s;

			do {
				s = in.readLine();
				if ((s != null) && !s.equals("")) {
					content.add(s);
				}
			} while ((s != null) && !s.equals(""));

			in.close();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return content;
	}

	/**
	 *
	 * @param fileContent
	 */
	public void uploadFile(byte[] fileContent) {
		try {
			File file = new File(filePath);
			FileOutputStream output = new FileOutputStream(file);
			output.write(fileContent);
			output.close();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 *
	 * @param newFileName
	 * @return
	 */
	public boolean renameFile(String newFileName) {
		if (checkFileExists()) {
			File file = new File(filePath);
			return file.renameTo(new File(newFileName));
		}

		return false;
	}

	/**
	 *
	 * @return
	 */
	public boolean checkFileExists() {
		if (filePath != null) {
			File file = new File(filePath);

			if (file.exists() && file.isFile()) {
				return true;
			}
		}

		return false;
	}

	/**
	 *
	 * @return
	 */
	public long checkFileSize() {
		File file = new File(filePath);
		return file.length();
	}

	/**
	 *
	 * @return
	 */
	public String getFilename() {
		File file = new File(filePath);
		return file.getName();
	}

	/**
	 *
	 * @return
	 */
	public boolean deleteFile() {
		File file = new File(filePath);
		return file.delete();
	}

}
