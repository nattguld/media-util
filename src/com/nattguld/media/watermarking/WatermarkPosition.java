package com.nattguld.media.watermarking;

/**
 * 
 * @author randqm
 *
 */

public enum WatermarkPosition {
    
    BOTTOM("overlay=(main_w-overlay_w)/2:(main_h-overlay_h)"
	    , "x=(w-text_w)/2:y=(h-text_h)"),
    TOP("overlay=(main_w-overlay_w)/2:0"
	    , "x=(w-text_w)/2:y=0"),
    MIDDLE("overlay=(main_w-overlay_w)/2:(main_h-overlay_h)/2"
	    , "x=(w-text_w)/2:y=(h-text_h)/2"),
    TOP_LEFT("overlay=0:0"
	    , "x=0:y=0"),
    BOTTOM_LEFT("overlay=0:(main_h-overlay_h)"
	    , "x=0:y=(h-text_h)"),
    TOP_RIGHT("overlay=(main_w-overlay_w):0"
	    , "x=(w-text_w):y=0"),
    BOTTOM_RIGHT("overlay=(main_w-overlay_w):(main_h-overlay_h)"
	    , "x=(w-text_w):y=(h-text_h)");
    
    
    /**
     * The watermarking filter.
     */
    private final String filter;
    
    /**
     * The positioning for text layers.
     */
    private final String textPos;
    
    
    /**
     * Creates a new watermarking location.
     * 
     * @param filter The watermarking filter.
     * 
     * @param textPos The positioning for text layers.
     */
    private WatermarkPosition(String filter, String textPos) {
	this.filter = filter;
	this.textPos = textPos;
    }
    
    
    /**
     * Retrieves the watermarking filter.
     * 
     * @return The filter.
     */
    public String getFilter() {
	return filter;
    }
    
    /**
     * Retrieves the positioning for text layers.
     * 
     * @return The positioning for text layers.
     */
    public String getTextPos() {
	return textPos;
    }
    
    @Override
    public String toString() {
	return name().replaceAll("_", " ");
    }

}
