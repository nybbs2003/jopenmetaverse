package com.ngt.jopenmetaverse.shared.protocol;


    public final class CoarseLocationUpdatePacket extends Packet
    {
        /// <exclude/>
        public final class LocationBlock extends PacketBlock
        {
            public byte X;
            public byte Y;
            public byte Z;

            @Override
			public int getLength()
            {
                                {
                    return 3;
                }
            }

            public LocationBlock() { }
            public LocationBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    X = (byte)bytes[i[0]++];
                    Y = (byte)bytes[i[0]++];
                    Z = (byte)bytes[i[0]++];
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = X;
                bytes[i[0]++] = Y;
                bytes[i[0]++] = Z;
            }

        }

        /// <exclude/>
        public final class IndexBlock extends PacketBlock
        {
            public short You;
            public short Prey;

            @Override
			public int getLength()
            {
                                {
                    return 4;
                }
            }

            public IndexBlock() { }
            public IndexBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    You = (short)(bytes[i[0]++] + (bytes[i[0]++] << 8));
                    Prey = (short)(bytes[i[0]++] + (bytes[i[0]++] << 8));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = (byte)(You % 256);
                bytes[i[0]++] = (byte)((You >> 8) % 256);
                bytes[i[0]++] = (byte)(Prey % 256);
                bytes[i[0]++] = (byte)((Prey >> 8) % 256);
            }

        }

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

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                for (int j = 0; j < Location.length; j++)
                    length += Location[j].getLength();
                length += Index.length;
                for (int j = 0; j < AgentData.getLength(); j++)
                    length += AgentData[j].getLength();
                return length;
            }
        }
        public LocationBlock[] Location;
        public IndexBlock Index;
        public AgentDataBlock[] AgentData;

        public CoarseLocationUpdatePacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.CoarseLocationUpdate;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Medium;
            header.ID = 6;
            header.Reliable = true;
            Location = null;
            Index = new IndexBlock();
            AgentData = null;
        }

        public CoarseLocationUpdatePacket(byte[] bytes, int[] i) 
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
            int count = (int)bytes[i[0]++];
            if(Location == null || Location.length != -1) {
                Location = new LocationBlock[count];
                for(int j = 0; j < count; j++)
                { Location[j] = new LocationBlock(); }
            }
            for (int j = 0; j < count; j++)
            { Location[j].FromBytes(bytes, i); }
            Index.FromBytes(bytes, i);
            count = (int)bytes[i[0]++];
            if(AgentData == null || AgentData.getLength() != -1) {
                AgentData = new AgentDataBlock[count];
                for(int j = 0; j < count; j++)
                { AgentData[j] = new AgentDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { AgentData[j].FromBytes(bytes, i); }
        }

        public CoarseLocationUpdatePacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            int count = (int)bytes[i[0]++];
            if(Location == null || Location.length != count) {
                Location = new LocationBlock[count];
                for(int j = 0; j < count; j++)
                { Location[j] = new LocationBlock(); }
            }
            for (int j = 0; j < count; j++)
            { Location[j].FromBytes(bytes, i); }
            Index.FromBytes(bytes, i);
            count = (int)bytes[i[0]++];
            if(AgentData == null || AgentData.getLength() != count) {
                AgentData = new AgentDataBlock[count];
                for(int j = 0; j < count; j++)
                { AgentData[j] = new AgentDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { AgentData[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 8;
            length += Index.length;
            length++;
            for (int j = 0; j < Location.length; j++) { length += Location[j].getLength(); }
            length++;
            for (int j = 0; j < AgentData.getLength(); j++) { length += AgentData[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)Location.length;
            for (int j = 0; j < Location.length; j++) { Location[j].ToBytes(bytes, i); }
            Index.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)AgentData.getLength();
            for (int j = 0; j < AgentData.getLength(); j++) { AgentData[j].ToBytes(bytes, i); }
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
