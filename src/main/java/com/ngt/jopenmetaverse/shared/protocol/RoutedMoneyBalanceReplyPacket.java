package com.ngt.jopenmetaverse.shared.protocol;


    public final class RoutedMoneyBalanceReplyPacket extends Packet
    {
        /// <exclude/>
        public final class TargetBlockBlock extends PacketBlock
        {
            public uint TargetIP;
            public ushort TargetPort;

            @Override
			public int getLength()
            {
                                {
                    return 6;
                }
            }

            public TargetBlockBlock() { }
            public TargetBlockBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    TargetIP = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    TargetPort = (ushort)((bytes[i++] << 8) + bytes[i++]);
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UIntToBytes(TargetIP, bytes, i); i += 4;
                bytes[i++] = (byte)((TargetPort >> 8) % 256);
                bytes[i++] = (byte)(TargetPort % 256);
            }

        }

        /// <exclude/>
        public final class MoneyDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID TransactionID;
            public bool TransactionSuccess;
            public int MoneyBalance;
            public int SquareMetersCredit;
            public int SquareMetersCommitted;
            public byte[] Description;

            @Override
			public int getLength()
            {
                                {
                    int length = 46;
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
                    AgentID.FromBytes(bytes, i); i += 16;
                    TransactionID.FromBytes(bytes, i); i += 16;
                    TransactionSuccess = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    MoneyBalance = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    SquareMetersCredit = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    SquareMetersCommitted = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    length = bytes[i++];
                    Description = new byte[length];
                    Utils.arraycopy(bytes, i, Description, 0, length); i += length;
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
                TransactionID.ToBytes(bytes, i); i += 16;
                bytes[i++] = (byte)((TransactionSuccess) ? 1 : 0);
                Utils.IntToBytes(MoneyBalance, bytes, i); i += 4;
                Utils.IntToBytes(SquareMetersCredit, bytes, i); i += 4;
                Utils.IntToBytes(SquareMetersCommitted, bytes, i); i += 4;
                bytes[i++] = (byte)Description.length;
                Utils.arraycopy(Description, 0, bytes, i, Description.length); i += Description.length;
            }

        }

        /// <exclude/>
        public final class TransactionInfoBlock extends PacketBlock
        {
            public int TransactionType;
            public UUID SourceID;
            public bool IsSourceGroup;
            public UUID DestID;
            public bool IsDestGroup;
            public int Amount;
            public byte[] ItemDescription;

            @Override
			public int getLength()
            {
                                {
                    int length = 43;
                    if (ItemDescription != null) { length += ItemDescription.length; }
                    return length;
                }
            }

            public TransactionInfoBlock() { }
            public TransactionInfoBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    TransactionType = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    SourceID.FromBytes(bytes, i); i += 16;
                    IsSourceGroup = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    DestID.FromBytes(bytes, i); i += 16;
                    IsDestGroup = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    Amount = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    length = bytes[i++];
                    ItemDescription = new byte[length];
                    Utils.arraycopy(bytes, i, ItemDescription, 0, length); i += length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.IntToBytes(TransactionType, bytes, i); i += 4;
                SourceID.ToBytes(bytes, i); i += 16;
                bytes[i++] = (byte)((IsSourceGroup) ? 1 : 0);
                DestID.ToBytes(bytes, i); i += 16;
                bytes[i++] = (byte)((IsDestGroup) ? 1 : 0);
                Utils.IntToBytes(Amount, bytes, i); i += 4;
                bytes[i++] = (byte)ItemDescription.length;
                Utils.arraycopy(ItemDescription, 0, bytes, i, ItemDescription.length); i += ItemDescription.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += TargetBlock.length;
                length += MoneyData.length;
                length += TransactionInfo.length;
                return length;
            }
        }
        public TargetBlockBlock TargetBlock;
        public MoneyDataBlock MoneyData;
        public TransactionInfoBlock TransactionInfo;

        public RoutedMoneyBalanceReplyPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.RoutedMoneyBalanceReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 315;
            header.Reliable = true;
            header.Zerocoded = true;
            TargetBlock = new TargetBlockBlock();
            MoneyData = new MoneyDataBlock();
            TransactionInfo = new TransactionInfoBlock();
        }

        public RoutedMoneyBalanceReplyPacket(byte[] bytes, int[] i) 
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
            TargetBlock.FromBytes(bytes, i);
            MoneyData.FromBytes(bytes, i);
            TransactionInfo.FromBytes(bytes, i);
        }

        public RoutedMoneyBalanceReplyPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            TargetBlock.FromBytes(bytes, i);
            MoneyData.FromBytes(bytes, i);
            TransactionInfo.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += TargetBlock.length;
            length += MoneyData.length;
            length += TransactionInfo.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            TargetBlock.ToBytes(bytes, i);
            MoneyData.ToBytes(bytes, i);
            TransactionInfo.ToBytes(bytes, i);
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
