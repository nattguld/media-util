package com.nattguld.media.video;

import com.nattguld.media.SequenceBuilder;

/**
 * 
 * @author randqm
 *
 */

public class VideoSequenceBuilder extends SequenceBuilder {

	
	@Override
	public String build() {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < getEntries().size(); i ++) {
			sb.append("file '" + getEntries().get(i) +  "'");
			
			if (i != (getEntries().size() - 1)) {
				sb.append(System.lineSeparator());
			}
		}
		return sb.toString();
	}

}
