package com.ngt.jopenmetaverse.shared.protocol;

import junit.framework.Assert;
import org.junit.Test;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;

public class AcceptCallingCardPacketTest {

	@Test
	public void ToBytesMultiple() throws MalformedDataException
	{
		UUID testID = UUID.Random();

		AcceptCallingCardPacket bigPacket = new AcceptCallingCardPacket();
		bigPacket.header.Zerocoded = false;
		bigPacket.header.Sequence = 42;
		bigPacket.header.AppendedAcks = true;
		//Unsigned int
		bigPacket.header.AckList = new long[50];
		for (int i = 0; i < bigPacket.header.AckList.length; i++) { bigPacket.header.AckList[i] = (long)i; }
		bigPacket.AgentData.AgentID = testID;
		bigPacket.AgentData.SessionID = testID;
		bigPacket.TransactionBlock.TransactionID = testID;
		
		bigPacket.FolderData = new AcceptCallingCardPacket.FolderDataBlock[200];
		for(int i = 0; i < bigPacket.FolderData.length; i++)
		{
			AcceptCallingCardPacket.FolderDataBlock fdb = new AcceptCallingCardPacket.FolderDataBlock();
			fdb.FolderID = testID;
			bigPacket.FolderData[i] = fdb;
		}
		
		//System.out.println(Utils.bytesToHexString(bigPacket.ToBytes(), "AcceptCallingCardPacketTest ToBytes"));
		
		byte[][] splitPackets = bigPacket.ToBytesMultiple();

		int queryDataCount = 0;
		for (int i = 0; i < splitPackets.length; i++)
		{
			byte[] packetData = splitPackets[i];
			
			//System.out.println(Utils.bytesToHexString(packetData, "AcceptCallingCardPacketTest SplitPacket " + i));
			
			int[] len = new int[]{packetData.length - 1};
			AcceptCallingCardPacket packet = (AcceptCallingCardPacket)Packet.BuildPacket(packetData, len, packetData);

			Assert.assertTrue(packet.AgentData.AgentID.equals(bigPacket.AgentData.AgentID));
			Assert.assertTrue(packet.AgentData.SessionID.equals(bigPacket.AgentData.SessionID));
			Assert.assertTrue(packet.TransactionBlock.TransactionID.equals(bigPacket.TransactionBlock.TransactionID));

			for (int j = 0; j < packet.FolderData.length; j++)
			{
				Assert.assertTrue("Expected FolderId of " + testID.toString() + " but got " + packet.FolderData[j].FolderID.toString(), 
						packet.FolderData[j].FolderID.equals(testID));
			}

			queryDataCount += packet.FolderData.length;
		}

		Assert.assertTrue(queryDataCount == bigPacket.FolderData.length);
	}
}
