package com.nattguld.media.tasks;

import java.io.File;

import com.nattguld.util.files.FileContainer;

/**
 * 
 * @author randqm
 *
 */

public abstract class FileContainerFlow {
	
	/**
	 * The file container.
	 */
	private final FileContainer container;
	 
	
	/**
	 * Creates a new file container flow.
	 * 
	 * @param inputPath The input path.
	 */
	public FileContainerFlow(String inputPath) {
		this.container = new FileContainer(inputPath);
	}
	
	/**
	 * Handles a file.
	 * 
	 * @param f The file to handle.
	 * 
	 * @return Whether the file was handled successfuly or not.
	 * 
	 * @throws Exception
	 */
	protected abstract boolean handleFile(File f) throws Exception;
	
	/**
	 * Processes the files.
	 */
	public void processFiles() {
		if (container.isContainerEmpty()) {
			System.err.println("No files found to populate file container flow with");
			return;
		}
		while (!container.isDequeEmpty()) {
			File f = container.next();
			
			try {
				if (!handleFile(f)) {
					System.err.println("Failed to handle " + f.getAbsolutePath());
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}
