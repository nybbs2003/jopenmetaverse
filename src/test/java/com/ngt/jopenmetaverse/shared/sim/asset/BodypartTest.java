package com.ngt.jopenmetaverse.shared.sim.asset;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.FileUtils;
import com.ngt.jopenmetaverse.shared.util.Utils;

public class BodypartTest {
	URL fileLocation;
	
	@Before
	public void setup()
	{
		fileLocation =  getClass().getClassLoader().getResource("data/files/asset/assetwearable/bodypart");
		Assert.assertNotNull(fileLocation);
	}
	
	@Test
	public void decodeTests()
	{
		for(File f: FileUtils.getFileList(fileLocation.getPath(), true))
		{
			decodeTest(f);
		}
	}
	public void decodeTest(File f)
	{
		byte[] bytes1;
		try {
			bytes1 = FileUtils.readBytes(f);
			AssetBodypart bodypart = new AssetBodypart(UUID.Random(), bytes1);
			bodypart.Decode();
			
		
		} catch (Exception e) {
			Assert.fail(Utils.getExceptionStackTraceAsString(e));
		}
	}

}
