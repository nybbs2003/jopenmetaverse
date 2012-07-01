package com.ngt.jopenmetaverse.shared.protocol;

import java.util.ArrayList;
import java.util.List;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;
	public final class RemoveInventoryObjectsPacket extends Packet
    {
        /// <exclude/>
        public static final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID = new UUID();
            public UUID SessionID = new UUID();

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
        public static final class FolderDataBlock extends PacketBlock
        {
            public UUID FolderID = new UUID();

            @Override
			public int getLength()
            {
                                {
                    return 16;
                }
            }

            public FolderDataBlock() { }
            public FolderDataBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    FolderID.FromBytes(bytes, i[0]); i[0] += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                FolderID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        /// <exclude/>
        public static final class ItemDataBlock extends PacketBlock
        {
            public UUID ItemID = new UUID();

            @Override
			public int getLength()
            {
                                {
                    return 16;
                }
            }

            public ItemDataBlock() { }
            public ItemDataBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ItemID.FromBytes(bytes, i[0]); i[0] += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ItemID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 12;
                length += AgentData.getLength();
                for (int j = 0; j < FolderData.length; j++)
                    length += FolderData[j].getLength();
                for (int j = 0; j < ItemData.length; j++)
                    length += ItemData[j].getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public FolderDataBlock[] FolderData;
        public ItemDataBlock[] ItemData;

        public RemoveInventoryObjectsPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.RemoveInventoryObjects;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 284;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            FolderData = null;
            ItemData = null;
        }

        public RemoveInventoryObjectsPacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            if(FolderData == null || FolderData.length != -1) {
                FolderData = new FolderDataBlock[count];
                for(int j = 0; j < count; j++)
                { FolderData[j] = new FolderDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { FolderData[j].FromBytes(bytes, i); }
            count = Utils.ubyteToInt(bytes[i[0]++]);
            if(ItemData == null || ItemData.length != -1) {
                ItemData = new ItemDataBlock[count];
                for(int j = 0; j < count; j++)
                { ItemData[j] = new ItemDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ItemData[j].FromBytes(bytes, i); }
        }

        public RemoveInventoryObjectsPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
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
            if(FolderData == null || FolderData.length != count) {
                FolderData = new FolderDataBlock[count];
                for(int j = 0; j < count; j++)
                { FolderData[j] = new FolderDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { FolderData[j].FromBytes(bytes, i); }
            count = Utils.ubyteToInt(bytes[i[0]++]);
            if(ItemData == null || ItemData.length != count) {
                ItemData = new ItemDataBlock[count];
                for(int j = 0; j < count; j++)
                { ItemData[j] = new ItemDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ItemData[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length++;
            for (int j = 0; j < FolderData.length; j++) { length += FolderData[j].getLength(); }
            length++;
            for (int j = 0; j < ItemData.length; j++) { length += ItemData[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)FolderData.length;
            for (int j = 0; j < FolderData.length; j++) { FolderData[j].ToBytes(bytes, i); }
            bytes[i[0]++] = (byte)ItemData.length;
            for (int j = 0; j < ItemData.length; j++) { ItemData[j].ToBytes(bytes, i); }
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
            fixedLength += 2;

            int FolderDataStart = 0;
            int ItemDataStart = 0;
            do
            {
                int variableLength = 0;
                int FolderDataCount = 0;
                int ItemDataCount = 0;

              i[0] =FolderDataStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < FolderData.length) {
                    int blockLength = FolderData[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++FolderDataCount;
                    }
                    else { break; }
                    i[0]++;
                }

              i[0] =ItemDataStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < ItemData.length) {
                    int blockLength = ItemData[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++ItemDataCount;
                    }
                    else { break; }
                    i[0]++;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)FolderDataCount;
                for (i[0] = FolderDataStart; i[0] < FolderDataStart + FolderDataCount; i[0]++) { FolderData[i[0]].ToBytes(packet, length); }
                FolderDataStart += FolderDataCount;

                packet[length[0]++] = (byte)ItemDataCount;
                for (i[0] = ItemDataStart; i[0] < ItemDataStart + ItemDataCount; i[0]++) { ItemData[i[0]].ToBytes(packet, length); }
                ItemDataStart += ItemDataCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                FolderDataStart < FolderData.length ||
                ItemDataStart < ItemData.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
