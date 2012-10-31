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

import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;

import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;

public class UseCircuitCodePacketTest {

	@Test
	public void ToBytesTest() throws MalformedDataException
	{
		UseCircuitCodePacket use1 = new UseCircuitCodePacket();
		use1.CircuitCode.Code = 1064081002;
		use1.CircuitCode.ID = new UUID("dca4f918-6e92-4c65-a42a-00c9887c2669");
		use1.CircuitCode.SessionID = new UUID("7818e42d-d2c4-462b-88c8-13015c93540f0000");
		
		byte[] data1 = use1.ToBytes();
		UseCircuitCodePacket tuse1 = (UseCircuitCodePacket)UseCircuitCodePacket.BuildPacket(data1, new int[]{data1.length-1}, null);
		
		
		System.out.println(Utils.bytesToHexDebugString(use1.ToBytes(), "use1"));

		System.out.println(Utils.bytesToHexDebugString(tuse1.ToBytes(), "tuse1"));

		byte[] bytes2 = new byte[36];
		use1.CircuitCode.ToBytes(bytes2, new int[]{0});
		System.out.println(Utils.bytesToHexDebugString(bytes2, "use1"));
		System.out.println(Utils.bytesToUInt((ArrayUtils.subarray(bytes2, 0, 4))));
		
		System.out.println("Got Cirtuit code: " + tuse1.CircuitCode.Code);
		Assert.assertTrue(tuse1.CircuitCode.Code == use1.CircuitCode.Code);
		
		Assert.assertEquals(tuse1.CircuitCode.ID, use1.CircuitCode.ID);
		Assert.assertEquals(tuse1.CircuitCode.SessionID, use1.CircuitCode.SessionID);
		
	}
}
