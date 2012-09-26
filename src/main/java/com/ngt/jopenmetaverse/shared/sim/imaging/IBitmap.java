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
 
 public void resize(int w, int h);
 public void tile(int tiles);
 public void rotateAndFlip(double radians, boolean flipx, boolean flipy);
 
 public IBitmap cloneAndResize(int w, int h);
 public IBitmap cloneAndTile(int tiles);
 public IBitmap cloneRotateAndFlip(double radians, boolean flipx, boolean flipy);
 
 public IBitmap createImageWithSolidColor(int width, int height, int r, int g, int b, int a);
 
 public byte[] exportTGA() throws Exception;
 public byte[] exportRAW() throws Exception;
 public int[] exportPixels() throws Exception;
 
 public void dumpToFile(String filePath) throws Exception;
 public void dispose();
}
