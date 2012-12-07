package com.ngt.jopenmetaverse.shared.util;

import java.io.File;
import java.net.URL;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

public class AbstractTest {

	protected String parentInputLocation;
	protected String parentOutputLocation;
	
	@Before
	public void setup() throws Exception
	{
		URL propLoc  =  getClass().getClassLoader().getResource("jomv_test_resources_dir.properties");

		Assert.assertNotNull(propLoc);

		String basePath = org.apache.commons.io.FileUtils.readFileToString(new File(propLoc.getPath())).trim();
		parentOutputLocation = basePath + "output";
		File f = new File(parentOutputLocation);
		if(!f.exists())
			f.mkdir();

		parentInputLocation = new File(basePath).getAbsolutePath();
	}
	
	@After
	public void clean() throws Exception
	{
		
	}
	
}
