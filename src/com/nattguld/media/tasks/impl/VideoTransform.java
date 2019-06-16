package com.nattguld.media.tasks.impl;

import java.io.File;

import com.nattguld.media.ffmpeg.FFmpegEncode;
import com.nattguld.media.tasks.FileContainerFlow;
import com.nattguld.util.files.MimeType;

/**
 * 
 * @author randqm
 *
 */

public class VideoTransform extends FileContainerFlow {
	
	/**
	 * The output path.
	 */
	private final String outputPath;
	
	/**
	 * The requested width.
	 */
	private final int width;
	
	/**
	 * The requested height.
	 */
	private final int height;
	
	/**
	 * Whether it should be portrait view or not.
	 */
	private final boolean portrait;
	
	/**
	 * The frames per second.
	 */
	private final int fps;
	
	/**
	 * The audio bit rate.
	 */
	private final int audioBitRate;
	

	/**
	 * Creates a new video transformer task.
	 * 
	 * @param inputFolder The input folder.
	 * 
	 * @param outputPath The output path.
	 * 
	 * @param width The width.
	 * 
	 * @param height The height.
	 * 
	 * @param fps The frames per second.
	 * 
	 * @param audioBitRate The audio bit rate.
	 * 
	 * @param portrait Whether it should be portrait view or not.
	 * 
	 * @param props The flow properties.
	 */
	public VideoTransform(String inputFolder, String outputPath, int width, int height, int fps, int audioBitRate, boolean portrait) {
		super(inputFolder);
		
		this.outputPath = outputPath;
		this.width = width;
		this.height = height;
		this.fps = fps;
		this.audioBitRate = audioBitRate;
		this.portrait = portrait;
		
		File outFile = new File(outputPath);
		
		if (!outFile.exists()) {
			outFile.mkdirs();
		}
	}
	
	@Override
	protected boolean handleFile(File f) throws Exception { //192k audio bitrate, 1MB video bitrate
		File out = new File(outputPath + "/" + f.getName());
		
		MimeType mt = MimeType.getByFile(f);
		
		if (!mt.isVideo()) {
			System.err.println(f.getName() + " is not a video");
			return true;
		}
		String cmd = "ffmpeg -i \"" + f.getAbsolutePath() + "\" -vf scale=" 
				+ (portrait ? height : width) + ":" 
				+ (portrait ? width : height) + " -b:v 1M -b:a " + audioBitRate + "k -r " + fps + " \"" + out.getAbsolutePath() + "\"";

		if (!new FFmpegEncode(cmd).execute()) {
			System.err.println("Failed to transform " + f.getName());
			return false;
		}
		return true;
	}

}
