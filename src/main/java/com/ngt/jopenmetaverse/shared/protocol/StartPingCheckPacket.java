package com.ngt.jopenmetaverse.shared.protocol;


    public final class StartPingCheckPacket extends Packet
    {
        /// <exclude/>
        public final class PingIDBlock extends PacketBlock
        {
            public byte PingID;
            public uint OldestUnacked;

            @Override
			public int getLength()
            {
                                {
                    return 5;
                }
            }

            public PingIDBlock() { }
            public PingIDBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    PingID = (byte)bytes[i[0]++];
                    OldestUnacked = (uint)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = PingID;
                Utils.UIntToBytes(OldestUnacked, bytes, i[0]); i[0] += 4;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 7;
                length += PingID.length;
                return length;
            }
        }
        public PingIDBlock PingID;

        public StartPingCheckPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.StartPingCheck;
            this.header =  new Header();
            header.Frequency = PacketFrequency.High;
            header.ID = 1;
            header.Reliable = true;
            PingID = new PingIDBlock();
        }

        public StartPingCheckPacket(byte[] bytes, int[] i) 
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
            PingID.FromBytes(bytes, i);
        }

        public StartPingCheckPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            PingID.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 7;
            length += PingID.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            PingID.ToBytes(bytes, i);
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
