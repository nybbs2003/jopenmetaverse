package com.ngt.jopenmetaverse.shared.protocol;


    public final class RemoveInventoryObjectsPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
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
            public AgentDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    AgentID.FromBytes(bytes, i); i += 16;
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
        public final class FolderDataBlock extends PacketBlock
        {
            public UUID FolderID;

            @Override
			public int getLength()
            {
                                {
                    return 16;
                }
            }

            public FolderDataBlock() { }
            public FolderDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    FolderID.FromBytes(bytes, i); i += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                FolderID.ToBytes(bytes, i); i += 16;
            }

        }

        /// <exclude/>
        public final class ItemDataBlock extends PacketBlock
        {
            public UUID ItemID;

            @Override
			public int getLength()
            {
                                {
                    return 16;
                }
            }

            public ItemDataBlock() { }
            public ItemDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ItemID.FromBytes(bytes, i); i += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ItemID.ToBytes(bytes, i); i += 16;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 12;
                length += AgentData.getLength();
                for (int j = 0; j < FolderData.length; j++)
                    length += FolderData[j].length;
                for (int j = 0; j < ItemData.length; j++)
                    length += ItemData[j].length;
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

        public RemoveInventoryObjectsPacket(byte[] bytes, int[] i) 
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(bytes, i, packetEnd, null);
        }

        @Override
		public void FromBytes(byte[] bytes, int[] i, int[] packetEnd, byte[] zeroBuffer)
        {
            header.FromBytes(bytes, i, packetEnd);
            if (header.Zerocoded && zeroBuffer != null)
            {
                packetEnd[0] = Helpers.ZeroDecode(bytes, packetEnd[0] + 1, zeroBuffer) - 1;
                bytes = zeroBuffer;
            }
            AgentData.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(FolderData == null || FolderData.length != -1) {
                FolderData = new FolderDataBlock[count];
                for(int j = 0; j < count; j++)
                { FolderData[j] = new FolderDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { FolderData[j].FromBytes(bytes, i); }
            count = (int)bytes[i++];
            if(ItemData == null || ItemData.length != -1) {
                ItemData = new ItemDataBlock[count];
                for(int j = 0; j < count; j++)
                { ItemData[j] = new ItemDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ItemData[j].FromBytes(bytes, i); }
        }

        public RemoveInventoryObjectsPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            AgentData.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(FolderData == null || FolderData.length != count) {
                FolderData = new FolderDataBlock[count];
                for(int j = 0; j < count; j++)
                { FolderData[j] = new FolderDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { FolderData[j].FromBytes(bytes, i); }
            count = (int)bytes[i++];
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
            for (int j = 0; j < FolderData.length; j++) { length += FolderData[j].length; }
            length++;
            for (int j = 0; j < ItemData.length; j++) { length += ItemData[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            bytes[i++] = (byte)FolderData.length;
            for (int j = 0; j < FolderData.length; j++) { FolderData[j].ToBytes(bytes, i); }
            bytes[i++] = (byte)ItemData.length;
            for (int j = 0; j < ItemData.length; j++) { ItemData[j].ToBytes(bytes, i); }
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            List<byte[]> packets = new ArrayList<byte[]>();
            int i = 0;
            int fixedLength = 10;

            byte[] ackBytes = null;
            int acksLength = 0;
            if (header.AckList != null && header.AckList.length > 0) {
                header.AppendedAcks = true;
                ackBytes = new byte[header.AckList.length * 4 + 1];
                header.AcksToBytes(ackBytes, ref acksLength);
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

                i = FolderDataStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < FolderData.length) {
                    int blockLength = FolderData[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++FolderDataCount;
                    }
                    else { break; }
                    ++i;
                }

                i = ItemDataStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < ItemData.length) {
                    int blockLength = ItemData[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++ItemDataCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Utils.arraycopy(fixedBytes, 0, packet, 0, length);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)FolderDataCount;
                for (i = FolderDataStart; i < FolderDataStart + FolderDataCount; i++) { FolderData[i].ToBytes(packet, ref length); }
                FolderDataStart += FolderDataCount;

                packet[length++] = (byte)ItemDataCount;
                for (i = ItemDataStart; i < ItemDataStart + ItemDataCount; i++) { ItemData[i].ToBytes(packet, ref length); }
                ItemDataStart += ItemDataCount;

                if (acksLength > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.add(packet);
            } while (
                FolderDataStart < FolderData.length ||
                ItemDataStart < ItemData.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
