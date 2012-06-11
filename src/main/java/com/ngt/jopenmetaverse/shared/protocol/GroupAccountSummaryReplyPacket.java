package com.ngt.jopenmetaverse.shared.protocol;


    public final class GroupAccountSummaryReplyPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID GroupID;

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
                    GroupID.FromBytes(bytes, i); i += 16;
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
                GroupID.ToBytes(bytes, i); i += 16;
            }

        }

        /// <exclude/>
        public final class MoneyDataBlock extends PacketBlock
        {
            public UUID RequestID;
            public int IntervalDays;
            public int CurrentInterval;
            public byte[] StartDate;
            public int Balance;
            public int TotalCredits;
            public int TotalDebits;
            public int ObjectTaxCurrent;
            public int LightTaxCurrent;
            public int LandTaxCurrent;
            public int GroupTaxCurrent;
            public int ParcelDirFeeCurrent;
            public int ObjectTaxEstimate;
            public int LightTaxEstimate;
            public int LandTaxEstimate;
            public int GroupTaxEstimate;
            public int ParcelDirFeeEstimate;
            public int NonExemptMembers;
            public byte[] LastTaxDate;
            public byte[] TaxDate;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 83;
                    if (StartDate != null) { length += StartDate.length; }
                    if (LastTaxDate != null) { length += LastTaxDate.length; }
                    if (TaxDate != null) { length += TaxDate.length; }
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
                    RequestID.FromBytes(bytes, i); i += 16;
                    IntervalDays = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    CurrentInterval = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    length = bytes[i++];
                    StartDate = new byte[length];
                    Buffer.BlockCopy(bytes, i, StartDate, 0, length); i += length;
                    Balance = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    TotalCredits = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    TotalDebits = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    ObjectTaxCurrent = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    LightTaxCurrent = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    LandTaxCurrent = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    GroupTaxCurrent = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    ParcelDirFeeCurrent = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    ObjectTaxEstimate = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    LightTaxEstimate = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    LandTaxEstimate = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    GroupTaxEstimate = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    ParcelDirFeeEstimate = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    NonExemptMembers = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    length = bytes[i++];
                    LastTaxDate = new byte[length];
                    Buffer.BlockCopy(bytes, i, LastTaxDate, 0, length); i += length;
                    length = bytes[i++];
                    TaxDate = new byte[length];
                    Buffer.BlockCopy(bytes, i, TaxDate, 0, length); i += length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                RequestID.ToBytes(bytes, i); i += 16;
                Utils.IntToBytes(IntervalDays, bytes, i); i += 4;
                Utils.IntToBytes(CurrentInterval, bytes, i); i += 4;
                bytes[i++] = (byte)StartDate.length;
                Buffer.BlockCopy(StartDate, 0, bytes, i, StartDate.length); i += StartDate.length;
                Utils.IntToBytes(Balance, bytes, i); i += 4;
                Utils.IntToBytes(TotalCredits, bytes, i); i += 4;
                Utils.IntToBytes(TotalDebits, bytes, i); i += 4;
                Utils.IntToBytes(ObjectTaxCurrent, bytes, i); i += 4;
                Utils.IntToBytes(LightTaxCurrent, bytes, i); i += 4;
                Utils.IntToBytes(LandTaxCurrent, bytes, i); i += 4;
                Utils.IntToBytes(GroupTaxCurrent, bytes, i); i += 4;
                Utils.IntToBytes(ParcelDirFeeCurrent, bytes, i); i += 4;
                Utils.IntToBytes(ObjectTaxEstimate, bytes, i); i += 4;
                Utils.IntToBytes(LightTaxEstimate, bytes, i); i += 4;
                Utils.IntToBytes(LandTaxEstimate, bytes, i); i += 4;
                Utils.IntToBytes(GroupTaxEstimate, bytes, i); i += 4;
                Utils.IntToBytes(ParcelDirFeeEstimate, bytes, i); i += 4;
                Utils.IntToBytes(NonExemptMembers, bytes, i); i += 4;
                bytes[i++] = (byte)LastTaxDate.length;
                Buffer.BlockCopy(LastTaxDate, 0, bytes, i, LastTaxDate.length); i += LastTaxDate.length;
                bytes[i++] = (byte)TaxDate.length;
                Buffer.BlockCopy(TaxDate, 0, bytes, i, TaxDate.length); i += TaxDate.length;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 10;
                length += AgentData.getLength();
                length += MoneyData.length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public MoneyDataBlock MoneyData;

        public GroupAccountSummaryReplyPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.GroupAccountSummaryReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 354;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            MoneyData = new MoneyDataBlock();
        }

        public GroupAccountSummaryReplyPacket(byte[] bytes, int[] i) 
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
            MoneyData.FromBytes(bytes, i);
        }

        public GroupAccountSummaryReplyPacket(Header head, byte[] bytes, int[] i)
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
            int i = 0;
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