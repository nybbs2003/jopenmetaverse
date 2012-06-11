package com.ngt.jopenmetaverse.shared.protocol;


    public final class ParcelOverlayPacket extends Packet
    {
        /// <exclude/>
        public final class ParcelDataBlock extends PacketBlock
        {
            public int SequenceID;
            public byte[] Data;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 6;
                    if (Data != null) { length += Data.getLength(); }
                    return length;
                }
            }

            public ParcelDataBlock() { }
            public ParcelDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    SequenceID = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
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
                Utils.IntToBytes(SequenceID, bytes, i); i += 4;
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
                int length = 10;
                length += ParcelData.length;
                return length;
            }
        }
        public ParcelDataBlock ParcelData;

        public ParcelOverlayPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.ParcelOverlay;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 196;
            header.Reliable = true;
            header.Zerocoded = true;
            ParcelData = new ParcelDataBlock();
        }

        public ParcelOverlayPacket(byte[] bytes, int[] i) 
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
            ParcelData.FromBytes(bytes, i);
        }

        public ParcelOverlayPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            ParcelData.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += ParcelData.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            ParcelData.ToBytes(bytes, i);
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