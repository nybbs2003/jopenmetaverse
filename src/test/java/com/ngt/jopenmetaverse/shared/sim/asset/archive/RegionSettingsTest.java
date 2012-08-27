package com.ngt.jopenmetaverse.shared.sim.asset.archive;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

import junit.framework.Assert;

import org.junit.Test;

import com.ngt.jopenmetaverse.shared.sim.asset.archiving.RegionSettings;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.JLogger;
import com.ngt.jopenmetaverse.shared.util.Utils;

public class RegionSettingsTest {

	@Test
	public void FromStreamTest()
	{
		try
		{
		URL fileLocation1 =  getClass().getClassLoader().getResource("data/files/xml/regionsettings.xml");
		File f1 = new File(fileLocation1.getPath());
		FileInputStream fs1 = new FileInputStream(f1);
		RegionSettings rs1 = RegionSettings.FromStream(fs1);
		System.out.println(rs1.TerrainDetail0.toString());
		Assert.assertEquals(rs1.TerrainDetail0, new UUID("98834a27-4edb-44c6-ab98-84bb8c101421"));
		
		}
		catch(Exception e)
		{
			JLogger.error(Utils.getExceptionStackTraceAsString(e));
			Assert.fail(Utils.getExceptionStackTraceAsString(e));
		}
		
	}
}
