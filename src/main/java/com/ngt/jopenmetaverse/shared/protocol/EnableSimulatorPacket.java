package com.ngt.jopenmetaverse.shared.protocol;


    public final class EnableSimulatorPacket extends Packet
    {
        /// <exclude/>
        public final class SimulatorInfoBlock extends PacketBlock
        {
            public ulong Handle;
            public uint IP;
            public ushort Port;

            @Override
			public int getLength()
            {
                get
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
                    Handle = (ulong)((ulong)bytes[i++] + ((ulong)bytes[i++] << 8) + ((ulong)bytes[i++] << 16) + ((ulong)bytes[i++] << 24) + ((ulong)bytes[i++] << 32) + ((ulong)bytes[i++] << 40) + ((ulong)bytes[i++] << 48) + ((ulong)bytes[i++] << 56));
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
                Utils.UInt64ToBytes(Handle, bytes, i); i += 8;
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

        public EnableSimulatorPacket(byte[] bytes, int[] i) 
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
            SimulatorInfo.FromBytes(bytes, i);
        }

        public EnableSimulatorPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
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
            int i = 0;
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