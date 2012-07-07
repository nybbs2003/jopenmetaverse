package com.ngt.jopenmetaverse.shared.protocol;

import java.math.BigInteger;

import org.junit.Assert;
import org.junit.Test;

import com.ngt.jopenmetaverse.shared.protocol.GroupRoleUpdatePacket.RoleDataBlock;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;

public class GroupRoleUpdatePacketTest {
	
	@Test
	public void ToBytesMultiple() throws MalformedDataException
	{
		UUID testID = UUID.Random();

		GroupRoleUpdatePacket bigPacket = new GroupRoleUpdatePacket();
		bigPacket.header.Zerocoded = false;
		bigPacket.header.Sequence = 42;
		bigPacket.header.AppendedAcks = true;
		//Unsigned int
		bigPacket.header.AckList = new long[50];
		for (int i = 0; i < bigPacket.header.AckList.length; i++) { bigPacket.header.AckList[i] = (long)i; }
		bigPacket.AgentData.AgentID = testID;
		bigPacket.AgentData.SessionID = testID;
//		bigPacket.TransactionBlock.TransactionID = testID;
		
		bigPacket.RoleData = new RoleDataBlock[200];
		for(int i = 0; i < bigPacket.RoleData.length; i++)
		{
			GroupRoleUpdatePacket.RoleDataBlock fdb = new GroupRoleUpdatePacket.RoleDataBlock();
			fdb.RoleID = testID;
			fdb.Name = Utils.stringToBytes("Test Name: " + i);
//			//System.out.println("Name Length" + fdb.Name.length);
			fdb.Description = Utils.stringToBytes("Test Description: " + i);
			fdb.Title = Utils.stringToBytes("Test Title: " + i);
			fdb.Powers = new BigInteger("4294967295");
			fdb.UpdateType = (byte)i;
			bigPacket.RoleData[i] = fdb;
		}
		
//		//System.out.println(Utils.bytesToHexString(bigPacket.ToBytes(), "GroupRoleUpdatePacketTest ToBytes"));
		
		byte[][] splitPackets = bigPacket.ToBytesMultiple();

		int roleDataCount = 0;
		int k = 0;
		for (int i = 0; i < splitPackets.length; i++)
		{
			byte[] packetData = splitPackets[i];
			
			int[] len = new int[]{packetData.length - 1};
			GroupRoleUpdatePacket packet = (GroupRoleUpdatePacket)Packet.BuildPacket(packetData, len, packetData);

			Assert.assertTrue(packet.AgentData.AgentID.equals(bigPacket.AgentData.AgentID));
			Assert.assertTrue(packet.AgentData.SessionID.equals(bigPacket.AgentData.SessionID));

			for (int j = 0; j < packet.RoleData.length; j++)
			{
				Assert.assertTrue("Expected FolderId of " + testID.toString() + " but got " + packet.RoleData[j].RoleID.toString(), 
						packet.RoleData[j].RoleID.equals(testID));
				Assert.assertArrayEquals(packet.RoleData[j].Name, Utils.stringToBytes("Test Name: " + k));
				Assert.assertArrayEquals(packet.RoleData[j].Description, Utils.stringToBytes("Test Description: " + k));
				Assert.assertArrayEquals(packet.RoleData[j].Title, Utils.stringToBytes("Test Title: " + k));
				
				Assert.assertArrayEquals(packet.RoleData[j].Powers.toByteArray(), bigPacket.RoleData[k].Powers.toByteArray());
				Assert.assertEquals(packet.RoleData[j].UpdateType, (byte)k);
				k += 1;
			}

			roleDataCount += packet.RoleData.length;
		}

		Assert.assertTrue(roleDataCount == bigPacket.RoleData.length);
	}
}
