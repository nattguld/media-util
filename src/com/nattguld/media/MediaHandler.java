package com.nattguld.media;

import java.awt.Color;
import java.io.File;

import com.nattguld.data.workspace.Workspace;
import com.nattguld.media.ffmpeg.FFmpegEncode;
import com.nattguld.media.ffmpeg.FetchVideoInfo;
import com.nattguld.media.tasks.impl.Slideshows;
import com.nattguld.media.tasks.impl.VideoCrop;
import com.nattguld.media.tasks.impl.VideoTransform;
import com.nattguld.media.video.VideoInfo;
import com.nattguld.media.watermarking.WatermarkColor;
import com.nattguld.media.watermarking.WatermarkPosition;
import com.nattguld.util.media.ProductionType;
import com.nattguld.util.media.edit.impl.ImageResize;

/**
 * 
 * @author randqm
 *
 */

public class MediaHandler {
	
	
	/**
	 * Fetches the info of a video.
	 * 
	 * @param videoFilePath The video file path.
	 * 
	 * @return The video info.
	 */
	public static VideoInfo fetchVideoInfo(String videoFilePath) {
		return new FetchVideoInfo(videoFilePath).fetch();
	}
	
	/**
	 * Fetches the info of a video.
	 * 
	 * @param videoFile The video file.
	 * 
	 * @return The video info.
	 */
	public static VideoInfo fetchVideoInfo(File videoFile) {
		return new FetchVideoInfo(videoFile).fetch();
	}
	
	/**
	 * Transforms a directory with videos.
	 * 
	 * @param inputDirPath The input directory path.
	 * 
	 * @param outputDirPath The output directory path.
	 * 
	 * @param width The video width.
	 * 
	 * @param height The video height.
	 * 
	 * @param fps The fps.
	 * 
	 * @param audioBitrate The audio bit rate.
	 * 
	 * @param portrait Whether to encode in portrait or not.
	 */
	public static void transformVideos(String inputDirPath, String outputDirPath, int width, int height, int fps, int audioBitrate, boolean portrait) {
		new VideoTransform(inputDirPath, outputDirPath, width, height, fps, audioBitrate, portrait).processFiles();
	}
	
	/**
	 * Crops a directory with videos.
	 * 
	 * @param inputDirPath The input directory path.
	 * 
	 * @param outputDirPath The output directory path.
	 * 
	 * @param x The x coordinate.
	 * 
	 * @param y The y coordinate.
	 * 
	 * @param width The video width.
	 * 
	 * @param height The video height.
	 */
	public static void cropVideos(String inputDirPath, String outputDirPath, int x, int y, int width, int height) {
		new VideoCrop(inputDirPath, outputDirPath, x, y, width, height).processFiles();
	}
	
	/**
	 * Watermarks a video with text.
	 * 
	 * @param input The input file.
	 * 
	 * @param output The output file.
	 * 
	 * @param text The watermark text.

	 * @param wmPos The watermark position.
	 * 
	 * @param color The watermark color.
	 * 
	 * @param fontPath The font path.
	 * 
	 * @param fontSize The font size.
	 * 
	 * @return The watermarked video.
	 */
	public static File textWatermarkVideo(File input, File output, String text
			, WatermarkPosition wmPos, Color color, String fontPath, int fontSize) {
		
		File fontFile = new File(fontPath);
		
		if (!fontFile.exists()) {
			System.err.println("Font file " + fontFile.getAbsolutePath() + " not found");
			return input;
		}
		WatermarkColor wmColor = null;
		
		for (WatermarkColor o : WatermarkColor.values()) {
			if (o.getColor() == color) {
				wmColor = o;
				break;
			}
		}
		if (wmColor == null) {
			System.err.println("Failed to fetch watermark color => " + color);
			return input;
		} //TODO maybe '' instead of "" in the fonts shit
		String cmd = "ffmpeg -i \"" + input.getAbsolutePath() + "\" -vf drawtext=fontfile=\"" + fontPath + "\":fontsize=" + fontSize + ":fontcolor=" + wmColor.getName() + ":" + wmPos.getTextPos() + ":text='" + text + "' -y -codec:a copy \"" + output.getAbsolutePath() + "\"";
		
		if (!new FFmpegEncode(cmd).execute()) {
			System.err.println("Failed to text watermark " + input.getAbsolutePath());
			return input;
		}
		return output;
	}
	
	/**
	 * Watermarks a video with an image.
	 * 
	 * @param input The input file.
	 * 
	 * @param output The output file.
	 * 
	 * @param wmPos The watermark position.
	 * 
	 * @param image The image.
	 * 
	 * @return The watermarked video.
	 */
	public static File imageWatermarkVideo(File input, File output, WatermarkPosition wmPos, File image) {
		VideoInfo vi = fetchVideoInfo(input);

		try (Workspace workspace = new Workspace();) {
			image = new ImageResize(image, new File(workspace.getWorkPath() + File.separator + image.getName())
					, vi.isPortrait() ? vi.getDimensions()[1] : vi.getDimensions()[0]
							, vi.isPortrait() ? vi.getDimensions()[0] : vi.getDimensions()[1], false).performOperation();
			
			String cmd = "ffmpeg -i \"" + input.getAbsolutePath() + "\""
					+ " -i \"" + image.getAbsolutePath() + "\""
					+ " -filter_complex " + wmPos.getFilter()
					+ " -codec:a copy \"" + output.getAbsolutePath() + "\"";
			
			if (!new FFmpegEncode(cmd).execute()) {
				System.err.println("Failed to image watermark " + input.getAbsolutePath());
				return input;
			}
			return output;
			
		} catch (Exception e) {
			e.printStackTrace();
			return input;
		}
	}
	
	/**
	 * Creates a slideshow.
	 * 
	 * @param outputPath The output path.
	 * 
	 * @param production The production type.
	 * 
	 * @param slideshowName The slideshow name.
	 * 
	 * @param media The media directory.
	 * 
	 * @param priorityMedia The priority media.
	 * 
	 * @param audio The audio file.
	 * 
	 * @param transitionDelay The transition delay.
	 * 
	 * @param amount The amount of images to use.
	 * 
	 * @param fps The fps.
	 * 
	 * @return Whether the slideshow was created successfuly or not.
	 */
	public static boolean createSlideshow(String outputPath, ProductionType productionType, String name, File mediaDir
			, File priorityMedia, File audio, int transitionDelay, int amount, int fps) {
		return new Slideshows(outputPath, productionType, name, mediaDir, priorityMedia
				, audio, transitionDelay, amount, fps).create();
	}

}
