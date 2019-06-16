package com.nattguld.media.tasks.impl;

import java.io.File;
import java.util.Objects;

import com.nattguld.data.workspace.Workspace;
import com.nattguld.media.MediaHandler;
import com.nattguld.media.ffmpeg.FFmpegEncode;
import com.nattguld.media.tasks.FileContainerFlow;
import com.nattguld.media.video.VideoInfo;
import com.nattguld.media.video.VideoSequenceBuilder;
import com.nattguld.util.files.FileOperations;

/**
 * 
 * @author randqm
 *
 */

public class IntroOutroAdder extends FileContainerFlow {
	
	/**
	 * The output path.
	 */
	private final String outputPath;
	
	/**
	 * The intro video.
	 */
	private File intro;
	
	/**
	 * The outro video.
	 */
	private File outro;
	

	/**
	 * Creates a new merger.
	 * 
	 * @param outputPath The output path.
	 * 
	 * @param files The files to merge.
	 * 
	 * @param production The production type.
	 * 
	 * @param fps The fps.
	 * 
	 * @param props The flow properties.
	 */
	public IntroOutroAdder(String outputPath, String inputPath, String introPath, String outroPath) {
		super(inputPath);
		
		this.outputPath = outputPath;
		this.intro = introPath == null ? null : new File(introPath);
		this.outro = outroPath == null ? null : new File(outroPath);
	}

	@Override
	protected boolean handleFile(File f) throws Exception {
		if (intro != null && !intro.exists()) {
			System.err.println("Intro file not found");
			return false;
		}
		if (outro != null && !outro.exists()) {
			System.err.println("Outro file not found");
			return false;
		}
		File outputFile = new File(outputPath + File.separator + f.getName());
		
		VideoInfo viMain = MediaHandler.fetchVideoInfo(f);
		VideoInfo viIntro = Objects.isNull(intro) ? null : MediaHandler.fetchVideoInfo(intro);
		VideoInfo viOutro = Objects.isNull(outro) ? null : MediaHandler.fetchVideoInfo(outro);
		//ProductionType productionType = viMain.getProductionType(false);
		
		try (Workspace workspace = new Workspace(); 
				Workspace export = new Workspace();) {
			
			f = export.addToWorkspace(f);
			boolean encodingRequired = false;
			
			if (intro != null) {
				if (!viIntro.isCompatible(viMain)) {
					workspace.addToWorkspace(intro);
					encodingRequired = true;
				} else {
					export.addToWorkspace(intro);
				}
				intro = new File(export.getWorkPath() + File.separator + intro.getName());
			}
			if (outro != null) {
				if (!viOutro.isCompatible(viMain)) {
					workspace.addToWorkspace(outro);
					encodingRequired = true;
				} else {
					export.addToWorkspace(outro);
				}
				outro = new File(export.getWorkPath() + File.separator + intro.getName());
			}
			if (encodingRequired) {
				MediaHandler.transformVideos(workspace.getWorkPath(), export.getWorkPath()
						, viMain.getDimensions()[0], viMain.getDimensions()[1], 24, 192, false);
			}
			VideoSequenceBuilder vsb = new VideoSequenceBuilder();
			
			if (intro != null) {
				vsb.addEntry(intro.getName());
			}
			vsb.addEntry(f.getName());
			
			if (outro != null) {
				vsb.addEntry(outro.getName());
			}
			String concatFilePath = export.getWorkPath() + File.separator + "concat_input.txt";
			
			FileOperations.write(concatFilePath, vsb.build(), false);
			
			String cmd = "ffmpeg -f concat -safe 0 -i \"" + concatFilePath + "\" -c copy \"" + outputFile.getAbsolutePath() + "\"";
			
			if (!new FFmpegEncode(cmd, true).execute()) {
				System.err.println("Failed to add intro/outro to " + f.getName());
				return false;
			}
			return true;
		}
	}

}
