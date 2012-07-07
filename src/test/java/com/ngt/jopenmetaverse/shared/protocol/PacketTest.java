package com.ngt.jopenmetaverse.shared.protocol;

import junit.framework.Assert;

import org.junit.Test;

import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;

import com.ngt.jopenmetaverse.shared.protocol.DirPlacesReplyPacket;

public class PacketTest {

	@Test

	public void HeaderFlags()
	{
		TestMessagePacket packet = new TestMessagePacket();

		packet.header.AppendedAcks = false;
		packet.header.Reliable = false;
		packet.header.Resent = false;
		packet.header.Zerocoded = false;

		Assert.assertFalse("AppendedAcks: Failed to initially set the flag to false", packet.header.AppendedAcks);
		Assert.assertFalse("Reliable: Failed to initially set the flag to false", packet.header.Reliable);
		Assert.assertFalse("Resent: Failed to initially set the flag to false", packet.header.Resent);
		Assert.assertFalse("Zerocoded: Failed to initially set the flag to false", packet.header.Zerocoded);

		packet.header.AppendedAcks = false;
		packet.header.Reliable = false;
		packet.header.Resent = false;
		packet.header.Zerocoded = false;

		Assert.assertFalse("AppendedAcks: Failed to set the flag to false a second time", packet.header.AppendedAcks );
		Assert.assertFalse("Reliable: Failed to set the flag to false a second time", packet.header.Reliable);
		Assert.assertFalse("Resent: Failed to set the flag to false a second time", packet.header.Resent);
		Assert.assertFalse("Zerocoded: Failed to set the flag to false a second time", packet.header.Zerocoded);

		packet.header.AppendedAcks = true;
		packet.header.Reliable = true;
		packet.header.Resent = true;
		packet.header.Zerocoded = true;

		Assert.assertTrue("AppendedAcks: Failed to set the flag to true", packet.header.AppendedAcks);
		Assert.assertTrue("Reliable: Failed to set the flag to true", packet.header.Reliable);
		Assert.assertTrue("Resent: Failed to set the flag to true", packet.header.Resent);
		Assert.assertTrue("Zerocoded: Failed to set the flag to true", packet.header.Zerocoded);

		packet.header.AppendedAcks = true;
		packet.header.Reliable = true;
		packet.header.Resent = true;
		packet.header.Zerocoded = true;

		Assert.assertTrue("AppendedAcks: Failed to set the flag to true a second time", packet.header.AppendedAcks);
		Assert.assertTrue("Reliable: Failed to set the flag to true a second time", packet.header.Reliable);
		Assert.assertTrue("Resent: Failed to set the flag to true a second time", packet.header.Resent);
		Assert.assertTrue("Zerocoded: Failed to set the flag to true a second time", packet.header.Zerocoded);

		packet.header.AppendedAcks = false;
		packet.header.Reliable = false;
		packet.header.Resent = false;
		packet.header.Zerocoded = false;

		Assert.assertFalse("AppendedAcks: Failed to set the flag back to false", packet.header.AppendedAcks);
		Assert.assertFalse( "Reliable: Failed to set the flag back to false", packet.header.Reliable);
		Assert.assertFalse("Resent: Failed to set the flag back to false", packet.header.Resent);
		Assert.assertFalse( "Zerocoded: Failed to set the flag back to false", packet.header.Zerocoded);
	}

	@Test
	public void ToBytesMultiple() throws MalformedDataException
	{
		UUID testID = UUID.Random();

		DirPlacesReplyPacket bigPacket = new DirPlacesReplyPacket();
		bigPacket.header.Zerocoded = false;
		bigPacket.header.Sequence = 42;
		bigPacket.header.AppendedAcks = true;
		//Unsigned int
		bigPacket.header.AckList = new long[50];
		for (int i = 0; i < bigPacket.header.AckList.length; i++) { bigPacket.header.AckList[i] = (long)i; }
		bigPacket.AgentData.AgentID = testID;
		bigPacket.QueryData = new DirPlacesReplyPacket.QueryDataBlock[100];
		for (int i = 0; i < bigPacket.QueryData.length; i++)
		{
			bigPacket.QueryData[i] = new DirPlacesReplyPacket.QueryDataBlock();
			bigPacket.QueryData[i].QueryID = testID;
		}
		bigPacket.QueryReplies = new DirPlacesReplyPacket.QueryRepliesBlock[100];
		for (int i = 0; i < bigPacket.QueryReplies.length; i++)
		{
			bigPacket.QueryReplies[i] = new DirPlacesReplyPacket.QueryRepliesBlock();
			bigPacket.QueryReplies[i].Auction = (i & 1) == 0;
			bigPacket.QueryReplies[i].Dwell = (float)i;
			bigPacket.QueryReplies[i].ForSale = (i & 1) == 0;
			bigPacket.QueryReplies[i].Name = Utils.stringToBytes("DirPlacesReply Test String");
			bigPacket.QueryReplies[i].ParcelID = testID;
		}
		bigPacket.StatusData = new DirPlacesReplyPacket.StatusDataBlock[100];
		for (int i = 0; i < bigPacket.StatusData.length; i++)
		{
			bigPacket.StatusData[i] = new DirPlacesReplyPacket.StatusDataBlock();
			bigPacket.StatusData[i].Status = (long)i;
		}
		
		byte[][] splitPackets = bigPacket.ToBytesMultiple();

		int queryDataCount = 0;
		int queryRepliesCount = 0;
		int statusDataCount = 0;
		for (int i = 0; i < splitPackets.length; i++)
		{
			byte[] packetData = splitPackets[i];
			int[] len = new int[]{packetData.length - 1};
			DirPlacesReplyPacket packet = (DirPlacesReplyPacket)Packet.BuildPacket(packetData, len, packetData);

			Assert.assertTrue(packet.AgentData.AgentID.equals(bigPacket.AgentData.AgentID));

			for (int j = 0; j < packet.QueryReplies.length; j++)
			{
				Assert.assertTrue("Expected Dwell of " + (float)(queryRepliesCount + j) + " but got " + packet.QueryReplies[j].Dwell, 
						packet.QueryReplies[j].Dwell == (float)(queryRepliesCount + j));
				Assert.assertTrue(packet.QueryReplies[j].ParcelID.equals(testID));
			}
			
			queryDataCount += packet.QueryData.length;
			queryRepliesCount += packet.QueryReplies.length;
			statusDataCount += packet.StatusData.length;
		}

		Assert.assertTrue(queryDataCount == bigPacket.QueryData.length);
		Assert.assertTrue(queryRepliesCount == bigPacket.QueryData.length);
		Assert.assertTrue(statusDataCount == bigPacket.StatusData.length);

		ScriptDialogPacket scriptDialogPacket = new ScriptDialogPacket();
		scriptDialogPacket.Data.ChatChannel = 0;
		scriptDialogPacket.Data.FirstName = Utils.EmptyBytes;
		scriptDialogPacket.Data.ImageID = UUID.Zero;
		scriptDialogPacket.Data.LastName = Utils.EmptyBytes;
		scriptDialogPacket.Data.Message = Utils.EmptyBytes;
		scriptDialogPacket.Data.ObjectID = UUID.Zero;
		scriptDialogPacket.Data.ObjectName = Utils.EmptyBytes;
		scriptDialogPacket.Buttons = new ScriptDialogPacket.ButtonsBlock[0];
		scriptDialogPacket.OwnerData = new ScriptDialogPacket.OwnerDataBlock[1];
		scriptDialogPacket.OwnerData[0] = new ScriptDialogPacket.OwnerDataBlock();
		scriptDialogPacket.OwnerData[0].OwnerID = UUID.Zero;

		byte[][] splitPacket = scriptDialogPacket.ToBytesMultiple();

		Assert.assertNotNull(splitPacket);
		Assert.assertTrue("Expected ScriptDialog packet to split into 1 packet but got " + splitPacket.length, splitPacket.length == 1);

		ParcelReturnObjectsPacket proPacket = new ParcelReturnObjectsPacket();
		proPacket.AgentData.AgentID = UUID.Zero;
		proPacket.AgentData.SessionID = UUID.Zero;
		proPacket.ParcelData.LocalID = 0;
		proPacket.ParcelData.ReturnType = 0;
		proPacket.TaskIDs = new ParcelReturnObjectsPacket.TaskIDsBlock[0];
		proPacket.OwnerIDs = new ParcelReturnObjectsPacket.OwnerIDsBlock[1];
		proPacket.OwnerIDs[0] = new ParcelReturnObjectsPacket.OwnerIDsBlock();
		proPacket.OwnerIDs[0].OwnerID = UUID.Zero;

		splitPacket = proPacket.ToBytesMultiple();

		Assert.assertNotNull(splitPacket);
		Assert.assertTrue("Expected ParcelReturnObjectsPacket packet to split into 1 packet but got " + splitPacket.length, splitPacket.length == 1);

		InventoryDescendentsPacket invPacket = new InventoryDescendentsPacket();
		invPacket.FolderData = new InventoryDescendentsPacket.FolderDataBlock[1];
		invPacket.FolderData[0] = new InventoryDescendentsPacket.FolderDataBlock();
		invPacket.FolderData[0].Name = Utils.EmptyBytes;
		invPacket.ItemData = new InventoryDescendentsPacket.ItemDataBlock[5];
		for (int i = 0; i < 5; i++)
		{
			invPacket.ItemData[i] = new InventoryDescendentsPacket.ItemDataBlock();
			invPacket.ItemData[i].Description = Utils.stringToBytes("Unit Test Item Description");
			invPacket.ItemData[i].Name = Utils.stringToBytes("Unit Test Item Name");
		}

		splitPacket = invPacket.ToBytesMultiple();

		Assert.assertNotNull(splitPacket);
		Assert.assertTrue("Split InventoryDescendents packet into " + splitPacket.length + " instead of 1 packet",splitPacket.length == 1);

		int[] x = new int[]{0};
		int[] y = new int[]{splitPacket[0].length - 1};
		invPacket.FromBytes(splitPacket[0], x, y, null);

		Assert.assertTrue("InventoryDescendents packet came back with " + invPacket.FolderData.length + " FolderData blocks", invPacket.FolderData.length == 1);
		Assert.assertTrue("InventoryDescendents packet came back with " + invPacket.ItemData.length + " ItemData blocks", invPacket.ItemData.length == 5);
	}
}
