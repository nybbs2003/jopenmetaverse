package com.ngt.jopenmetaverse.shared.protocol;


    public final class LoadURLPacket extends Packet
    {
        /// <exclude/>
        public final class DataBlock extends PacketBlock
        {
            public byte[] ObjectName;
            public UUID ObjectID;
            public UUID OwnerID;
            public bool OwnerIsGroup;
            public byte[] Message;
            public byte[] URL;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 36;
                    if (ObjectName != null) { length += ObjectName.length; }
                    if (Message != null) { length += Message.length; }
                    if (URL != null) { length += URL.length; }
                    return length;
                }
            }

            public DataBlock() { }
            public DataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    length = bytes[i++];
                    ObjectName = new byte[length];
                    Buffer.BlockCopy(bytes, i, ObjectName, 0, length); i += length;
                    ObjectID.FromBytes(bytes, i); i += 16;
                    OwnerID.FromBytes(bytes, i); i += 16;
                    OwnerIsGroup = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    length = bytes[i++];
                    Message = new byte[length];
                    Buffer.BlockCopy(bytes, i, Message, 0, length); i += length;
                    length = bytes[i++];
                    URL = new byte[length];
                    Buffer.BlockCopy(bytes, i, URL, 0, length); i += length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i++] = (byte)ObjectName.length;
                Buffer.BlockCopy(ObjectName, 0, bytes, i, ObjectName.length); i += ObjectName.length;
                ObjectID.ToBytes(bytes, i); i += 16;
                OwnerID.ToBytes(bytes, i); i += 16;
                bytes[i++] = (byte)((OwnerIsGroup) ? 1 : 0);
                bytes[i++] = (byte)Message.length;
                Buffer.BlockCopy(Message, 0, bytes, i, Message.length); i += Message.length;
                bytes[i++] = (byte)URL.length;
                Buffer.BlockCopy(URL, 0, bytes, i, URL.length); i += URL.length;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 10;
                length += Data.getLength();
                return length;
            }
        }
        public DataBlock Data;

        public LoadURLPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.LoadURL;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 194;
            header.Reliable = true;
            Data = new DataBlock();
        }

        public LoadURLPacket(byte[] bytes, int[] i) 
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
            Data.FromBytes(bytes, i);
        }

        public LoadURLPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            Data.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += Data.getLength();
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            Data.ToBytes(bytes, i);
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
