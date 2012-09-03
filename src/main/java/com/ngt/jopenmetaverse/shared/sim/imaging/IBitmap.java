package com.ngt.jopenmetaverse.shared.sim.imaging;

import com.ngt.jopenmetaverse.shared.exception.NotSupportedException;

public interface IBitmap {
 public boolean hasPixelFormat(PixelFormat pixelFormat) throws NotSupportedException;
 public int getRGB(int x, int y);
 public int getHeight();
 public int getWidth();
 public String getPixelFormatAsString();
 public PixelFormat getPixelFormat(); 
 public IBitmap createIBitmap(int w, int h, int[] pixels);
public void setRGB(int w, int h, int i);
}
