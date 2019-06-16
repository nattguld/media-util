package com.nattguld.media.video;

import com.nattguld.media.SequenceBuilder;

/**
 * 
 * @author randqm
 *
 */

public class ImageSequenceBuilder extends SequenceBuilder {
	
	/**
	 * The transition delay between images.
	 */
	private final int transitionDelay;
	
	
	/**
	 * Creates a new image sequence builder.
	 * 
	 * @param transitionDelay The transition delay between images.
	 */
	public ImageSequenceBuilder(int transitionDelay) {
		this.transitionDelay = transitionDelay;
	}
	
	@Override
	public String build() {
		StringBuilder sb = new StringBuilder();
		
		for (String entry : getEntries()) {
			sb.append("file '" + entry + "'");
			sb.append(System.lineSeparator() + "duration " + transitionDelay + System.lineSeparator());
		}
		sb.append("file '" + (getEntries().get(getEntries().size() - 1) + "'"));

		return sb.toString();
	}

}
