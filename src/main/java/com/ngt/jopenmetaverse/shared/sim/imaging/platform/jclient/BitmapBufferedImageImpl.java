package com.ngt.jopenmetaverse.shared.sim.imaging.platform.jclient;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.ngt.jopenmetaverse.shared.exception.NotSupportedException;
import com.ngt.jopenmetaverse.shared.sim.imaging.IBitmap;
import com.ngt.jopenmetaverse.shared.sim.imaging.ManagedImage;
import com.ngt.jopenmetaverse.shared.sim.imaging.PixelFormat;

public class BitmapBufferedImageImpl  implements IBitmap
{
	BufferedImage img;
	static Map<PixelFormat, Integer> PixelFormatMap = new HashMap<PixelFormat, Integer>();
	static Map<Integer, PixelFormat> PixelFormatMapReverse = new HashMap<Integer, PixelFormat>();
	static 
	{
		PixelFormatMap.put(PixelFormat.Format32bppArgb, BufferedImage.TYPE_INT_ARGB);
		//Add here more mapping
		
		for(Entry<PixelFormat, Integer> e: PixelFormatMap.entrySet())
		{
			PixelFormatMapReverse.put(e.getValue(), e.getKey());
		}
	}
	
	public BitmapBufferedImageImpl(int width, int height, int imageType) {
		img = new BufferedImage(width, height, imageType);
	}
	
	public BitmapBufferedImageImpl(BufferedImage img) {
		this.img = img;
	}
	
	public BitmapBufferedImageImpl(int w, int h, int[] pixels) {
        Image piximg =  Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(w, h, pixels, 0, w));
		this.img = (BufferedImage)piximg;
	}
	
	public boolean hasPixelFormat(PixelFormat pixelFormat) throws NotSupportedException{
		if(!PixelFormatMap.containsKey(pixelFormat))
			throw new NotSupportedException(pixelFormat + " is not supported");
		return img.getType() == PixelFormatMap.get(pixelFormat); 
	}
	
	
	 public int getRGB(int x, int y)
	{
		return img.getRGB(x, y);
	}

	public String getPixelFormatAsString() {
		if(PixelFormatMapReverse.containsKey(img.getType()))
			return PixelFormatMapReverse.get(img.getType()).toString();
		else
			return String.format("Unknown Format with int value (%d)", img.getType());  
			
	}

	public PixelFormat getPixelFormat() {
		return PixelFormatMapReverse.get(img.getType());
	}

	public int getHeight() {
		return img.getHeight();
	}

	public int getWidth() {
		return img.getWidth();
	}
	
	public BufferedImage getImage()
	{
		return img;
	}

	public void setImage(BufferedImage img)
	{
		this.img = img;
	}

	public IBitmap createIBitmap(int w, int h, int[] pixels) {
		return new BitmapBufferedImageImpl(h, w, pixels);
	}	
}
