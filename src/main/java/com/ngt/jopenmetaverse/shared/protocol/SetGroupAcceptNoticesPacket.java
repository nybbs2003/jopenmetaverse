package com.ngt.jopenmetaverse.shared.protocol;


    public final class SetGroupAcceptNoticesPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID SessionID;

            @Override
			public int getLength()
            {
                get
                {
                    return 32;
                }
            }

            public AgentDataBlock() { }
            public AgentDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    AgentID.FromBytes(bytes, i); i += 16;
                    SessionID.FromBytes(bytes, i); i += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                AgentID.ToBytes(bytes, i); i += 16;
                SessionID.ToBytes(bytes, i); i += 16;
            }

        }

        /// <exclude/>
        public final class DataBlock extends PacketBlock
        {
            public UUID GroupID;
            public bool AcceptNotices;

            @Override
			public int getLength()
            {
                get
                {
                    return 17;
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
                try
                {
                    GroupID.FromBytes(bytes, i); i += 16;
                    AcceptNotices = (bytes[i++] != 0) ? (bool)true : (bool)false;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                GroupID.ToBytes(bytes, i); i += 16;
                bytes[i++] = (byte)((AcceptNotices) ? 1 : 0);
            }

        }

        /// <exclude/>
        public final class NewDataBlock extends PacketBlock
        {
            public bool ListInProfile;

            @Override
			public int getLength()
            {
                get
                {
                    return 1;
                }
            }

            public NewDataBlock() { }
            public NewDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ListInProfile = (bytes[i++] != 0) ? (bool)true : (bool)false;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i++] = (byte)((ListInProfile) ? 1 : 0);
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 10;
                length += AgentData.getLength();
                length += Data.getLength();
                length += NewData.length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public DataBlock Data;
        public NewDataBlock NewData;

        public SetGroupAcceptNoticesPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.SetGroupAcceptNotices;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 370;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            Data = new DataBlock();
            NewData = new NewDataBlock();
        }

        public SetGroupAcceptNoticesPacket(byte[] bytes, int[] i) 
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
            AgentData.FromBytes(bytes, i);
            Data.FromBytes(bytes, i);
            NewData.FromBytes(bytes, i);
        }

        public SetGroupAcceptNoticesPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            AgentData.FromBytes(bytes, i);
            Data.FromBytes(bytes, i);
            NewData.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += Data.getLength();
            length += NewData.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            Data.ToBytes(bytes, i);
            NewData.ToBytes(bytes, i);
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
