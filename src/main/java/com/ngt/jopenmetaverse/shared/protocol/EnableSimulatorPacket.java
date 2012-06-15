package com.ngt.jopenmetaverse.shared.protocol;


    public final class EnableSimulatorPacket extends Packet
    {
        /// <exclude/>
        public final class SimulatorInfoBlock extends PacketBlock
        {
            public BigInteger Handle;
            public long IP;
            public ushort Port;

            @Override
			public int getLength()
            {
                                {
                    return 14;
                }
            }

            public SimulatorInfoBlock() { }
            public SimulatorInfoBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    Handle = Utils.bytesToULong(bytes, i[0]); i[0] += 8;
                    IP = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    Port = (int)Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.ulongToBytes(Handle, bytes, i[0]); i[0] += 8;
                Utils.uintToBytes(IP, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = (byte)((Port >> 8) % 256);
                bytes[i[0]++] = (byte)(Port % 256);
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += SimulatorInfo.length;
                return length;
            }
        }
        public SimulatorInfoBlock SimulatorInfo;

        public EnableSimulatorPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.EnableSimulator;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 151;
            header.Reliable = true;
            SimulatorInfo = new SimulatorInfoBlock();
        }

        public EnableSimulatorPacket(byte[] bytes, int[] i) throws MalformedDataException 
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(bytes, i, packetEnd, null);
        }

        @Override
		public void FromBytes(byte[] bytes, int[] i, int[] packetEnd, byte[] zeroBuffer) throws MalformedDataException
        {
            header.FromBytes(bytes, i, packetEnd);
            if (header.Zerocoded && zeroBuffer != null)
            {
                packetEnd[0] = Helpers.ZeroDecode(bytes, packetEnd[0] + 1, zeroBuffer) - 1;
                bytes = zeroBuffer;
            }
            SimulatorInfo.FromBytes(bytes, i);
        }

        public EnableSimulatorPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd) throws MalformedDataException
        {
            this.header =  header;
            SimulatorInfo.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += SimulatorInfo.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            SimulatorInfo.ToBytes(bytes, i);
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
