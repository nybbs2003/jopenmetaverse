package com.ngt.jopenmetaverse.shared.sim.imaging.platform.jclient;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import com.ngt.jopenmetaverse.shared.sim.imaging.IOpenJPEG;
import com.ngt.jopenmetaverse.shared.sim.imaging.IOpenJPEG.DecodeToImageResult;
import com.ngt.jopenmetaverse.shared.sim.imaging.ManagedImage;
import com.ngt.jopenmetaverse.shared.util.FileUtils;
import com.ngt.jopenmetaverse.shared.util.JLogger;
import com.ngt.jopenmetaverse.shared.util.Utils;

public class OpenJPEGImplTest {

	URL fileLocation;
	IOpenJPEG iOpenJPEG;
	
	@Before
	public void setup()
	{
		fileLocation =  getClass().getClassLoader().getResource("data/files/images/jpeg2000");
		iOpenJPEG= new OpenJPEGImpl();
		((OpenJPEGImpl)iOpenJPEG).setBitmapFactory(new BitmapFactoryImpl());
	}

	@Test
	public void DecodeToIBitMapTests()
	{
		File[] files = FileUtils.getFileList(fileLocation.getPath(), true);
		for(File f: files)
		{
			DecodeToIBitMapTest(f.getName());
		}
	}
	
	@Test
	public void DecodeToImage2Tests()
	{
		File[] files = FileUtils.getFileList(fileLocation.getPath(), true);
		for(File f: files)
		{
			DecodeToImage2Test(f.getName());
		}		
	}
	
	
	public void DecodeToIBitMapTest(String imageName)
	{
		try {
			String imagePath1 = FileUtils.combineFilePath(fileLocation.getPath(), imageName);
			byte[] bytes1 = FileUtils.readBytes(new File(imagePath1));
			BitmapBufferedImageImpl bitmap = (BitmapBufferedImageImpl)iOpenJPEG.DecodeToIBitMap(bytes1);
//			printByeArray(bitmap.getImage());
//			File f1 = new File(fileLocation.getPath() + "/" + "test1" + ".jpg");
//			f1.createNewFile();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write( bitmap.getImage(), "jpg",  bos );
			bos.flush();
			BufferedImage bimage = ImageIO.read( new ByteArrayInputStream(bos.toByteArray()));
			System.out.println(String.format("Original Image %s Type %d, and size %d, jpeg conversion image type %d and size %d", 
					imageName, bitmap.getImage().getType(), 56, bimage.getType(), 56));
		} 
		catch (Exception e)
		{
			Assert.fail(Utils.getExceptionStackTraceAsString(e));
		}
	}

	
	public void DecodeToImage2Test(String imageName)
	{
		try {
			String imagePath1 = FileUtils.combineFilePath(fileLocation.getPath(), imageName);
			byte[] bytes1 = FileUtils.readBytes(new File(imagePath1));
			DecodeToImageResult decodeToImageResult = iOpenJPEG.DecodeToImage2(bytes1);
			ManagedImage mi = decodeToImageResult.getManagedImage();
			
		} 
		catch (Exception e)
		{
			Assert.fail(Utils.getExceptionStackTraceAsString(e));
		}
	}
	
	private void printByeArray(BufferedImage bitmap ) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		int i = 0;
		for(int x = 0; x < bitmap.getWidth(); x++)
			for(int y = 0; y< bitmap.getHeight(); y++)
			{
				
				i = x*bitmap.getHeight() + y;
				int pixel = bitmap.getRGB(x, y);
				baos.write(Utils.intToBytes(pixel));
			}
		JLogger.debug(Utils.bytesToHexDebugString(baos.toByteArray(), ""));
	}
	
}
