package com.ngt.jopenmetaverse.shared.protocol;

import java.util.ArrayList;
import java.util.List;

import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;


    public final class CopyInventoryItemPacket extends Packet
    {
        /// <exclude/>
        public static final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID SessionID;

            @Override
			public int getLength()
            {
                                {
                    return 32;
                }
            }

            public AgentDataBlock() { }
            public AgentDataBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    AgentID.FromBytes(bytes, i[0]); i[0] += 16;
                    SessionID.FromBytes(bytes, i[0]); i[0] += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                AgentID.ToBytes(bytes, i[0]); i[0] += 16;
                SessionID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        /// <exclude/>
        public static final class InventoryDataBlock extends PacketBlock
        {
            public long CallbackID;
            public UUID OldAgentID;
            public UUID OldItemID;
            public UUID NewFolderID;
		/** Unsigned Byte */ 
		public byte[] NewName;

            @Override
			public int getLength()
            {
                                {
                    int length = 53;
                    if (NewName != null) { length += NewName.length; }
                    return length;
                }
            }

            public InventoryDataBlock() { }
            public InventoryDataBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    CallbackID = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    OldAgentID.FromBytes(bytes, i[0]); i[0] += 16;
                    OldItemID.FromBytes(bytes, i[0]); i[0] += 16;
                    NewFolderID.FromBytes(bytes, i[0]); i[0] += 16;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    NewName = new byte[length];
                    Utils.arraycopy(bytes, i[0], NewName, 0, length); i[0] +=  length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.uintToBytes(CallbackID, bytes, i[0]); i[0] += 4;
                OldAgentID.ToBytes(bytes, i[0]); i[0] += 16;
                OldItemID.ToBytes(bytes, i[0]); i[0] += 16;
                NewFolderID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)NewName.length;
                Utils.arraycopy(NewName, 0, bytes, i[0], NewName.length); i[0] +=  NewName.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += AgentData.getLength();
                for (int j = 0; j < InventoryData.length; j++)
                    length += InventoryData[j].getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public InventoryDataBlock[] InventoryData;

        public CopyInventoryItemPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.CopyInventoryItem;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 269;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            InventoryData = null;
        }

        public CopyInventoryItemPacket(byte[] bytes, int[] i) throws MalformedDataException 
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(bytes, i, packetEnd, null);
        }

        @Override
		public void FromBytes(byte[] bytes, int[] i, int[] packetEnd, byte[] zeroBuffer) throws MalformedDataException
        {
            header.FromBytes(bytes, i, packetEnd);
            if (header.Zerocoded && zeroBuffer != null)
            {
                packetEnd[0] = Helpers.ZeroDecode(bytes, packetEnd[0] + 1, zeroBuffer) - 1;
                bytes = zeroBuffer;
            }
            AgentData.FromBytes(bytes, i);
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(InventoryData == null || InventoryData.length != -1) {
                InventoryData = new InventoryDataBlock[count];
                for(int j = 0; j < count; j++)
                { InventoryData[j] = new InventoryDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { InventoryData[j].FromBytes(bytes, i); }
        }

        public CopyInventoryItemPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd) throws MalformedDataException
        {
            this.header =  header;
            AgentData.FromBytes(bytes, i);
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(InventoryData == null || InventoryData.length != count) {
                InventoryData = new InventoryDataBlock[count];
                for(int j = 0; j < count; j++)
                { InventoryData[j] = new InventoryDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { InventoryData[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length++;
            for (int j = 0; j < InventoryData.length; j++) { length += InventoryData[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)InventoryData.length;
            for (int j = 0; j < InventoryData.length; j++) { InventoryData[j].ToBytes(bytes, i); }
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            List<byte[]> packets = new ArrayList<byte[]>();
            int[] i = new int[]{0};
            int fixedLength = 10;

            byte[] ackBytes = null;
            int[] acksLength = new int[]{0};
            if (header.AckList != null && header.AckList.length > 0) {
                header.AppendedAcks = true;
                ackBytes = new byte[header.AckList.length * 4 + 1];
                header.AcksToBytes(ackBytes, acksLength);
            }

            fixedLength += AgentData.getLength();
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AgentData.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int InventoryDataStart = 0;
            do
            {
                int variableLength = 0;
                int InventoryDataCount = 0;

              i[0] =InventoryDataStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < InventoryData.length) {
                    int blockLength = InventoryData[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++InventoryDataCount;
                    }
                    else { break; }
                    i[0]++;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)InventoryDataCount;
                for (i[0] = InventoryDataStart; i[0] < InventoryDataStart + InventoryDataCount; i[0]++) { InventoryData[i[0]].ToBytes(packet, length); }
                InventoryDataStart += InventoryDataCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                InventoryDataStart < InventoryData.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
