package com.nattguld.media.ffmpeg;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.nattguld.media.cfg.MediaConfig;

/**
 * 
 * @author randqm
 *
 */

public class FFmpegEncode extends FFmpegCommander {
	
	/**
	 * The progress percentage.
	 */
	private String percentage;
	
	/**
	 * The video length.
	 */
	private long videoLength;
	
	
	/**
	 * Creates a new FFMpeg task.
	 * 
	 * @param command The command.
	 */
	public FFmpegEncode(String command) {
		this(command, false);
	}
	
	/**
	 * Creates a new FFMpeg task.
	 * 
	 * @param command The command.
	 * 
	 * @param excluded Whether the caller is excluded from counting towards the parallel task count or not.
	 */
	public FFmpegEncode(String command, boolean excluded) {
		super(command, excluded);
		
		this.percentage = "";
		this.videoLength = -1;
	}
	
	@Override
	protected void handleLine(String line) throws Exception {
		if (line.startsWith("duration") && videoLength == -1) {
    		String duration = line.trim().substring(10, 21);
	    
    		if (duration.contains(":")) {
    			if (MediaConfig.getConfig().isDebug()) {
    				System.out.println("Duration: " + duration);
    			}
    			videoLength = asMillis(duration);
    		}
    		return;
    	}
    	if (line.contains("time") && line.contains("bitrate")) {
    		String[] split1 = line.split("time=");
    		String[] split2 = split1[1].split("bitrate=");
	    
    		String elapsed = split2[0].trim();
	    
    		String[] split3 = split2[1].split("kbits");
	    
    		String bitrate = split3[0].trim();
    		
    		percentage = videoLength == -1 ? "" : ("Progress: " + ((int)((asMillis(elapsed) * 100) / videoLength) + "%"));
    		
    		if (MediaConfig.getConfig().isDebug()) {
    			System.out.println(percentage + "% (Elapsed: " + elapsed + ") (" + bitrate + " kbit/s)");
    		}
    	}
    	//System.err.println(line);
	}
    
    /**
     * Retrieves a time string in milliseconds.
     * 
     * @param timeString The time string.
     * 
     * @return The milliseconds.
     * 
     * @throws ParseException 
     */
    private long asMillis(String timeString) throws ParseException {
    	try {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    		Date date = sdf.parse("1970-01-01 " + timeString);
    		return date.getTime();
    	} catch (ParseException ex) {
    		return System.currentTimeMillis();
    	}
    }
    
    /**
     * Retrieves the video length.
     * 
     * @return The video length.
     */
    public long getVideoLength() {
    	return videoLength;
    }

}
