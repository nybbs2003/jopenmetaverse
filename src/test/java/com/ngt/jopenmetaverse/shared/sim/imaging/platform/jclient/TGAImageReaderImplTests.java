package com.ngt.jopenmetaverse.shared.sim.imaging.platform.jclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import javax.imageio.ImageIO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ngt.jopenmetaverse.shared.sim.imaging.LoadTGAClass;
import com.ngt.jopenmetaverse.shared.sim.imaging.ManagedImage;
import com.ngt.jopenmetaverse.shared.sim.imaging.platform.jclient.tga.TGADecoder2;
import com.ngt.jopenmetaverse.shared.util.FileUtils;
import com.ngt.jopenmetaverse.shared.util.JLogger;
import com.ngt.jopenmetaverse.shared.util.Utils;

public class TGAImageReaderImplTests {

	URL fileLocation;
	@Before
	public void setup() throws Exception
	{
		fileLocation =  getClass().getClassLoader().getResource("data/files/images/tga");
	}

	@Test
	public void ExportTGA()
	{
		try
		{
			File[] files = FileUtils.getFileList(fileLocation.getPath(),".*\\.tga$" ,true);

			for(File f: files)
			{
				JLogger.debug("Reading from File: " + f.getAbsolutePath());
				BitmapBufferedImageImpl bitmap = (BitmapBufferedImageImpl)new TGAImageReaderImpl().read(new FileInputStream(f));

				File f1 = new File(fileLocation.getPath() + "/" + f.getName() + ".jpg");
				f1.createNewFile();
				FileOutputStream bos = new FileOutputStream(f1);
				ImageIO.write( bitmap.getImage(), "jpg",  bos );
				bos.close();
				Assert.assertTrue(bitmap.getHeight() > 0 && bitmap.getWidth() > 0);
			}
		}
		catch(Exception e)
		{
			Assert.fail(Utils.getExceptionStackTraceAsString(e));			
		}
	}

	@Test
	public void ExportTGAPrecomiledFiles()
	{
		try
		{
			File[] files = FileUtils.getFileList(fileLocation.getPath(), ".*\\.tga$", true);

			//			File[] files = FileUtils.getFileList(fileLocation.getPath(), "head_color.tga$", true);

			for(File f: files)
			{
				JLogger.debug("Reading from File: " + f.getAbsolutePath());
				InputStream is = new FileInputStream(f);
				BitmapBufferedImageImpl bitmap = (BitmapBufferedImageImpl)LoadTGAClass.LoadTGA(is);
				is.close();
				int width = bitmap.getWidth();
				int height = bitmap.getHeight();
				
				ManagedImage inputImage = new ManagedImage(bitmap);
				byte[] inputImagePixels = inputImage.ExportRaw();

				File[] compiledfiles = FileUtils.getFileList(fileLocation.getPath() + "/compiled", f.getName() + ".bin", true);
				Assert.assertTrue("No File or Multiple files exists with name: " + f.getName()  + " on path: " + fileLocation.getPath() + "/compiled", 
						compiledfiles.length == 1 );

				byte[] compiledImagePixels = FileUtils.readBytes(compiledfiles[0]);
				//				byte[] compiledImagePixels =  new ManagedImage(new BitmapBufferedImageImpl(TGADecoder2.loadImage(new FileInputStream(f)))).ExportRaw(); 

//				JLogger.debug("inputImagePixels");
//				printPixels(inputImagePixels, width, height);
//				JLogger.debug("compiledImagePixels");
//				printPixels(compiledImagePixels, width, height);

				Assert.assertEquals("No of image pixels differ" , inputImagePixels.length, compiledImagePixels.length);

				//				JLogger.debug(String.format("Compiled Image \n%s"
				//						, Utils.bytesToHexDebugString(compiledImagePixels, "")));

				
				 // RGBA
	            int Height = bitmap.getHeight();
	            int Width = bitmap.getWidth();
				for (int h = 0; h < Height; h++)
				{
					for (int w = 0; w < Width; w++)
					{
						int pos = (Height - 1 - h) * Width + w;
						int srcPos = h * Width + w;

						int origColor = bitmap.getRGB(w, h); 
						
						int origColor2 = Utils.ubyteToInt(inputImage.Red[srcPos]) << 16 | 
								Utils.ubyteToInt(inputImage.Green[srcPos]) << 8 |
								Utils.ubyteToInt(inputImage.Blue[srcPos]) |
								Utils.ubyteToInt(inputImage.Alpha[srcPos]) << 24;
						
						int newColor = Utils.ubyteToInt(compiledImagePixels[pos * 4 + 0]) << 16 | 
						Utils.ubyteToInt(compiledImagePixels[pos * 4 + 1]) << 8 |
						Utils.ubyteToInt(compiledImagePixels[pos * 4 + 2]) |
						Utils.ubyteToInt(compiledImagePixels[pos * 4 + 3]) << 24;
												
						Assert.assertEquals(origColor, origColor2);

						Assert.assertEquals(origColor, newColor);
						
						if(origColor != newColor)
							System.out.println(String.format("X %d Y %d orig color %d New color %d", w, h, origColor, bitmap.getRGB(w, h)));
					}
				}
				
				
				int length = 32;
				for(int i = 0; i < compiledImagePixels.length; i+= length)
				{
					int actuallength = Math.min(length, compiledImagePixels.length - i);
					byte[] inputPixelsSubArray = Arrays.copyOfRange(inputImagePixels, i, i + actuallength);
					byte[] compiledPixelsSubArray = Arrays.copyOfRange(compiledImagePixels, i, i + actuallength);

//					JLogger.debug(String.format("Comparing Pixel Index: %d <X= %d Y= %d> original: \n\t%s\n Compiled: \n\t%s\n"
//							,i, (int)(i/(4*width)),  (int)((i/4)%width) 
//							, Utils.bytesToHexDebugString(compiledPixelsSubArray, "")
//							, Utils.bytesToHexDebugString(inputPixelsSubArray, "")
//							));

					Assert.assertArrayEquals(String.format("Error at Pixel Index: %d <X= %d Y= %d> ",i, (int)(i/(4*width)),  (int)((i/4)%width) ), compiledPixelsSubArray, inputPixelsSubArray);

				}
			}
		}
		catch(Exception e)
		{
			Assert.fail(Utils.getExceptionStackTraceAsString(e));			
		}
	}


	void printPixels(byte[] bytes, int w, int h)
	{
		StringBuilder sb2 = new StringBuilder();
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for(int y=0; y< h; y++)
		{
			for(int x =0; x< w; x++)
			{
				index = x*4*w + y*4;
				byte[] next4bytes = Arrays.copyOfRange(bytes, index, index+4);
				if(Utils.bytesToUInt(next4bytes) > 0)
				{
					sb.append(String.format("<X = %d,  Y = %d, %s>\n", x, y, Utils.bytesToHexDebugString(next4bytes, ""))) ;
				}
				if(Utils.bytesToUInt(next4bytes) > 0)
				{
					sb2.append("1");
				}
				else
				{
					sb2.append("0");
				}
			}
			sb2.append("\n");
		}
		JLogger.debug(sb.toString());
		JLogger.debug(sb2.toString());
	}
}
