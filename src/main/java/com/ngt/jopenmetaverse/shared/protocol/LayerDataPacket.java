package com.ngt.jopenmetaverse.shared.protocol;


    public final class LayerDataPacket extends Packet
    {
        /// <exclude/>
        public final class LayerIDBlock extends PacketBlock
        {
            public byte Type;

            @Override
			public int getLength()
            {
                get
                {
                    return 1;
                }
            }

            public LayerIDBlock() { }
            public LayerIDBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    Type = (byte)bytes[i++];
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i++] = Type;
            }

        }

        /// <exclude/>
        public final class LayerDataBlock extends PacketBlock
        {
            public byte[] Data;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 2;
                    if (Data != null) { length += Data.getLength(); }
                    return length;
                }
            }

            public LayerDataBlock() { }
            public LayerDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    length = (bytes[i++] + (bytes[i++] << 8));
                    Data = new byte[length];
                    Buffer.BlockCopy(bytes, i, Data, 0, length); i += length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i++] = (byte)(Data.length % 256);
                bytes[i++] = (byte)((Data.length >> 8) % 256);
                Buffer.BlockCopy(Data, 0, bytes, i, Data.getLength()); i += Data.getLength();
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 7;
                length += LayerID.length;
                length += LayerData.length;
                return length;
            }
        }
        public LayerIDBlock LayerID;
        public LayerDataBlock LayerData;

        public LayerDataPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.LayerData;
            this.header =  new Header();
            header.Frequency = PacketFrequency.High;
            header.ID = 11;
            header.Reliable = true;
            LayerID = new LayerIDBlock();
            LayerData = new LayerDataBlock();
        }

        public LayerDataPacket(byte[] bytes, int[] i) 
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
            LayerID.FromBytes(bytes, i);
            LayerData.FromBytes(bytes, i);
        }

        public LayerDataPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            LayerID.FromBytes(bytes, i);
            LayerData.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 7;
            length += LayerID.length;
            length += LayerData.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            LayerID.ToBytes(bytes, i);
            LayerData.ToBytes(bytes, i);
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
