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
package com.ngt.jopenmetaverse.shared.sim;

import java.util.Map;
import java.util.Map.Entry;

import junit.framework.Assert;
import org.junit.Test;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.JLogger;

public class AnimationsTest 
{
	@Test
	public void toMapTests()
	{
		Animations animations = new Animations();
		try {
			Map<UUID, String> map = Animations.toMap();
			StringBuilder result = new StringBuilder(); 
			for(Entry<UUID, String> entry : map.entrySet())
			{
				result.append(entry.getKey().toString() + " => " + entry.getValue() + "\n");
			}
			JLogger.debug(result.toString());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		} 
	}
}
