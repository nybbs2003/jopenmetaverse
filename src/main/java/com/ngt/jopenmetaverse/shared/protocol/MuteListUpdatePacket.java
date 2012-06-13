package com.ngt.jopenmetaverse.shared.protocol;


    public final class MuteListUpdatePacket extends Packet
    {
        /// <exclude/>
        public final class MuteDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public byte[] Filename;

            @Override
			public int getLength()
            {
                                {
                    int length = 17;
                    if (Filename != null) { length += Filename.length; }
                    return length;
                }
            }

            public MuteDataBlock() { }
            public MuteDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    AgentID.FromBytes(bytes, i[0]); i[0] += 16;
                    length = bytes[i[0]++];
                    Filename = new byte[length];
                    Utils.arraycopy(bytes, i, Filename, 0, length); i[0] +=  length;
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
                bytes[i[0]++] = (byte)Filename.length;
                Utils.arraycopy(Filename, 0, bytes, i, Filename.length); i[0] +=  Filename.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += MuteData.length;
                return length;
            }
        }
        public MuteDataBlock MuteData;

        public MuteListUpdatePacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.MuteListUpdate;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 318;
            header.Reliable = true;
            MuteData = new MuteDataBlock();
        }

        public MuteListUpdatePacket(byte[] bytes, int[] i) 
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
            MuteData.FromBytes(bytes, i);
        }

        public MuteListUpdatePacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            MuteData.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += MuteData.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            MuteData.ToBytes(bytes, i);
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
