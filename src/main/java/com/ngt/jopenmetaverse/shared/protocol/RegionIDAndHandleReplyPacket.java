package com.ngt.jopenmetaverse.shared.protocol;


    public final class RegionIDAndHandleReplyPacket extends Packet
    {
        /// <exclude/>
        public final class ReplyBlockBlock extends PacketBlock
        {
            public UUID RegionID;
            public BigInteger RegionHandle;

            @Override
			public int getLength()
            {
                                {
                    return 24;
                }
            }

            public ReplyBlockBlock() { }
            public ReplyBlockBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    RegionID.FromBytes(bytes, i[0]); i[0] += 16;
                    RegionHandle = Utils.bytesToULong(bytes, i[0]); i[0] += 8;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                RegionID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.ulongToBytes(RegionHandle, bytes, i[0]); i[0] += 8;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += ReplyBlock.length;
                return length;
            }
        }
        public ReplyBlockBlock ReplyBlock;

        public RegionIDAndHandleReplyPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.RegionIDAndHandleReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 310;
            header.Reliable = true;
            ReplyBlock = new ReplyBlockBlock();
        }

        public RegionIDAndHandleReplyPacket(byte[] bytes, int[] i) throws MalformedDataException 
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(bytes, i, packetEnd, null);
        }

        @Override
		public void FromBytes(byte[] bytes, int[] i, int[] packetEnd, byte[] zeroBuffer) throws MalformedDataException
        {
            header.FromBytes(bytes, i, packetEnd);
            if (header.Zerocoded && zeroBuffer != null)
            {
                packetEnd[0] = Helpers.ZeroDecode(bytes, packetEnd[0] + 1, zeroBuffer) - 1;
                bytes = zeroBuffer;
            }
            ReplyBlock.FromBytes(bytes, i);
        }

        public RegionIDAndHandleReplyPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd) throws MalformedDataException
        {
            this.header =  header;
            ReplyBlock.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += ReplyBlock.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            ReplyBlock.ToBytes(bytes, i);
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
