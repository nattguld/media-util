package com.nattguld.media.watermarking;

/**
 * 
 * @author randqm
 *
 */

public enum WatermarkType {
	
	TEXT("Text"),
	IMAGE("Image");
	
	
	/**
	 * The name.
	 */
	private final String name;
	
	
	/**
	 * Creates a new watermark type.
	 * 
	 * @param name The name.
	 */
	private WatermarkType(String name) {
		this.name = name;
	}
	
	/**
	 * Retrieves the name.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return getName();
	}

}
