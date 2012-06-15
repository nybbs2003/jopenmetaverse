package com.ngt.jopenmetaverse.shared.protocol;


    public final class EventInfoReplyPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;

            @Override
			public int getLength()
            {
                                {
                    return 16;
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
                    AgentID.FromBytes(bytes, i[0]); i[0] += 16;
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
            }

        }

        /// <exclude/>
        public final class EventDataBlock extends PacketBlock
        {
            public long EventID;
            public byte[] Creator;
            public byte[] Name;
            public byte[] Category;
            public byte[] Desc;
            public byte[] Date;
            public long DateUTC;
            public long Duration;
            public long Cover;
            public long Amount;
            public byte[] SimName;
            public Vector3d GlobalPos;
            public long EventFlags;

            @Override
			public int getLength()
            {
                                {
                    int length = 55;
                    if (Creator != null) { length += Creator.length; }
                    if (Name != null) { length += Name.length; }
                    if (Category != null) { length += Category.length; }
                    if (Desc != null) { length += Desc.length; }
                    if (Date != null) { length += Date.length; }
                    if (SimName != null) { length += SimName.length; }
                    return length;
                }
            }

            public EventDataBlock() { }
            public EventDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    EventID = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    Creator = new byte[length];
                    Utils.arraycopy(bytes, i[0], Creator, 0, length); i[0] +=  length;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    Name = new byte[length];
                    Utils.arraycopy(bytes, i[0], Name, 0, length); i[0] +=  length;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    Category = new byte[length];
                    Utils.arraycopy(bytes, i[0], Category, 0, length); i[0] +=  length;
                    length = (bytes[i[0]++] + (bytes[i[0]++] << 8));
                    Desc = new byte[length];
                    Utils.arraycopy(bytes, i[0], Desc, 0, length); i[0] +=  length;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    Date = new byte[length];
                    Utils.arraycopy(bytes, i[0], Date, 0, length); i[0] +=  length;
                    DateUTC = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    Duration = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    Cover = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    Amount = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    SimName = new byte[length];
                    Utils.arraycopy(bytes, i[0], SimName, 0, length); i[0] +=  length;
                    GlobalPos.FromBytes(bytes, i[0]); i[0] += 24;
                    EventFlags = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.uintToBytes(EventID, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = (byte)Creator.length;
                Utils.arraycopy(Creator, 0, bytes, i[0], Creator.length); i[0] +=  Creator.length;
                bytes[i[0]++] = (byte)Name.length;
                Utils.arraycopy(Name, 0, bytes, i[0], Name.length); i[0] +=  Name.length;
                bytes[i[0]++] = (byte)Category.length;
                Utils.arraycopy(Category, 0, bytes, i[0], Category.length); i[0] +=  Category.length;
                bytes[i[0]++] = (byte)(Desc.length % 256);
                bytes[i[0]++] = (byte)((Desc.length >> 8) % 256);
                Utils.arraycopy(Desc, 0, bytes, i[0], Desc.length); i[0] +=  Desc.length;
                bytes[i[0]++] = (byte)Date.length;
                Utils.arraycopy(Date, 0, bytes, i[0], Date.length); i[0] +=  Date.length;
                Utils.uintToBytes(DateUTC, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(Duration, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(Cover, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(Amount, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = (byte)SimName.length;
                Utils.arraycopy(SimName, 0, bytes, i[0], SimName.length); i[0] +=  SimName.length;
                GlobalPos.ToBytes(bytes, i[0]); i[0] += 24;
                Utils.uintToBytes(EventFlags, bytes, i[0]); i[0] += 4;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += AgentData.getLength();
                length += EventData.length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public EventDataBlock EventData;

        public EventInfoReplyPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.EventInfoReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 180;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            EventData = new EventDataBlock();
        }

        public EventInfoReplyPacket(byte[] bytes, int[] i) 
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
            EventData.FromBytes(bytes, i);
        }

        public EventInfoReplyPacket(Header head, byte[] bytes, int[] i)
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
            EventData.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += EventData.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            EventData.ToBytes(bytes, i);
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
