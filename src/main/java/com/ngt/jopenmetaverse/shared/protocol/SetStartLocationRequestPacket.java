package com.ngt.jopenmetaverse.shared.protocol;


    public final class SetStartLocationRequestPacket extends Packet
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
        public final class StartLocationDataBlock extends PacketBlock
        {
            public byte[] SimName;
            public long LocationID;
            public Vector3 LocationPos;
            public Vector3 LocationLookAt;

            @Override
			public int getLength()
            {
                                {
                    int length = 29;
                    if (SimName != null) { length += SimName.length; }
                    return length;
                }
            }

            public StartLocationDataBlock() { }
            public StartLocationDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    SimName = new byte[length];
                    Utils.arraycopy(bytes, i[0], SimName, 0, length); i[0] +=  length;
                    LocationID = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    LocationPos.FromBytes(bytes, i[0]); i[0] += 12;
                    LocationLookAt.FromBytes(bytes, i[0]); i[0] += 12;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = (byte)SimName.length;
                Utils.arraycopy(SimName, 0, bytes, i[0], SimName.length); i[0] +=  SimName.length;
                Utils.uintToBytes(LocationID, bytes, i[0]); i[0] += 4;
                LocationPos.ToBytes(bytes, i[0]); i[0] += 12;
                LocationLookAt.ToBytes(bytes, i[0]); i[0] += 12;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += AgentData.getLength();
                length += StartLocationData.length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public StartLocationDataBlock StartLocationData;

        public SetStartLocationRequestPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.SetStartLocationRequest;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 324;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            StartLocationData = new StartLocationDataBlock();
        }

        public SetStartLocationRequestPacket(byte[] bytes, int[] i) 
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
            StartLocationData.FromBytes(bytes, i);
        }

        public SetStartLocationRequestPacket(Header head, byte[] bytes, int[] i)
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
            StartLocationData.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += StartLocationData.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            StartLocationData.ToBytes(bytes, i);
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
