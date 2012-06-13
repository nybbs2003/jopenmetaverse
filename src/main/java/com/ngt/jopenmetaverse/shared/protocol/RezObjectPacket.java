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
                    AgentID.FromBytes(bytes, i[0]); i[0] += 16;
                    SessionID.FromBytes(bytes, i[0]); i[0] += 16;
                    GroupID.FromBytes(bytes, i[0]); i[0] += 16;
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
                GroupID.ToBytes(bytes, i[0]); i[0] += 16;
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
            public boolean RayEndIsIntersection;
            public boolean RezSelected;
            public boolean RemoveItem;
            public long ItemFlags;
            public long GroupMask;
            public long EveryoneMask;
            public long NextOwnerMask;

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
                    FromTaskID.FromBytes(bytes, i[0]); i[0] += 16;
                    BypassRaycast = (byte)bytes[i[0]++];
                    RayStart.FromBytes(bytes, i[0]); i[0] += 12;
                    RayEnd.FromBytes(bytes, i[0]); i[0] += 12;
                    RayTargetID.FromBytes(bytes, i[0]); i[0] += 16;
                    RayEndIsIntersection = (bytes[i[0]++] != 0) ? true : false;
                    RezSelected = (bytes[i[0]++] != 0) ? true : false;
                    RemoveItem = (bytes[i[0]++] != 0) ? true : false;
                    ItemFlags = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    GroupMask = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    EveryoneMask = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    NextOwnerMask = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                FromTaskID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = BypassRaycast;
                RayStart.ToBytes(bytes, i[0]); i[0] += 12;
                RayEnd.ToBytes(bytes, i[0]); i[0] += 12;
                RayTargetID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)((RayEndIsIntersection) ? 1 : 0);
                bytes[i[0]++] = (byte)((RezSelected) ? 1 : 0);
                bytes[i[0]++] = (byte)((RemoveItem) ? 1 : 0);
                Utils.uintToBytes(ItemFlags, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(GroupMask, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(EveryoneMask, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(NextOwnerMask, bytes, i[0]); i[0] += 4;
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
            public long BaseMask;
            public long OwnerMask;
            public long GroupMask;
            public long EveryoneMask;
            public long NextOwnerMask;
            public boolean GroupOwned;
            public UUID TransactionID;
            public sbyte Type;
            public sbyte InvType;
            public long Flags;
            public byte SaleType;
            public int SalePrice;
            public byte[] Name;
            public byte[] Description;
            public int CreationDate;
            public long CRC;

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
                    ItemID.FromBytes(bytes, i[0]); i[0] += 16;
                    FolderID.FromBytes(bytes, i[0]); i[0] += 16;
                    CreatorID.FromBytes(bytes, i[0]); i[0] += 16;
                    OwnerID.FromBytes(bytes, i[0]); i[0] += 16;
                    GroupID.FromBytes(bytes, i[0]); i[0] += 16;
                    BaseMask = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    OwnerMask = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    GroupMask = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    EveryoneMask = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    NextOwnerMask = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    GroupOwned = (bytes[i[0]++] != 0) ? true : false;
                    TransactionID.FromBytes(bytes, i[0]); i[0] += 16;
                    Type = (sbyte)bytes[i[0]++];
                    InvType = (sbyte)bytes[i[0]++];
                    Flags = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    SaleType = (byte)bytes[i[0]++];
                    SalePrice = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    length = bytes[i[0]++];
                    Name = new byte[length];
                    Utils.arraycopy(bytes, i[0], Name, 0, length); i[0] +=  length;
                    length = bytes[i[0]++];
                    Description = new byte[length];
                    Utils.arraycopy(bytes, i[0], Description, 0, length); i[0] +=  length;
                    CreationDate = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    CRC = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
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
                Utils.uintToBytes(BaseMask, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(OwnerMask, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(GroupMask, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(EveryoneMask, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(NextOwnerMask, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = (byte)((GroupOwned) ? 1 : 0);
                TransactionID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)Type;
                bytes[i[0]++] = (byte)InvType;
                Utils.uintToBytes(Flags, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = SaleType;
                Utils.intToBytes(SalePrice, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = (byte)Name.length;
                Utils.arraycopy(Name, 0, bytes, i[0], Name.length); i[0] +=  Name.length;
                bytes[i[0]++] = (byte)Description.length;
                Utils.arraycopy(Description, 0, bytes, i[0], Description.length); i[0] +=  Description.length;
                Utils.intToBytes(CreationDate, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(CRC, bytes, i[0]); i[0] += 4;
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
            int[] i = new int[]{0};
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
