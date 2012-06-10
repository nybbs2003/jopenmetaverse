package com.ngt.jopenmetaverse.shared.protocol;


    public final class AssetUploadRequestPacket extends Packet
    {
        /// <exclude/>
        public final class AssetBlockBlock extends PacketBlock
        {
            public UUID TransactionID;
            public sbyte Type;
            public bool Tempfile;
            public bool StoreLocal;
            public byte[] AssetData;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 21;
                    if (AssetData != null) { length += AssetData.length; }
                    return length;
                }
            }

            public AssetBlockBlock() { }
            public AssetBlockBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    TransactionID.FromBytes(bytes, i); i += 16;
                    Type = (sbyte)bytes[i++];
                    Tempfile = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    StoreLocal = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    length = (bytes[i++] + (bytes[i++] << 8));
                    AssetData = new byte[length];
                    Buffer.BlockCopy(bytes, i, AssetData, 0, length); i += length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                TransactionID.ToBytes(bytes, i); i += 16;
                bytes[i++] = (byte)Type;
                bytes[i++] = (byte)((Tempfile) ? 1 : 0);
                bytes[i++] = (byte)((StoreLocal) ? 1 : 0);
                bytes[i++] = (byte)(AssetData.length % 256);
                bytes[i++] = (byte)((AssetData.length >> 8) % 256);
                Buffer.BlockCopy(AssetData, 0, bytes, i, AssetData.length); i += AssetData.length;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 10;
                length += AssetBlock.length;
                return length;
            }
        }
        public AssetBlockBlock AssetBlock;

        public AssetUploadRequestPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.AssetUploadRequest;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 333;
            header.Reliable = true;
            AssetBlock = new AssetBlockBlock();
        }

        public AssetUploadRequestPacket(byte[] bytes, int[] i) 
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
            AssetBlock.FromBytes(bytes, i);
        }

        public AssetUploadRequestPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            AssetBlock.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AssetBlock.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AssetBlock.ToBytes(bytes, i);
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
