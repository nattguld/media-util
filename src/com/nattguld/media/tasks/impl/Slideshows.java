package com.nattguld.media.tasks.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.nattguld.data.workspace.Workspace;
import com.nattguld.media.ffmpeg.FFmpegEncode;
import com.nattguld.media.video.ImageSequenceBuilder;
import com.nattguld.util.files.FileOperations;
import com.nattguld.util.maths.Maths;
import com.nattguld.util.media.ProductionType;
import com.nattguld.util.media.edit.impl.ImageResize;
import com.nattguld.util.text.TextUtil;

/**
 * 
 * @author randqm
 *
 */

public class Slideshows {
	
	/**
	 * The output path.
	 */
	private final String outputPath;
	
	/**
	 * The production type.
	 */
	private final ProductionType production;
	
	/**
	 * The slideshow name.
	 */
	private final String slideshowName;
	
	/**
	 * The media directory.
	 */
	private final File media;
	
	/**
	 * The priority media.
	 */
	private final File priorityMedia;
	
	/**
	 * The audio.
	 */
	private final File audio;
	
	/**
	 * The transition delay between slides.
	 */
	private final int transitionDelay;
	
	/**
	 * The frames per second.
	 */
	private final int fps;
	
	/**
	 * The amount of slides.
	 */
	private int amount;
	
	
	/**
	 * Creates a new slideshows task.
	 * 
	 * @param String outputPath The output path.
	 * 
	 * @param production The production type.
	 * 
	 * @param slideshowName The slideshow name.
	 * 
	 * @param media The media directory.
	 * 
	 * @param priorityMedia The priority media.
	 * 
	 * @param audio The audio.
	 * 
	 * @param transitionDelay The transition delay between slides.
	 * 
	 * @param amount The amount of slides.
	 * 
	 * @param fps The frames per second.
	 * 
	 * @param props The flow properties.
	 */
	public Slideshows(String outputPath, ProductionType production, String slideshowName, File media
			, File priorityMedia, File audio, int transitionDelay, int amount, int fps) {
		this.outputPath = outputPath;
		this.production = production;
		this.slideshowName = slideshowName;
		this.media = media;
		this.priorityMedia = priorityMedia;
		this.audio = audio;
		this.transitionDelay = transitionDelay;
		this.amount = amount;
		this.fps = fps;
	}

	/**
	 * Creates the slideshow.
	 * 
	 * @return Whether the slideshow was created successfuly or not.
	 */
	public boolean create() {
		final File slideshowOutputFile = new File(outputPath + File.separator + slideshowName + ".mp4");
				
		File[] files = media.listFiles();

		if (Objects.isNull(files) || files.length == 0 || (amount != 0 && files.length < amount)) {
			System.err.println("Not enough files in the specified directory");
			return false;
		}
		ImageSequenceBuilder isb = new ImageSequenceBuilder(transitionDelay);
				
		if (Objects.nonNull(priorityMedia)) {
			File[] priorFiles = priorityMedia.listFiles();
					
			if (Objects.isNull(priorFiles) || priorFiles.length == 0) {
				System.err.println("No priority files found in " + priorityMedia.getAbsolutePath());
				return false;
			}
			File priorityFile = priorFiles[Maths.random(priorFiles.length)];
			isb.addEntry(priorityFile.getAbsolutePath());
		}
		List<File> mediaList = new ArrayList<>();
				
		for (File f : files) {
			mediaList.add(f);
		}
		for (int i = 0; i < amount; i ++) {
			File f = mediaList.get(Maths.random(mediaList.size()));
			mediaList.remove(f);
			isb.addEntry(f.getAbsolutePath());
		}
		try (Workspace copyDir = new Workspace();
				Workspace resizeDir = new Workspace();
				Workspace transformDir = new Workspace();
				Workspace exportDir = new Workspace()) {
					
			for (String entry : isb.getEntries()) {
				FileOperations.copy(entry, copyDir.getWorkPath() + "/" + new File(entry).getName());
			}
			for (File f : new File(copyDir.getWorkPath()).listFiles()) {
				new ImageResize(f, new File(resizeDir.getWorkPath() + File.separator + f.getName())
						, production.getWidth(), production.getHeight(), false).performOperation();
			}
			ImageSequenceBuilder modifiedIsb = new ImageSequenceBuilder(transitionDelay);
					
			for (String entry : isb.getEntries()) {
				modifiedIsb.addEntry(transformDir.getWorkPath() + "/" + new File(entry).getName());
			}	
			String slideshowInputPath = transformDir.getWorkPath() + "/slideshow_input.txt";
			File output = new File(exportDir.getWorkPath() + "/" + TextUtil.generateRandomNumber() + ".mp4"); //TODO slideshowOutputFile
					
			FileOperations.write(slideshowInputPath, modifiedIsb.build(), false);
					
			//String cmd = "ffmpeg -f concat -safe 0 -i \"" + slideshowInputPath + "\" -c:v libx264 -pix_fmt yuv420p \"" + output.getAbsolutePath() + "\"";
			String cmd = "ffmpeg -s " + production.getWidth() + ":" + production.getHeight() + " -f concat -safe 0 -i \"" + slideshowInputPath + "\" -c:v libx264 -r " + fps + " \"" + output.getAbsolutePath() + "\"";
			
			if (!new FFmpegEncode(cmd).execute()) {
				System.err.println("Failed to create slideshow");
				return false;
			}
			if (audio != null) { //TODO use audio task
				File output2 = new File(exportDir.getWorkPath() + "/" + TextUtil.generateRandomNumber() + ".mp4");
				String cmd2 = "ffmpeg -i \"" + output.getAbsolutePath() + "\" -i \"" + audio.getAbsolutePath() + "\" -codec copy -shortest \"" + output2.getAbsolutePath() + "\"";
						
				if (!new FFmpegEncode(cmd2).execute()) {
					System.err.println("Failed to add audio to slideshow");
					return false;
				}
				output.delete();
				output = output2;
			}
			FileOperations.copy(output.getAbsolutePath(), slideshowOutputFile.getAbsolutePath());
			System.out.println("Created slideshow successfully! (" + output.getName() + ")");
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
