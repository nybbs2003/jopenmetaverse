package com.ngt.jopenmetaverse.shared.protocol;


    public final class InventoryDescendentsPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID FolderID;
            public UUID OwnerID;
            public int Version;
            public int Descendents;

            @Override
			public int getLength()
            {
                                {
                    return 56;
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
                    AgentID.FromBytes(bytes, i[0]); i[0] += 16;
                    FolderID.FromBytes(bytes, i[0]); i[0] += 16;
                    OwnerID.FromBytes(bytes, i[0]); i[0] += 16;
                    Version = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    Descendents = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
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
                FolderID.ToBytes(bytes, i[0]); i[0] += 16;
                OwnerID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.IntToBytes(Version, bytes, i[0]); i[0] += 4;
                Utils.IntToBytes(Descendents, bytes, i[0]); i[0] += 4;
            }

        }

        /// <exclude/>
        public final class FolderDataBlock extends PacketBlock
        {
            public UUID FolderID;
            public UUID ParentID;
            public sbyte Type;
            public byte[] Name;

            @Override
			public int getLength()
            {
                                {
                    int length = 34;
                    if (Name != null) { length += Name.length; }
                    return length;
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
                int length;
                try
                {
                    FolderID.FromBytes(bytes, i[0]); i[0] += 16;
                    ParentID.FromBytes(bytes, i[0]); i[0] += 16;
                    Type = (sbyte)bytes[i[0]++];
                    length = bytes[i[0]++];
                    Name = new byte[length];
                    Utils.arraycopy(bytes, i[0], Name, 0, length); i[0] +=  length;
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
                ParentID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)Type;
                bytes[i[0]++] = (byte)Name.length;
                Utils.arraycopy(Name, 0, bytes, i[0], Name.length); i[0] +=  Name.length;
            }

        }

        /// <exclude/>
        public final class ItemDataBlock extends PacketBlock
        {
            public UUID ItemID;
            public UUID FolderID;
            public UUID CreatorID;
            public UUID OwnerID;
            public UUID GroupID;
            public uint BaseMask;
            public uint OwnerMask;
            public uint GroupMask;
            public uint EveryoneMask;
            public uint NextOwnerMask;
            public bool GroupOwned;
            public UUID AssetID;
            public sbyte Type;
            public sbyte InvType;
            public uint Flags;
            public byte SaleType;
            public int SalePrice;
            public byte[] Name;
            public byte[] Description;
            public int CreationDate;
            public uint CRC;

            @Override
			public int getLength()
            {
                                {
                    int length = 138;
                    if (Name != null) { length += Name.length; }
                    if (Description != null) { length += Description.length; }
                    return length;
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
                int length;
                try
                {
                    ItemID.FromBytes(bytes, i[0]); i[0] += 16;
                    FolderID.FromBytes(bytes, i[0]); i[0] += 16;
                    CreatorID.FromBytes(bytes, i[0]); i[0] += 16;
                    OwnerID.FromBytes(bytes, i[0]); i[0] += 16;
                    GroupID.FromBytes(bytes, i[0]); i[0] += 16;
                    BaseMask = (uint)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    OwnerMask = (uint)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    GroupMask = (uint)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    EveryoneMask = (uint)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    NextOwnerMask = (uint)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    GroupOwned = (bytes[i[0]++] != 0) ? (bool)true : (bool)false;
                    AssetID.FromBytes(bytes, i[0]); i[0] += 16;
                    Type = (sbyte)bytes[i[0]++];
                    InvType = (sbyte)bytes[i[0]++];
                    Flags = (uint)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    SaleType = (byte)bytes[i[0]++];
                    SalePrice = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    length = bytes[i[0]++];
                    Name = new byte[length];
                    Utils.arraycopy(bytes, i[0], Name, 0, length); i[0] +=  length;
                    length = bytes[i[0]++];
                    Description = new byte[length];
                    Utils.arraycopy(bytes, i[0], Description, 0, length); i[0] +=  length;
                    CreationDate = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    CRC = (uint)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
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
                FolderID.ToBytes(bytes, i[0]); i[0] += 16;
                CreatorID.ToBytes(bytes, i[0]); i[0] += 16;
                OwnerID.ToBytes(bytes, i[0]); i[0] += 16;
                GroupID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.UIntToBytes(BaseMask, bytes, i[0]); i[0] += 4;
                Utils.UIntToBytes(OwnerMask, bytes, i[0]); i[0] += 4;
                Utils.UIntToBytes(GroupMask, bytes, i[0]); i[0] += 4;
                Utils.UIntToBytes(EveryoneMask, bytes, i[0]); i[0] += 4;
                Utils.UIntToBytes(NextOwnerMask, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = (byte)((GroupOwned) ? 1 : 0);
                AssetID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)Type;
                bytes[i[0]++] = (byte)InvType;
                Utils.UIntToBytes(Flags, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = SaleType;
                Utils.IntToBytes(SalePrice, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = (byte)Name.length;
                Utils.arraycopy(Name, 0, bytes, i[0], Name.length); i[0] +=  Name.length;
                bytes[i[0]++] = (byte)Description.length;
                Utils.arraycopy(Description, 0, bytes, i[0], Description.length); i[0] +=  Description.length;
                Utils.IntToBytes(CreationDate, bytes, i[0]); i[0] += 4;
                Utils.UIntToBytes(CRC, bytes, i[0]); i[0] += 4;
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

        public InventoryDescendentsPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.InventoryDescendents;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 278;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            FolderData = null;
            ItemData = null;
        }

        public InventoryDescendentsPacket(byte[] bytes, int[] i) 
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
            int count = (int)bytes[i[0]++];
            if(FolderData == null || FolderData.length != -1) {
                FolderData = new FolderDataBlock[count];
                for(int j = 0; j < count; j++)
                { FolderData[j] = new FolderDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { FolderData[j].FromBytes(bytes, i); }
            count = (int)bytes[i[0]++];
            if(ItemData == null || ItemData.length != -1) {
                ItemData = new ItemDataBlock[count];
                for(int j = 0; j < count; j++)
                { ItemData[j] = new ItemDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ItemData[j].FromBytes(bytes, i); }
        }

        public InventoryDescendentsPacket(Header head, byte[] bytes, int[] i)
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
            int count = (int)bytes[i[0]++];
            if(FolderData == null || FolderData.length != count) {
                FolderData = new FolderDataBlock[count];
                for(int j = 0; j < count; j++)
                { FolderData[j] = new FolderDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { FolderData[j].FromBytes(bytes, i); }
            count = (int)bytes[i[0]++];
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
