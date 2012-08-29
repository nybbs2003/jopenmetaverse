package com.ngt.jopenmetaverse.shared.sim.imaging.platform.jclient;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.ngt.jopenmetaverse.shared.sim.imaging.IBitmap;
import com.ngt.jopenmetaverse.shared.sim.imaging.IOpenJPEG;
import com.ngt.jopenmetaverse.shared.util.JLogger;
import com.ngt.jopenmetaverse.shared.util.Utils;

public class OpenJPEGImplTests {

	URL fileLocation;
	@Before
	public void setup() throws Exception
	{
		fileLocation =  getClass().getClassLoader().getResource("data/files/images");
	}
	
	@Test
	public void EncodeFromImageTest()
	{
		try
		{
		File[] files = getFileList(fileLocation.getPath(), "jpg");

		for(File f: files)
		{
			IBitmap origbitmap = new BitmapBufferedImageImpl(ImageIO.read(f));
			Assert.assertTrue(origbitmap.getHeight() > 0  && origbitmap.getWidth() > 0);
			System.out.println(origbitmap.getHeight() + " : " + origbitmap.getWidth());
			IOpenJPEG iojpeg = new OpenJPEGFactoryImpl().getNewIntance();
			byte[] bytearray = iojpeg.EncodeFromImage(origbitmap, false);
			Assert.assertTrue(bytearray.length > 0);
			System.out.println("Output File: " + fileLocation.getPath() + "/" + f.getName() + ".jp2");
			FileOutputStream fos = new FileOutputStream(new File(fileLocation.getPath() + "/" + f.getName() + ".jp2"));
			fos.write(bytearray);
			fos.flush();
			fos.close();
		}
		}
		catch(Exception e)
		{
			Assert.fail(Utils.getExceptionStackTraceAsString(e));			
		}
	}
	
	private File[] getFileList(String dirname, String suffix)
	{
		JLogger.debug("Try to traverse the directory" + dirname);
		List<File> files = new ArrayList<File>(); 
		File file = new File(dirname); 

		if(file.isDirectory())
		{
			System.out.println("Directory is  " + dirname);
			String str[] = file.list();
			for( int i = 0; i < str.length; i++)
			{
				if(str[i].endsWith(suffix))
				{
				File f=new File(dirname + "/" + str[i]);
				if(f.isDirectory()){
					System.out.println(str[i] + " is a directory");
				}
				else
				{
					files.add(f);
					System.out.println(str[i] + " is a file");
				}
				}
			}
		}
		return files.toArray(new File[0]);
	}
	
}
