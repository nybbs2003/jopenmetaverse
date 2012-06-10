package com.ngt.jopenmetaverse.shared.protocol;


    public final class AgentHeightWidthPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID SessionID;
            public uint CircuitCode;

            @Override
			public int getLength()
            {
                get
                {
                    return 36;
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
                    CircuitCode = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
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
                Utils.UIntToBytes(CircuitCode, bytes, i); i += 4;
            }

        }

        /// <exclude/>
        public final class HeightWidthBlockBlock extends PacketBlock
        {
            public uint GenCounter;
            public ushort Height;
            public ushort Width;

            @Override
			public int getLength()
            {
                get
                {
                    return 8;
                }
            }

            public HeightWidthBlockBlock() { }
            public HeightWidthBlockBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    GenCounter = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Height = (ushort)(bytes[i++] + (bytes[i++] << 8));
                    Width = (ushort)(bytes[i++] + (bytes[i++] << 8));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UIntToBytes(GenCounter, bytes, i); i += 4;
                bytes[i++] = (byte)(Height % 256);
                bytes[i++] = (byte)((Height >> 8) % 256);
                bytes[i++] = (byte)(Width % 256);
                bytes[i++] = (byte)((Width >> 8) % 256);
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 10;
                length += AgentData.getLength();
                length += HeightWidthBlock.length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public HeightWidthBlockBlock HeightWidthBlock;

        public AgentHeightWidthPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.AgentHeightWidth;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 83;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            HeightWidthBlock = new HeightWidthBlockBlock();
        }

        public AgentHeightWidthPacket(byte[] bytes, int[] i) 
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
            HeightWidthBlock.FromBytes(bytes, i);
        }

        public AgentHeightWidthPacket(Header head, byte[] bytes, int[] i)
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
            HeightWidthBlock.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += HeightWidthBlock.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            HeightWidthBlock.ToBytes(bytes, i);
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
