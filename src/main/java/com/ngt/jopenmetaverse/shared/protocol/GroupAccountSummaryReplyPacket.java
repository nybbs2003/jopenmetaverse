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
                    GroupID.FromBytes(bytes, i[0]); i[0] += 16;
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
                GroupID.ToBytes(bytes, i[0]); i[0] += 16;
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
                    RequestID.FromBytes(bytes, i[0]); i[0] += 16;
                    IntervalDays = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    CurrentInterval = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    length = bytes[i[0]++];
                    StartDate = new byte[length];
                    Utils.arraycopy(bytes, i[0], StartDate, 0, length); i[0] +=  length;
                    Balance = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    TotalCredits = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    TotalDebits = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    ObjectTaxCurrent = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    LightTaxCurrent = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    LandTaxCurrent = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    GroupTaxCurrent = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    ParcelDirFeeCurrent = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    ObjectTaxEstimate = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    LightTaxEstimate = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    LandTaxEstimate = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    GroupTaxEstimate = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    ParcelDirFeeEstimate = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    NonExemptMembers = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    length = bytes[i[0]++];
                    LastTaxDate = new byte[length];
                    Utils.arraycopy(bytes, i[0], LastTaxDate, 0, length); i[0] +=  length;
                    length = bytes[i[0]++];
                    TaxDate = new byte[length];
                    Utils.arraycopy(bytes, i[0], TaxDate, 0, length); i[0] +=  length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                RequestID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.intToBytes(IntervalDays, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(CurrentInterval, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = (byte)StartDate.length;
                Utils.arraycopy(StartDate, 0, bytes, i[0], StartDate.length); i[0] +=  StartDate.length;
                Utils.intToBytes(Balance, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(TotalCredits, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(TotalDebits, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(ObjectTaxCurrent, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(LightTaxCurrent, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(LandTaxCurrent, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(GroupTaxCurrent, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(ParcelDirFeeCurrent, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(ObjectTaxEstimate, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(LightTaxEstimate, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(LandTaxEstimate, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(GroupTaxEstimate, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(ParcelDirFeeEstimate, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(NonExemptMembers, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = (byte)LastTaxDate.length;
                Utils.arraycopy(LastTaxDate, 0, bytes, i[0], LastTaxDate.length); i[0] +=  LastTaxDate.length;
                bytes[i[0]++] = (byte)TaxDate.length;
                Utils.arraycopy(TaxDate, 0, bytes, i[0], TaxDate.length); i[0] +=  TaxDate.length;
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
                packetEnd[0] = Helpers.ZeroDecode(bytes, packetEnd[0] + 1, zeroBuffer) - 1;
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
