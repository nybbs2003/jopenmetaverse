package com.ngt.jopenmetaverse.shared.protocol;


    public final class BulkUpdateInventoryPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID TransactionID;

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
                    TransactionID.FromBytes(bytes, i); i += 16;
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
                TransactionID.ToBytes(bytes, i); i += 16;
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
                    FolderID.FromBytes(bytes, i); i += 16;
                    ParentID.FromBytes(bytes, i); i += 16;
                    Type = (sbyte)bytes[i++];
                    length = bytes[i++];
                    Name = new byte[length];
                    Utils.arraycopy(bytes, i, Name, 0, length); i += length;
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
                ParentID.ToBytes(bytes, i); i += 16;
                bytes[i++] = (byte)Type;
                bytes[i++] = (byte)Name.length;
                Utils.arraycopy(Name, 0, bytes, i, Name.length); i += Name.length;
            }

        }

        /// <exclude/>
        public final class ItemDataBlock extends PacketBlock
        {
            public UUID ItemID;
            public uint CallbackID;
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
                    int length = 142;
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
                    ItemID.FromBytes(bytes, i); i += 16;
                    CallbackID = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    FolderID.FromBytes(bytes, i); i += 16;
                    CreatorID.FromBytes(bytes, i); i += 16;
                    OwnerID.FromBytes(bytes, i); i += 16;
                    GroupID.FromBytes(bytes, i); i += 16;
                    BaseMask = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    OwnerMask = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    GroupMask = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    EveryoneMask = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    NextOwnerMask = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    GroupOwned = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    AssetID.FromBytes(bytes, i); i += 16;
                    Type = (sbyte)bytes[i++];
                    InvType = (sbyte)bytes[i++];
                    Flags = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    SaleType = (byte)bytes[i++];
                    SalePrice = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    length = bytes[i++];
                    Name = new byte[length];
                    Utils.arraycopy(bytes, i, Name, 0, length); i += length;
                    length = bytes[i++];
                    Description = new byte[length];
                    Utils.arraycopy(bytes, i, Description, 0, length); i += length;
                    CreationDate = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    CRC = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
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
                Utils.UIntToBytes(CallbackID, bytes, i); i += 4;
                FolderID.ToBytes(bytes, i); i += 16;
                CreatorID.ToBytes(bytes, i); i += 16;
                OwnerID.ToBytes(bytes, i); i += 16;
                GroupID.ToBytes(bytes, i); i += 16;
                Utils.UIntToBytes(BaseMask, bytes, i); i += 4;
                Utils.UIntToBytes(OwnerMask, bytes, i); i += 4;
                Utils.UIntToBytes(GroupMask, bytes, i); i += 4;
                Utils.UIntToBytes(EveryoneMask, bytes, i); i += 4;
                Utils.UIntToBytes(NextOwnerMask, bytes, i); i += 4;
                bytes[i++] = (byte)((GroupOwned) ? 1 : 0);
                AssetID.ToBytes(bytes, i); i += 16;
                bytes[i++] = (byte)Type;
                bytes[i++] = (byte)InvType;
                Utils.UIntToBytes(Flags, bytes, i); i += 4;
                bytes[i++] = SaleType;
                Utils.IntToBytes(SalePrice, bytes, i); i += 4;
                bytes[i++] = (byte)Name.length;
                Utils.arraycopy(Name, 0, bytes, i, Name.length); i += Name.length;
                bytes[i++] = (byte)Description.length;
                Utils.arraycopy(Description, 0, bytes, i, Description.length); i += Description.length;
                Utils.IntToBytes(CreationDate, bytes, i); i += 4;
                Utils.UIntToBytes(CRC, bytes, i); i += 4;
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

        public BulkUpdateInventoryPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.BulkUpdateInventory;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 281;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            FolderData = null;
            ItemData = null;
        }

        public BulkUpdateInventoryPacket(byte[] bytes, int[] i) 
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

        public BulkUpdateInventoryPacket(Header head, byte[] bytes, int[] i)
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
