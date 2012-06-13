package com.ngt.jopenmetaverse.shared.protocol;


    public final class FetchInventoryDescendentsPacket extends Packet
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
        public final class InventoryDataBlock extends PacketBlock
        {
            public UUID FolderID;
            public UUID OwnerID;
            public int SortOrder;
            public boolean FetchFolders;
            public boolean FetchItems;

            @Override
			public int getLength()
            {
                                {
                    return 38;
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
                try
                {
                    FolderID.FromBytes(bytes, i[0]); i[0] += 16;
                    OwnerID.FromBytes(bytes, i[0]); i[0] += 16;
                    SortOrder = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    FetchFolders = (bytes[i[0]++] != 0) ? true : false;
                    FetchItems = (bytes[i[0]++] != 0) ? true : false;
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
                OwnerID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.intToBytes(SortOrder, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = (byte)((FetchFolders) ? 1 : 0);
                bytes[i[0]++] = (byte)((FetchItems) ? 1 : 0);
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += AgentData.getLength();
                length += InventoryData.length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public InventoryDataBlock InventoryData;

        public FetchInventoryDescendentsPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.FetchInventoryDescendents;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 277;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            InventoryData = new InventoryDataBlock();
        }

        public FetchInventoryDescendentsPacket(byte[] bytes, int[] i) 
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
            InventoryData.FromBytes(bytes, i);
        }

        public FetchInventoryDescendentsPacket(Header head, byte[] bytes, int[] i)
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
            InventoryData.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += InventoryData.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
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
