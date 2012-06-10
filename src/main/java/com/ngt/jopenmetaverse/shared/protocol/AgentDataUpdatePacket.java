package com.ngt.jopenmetaverse.shared.protocol;


    public final class AgentDataUpdatePacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public byte[] FirstName;
            public byte[] LastName;
            public byte[] GroupTitle;
            public UUID ActiveGroupID;
            public ulong GroupPowers;
            public byte[] GroupName;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 44;
                    if (FirstName != null) { length += FirstName.length; }
                    if (LastName != null) { length += LastName.length; }
                    if (GroupTitle != null) { length += GroupTitle.length; }
                    if (GroupName != null) { length += GroupName.length; }
                    return length;
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
                int length;
                try
                {
                    AgentID.FromBytes(bytes, i); i += 16;
                    length = bytes[i++];
                    FirstName = new byte[length];
                    Buffer.BlockCopy(bytes, i, FirstName, 0, length); i += length;
                    length = bytes[i++];
                    LastName = new byte[length];
                    Buffer.BlockCopy(bytes, i, LastName, 0, length); i += length;
                    length = bytes[i++];
                    GroupTitle = new byte[length];
                    Buffer.BlockCopy(bytes, i, GroupTitle, 0, length); i += length;
                    ActiveGroupID.FromBytes(bytes, i); i += 16;
                    GroupPowers = (ulong)((ulong)bytes[i++] + ((ulong)bytes[i++] << 8) + ((ulong)bytes[i++] << 16) + ((ulong)bytes[i++] << 24) + ((ulong)bytes[i++] << 32) + ((ulong)bytes[i++] << 40) + ((ulong)bytes[i++] << 48) + ((ulong)bytes[i++] << 56));
                    length = bytes[i++];
                    GroupName = new byte[length];
                    Buffer.BlockCopy(bytes, i, GroupName, 0, length); i += length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                AgentID.ToBytes(bytes, i); i += 16;
                bytes[i++] = (byte)FirstName.length;
                Buffer.BlockCopy(FirstName, 0, bytes, i, FirstName.length); i += FirstName.length;
                bytes[i++] = (byte)LastName.length;
                Buffer.BlockCopy(LastName, 0, bytes, i, LastName.length); i += LastName.length;
                bytes[i++] = (byte)GroupTitle.length;
                Buffer.BlockCopy(GroupTitle, 0, bytes, i, GroupTitle.length); i += GroupTitle.length;
                ActiveGroupID.ToBytes(bytes, i); i += 16;
                Utils.UInt64ToBytes(GroupPowers, bytes, i); i += 8;
                bytes[i++] = (byte)GroupName.length;
                Buffer.BlockCopy(GroupName, 0, bytes, i, GroupName.length); i += GroupName.length;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 10;
                length += AgentData.getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;

        public AgentDataUpdatePacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.AgentDataUpdate;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 387;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
        }

        public AgentDataUpdatePacket(byte[] bytes, int[] i) 
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
                packetEnd = Helpers.ZeroDecode(bytes, packetEnd + 1, zeroBuffer) - 1;
                bytes = zeroBuffer;
            }
            AgentData.FromBytes(bytes, i);
        }

        public AgentDataUpdatePacket(Header head, byte[] bytes, int[] i)
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
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
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
