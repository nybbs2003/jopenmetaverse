package com.ngt.jopenmetaverse.shared.protocol;


    public final class RezObjectPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID SessionID;
            public UUID GroupID;

            @Override
			public int getLength()
            {
                                {
                    return 48;
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
                    GroupID.FromBytes(bytes, i); i += 16;
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
                GroupID.ToBytes(bytes, i); i += 16;
            }

        }

        /// <exclude/>
        public final class RezDataBlock extends PacketBlock
        {
            public UUID FromTaskID;
            public byte BypassRaycast;
            public Vector3 RayStart;
            public Vector3 RayEnd;
            public UUID RayTargetID;
            public bool RayEndIsIntersection;
            public bool RezSelected;
            public bool RemoveItem;
            public uint ItemFlags;
            public uint GroupMask;
            public uint EveryoneMask;
            public uint NextOwnerMask;

            @Override
			public int getLength()
            {
                                {
                    return 76;
                }
            }

            public RezDataBlock() { }
            public RezDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    FromTaskID.FromBytes(bytes, i); i += 16;
                    BypassRaycast = (byte)bytes[i++];
                    RayStart.FromBytes(bytes, i); i += 12;
                    RayEnd.FromBytes(bytes, i); i += 12;
                    RayTargetID.FromBytes(bytes, i); i += 16;
                    RayEndIsIntersection = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    RezSelected = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    RemoveItem = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    ItemFlags = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    GroupMask = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    EveryoneMask = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    NextOwnerMask = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                FromTaskID.ToBytes(bytes, i); i += 16;
                bytes[i++] = BypassRaycast;
                RayStart.ToBytes(bytes, i); i += 12;
                RayEnd.ToBytes(bytes, i); i += 12;
                RayTargetID.ToBytes(bytes, i); i += 16;
                bytes[i++] = (byte)((RayEndIsIntersection) ? 1 : 0);
                bytes[i++] = (byte)((RezSelected) ? 1 : 0);
                bytes[i++] = (byte)((RemoveItem) ? 1 : 0);
                Utils.UIntToBytes(ItemFlags, bytes, i); i += 4;
                Utils.UIntToBytes(GroupMask, bytes, i); i += 4;
                Utils.UIntToBytes(EveryoneMask, bytes, i); i += 4;
                Utils.UIntToBytes(NextOwnerMask, bytes, i); i += 4;
            }

        }

        /// <exclude/>
        public final class InventoryDataBlock extends PacketBlock
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
            public UUID TransactionID;
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

            public InventoryDataBlock() { }
            public InventoryDataBlock(byte[] bytes, int[] i)
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
                    TransactionID.FromBytes(bytes, i); i += 16;
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
                TransactionID.ToBytes(bytes, i); i += 16;
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
                int length = 10;
                length += AgentData.getLength();
                length += RezData.length;
                length += InventoryData.length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public RezDataBlock RezData;
        public InventoryDataBlock InventoryData;

        public RezObjectPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.RezObject;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 293;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            RezData = new RezDataBlock();
            InventoryData = new InventoryDataBlock();
        }

        public RezObjectPacket(byte[] bytes, int[] i) 
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
            RezData.FromBytes(bytes, i);
            InventoryData.FromBytes(bytes, i);
        }

        public RezObjectPacket(Header head, byte[] bytes, int[] i)
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
            RezData.FromBytes(bytes, i);
            InventoryData.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += RezData.length;
            length += InventoryData.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            RezData.ToBytes(bytes, i);
            InventoryData.ToBytes(bytes, i);
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
