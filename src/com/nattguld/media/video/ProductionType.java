package com.nattguld.media.video;

import java.awt.Dimension;

/**
 * 
 * @author randqm
 *
 */

public enum ProductionType {

	NTSC("240p 4:3", 320, 240),
	NTSC_WIDE("240p 16:9", 426, 240),
	WIDE("360p 16:9", 640, 360),
	VGA("VGA", 640, 480),
	SVGA("SVGA", 800, 600),
	XGA("XGA", 1024, 768),
	HD("HD 720p", 1280, 720),
	HD_PLUS("HD+ 900p", 1600, 900),
	FHD("FHD 1080p", 1920, 1080),
	QHD("QHD 1440p", 2560, 1440),
	UHD("UHD 2160p", 840, 2160);
	
	
	/**
	 * The name.
	 */
	private final String name;
	
	/**
	 * The width.
	 */
	private final int width;
	
	/**
	 * The height.
	 */
	private final int height;
	
	
	/**
	 * Creates a new production type.
	 * 
	 * @param name The name.
	 * 
	 * @param width The width.
	 * 
	 * @param height The height.
	 */
	private ProductionType(String name, int width, int height) {
		this.name = name;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Retrieves whether the production type is HD or not.
	 * 
	 * @return The result.
	 */
	public boolean isHD() {
		return this == HD || this == HD_PLUS
				|| this == FHD || this == QHD
				|| this == UHD;
	}
	
	/**
	 * Retrieves the name.
	 * 
	 * @return The name.
	 */
	private String getName() {
		return name;
	}
	
	/**
	 * Retrieves the width.
	 * 
	 * @return The width.
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Retrieves the height.
	 * 
	 * @return The height.
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Retrieves the dimension.
	 * 
	 * @return The dimension.
	 */
	public Dimension getDimension() {
		return new Dimension(getWidth(), getHeight());
	}
	
	@Override
	public String toString() {
		return getName() + " (" + getWidth() + ":" + getHeight() + "px)";
	}
	
	/**
	 * Retrieves the production type for a given resolution.
	 * 
	 * @param width The width.
	 * 
	 * @param height The height.
	 * 
	 * @return The production type.
	 */
	public static ProductionType getByResolution(int width, int height, boolean closest) {
		ProductionType pt = null;
		
		for (ProductionType o : values()) {
			if (o.getWidth() == width && o.getHeight() == height) {
				return o;
			}
			if (!closest) {
				continue;
			}
			if (pt == null) {
				pt = o;
				continue;
			}
			int currWidthDiff = Math.abs(pt.getWidth() - width);
			int currHeightDiff = Math.abs(pt.getHeight() - height);
			
			int newWidthDiff = Math.abs(pt.getWidth() - width);
			int newHeightDiff = Math.abs(pt.getHeight() - height);
			
			if ((currWidthDiff + currHeightDiff) >= (newWidthDiff + newHeightDiff)) {
				pt = o;
			}
		}
		System.out.println("Dimensions[" + width + ":" + height + "] Closest[" + pt.getWidth() + ":" + pt.getHeight() + " (" + pt.getName() + ")]");
		return pt;
	}

}
