package com.ngt.jopenmetaverse.shared.sim.imaging.platform.jclient;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.ngt.jopenmetaverse.shared.sim.AppearanceManager.BakeType;
import com.ngt.jopenmetaverse.shared.sim.imaging.Baker;
import com.ngt.jopenmetaverse.shared.util.JLogger;
import com.ngt.jopenmetaverse.shared.util.Utils;

public class BakerTest {

	URL fileLocation;
	
	@Before
	public void setup()
	{
		fileLocation =  getClass().getClassLoader().getResource("data/files/images");
	}
	
	@Test
	public void bakesTests()
	{
		Baker oven = new Baker(BakeType.Head);
		//TODO need to implement
		Baker.LoadResourceLayer("head_hair.tga");
		byte b1 = (byte)0xff;
		byte b2 = (byte)0xff;
		byte b3 = (byte)0xff;
		byte b4 = (byte)0xff;

		Assert.assertEquals((0xff << 24) | (0xff<<16) | (0xff<<8) | 0xff, 
				(b1 << 24) | (b2 <<16) | (b3 << 8) | b4);
//		printImage(fileLocation.getPath() + "/jpg/shirt_collar_back_alpha.JPEG");
//		printImage(fileLocation.getPath() + "/jpg/shirt_collar_back_alpha.tga.jpg");		
	}
	
	public void printImage(String relfilepath)
	{
		StringBuilder sb = new StringBuilder();
		File f1 = new File( relfilepath);
		BufferedImage img = null;
		try {
		    img = ImageIO.read(f1);
		    for(int i = 0; i< img.getHeight(); i++)
		    {
		    	for(int j =0; j< img.getWidth(); j++)
		    	{
		    		sb.append(Utils.bytesToHexString(Utils.intToBytes(img.getRGB(j,  i)), false) + "  ");
		    	}
		    	sb.append("\n");
		    }
		    JLogger.debug(sb.toString());
		    JLogger.debug("Image Type: " + img.getType());
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
	}
}
