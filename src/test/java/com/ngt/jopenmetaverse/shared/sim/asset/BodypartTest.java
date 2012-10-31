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
