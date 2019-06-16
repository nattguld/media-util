package com.nattguld.media.video;

/**
 * 
 * @author randqm
 *
 */

public class VideoInfo {
	
	private String videoCodec = "h264";
	
	private String encoding = "yuv420p";
	
	private String resolution = "1280x720";
	
	private String videoBitRate = "1218";
	
	private String fps = "24";
	
	private String audioCodec = "aac";
	
	private String hz = "44100";
	
	private String audioType = "stereo";
	
	private String audioBitRate = "192";

	
	public String getVideoCodec() {
		return videoCodec;
	}

	public void setVideoCodec(String videoCodec) {
		this.videoCodec = videoCodec;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getVideoBitRate() {
		return videoBitRate;
	}

	public void setVideoBitRate(String videoBitRate) {
		this.videoBitRate = videoBitRate;
	}
	
	public int getFps() {
		return (int)Math.round(Double.parseDouble(fps));
	}
	
	public String getFpsAsString() {
		return fps;
	}

	public void setFps(String fps) {
		this.fps = fps;
	}

	public String getAudioCodec() {
		return audioCodec;
	}

	public void setAudioCodec(String audioCodec) {
		this.audioCodec = audioCodec;
	}

	public String getHz() {
		return hz;
	}

	public void setHz(String hz) {
		this.hz = hz;
	}

	public String getAudioType() {
		return audioType;
	}

	public void setAudioType(String audioType) {
		this.audioType = audioType;
	}

	public String getAudioBitRate() {
		return audioBitRate;
	}

	public void setAudioBitRate(String audioBitRate) {
		this.audioBitRate = audioBitRate;
	}
	
	public boolean isCompatible(VideoInfo vi) {
		return vi.getVideoCodec().equals(getVideoCodec())
				&& vi.getEncoding().equals(getEncoding())
				&& vi.getResolution().equals(getResolution())
				&& vi.getVideoBitRate().equals(getVideoBitRate())
				&& vi.getFpsAsString().equals(getFpsAsString())
				&& vi.getAudioCodec().equals(getAudioCodec())
				&& vi.getHz().equals(getHz())
				&& vi.getAudioType().equals(getAudioType())
				&& vi.getAudioBitRate().equals(getAudioBitRate());
	}
	
	public void print() {
		System.out.println("video codec: " + getVideoCodec());
		System.out.println("encoding: " + getEncoding());
		System.out.println("resolution: " + getResolution());
		System.out.println("video bitrate: " + getVideoBitRate());
		System.out.println("fps: " + getFps());
		System.out.println("audio codec: " + getAudioCodec());
		System.out.println("hz: " + getHz());
		System.out.println("audio type: " + getAudioType());
		System.out.println("audio bitrate: " + getAudioBitRate());
	}
	
	/**
	 * Retrieves the dimensions.
	 * 
	 * @return The dimensions.
	 */
	public int[] getDimensions() {
		String[] split = getResolution().split("x");
		int width = Integer.parseInt(split[0]);
		int height = Integer.parseInt(split[1]);
		
		return new int[] {width, height};
	}

	/**
	 * Retrieves the production type.
	 * 
	 * @param Whether to search the closest or not.
	 * 
	 * @return The production type.
	 */
	public ProductionType getProductionType(boolean closest) {
		ProductionType pt = ProductionType.getByResolution(getDimensions()[0], getDimensions()[1], closest);
		
		if (pt == null) {
			System.err.println("No production type found for [" + getDimensions()[0] + ":" + getDimensions()[1] + "]");
		}
		return pt;
	}
	
	/**
	 * Retrieves whether the video is in portrait or not.
	 * 
	 * @return The result.
	 */
	public boolean isPortrait() {
		return getDimensions()[1] > getDimensions()[0];
	}

}
