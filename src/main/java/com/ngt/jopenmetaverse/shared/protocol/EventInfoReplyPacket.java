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
                    AgentID.FromBytes(bytes, i); i += 16;
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
            public uint EventID;
            public byte[] Creator;
            public byte[] Name;
            public byte[] Category;
            public byte[] Desc;
            public byte[] Date;
            public uint DateUTC;
            public uint Duration;
            public uint Cover;
            public uint Amount;
            public byte[] SimName;
            public Vector3d GlobalPos;
            public uint EventFlags;

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
                    EventID = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    length = bytes[i++];
                    Creator = new byte[length];
                    Utils.arraycopy(bytes, i, Creator, 0, length); i += length;
                    length = bytes[i++];
                    Name = new byte[length];
                    Utils.arraycopy(bytes, i, Name, 0, length); i += length;
                    length = bytes[i++];
                    Category = new byte[length];
                    Utils.arraycopy(bytes, i, Category, 0, length); i += length;
                    length = (bytes[i++] + (bytes[i++] << 8));
                    Desc = new byte[length];
                    Utils.arraycopy(bytes, i, Desc, 0, length); i += length;
                    length = bytes[i++];
                    Date = new byte[length];
                    Utils.arraycopy(bytes, i, Date, 0, length); i += length;
                    DateUTC = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Duration = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Cover = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Amount = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    length = bytes[i++];
                    SimName = new byte[length];
                    Utils.arraycopy(bytes, i, SimName, 0, length); i += length;
                    GlobalPos.FromBytes(bytes, i); i += 24;
                    EventFlags = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UIntToBytes(EventID, bytes, i); i += 4;
                bytes[i++] = (byte)Creator.length;
                Utils.arraycopy(Creator, 0, bytes, i, Creator.length); i += Creator.length;
                bytes[i++] = (byte)Name.length;
                Utils.arraycopy(Name, 0, bytes, i, Name.length); i += Name.length;
                bytes[i++] = (byte)Category.length;
                Utils.arraycopy(Category, 0, bytes, i, Category.length); i += Category.length;
                bytes[i++] = (byte)(Desc.length % 256);
                bytes[i++] = (byte)((Desc.length >> 8) % 256);
                Utils.arraycopy(Desc, 0, bytes, i, Desc.length); i += Desc.length;
                bytes[i++] = (byte)Date.length;
                Utils.arraycopy(Date, 0, bytes, i, Date.length); i += Date.length;
                Utils.UIntToBytes(DateUTC, bytes, i); i += 4;
                Utils.UIntToBytes(Duration, bytes, i); i += 4;
                Utils.UIntToBytes(Cover, bytes, i); i += 4;
                Utils.UIntToBytes(Amount, bytes, i); i += 4;
                bytes[i++] = (byte)SimName.length;
                Utils.arraycopy(SimName, 0, bytes, i, SimName.length); i += SimName.length;
                GlobalPos.ToBytes(bytes, i); i += 24;
                Utils.UIntToBytes(EventFlags, bytes, i); i += 4;
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
            int i = 0;
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
