package com.ngt.jopenmetaverse.shared.protocol;

import junit.framework.Assert;

import org.junit.Test;

import com.ngt.jopenmetaverse.shared.protocol.NameValue.ClassType;
import com.ngt.jopenmetaverse.shared.protocol.NameValue.SendtoType;
import com.ngt.jopenmetaverse.shared.protocol.NameValue.ValueType;

public class NameValueTest {
	@Test
	public void nameValueParsingTest()
	{
		String test1 = "FirstName STRING RW SV jitendra\n";
		NameValue value1 = new NameValue(test1);
		assertEquals(value1, "FirstName", ValueType.String, 
				ClassType.ReadOnly, SendtoType.Sim, "jitendra");
		
//		 
//			String test2 = "LastName STRING RW SV chauhan81\n";
//			
//			String test3 = "Title STRING RW SV  ";
	}
	
	private void assertEquals(NameValue exp, String name, ValueType vt, ClassType ct, SendtoType st, String value)
	{
		Assert.assertEquals(name, exp.Name);
		Assert.assertEquals(vt, exp.Type);
		Assert.assertEquals(ct, exp.Class);
		Assert.assertEquals(st, exp.Sendto);
	}
}
