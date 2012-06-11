package com.ngt.jopenmetaverse.shared.protocol;


    public final class OpenCircuitPacket extends Packet
    {
        /// <exclude/>
        public final class CircuitInfoBlock extends PacketBlock
        {
            public uint IP;
            public ushort Port;

            @Override
			public int getLength()
            {
                get
                {
                    return 6;
                }
            }

            public CircuitInfoBlock() { }
            public CircuitInfoBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    IP = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Port = (ushort)((bytes[i++] << 8) + bytes[i++]);
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UIntToBytes(IP, bytes, i); i += 4;
                bytes[i++] = (byte)((Port >> 8) % 256);
                bytes[i++] = (byte)(Port % 256);
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 10;
                length += CircuitInfo.length;
                return length;
            }
        }
        public CircuitInfoBlock CircuitInfo;

        public OpenCircuitPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.OpenCircuit;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 65532;
            header.Reliable = true;
            CircuitInfo = new CircuitInfoBlock();
        }

        public OpenCircuitPacket(byte[] bytes, int[] i) 
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
            CircuitInfo.FromBytes(bytes, i);
        }

        public OpenCircuitPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            CircuitInfo.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += CircuitInfo.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            CircuitInfo.ToBytes(bytes, i);
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