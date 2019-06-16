package com.nattguld.media;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author randqm
 *
 */

public abstract class SequenceBuilder {
	
	/**
	 * The sequence entries.
	 */
	private final List<String> entries = new ArrayList<>();
	
	
	/**
	 * Adds a new entry.
	 * 
	 * @param entry The new entry.
	 */
	public void addEntry(String entry) {
		entries.add(entry);
	}
	
	/**
	 * Builds the sequence string.
	 * 
	 * @return The sequence string.
	 */
	public abstract String build();
	
	/**
	 * Retrieves the sequence entries.
	 * 
	 * @return The entries.
	 */
	public List<String> getEntries() {
		return entries;
	}

}
