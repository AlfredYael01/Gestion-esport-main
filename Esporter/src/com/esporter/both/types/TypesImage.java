package com.esporter.both.types;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.util.Base64;

import javax.imageio.ImageIO;

public class TypesImage implements Types, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1572748294734274384L;
	private String image;
	
	public TypesImage (BufferedImage img, String formatName) {
		image = imgToBase64String(img, formatName);
	}
	
	public BufferedImage getImage() {
		
		byte[] decodedBytes = Base64.getDecoder().decode(image);
		BufferedImage bufferedimage = null;
		try {
			 bufferedimage = ImageIO.read(new ByteArrayInputStream(decodedBytes));
		} catch (IOException e) {

		}
		return bufferedimage;
	}
	
	public String getBase64Image() {
		return image;
	}
	
	public static String imgToBase64String(final RenderedImage img, final String formatName)
	{
	  final ByteArrayOutputStream os = new ByteArrayOutputStream();

	  try
	  {
	    ImageIO.write(img, formatName, os);
	    return Base64.getEncoder().encodeToString(os.toByteArray());
	  }
	  catch (final IOException ioe)
	  {
	    throw new UncheckedIOException(ioe);
	  }
	}
	
	public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}
	
}
