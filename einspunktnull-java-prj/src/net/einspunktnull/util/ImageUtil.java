package net.einspunktnull.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtil
{

	public static void bytesToJpegFile(byte[] bytes, String filename) throws IOException
	{
		bytesToImageFile(bytes, "JPEG", filename);
	}
	
	public static void bytesToPngFile(byte[] bytes, String filename) throws IOException
	{
		bytesToImageFile(bytes, "PNG", filename);
	}
	
	public static void bytesToImageFile(byte[] bytes, String imageFileType, String filename) throws IOException
	{
		BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));
		File outputfile = new File(filename);
		ImageIO.write(img, imageFileType, outputfile);
	}
}
