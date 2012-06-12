package com.ngt.jopenmetaverse.shared.protocol;


    public final class ObjectPropertiesFamilyPacket extends Packet
    {
        /// <exclude/>
        public final class ObjectDataBlock extends PacketBlock
        {
            public uint RequestFlags;
            public UUID ObjectID;
            public UUID OwnerID;
            public UUID GroupID;
            public uint BaseMask;
            public uint OwnerMask;
            public uint GroupMask;
            public uint EveryoneMask;
            public uint NextOwnerMask;
            public int OwnershipCost;
            public byte SaleType;
            public int SalePrice;
            public uint Category;
            public UUID LastOwnerID;
            public byte[] Name;
            public byte[] Description;

            @Override
			public int getLength()
            {
                                {
                    int length = 103;
                    if (Name != null) { length += Name.length; }
                    if (Description != null) { length += Description.length; }
                    return length;
                }
            }

            public ObjectDataBlock() { }
            public ObjectDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    RequestFlags = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    ObjectID.FromBytes(bytes, i); i += 16;
                    OwnerID.FromBytes(bytes, i); i += 16;
                    GroupID.FromBytes(bytes, i); i += 16;
                    BaseMask = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    OwnerMask = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    GroupMask = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    EveryoneMask = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    NextOwnerMask = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    OwnershipCost = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    SaleType = (byte)bytes[i++];
                    SalePrice = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Category = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    LastOwnerID.FromBytes(bytes, i); i += 16;
                    length = bytes[i++];
                    Name = new byte[length];
                    Utils.arraycopy(bytes, i, Name, 0, length); i += length;
                    length = bytes[i++];
                    Description = new byte[length];
                    Utils.arraycopy(bytes, i, Description, 0, length); i += length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UIntToBytes(RequestFlags, bytes, i); i += 4;
                ObjectID.ToBytes(bytes, i); i += 16;
                OwnerID.ToBytes(bytes, i); i += 16;
                GroupID.ToBytes(bytes, i); i += 16;
                Utils.UIntToBytes(BaseMask, bytes, i); i += 4;
                Utils.UIntToBytes(OwnerMask, bytes, i); i += 4;
                Utils.UIntToBytes(GroupMask, bytes, i); i += 4;
                Utils.UIntToBytes(EveryoneMask, bytes, i); i += 4;
                Utils.UIntToBytes(NextOwnerMask, bytes, i); i += 4;
                Utils.IntToBytes(OwnershipCost, bytes, i); i += 4;
                bytes[i++] = SaleType;
                Utils.IntToBytes(SalePrice, bytes, i); i += 4;
                Utils.UIntToBytes(Category, bytes, i); i += 4;
                LastOwnerID.ToBytes(bytes, i); i += 16;
                bytes[i++] = (byte)Name.length;
                Utils.arraycopy(Name, 0, bytes, i, Name.length); i += Name.length;
                bytes[i++] = (byte)Description.length;
                Utils.arraycopy(Description, 0, bytes, i, Description.length); i += Description.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 8;
                length += ObjectData.length;
                return length;
            }
        }
        public ObjectDataBlock ObjectData;

        public ObjectPropertiesFamilyPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.ObjectPropertiesFamily;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Medium;
            header.ID = 10;
            header.Reliable = true;
            header.Zerocoded = true;
            ObjectData = new ObjectDataBlock();
        }

        public ObjectPropertiesFamilyPacket(byte[] bytes, int[] i) 
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
            ObjectData.FromBytes(bytes, i);
        }

        public ObjectPropertiesFamilyPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            ObjectData.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 8;
            length += ObjectData.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            ObjectData.ToBytes(bytes, i);
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            return new byte[][] { ToBytes() };
        }
    }

    /// <exclude/>
