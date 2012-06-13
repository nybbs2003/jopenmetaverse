package com.ngt.jopenmetaverse.shared.protocol;


    public final class ScriptRunningReplyPacket extends Packet
    {
        /// <exclude/>
        public final class ScriptBlock extends PacketBlock
        {
            public UUID ObjectID;
            public UUID ItemID;
            public bool Running;

            @Override
			public int getLength()
            {
                                {
                    return 33;
                }
            }

            public ScriptBlock() { }
            public ScriptBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ObjectID.FromBytes(bytes, i[0]); i[0] += 16;
                    ItemID.FromBytes(bytes, i[0]); i[0] += 16;
                    Running = (bytes[i[0]++] != 0) ? (bool)true : (bool)false;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ObjectID.ToBytes(bytes, i[0]); i[0] += 16;
                ItemID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)((Running) ? 1 : 0);
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += Script.length;
                return length;
            }
        }
        public ScriptBlock Script;

        public ScriptRunningReplyPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.ScriptRunningReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 244;
            header.Reliable = true;
            Script = new ScriptBlock();
        }

        public ScriptRunningReplyPacket(byte[] bytes, int[] i) 
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
            Script.FromBytes(bytes, i);
        }

        public ScriptRunningReplyPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            Script.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += Script.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            Script.ToBytes(bytes, i);
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
