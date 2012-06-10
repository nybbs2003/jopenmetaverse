package com.ngt.jopenmetaverse.shared.protocol;


    public final class RequestXferPacket extends Packet
    {
        /// <exclude/>
        public final class XferIDBlock extends PacketBlock
        {
            public ulong ID;
            public byte[] Filename;
            public byte FilePath;
            public bool DeleteOnCompletion;
            public bool UseBigPackets;
            public UUID VFileID;
            public short VFileType;

            @Override
			public int getLength()
            {
                get
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
                    ID = (ulong)((ulong)bytes[i++] + ((ulong)bytes[i++] << 8) + ((ulong)bytes[i++] << 16) + ((ulong)bytes[i++] << 24) + ((ulong)bytes[i++] << 32) + ((ulong)bytes[i++] << 40) + ((ulong)bytes[i++] << 48) + ((ulong)bytes[i++] << 56));
                    length = bytes[i++];
                    Filename = new byte[length];
                    Buffer.BlockCopy(bytes, i, Filename, 0, length); i += length;
                    FilePath = (byte)bytes[i++];
                    DeleteOnCompletion = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    UseBigPackets = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    VFileID.FromBytes(bytes, i); i += 16;
                    VFileType = (short)(bytes[i++] + (bytes[i++] << 8));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UInt64ToBytes(ID, bytes, i); i += 8;
                bytes[i++] = (byte)Filename.length;
                Buffer.BlockCopy(Filename, 0, bytes, i, Filename.length); i += Filename.length;
                bytes[i++] = FilePath;
                bytes[i++] = (byte)((DeleteOnCompletion) ? 1 : 0);
                bytes[i++] = (byte)((UseBigPackets) ? 1 : 0);
                VFileID.ToBytes(bytes, i); i += 16;
                bytes[i++] = (byte)(VFileType % 256);
                bytes[i++] = (byte)((VFileType >> 8) % 256);
            }

        }

        @Override
			public int getLength()
        {
            get
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
                packetEnd = Helpers.ZeroDecode(bytes, packetEnd + 1, zeroBuffer) - 1;
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
            int i = 0;
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
