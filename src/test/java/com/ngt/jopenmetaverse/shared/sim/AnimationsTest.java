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
