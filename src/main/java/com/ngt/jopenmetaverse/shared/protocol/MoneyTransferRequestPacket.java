package com.ngt.jopenmetaverse.shared.protocol;


    public final class MoneyTransferRequestPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID SessionID;

            @Override
			public int getLength()
            {
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
                    AgentID.FromBytes(bytes, i[0]); i[0] += 16;
                    SessionID.FromBytes(bytes, i[0]); i[0] += 16;
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
            }

        }

        /// <exclude/>
        public final class MoneyDataBlock extends PacketBlock
        {
            public UUID SourceID;
            public UUID DestID;
            public byte Flags;
            public int Amount;
            public byte AggregatePermNextOwner;
            public byte AggregatePermInventory;
            public int TransactionType;
            public byte[] Description;

            @Override
			public int getLength()
            {
                                {
                    int length = 44;
                    if (Description != null) { length += Description.length; }
                    return length;
                }
            }

            public MoneyDataBlock() { }
            public MoneyDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    SourceID.FromBytes(bytes, i[0]); i[0] += 16;
                    DestID.FromBytes(bytes, i[0]); i[0] += 16;
                    Flags = (byte)bytes[i[0]++];
                    Amount = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    AggregatePermNextOwner = (byte)bytes[i[0]++];
                    AggregatePermInventory = (byte)bytes[i[0]++];
                    TransactionType = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    length = bytes[i[0]++];
                    Description = new byte[length];
                    Utils.arraycopy(bytes, i[0], Description, 0, length); i[0] +=  length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                SourceID.ToBytes(bytes, i[0]); i[0] += 16;
                DestID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = Flags;
                Utils.intToBytes(Amount, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = AggregatePermNextOwner;
                bytes[i[0]++] = AggregatePermInventory;
                Utils.intToBytes(TransactionType, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = (byte)Description.length;
                Utils.arraycopy(Description, 0, bytes, i[0], Description.length); i[0] +=  Description.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += AgentData.getLength();
                length += MoneyData.length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public MoneyDataBlock MoneyData;

        public MoneyTransferRequestPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.MoneyTransferRequest;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 311;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            MoneyData = new MoneyDataBlock();
        }

        public MoneyTransferRequestPacket(byte[] bytes, int[] i) 
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
            MoneyData.FromBytes(bytes, i);
        }

        public MoneyTransferRequestPacket(Header head, byte[] bytes, int[] i)
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
            MoneyData.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += MoneyData.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            MoneyData.ToBytes(bytes, i);
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
