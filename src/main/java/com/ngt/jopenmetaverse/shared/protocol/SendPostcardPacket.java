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
            public boolean AllowPublish;
            public boolean MaturePublish;

            @Override
			public int getLength()
            {
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
                    AgentID.FromBytes(bytes, i[0]); i[0] += 16;
                    SessionID.FromBytes(bytes, i[0]); i[0] += 16;
                    AssetID.FromBytes(bytes, i[0]); i[0] += 16;
                    PosGlobal.FromBytes(bytes, i[0]); i[0] += 24;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    To = new byte[length];
                    Utils.arraycopy(bytes, i[0], To, 0, length); i[0] +=  length;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    From = new byte[length];
                    Utils.arraycopy(bytes, i[0], From, 0, length); i[0] +=  length;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    Name = new byte[length];
                    Utils.arraycopy(bytes, i[0], Name, 0, length); i[0] +=  length;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    Subject = new byte[length];
                    Utils.arraycopy(bytes, i[0], Subject, 0, length); i[0] +=  length;
                    length = (bytes[i[0]++] + (bytes[i[0]++] << 8));
                    Msg = new byte[length];
                    Utils.arraycopy(bytes, i[0], Msg, 0, length); i[0] +=  length;
                    AllowPublish = (bytes[i[0]++] != 0) ? true : false;
                    MaturePublish = (bytes[i[0]++] != 0) ? true : false;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                AgentID.ToBytes(bytes, i[0]); i[0] += 16;
                SessionID.ToBytes(bytes, i[0]); i[0] += 16;
                AssetID.ToBytes(bytes, i[0]); i[0] += 16;
                PosGlobal.ToBytes(bytes, i[0]); i[0] += 24;
                bytes[i[0]++] = (byte)To.length;
                Utils.arraycopy(To, 0, bytes, i[0], To.length); i[0] +=  To.length;
                bytes[i[0]++] = (byte)From.length;
                Utils.arraycopy(From, 0, bytes, i[0], From.length); i[0] +=  From.length;
                bytes[i[0]++] = (byte)Name.length;
                Utils.arraycopy(Name, 0, bytes, i[0], Name.length); i[0] +=  Name.length;
                bytes[i[0]++] = (byte)Subject.length;
                Utils.arraycopy(Subject, 0, bytes, i[0], Subject.length); i[0] +=  Subject.length;
                bytes[i[0]++] = (byte)(Msg.length % 256);
                bytes[i[0]++] = (byte)((Msg.length >> 8) % 256);
                Utils.arraycopy(Msg, 0, bytes, i[0], Msg.length); i[0] +=  Msg.length;
                bytes[i[0]++] = (byte)((AllowPublish) ? 1 : 0);
                bytes[i[0]++] = (byte)((MaturePublish) ? 1 : 0);
            }

        }

        @Override
			public int getLength()
        {
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
                packetEnd[0] = Helpers.ZeroDecode(bytes, packetEnd[0] + 1, zeroBuffer) - 1;
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
            int[] i = new int[]{0};
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
