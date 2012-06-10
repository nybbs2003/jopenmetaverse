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
                get
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
                    SessionID.FromBytes(bytes, i); i += 16;
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
                SessionID.ToBytes(bytes, i); i += 16;
            }

        }

        /// <exclude/>
        public final class StartLocationDataBlock extends PacketBlock
        {
            public byte[] SimName;
            public uint LocationID;
            public Vector3 LocationPos;
            public Vector3 LocationLookAt;

            @Override
			public int getLength()
            {
                get
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
                    length = bytes[i++];
                    SimName = new byte[length];
                    Buffer.BlockCopy(bytes, i, SimName, 0, length); i += length;
                    LocationID = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    LocationPos.FromBytes(bytes, i); i += 12;
                    LocationLookAt.FromBytes(bytes, i); i += 12;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i++] = (byte)SimName.length;
                Buffer.BlockCopy(SimName, 0, bytes, i, SimName.length); i += SimName.length;
                Utils.UIntToBytes(LocationID, bytes, i); i += 4;
                LocationPos.ToBytes(bytes, i); i += 12;
                LocationLookAt.ToBytes(bytes, i); i += 12;
            }

        }

        @Override
			public int getLength()
        {
            get
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
                packetEnd = Helpers.ZeroDecode(bytes, packetEnd + 1, zeroBuffer) - 1;
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
            int i = 0;
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
