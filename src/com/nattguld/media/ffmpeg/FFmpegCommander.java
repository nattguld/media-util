package com.nattguld.media.ffmpeg;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.nattguld.media.cfg.MediaConfig;
import com.nattguld.util.Misc;
import com.nattguld.util.SystemUtil;

/**
 * 
 * @author randqm
 *
 */

public abstract class FFmpegCommander {
	
	/**
	 * How many of these tasks are currently running in parallel.
	 */
	private static AtomicInteger parallellCounter = new AtomicInteger();

	/**
	 * The command.
	 */
	private final String command;
	
	/**
	 * The FFMpeg CMD execution commands.
	 */
	private final List<String> commands;
	
	/**
	 * Whether the caller is excluded from counting towards the parallel task count or not.
	 */
	private final boolean excluded;
	
	
	/**
	 * Creates a new FFMpeg commander.
	 * 
	 * @param command The command.
	 * 
	 * @param excluded Whether the caller is excluded from counting towards the parallel task count or not.
	 */
	public FFmpegCommander(String command, boolean excluded) {
		this.command = command;
		this.commands = new ArrayList<>();
		this.excluded = excluded;
	}
	
	/**
	 * Prepares the task for execution.
	 * 
	 * @param Whether preparation was successful or not.
	 */
	protected boolean prepare() {
		if (command == null || command.isEmpty()) {
			System.err.println("No FFMpeg command provided");
			return false;
		}
		if (SystemUtil.isWindows()) {
			if (!MediaConfig.getConfig().isFFMpegFound()) {
				System.err.println("Failed to detect FFMpeg binary");
				return false;
			}
			commands.add("cmd");
			commands.add("/c");
		}
		commands.add(command);
		return true;
	}
	
	/**
	 * Handles an output line.
	 * 
	 * @param line The line.
	 * 
	 * @throws Exception
	 */
	protected abstract void handleLine(String line) throws Exception;
	
	/**
	 * Executes the task.
	 * 
	 * @return Whether the task was executed successfuly or not.
	 */
	public boolean execute() {
		if (!prepare()) {
			return false;
		}
		if (!excluded) {
			while (parallellCounter.get() >= MediaConfig.getConfig().getMaxEncodingTasks()) {
				Misc.sleep(1000);
			}
			parallellCounter.incrementAndGet();
		}
		try {
			ProcessBuilder pb = new ProcessBuilder(commands);
	    
			pb.directory(new File(SystemUtil.isWindows() ? MediaConfig.getConfig().getFFMpegPath() : "./"));
			pb.redirectErrorStream(true);
	    
			Process process = pb.start();
		
			try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
				String line = null;
	            
				while ((line = in.readLine()) != null) {            
					line = line.trim().toLowerCase();
				
					try {
						handleLine(line);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
			int exitCode = process.waitFor();
			//process.destroy();
			process.destroyForcibly();
			
			return exitCode == getExpectedExitCode();
			
		} catch (Exception ex) {
			ex.printStackTrace();
			parallellCounter.decrementAndGet();
			return false;
		}
	}
	
	/**
	 * Retrieves the expected exit code.
	 * 
	 * @return The expected exit code.
	 */
	protected int getExpectedExitCode() {
		return 0;
	}

}
