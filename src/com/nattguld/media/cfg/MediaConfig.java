package com.nattguld.media.cfg;

import java.io.File;

import com.nattguld.data.cfg.Config;
import com.nattguld.data.cfg.ConfigManager;
import com.nattguld.data.json.JsonReader;
import com.nattguld.data.json.JsonWriter;

/**
 * 
 * @author randqm
 *
 */

public class MediaConfig extends Config {
	
	/**
	 * The maximum amount of encoding tasks allowed in parallel.
	 */
	private int maxEncodingTasks = 1;
	
	/**
	 * Whether to debug or not.
	 */
	private boolean debug;
	
	/**
	 * The path to the ffmpeg exe.
	 */
	private String ffMpegPath = "./ffmpeg.exe";
	

	@Override
	protected void read(JsonReader reader) {
		this.maxEncodingTasks = reader.getAsInt("max_encoding_tasks", 1);
		this.debug = reader.getAsBoolean("debug", false);
		this.ffMpegPath = reader.getAsString("ffmpeg_path", "./ffmpeg.exe");
	}

	@Override
	protected void write(JsonWriter writer) {
		writer.write("max_encoding_tasks", maxEncodingTasks);
		writer.write("debug", debug);
		writer.write("ffmpeg_path", ffMpegPath);
	}
	
	@Override
	protected String getSaveFileName() {
		return ".media_config";
	}
	
	/**
	 * Modifies the FFMpeg path.
	 * 
	 * @param ffMpegPath The new path.
	 * 
	 * @return The config.
	 */
	public MediaConfig setFFMpegPath(String ffMpegPath) {
		this.ffMpegPath = ffMpegPath;
		return this;
	}
	
	/**
	 * Retrieves the FFMpeg path.
	 * 
	 * @return The path.
	 */
	public String getFFMpegPath() {
		return ffMpegPath;
	}
	
	/**
	 * Retrieves whether the FFMpeg exe is found.
	 * 
	 * @return The result.
	 */
	public boolean isFFMpegFound() {
		return new File(getFFMpegPath()).exists();
	}
	
	/**
	 * Modifies the maximum amount of encoding tasks allowed in parallel.
	 * 
	 * @param maxEncodingTasks The new amount.
	 * 
	 * @return The config.
	 */
	public MediaConfig setMaxEncodingTasks(int maxEncodingTasks) {
		this.maxEncodingTasks = maxEncodingTasks;
		return this;
	}
	
	/**
	 * Retrieves the maximum amount of encoding tasks allowed in parallel.
	 * 
	 * @return The amount.
	 */
	public int getMaxEncodingTasks() {
		return maxEncodingTasks;
	}
	
	/**
	 * Modifies whether to debug or not.
	 * 
	 * @param debug The new state.
	 * 
	 * @return The config.
	 */
	public MediaConfig setDebug(boolean debug) {
		this.debug = debug;
		return this;
	}

	/**
	 * Retrieves whether to debug or not.
	 * 
	 * @return The result.
	 */
	public boolean isDebug() {
		return debug;
	}
	
	/**
	 * Retrieves the media config.
	 * 
	 * @return The media config.
	 */
	public static MediaConfig getConfig() {
		return (MediaConfig)ConfigManager.getConfig(new MediaConfig());
	}

}
