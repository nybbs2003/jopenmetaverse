package com.ngt.jopenmetaverse.shared.protocol;


    public final class ChildAgentPositionUpdatePacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public ulong RegionHandle;
            public uint ViewerCircuitCode;
            public UUID AgentID;
            public UUID SessionID;
            public Vector3 AgentPos;
            public Vector3 AgentVel;
            public Vector3 Center;
            public Vector3 Size;
            public Vector3 AtAxis;
            public Vector3 LeftAxis;
            public Vector3 UpAxis;
            public bool ChangedGrid;

            @Override
			public int getLength()
            {
                                {
                    return 129;
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
                    RegionHandle = (ulong)((ulong)bytes[i[0]++] + ((ulong)bytes[i[0]++] << 8) + ((ulong)bytes[i[0]++] << 16) + ((ulong)bytes[i[0]++] << 24) + ((ulong)bytes[i[0]++] << 32) + ((ulong)bytes[i[0]++] << 40) + ((ulong)bytes[i[0]++] << 48) + ((ulong)bytes[i[0]++] << 56));
                    ViewerCircuitCode = (uint)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    AgentID.FromBytes(bytes, i[0]); i[0] += 16;
                    SessionID.FromBytes(bytes, i[0]); i[0] += 16;
                    AgentPos.FromBytes(bytes, i[0]); i[0] += 12;
                    AgentVel.FromBytes(bytes, i[0]); i[0] += 12;
                    Center.FromBytes(bytes, i[0]); i[0] += 12;
                    Size.FromBytes(bytes, i[0]); i[0] += 12;
                    AtAxis.FromBytes(bytes, i[0]); i[0] += 12;
                    LeftAxis.FromBytes(bytes, i[0]); i[0] += 12;
                    UpAxis.FromBytes(bytes, i[0]); i[0] += 12;
                    ChangedGrid = (bytes[i[0]++] != 0) ? (bool)true : (bool)false;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UInt64ToBytes(RegionHandle, bytes, i); i += 8;
                Utils.UIntToBytes(ViewerCircuitCode, bytes, i); i += 4;
                AgentID.ToBytes(bytes, i[0]); i[0] += 16;
                SessionID.ToBytes(bytes, i[0]); i[0] += 16;
                AgentPos.ToBytes(bytes, i[0]); i[0] += 12;
                AgentVel.ToBytes(bytes, i[0]); i[0] += 12;
                Center.ToBytes(bytes, i[0]); i[0] += 12;
                Size.ToBytes(bytes, i[0]); i[0] += 12;
                AtAxis.ToBytes(bytes, i[0]); i[0] += 12;
                LeftAxis.ToBytes(bytes, i[0]); i[0] += 12;
                UpAxis.ToBytes(bytes, i[0]); i[0] += 12;
                bytes[i[0]++] = (byte)((ChangedGrid) ? 1 : 0);
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 7;
                length += AgentData.getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;

        public ChildAgentPositionUpdatePacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.ChildAgentPositionUpdate;
            this.header =  new Header();
            header.Frequency = PacketFrequency.High;
            header.ID = 27;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
        }

        public ChildAgentPositionUpdatePacket(byte[] bytes, int[] i) 
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
        }

        public ChildAgentPositionUpdatePacket(Header head, byte[] bytes, int[] i)
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
            int length = 7;
            length += AgentData.getLength();
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
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
