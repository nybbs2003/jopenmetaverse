package com.ngt.jopenmetaverse.shared.protocol;


    public final class RequestXferPacket extends Packet
    {
        /// <exclude/>
        public final class XferIDBlock extends PacketBlock
        {
            public BigInteger ID;
            public byte[] Filename;
            public byte FilePath;
            public boolean DeleteOnCompletion;
            public boolean UseBigPackets;
            public UUID VFileID;
            public short VFileType;

            @Override
			public int getLength()
            {
                                {
                    int length = 30;
                    if (Filename != null) { length += Filename.length; }
                    return length;
                }
            }

            public XferIDBlock() { }
            public XferIDBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    ID = Utils.bytesToULong(bytes, i[0]); i[0] += 8;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    Filename = new byte[length];
                    Utils.arraycopy(bytes, i[0], Filename, 0, length); i[0] +=  length;
                    FilePath = (byte)bytes[i[0]++];
                    DeleteOnCompletion = (bytes[i[0]++] != 0) ? true : false;
                    UseBigPackets = (bytes[i[0]++] != 0) ? true : false;
                    VFileID.FromBytes(bytes, i[0]); i[0] += 16;
                    VFileType = (short)Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.ulongToBytes(ID, bytes, i[0]); i[0] += 8;
                bytes[i[0]++] = (byte)Filename.length;
                Utils.arraycopy(Filename, 0, bytes, i[0], Filename.length); i[0] +=  Filename.length;
                bytes[i[0]++] = FilePath;
                bytes[i[0]++] = (byte)((DeleteOnCompletion) ? 1 : 0);
                bytes[i[0]++] = (byte)((UseBigPackets) ? 1 : 0);
                VFileID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)(VFileType % 256);
                bytes[i[0]++] = (byte)((VFileType >> 8) % 256);
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += XferID.length;
                return length;
            }
        }
        public XferIDBlock XferID;

        public RequestXferPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.RequestXfer;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 156;
            header.Reliable = true;
            header.Zerocoded = true;
            XferID = new XferIDBlock();
        }

        public RequestXferPacket(byte[] bytes, int[] i) 
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
            XferID.FromBytes(bytes, i);
        }

        public RequestXferPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            XferID.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += XferID.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            XferID.ToBytes(bytes, i);
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
