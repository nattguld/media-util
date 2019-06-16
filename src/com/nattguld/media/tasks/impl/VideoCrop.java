package com.nattguld.media.tasks.impl;

import java.io.File;

import com.nattguld.media.ffmpeg.FFmpegEncode;
import com.nattguld.media.tasks.FileContainerFlow;

/**
 * 
 * @author randqm
 *
 */

public class VideoCrop extends FileContainerFlow {
	
	/**
	 * The output path.
	 */
	private final String outputPath;
	
	/**
	 * The x coordinate.
	 */
	private final int x;
	
	/**
	 * The y coordinate.
	 */
	private final int y;
	
	/**
	 * The width.
	 */
	private final int width;
	
	/**
	 * The height.
	 */
	private final int height;
	

	/**
	 * Creates a new video transformer task.
	 * 
	 * @param inputFolder The input folder.
	 * 
	 * @param outputPath The output path.
	 * 
	 * @param x The x coordinate.
	 * 
	 * @param y The y coordinate.
	 * 
	 * @param width The width.
	 * 
	 * @param height The height.
	 * 
	 * @param props The flow properties.
	 */
	public VideoCrop(String inputFolder, String outputPath, int x, int y, int width, int height) {
		super(inputFolder);
		
		this.outputPath = outputPath;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		File outFile = new File(outputPath);
		
		if (!outFile.exists()) {
			outFile.mkdirs();
		}
	}
	
	@Override
	protected boolean handleFile(File f) throws Exception {
		File out = new File(outputPath + "/" + f.getName());
		
		String cmd = "ffmpeg -i \"" + f.getAbsolutePath() + "\" -filter:v \"crop=" + width + ":" + height + ":" + x + ":" + y + "\" -c:a copy \"" + out.getAbsolutePath() + "\""; 

		if (!new FFmpegEncode(cmd).execute()) {
			System.err.println("Failed to crop " + f.getName());
			return false;
		}
		return true;
	}

}
