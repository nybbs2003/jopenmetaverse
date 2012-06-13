package com.ngt.jopenmetaverse.shared.protocol;


    public final class LiveHelpGroupReplyPacket extends Packet
    {
        /// <exclude/>
        public final class ReplyDataBlock extends PacketBlock
        {
            public UUID RequestID;
            public UUID GroupID;
            public byte[] Selection;

            @Override
			public int getLength()
            {
                                {
                    int length = 33;
                    if (Selection != null) { length += Selection.length; }
                    return length;
                }
            }

            public ReplyDataBlock() { }
            public ReplyDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    RequestID.FromBytes(bytes, i[0]); i[0] += 16;
                    GroupID.FromBytes(bytes, i[0]); i[0] += 16;
                    length = bytes[i[0]++];
                    Selection = new byte[length];
                    Utils.arraycopy(bytes, i, Selection, 0, length); i[0] +=  length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                RequestID.ToBytes(bytes, i[0]); i[0] += 16;
                GroupID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)Selection.length;
                Utils.arraycopy(Selection, 0, bytes, i, Selection.length); i[0] +=  Selection.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += ReplyData.length;
                return length;
            }
        }
        public ReplyDataBlock ReplyData;

        public LiveHelpGroupReplyPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.LiveHelpGroupReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 380;
            header.Reliable = true;
            ReplyData = new ReplyDataBlock();
        }

        public LiveHelpGroupReplyPacket(byte[] bytes, int[] i) 
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
            ReplyData.FromBytes(bytes, i);
        }

        public LiveHelpGroupReplyPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            ReplyData.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += ReplyData.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            ReplyData.ToBytes(bytes, i);
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
