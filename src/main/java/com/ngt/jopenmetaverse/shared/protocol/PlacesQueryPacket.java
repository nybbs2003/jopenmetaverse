package com.ngt.jopenmetaverse.shared.protocol;


    public final class PlacesQueryPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID SessionID;
            public UUID QueryID;

            @Override
			public int getLength()
            {
                                {
                    return 48;
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
                    SessionID.FromBytes(bytes, i[0]); i[0] += 16;
                    QueryID.FromBytes(bytes, i[0]); i[0] += 16;
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
                QueryID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        /// <exclude/>
        public final class TransactionDataBlock extends PacketBlock
        {
            public UUID TransactionID;

            @Override
			public int getLength()
            {
                                {
                    return 16;
                }
            }

            public TransactionDataBlock() { }
            public TransactionDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    TransactionID.FromBytes(bytes, i[0]); i[0] += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                TransactionID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        /// <exclude/>
        public final class QueryDataBlock extends PacketBlock
        {
            public byte[] QueryText;
            public uint QueryFlags;
            public sbyte Category;
            public byte[] SimName;

            @Override
			public int getLength()
            {
                                {
                    int length = 7;
                    if (QueryText != null) { length += QueryText.length; }
                    if (SimName != null) { length += SimName.length; }
                    return length;
                }
            }

            public QueryDataBlock() { }
            public QueryDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    length = bytes[i[0]++];
                    QueryText = new byte[length];
                    Utils.arraycopy(bytes, i[0], QueryText, 0, length); i[0] +=  length;
                    QueryFlags = (uint)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    Category = (sbyte)bytes[i[0]++];
                    length = bytes[i[0]++];
                    SimName = new byte[length];
                    Utils.arraycopy(bytes, i[0], SimName, 0, length); i[0] +=  length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = (byte)QueryText.length;
                Utils.arraycopy(QueryText, 0, bytes, i[0], QueryText.length); i[0] +=  QueryText.length;
                Utils.UIntToBytes(QueryFlags, bytes, i); i += 4;
                bytes[i[0]++] = (byte)Category;
                bytes[i[0]++] = (byte)SimName.length;
                Utils.arraycopy(SimName, 0, bytes, i[0], SimName.length); i[0] +=  SimName.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += AgentData.getLength();
                length += TransactionData.length;
                length += QueryData.length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public TransactionDataBlock TransactionData;
        public QueryDataBlock QueryData;

        public PlacesQueryPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.PlacesQuery;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 29;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            TransactionData = new TransactionDataBlock();
            QueryData = new QueryDataBlock();
        }

        public PlacesQueryPacket(byte[] bytes, int[] i) 
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
            TransactionData.FromBytes(bytes, i);
            QueryData.FromBytes(bytes, i);
        }

        public PlacesQueryPacket(Header head, byte[] bytes, int[] i)
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
            TransactionData.FromBytes(bytes, i);
            QueryData.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += TransactionData.length;
            length += QueryData.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            TransactionData.ToBytes(bytes, i);
            QueryData.ToBytes(bytes, i);
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
