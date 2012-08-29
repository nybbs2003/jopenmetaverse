package com.ngt.jopenmetaverse.shared.sim.imaging.platform.jclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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
	public void tests()
	{
		try
		{
		File[] files = getFileList(fileLocation.getPath());

		for(File f: files)
		{
			JLogger.debug("Reading from File: " + f.getAbsolutePath());
			BitmapBufferedImageImpl bitmap = (BitmapBufferedImageImpl)new TGAImageReaderImpl().read(new FileInputStream(f));
			System.out.println(bitmap.getPixelFormatAsString());
			
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
	
	private File[] getFileList(String dirname)
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
				if(str[i].endsWith("tga"))
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
