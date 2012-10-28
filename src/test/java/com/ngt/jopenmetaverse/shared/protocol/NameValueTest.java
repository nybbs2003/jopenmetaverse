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
