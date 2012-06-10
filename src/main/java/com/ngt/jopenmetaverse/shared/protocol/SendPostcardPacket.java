package com.ngt.jopenmetaverse.shared.protocol;


    public final class SendPostcardPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID SessionID;
            public UUID AssetID;
            public Vector3d PosGlobal;
            public byte[] To;
            public byte[] From;
            public byte[] Name;
            public byte[] Subject;
            public byte[] Msg;
            public bool AllowPublish;
            public bool MaturePublish;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 80;
                    if (To != null) { length += To.length; }
                    if (From != null) { length += From.length; }
                    if (Name != null) { length += Name.length; }
                    if (Subject != null) { length += Subject.length; }
                    if (Msg != null) { length += Msg.length; }
                    return length;
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
                int length;
                try
                {
                    AgentID.FromBytes(bytes, i); i += 16;
                    SessionID.FromBytes(bytes, i); i += 16;
                    AssetID.FromBytes(bytes, i); i += 16;
                    PosGlobal.FromBytes(bytes, i); i += 24;
                    length = bytes[i++];
                    To = new byte[length];
                    Buffer.BlockCopy(bytes, i, To, 0, length); i += length;
                    length = bytes[i++];
                    From = new byte[length];
                    Buffer.BlockCopy(bytes, i, From, 0, length); i += length;
                    length = bytes[i++];
                    Name = new byte[length];
                    Buffer.BlockCopy(bytes, i, Name, 0, length); i += length;
                    length = bytes[i++];
                    Subject = new byte[length];
                    Buffer.BlockCopy(bytes, i, Subject, 0, length); i += length;
                    length = (bytes[i++] + (bytes[i++] << 8));
                    Msg = new byte[length];
                    Buffer.BlockCopy(bytes, i, Msg, 0, length); i += length;
                    AllowPublish = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    MaturePublish = (bytes[i++] != 0) ? (bool)true : (bool)false;
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
                AssetID.ToBytes(bytes, i); i += 16;
                PosGlobal.ToBytes(bytes, i); i += 24;
                bytes[i++] = (byte)To.length;
                Buffer.BlockCopy(To, 0, bytes, i, To.length); i += To.length;
                bytes[i++] = (byte)From.length;
                Buffer.BlockCopy(From, 0, bytes, i, From.length); i += From.length;
                bytes[i++] = (byte)Name.length;
                Buffer.BlockCopy(Name, 0, bytes, i, Name.length); i += Name.length;
                bytes[i++] = (byte)Subject.length;
                Buffer.BlockCopy(Subject, 0, bytes, i, Subject.length); i += Subject.length;
                bytes[i++] = (byte)(Msg.length % 256);
                bytes[i++] = (byte)((Msg.length >> 8) % 256);
                Buffer.BlockCopy(Msg, 0, bytes, i, Msg.length); i += Msg.length;
                bytes[i++] = (byte)((AllowPublish) ? 1 : 0);
                bytes[i++] = (byte)((MaturePublish) ? 1 : 0);
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 10;
                length += AgentData.getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;

        public SendPostcardPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.SendPostcard;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 412;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
        }

        public SendPostcardPacket(byte[] bytes, int[] i) 
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
        }

        public SendPostcardPacket(Header head, byte[] bytes, int[] i)
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
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
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
