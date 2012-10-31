/**
 * A library to interact with Virtual Worlds such as OpenSim
 * Copyright (C) 2012  Jitendra Chauhan, Email: jitendra.chauhan@gmail.com
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
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
