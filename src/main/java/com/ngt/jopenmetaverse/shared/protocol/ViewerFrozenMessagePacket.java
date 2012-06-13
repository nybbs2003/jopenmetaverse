package com.ngt.jopenmetaverse.shared.protocol;


    public final class ViewerFrozenMessagePacket extends Packet
    {
        /// <exclude/>
        public final class FrozenDataBlock extends PacketBlock
        {
            public bool Data;

            @Override
			public int getLength()
            {
                                {
                    return 1;
                }
            }

            public FrozenDataBlock() { }
            public FrozenDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    Data = (bytes[i[0]++] != 0) ? (bool)true : (bool)false;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = (byte)((Data) ? 1 : 0);
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += FrozenData.length;
                return length;
            }
        }
        public FrozenDataBlock FrozenData;

        public ViewerFrozenMessagePacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.ViewerFrozenMessage;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 137;
            header.Reliable = true;
            FrozenData = new FrozenDataBlock();
        }

        public ViewerFrozenMessagePacket(byte[] bytes, int[] i) 
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
            FrozenData.FromBytes(bytes, i);
        }

        public ViewerFrozenMessagePacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            FrozenData.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += FrozenData.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            FrozenData.ToBytes(bytes, i);
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
