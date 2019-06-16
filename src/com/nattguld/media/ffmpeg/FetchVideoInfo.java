package com.nattguld.media.ffmpeg;

import java.io.File;
import java.util.Objects;

import com.nattguld.media.video.VideoInfo;

/**
 * 
 * @author randqm
 *
 */

public class FetchVideoInfo extends FFmpegCommander {
	
	/**
	 * The video.
	 */
	private final File video;
	
	/**
	 * The video info.
	 */
	private final VideoInfo vi;
	
	
	/**
	 * Creates a new fetch video info task.
	 * 
	 * @param videoPath The video path.
	 */
	public FetchVideoInfo(String videoPath) {
		this(new File(videoPath));
	}
	
	/**
	 * Creates a new fetch video info task.
	 * 
	 * @param videoPath The video file.
	 */
	public FetchVideoInfo(File video) {
		super("ffmpeg -i \"" + video.getAbsolutePath() + "\"", true);
		
		this.video = video;
		this.vi = new VideoInfo();
	}
	
	/**
	 * Fetches the video info.
	 * 
	 * @return The video info.
	 */
	public VideoInfo fetch() {
		if (Objects.isNull(video) || !video.exists()) {
			System.err.println(video.getAbsolutePath() + " not found");
			return vi;
		}
		execute();
		return vi;
	}

	@Override
	protected void handleLine(String line) throws Exception {
		if (line.startsWith("stream #0:")) {
			String format = line.substring(line.indexOf(":") + 1, line.length());
			format = format.substring(format.indexOf(":") + 1, format.length());
        		
			String[] args = format.split(",");
        		
			if (args[0].contains("video")) {
				String videoCodec = args[0].split(":")[1];
				String formatCodec = videoCodec.substring(0, videoCodec.indexOf("(")).trim();
				vi.setVideoCodec(formatCodec);
        			
				vi.setEncoding(args[1].trim());
				
				String resolution = args[2].trim();
        			
				if (resolution.contains("[")) {
					resolution = resolution.substring(0, resolution.indexOf("[")).trim();
				}
				vi.setResolution(resolution);
				
				vi.setVideoBitRate(args[3].split("kb")[0].trim());
				vi.setFps(args[4].split("fps")[0].trim());
				//Optional => , 24 tbr, 12288 tbn, 48 tbc (default)
        			
			} else if (args[0].contains("audio")) {
				String audioCodec = args[0].split(":")[1];
				String formatCodec = audioCodec.substring(0, audioCodec.indexOf("(")).trim();
				vi.setAudioCodec(formatCodec);
        			
				vi.setHz(args[1].split("hz")[0].trim());
				vi.setAudioType(args[2].trim());
				vi.setAudioBitRate(args[4].split("kb")[0].trim());
        			
			} else {
				System.err.println("Undefined main arg: " + args[0]);
			}
		}
	}
	
	@Override
	protected int getExpectedExitCode() {
		return 1;
	}

}
