package com.ngt.jopenmetaverse.shared.protocol;


    public final class GroupAccountDetailsReplyPacket extends Packet
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
                AgentID.ToBytes(bytes, i[0]); i[0] += 16;
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

            @Override
			public int getLength()
            {
                                {
                    int length = 25;
                    if (StartDate != null) { length += StartDate.length; }
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
                    Utils.arraycopy(bytes, i, StartDate, 0, length); i += length;
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
                Utils.arraycopy(StartDate, 0, bytes, i, StartDate.length); i += StartDate.length;
            }

        }

        /// <exclude/>
        public final class HistoryDataBlock extends PacketBlock
        {
            public byte[] Description;
            public int Amount;

            @Override
			public int getLength()
            {
                                {
                    int length = 5;
                    if (Description != null) { length += Description.length; }
                    return length;
                }
            }

            public HistoryDataBlock() { }
            public HistoryDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    length = bytes[i++];
                    Description = new byte[length];
                    Utils.arraycopy(bytes, i, Description, 0, length); i += length;
                    Amount = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i++] = (byte)Description.length;
                Utils.arraycopy(Description, 0, bytes, i, Description.length); i += Description.length;
                Utils.IntToBytes(Amount, bytes, i); i += 4;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += AgentData.getLength();
                length += MoneyData.length;
                for (int j = 0; j < HistoryData.length; j++)
                    length += HistoryData[j].length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public MoneyDataBlock MoneyData;
        public HistoryDataBlock[] HistoryData;

        public GroupAccountDetailsReplyPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.GroupAccountDetailsReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 356;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            MoneyData = new MoneyDataBlock();
            HistoryData = null;
        }

        public GroupAccountDetailsReplyPacket(byte[] bytes, int[] i) 
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
            int count = (int)bytes[i++];
            if(HistoryData == null || HistoryData.length != -1) {
                HistoryData = new HistoryDataBlock[count];
                for(int j = 0; j < count; j++)
                { HistoryData[j] = new HistoryDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { HistoryData[j].FromBytes(bytes, i); }
        }

        public GroupAccountDetailsReplyPacket(Header head, byte[] bytes, int[] i)
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
            int count = (int)bytes[i++];
            if(HistoryData == null || HistoryData.length != count) {
                HistoryData = new HistoryDataBlock[count];
                for(int j = 0; j < count; j++)
                { HistoryData[j] = new HistoryDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { HistoryData[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += MoneyData.length;
            length++;
            for (int j = 0; j < HistoryData.length; j++) { length += HistoryData[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            MoneyData.ToBytes(bytes, i);
            bytes[i++] = (byte)HistoryData.length;
            for (int j = 0; j < HistoryData.length; j++) { HistoryData[j].ToBytes(bytes, i); }
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            List<byte[]> packets = new ArrayList<byte[]>();
            int i = 0;
            int fixedLength = 10;

            byte[] ackBytes = null;
            int acksLength = 0;
            if (header.AckList != null && header.AckList.length > 0) {
                header.AppendedAcks = true;
                ackBytes = new byte[header.AckList.length * 4 + 1];
                header.AcksToBytes(ackBytes, ref acksLength);
            }

            fixedLength += AgentData.getLength();
            fixedLength += MoneyData.length;
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AgentData.ToBytes(fixedBytes, i);
            MoneyData.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int HistoryDataStart = 0;
            do
            {
                int variableLength = 0;
                int HistoryDataCount = 0;

                i = HistoryDataStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < HistoryData.length) {
                    int blockLength = HistoryData[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++HistoryDataCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Utils.arraycopy(fixedBytes, 0, packet, 0, length);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)HistoryDataCount;
                for (i = HistoryDataStart; i < HistoryDataStart + HistoryDataCount; i++) { HistoryData[i].ToBytes(packet, ref length); }
                HistoryDataStart += HistoryDataCount;

                if (acksLength > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.add(packet);
            } while (
                HistoryDataStart < HistoryData.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
