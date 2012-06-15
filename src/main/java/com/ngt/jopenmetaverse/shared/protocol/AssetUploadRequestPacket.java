package com.ngt.jopenmetaverse.shared.protocol;


    public final class AssetUploadRequestPacket extends Packet
    {
        /// <exclude/>
        public final class AssetBlockBlock extends PacketBlock
        {
            public UUID TransactionID;
            public sbyte Type;
            public boolean Tempfile;
            public boolean StoreLocal;
            public byte[] AssetData;

            @Override
			public int getLength()
            {
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
                    TransactionID.FromBytes(bytes, i[0]); i[0] += 16;
                    Type = (sbyte)bytes[i[0]++];
                    Tempfile = (bytes[i[0]++] != 0) ? true : false;
                    StoreLocal = (bytes[i[0]++] != 0) ? true : false;
                    length = Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    AssetData = new byte[length];
                    Utils.arraycopy(bytes, i[0], AssetData, 0, length); i[0] +=  length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                TransactionID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)Type;
                bytes[i[0]++] = (byte)((Tempfile) ? 1 : 0);
                bytes[i[0]++] = (byte)((StoreLocal) ? 1 : 0);
                bytes[i[0]++] = (byte)(AssetData.length % 256);
                bytes[i[0]++] = (byte)((AssetData.length >> 8) % 256);
                Utils.arraycopy(AssetData, 0, bytes, i[0], AssetData.length); i[0] +=  AssetData.length;
            }

        }

        @Override
			public int getLength()
        {
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
                packetEnd[0] = Helpers.ZeroDecode(bytes, packetEnd[0] + 1, zeroBuffer) - 1;
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
            int[] i = new int[]{0};
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
