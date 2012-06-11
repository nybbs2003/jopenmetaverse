package com.ngt.jopenmetaverse.shared.protocol;


    public final class GetScriptRunningPacket extends Packet
    {
        /// <exclude/>
        public final class ScriptBlock extends PacketBlock
        {
            public UUID ObjectID;
            public UUID ItemID;

            @Override
			public int getLength()
            {
                get
                {
                    return 32;
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
                    ObjectID.FromBytes(bytes, i); i += 16;
                    ItemID.FromBytes(bytes, i); i += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ObjectID.ToBytes(bytes, i); i += 16;
                ItemID.ToBytes(bytes, i); i += 16;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 10;
                length += Script.length;
                return length;
            }
        }
        public ScriptBlock Script;

        public GetScriptRunningPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.GetScriptRunning;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 243;
            header.Reliable = true;
            Script = new ScriptBlock();
        }

        public GetScriptRunningPacket(byte[] bytes, int[] i) 
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
            Script.FromBytes(bytes, i);
        }

        public GetScriptRunningPacket(Header head, byte[] bytes, int[] i)
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
            int i = 0;
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