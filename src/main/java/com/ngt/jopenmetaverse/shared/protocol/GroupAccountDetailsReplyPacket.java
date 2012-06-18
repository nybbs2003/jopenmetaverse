package com.ngt.jopenmetaverse.shared.protocol;

import java.util.ArrayList;
import java.util.List;

import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;

    public final class GroupAccountDetailsReplyPacket extends Packet
    {
        /// <exclude/>
        public static final class AgentDataBlock extends PacketBlock
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
            public AgentDataBlock(byte[] bytes, int[] i) throws MalformedDataException
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
        public static final class MoneyDataBlock extends PacketBlock
        {
            public UUID RequestID;
            public int IntervalDays;
            public int CurrentInterval;
		/** Unsigned Byte */ 
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
            public MoneyDataBlock(byte[] bytes, int[] i) throws MalformedDataException
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
                    IntervalDays = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    CurrentInterval = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    StartDate = new byte[length];
                    Utils.arraycopy(bytes, i[0], StartDate, 0, length); i[0] +=  length;
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
            }

        }

        /// <exclude/>
        public static final class HistoryDataBlock extends PacketBlock
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
            public HistoryDataBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    Description = new byte[length];
                    Utils.arraycopy(bytes, i[0], Description, 0, length); i[0] +=  length;
                    Amount = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = (byte)Description.length;
                Utils.arraycopy(Description, 0, bytes, i[0], Description.length); i[0] +=  Description.length;
                Utils.intToBytes(Amount, bytes, i[0]); i[0] += 4;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += AgentData.getLength();
                length += MoneyData.getLength();
                for (int j = 0; j < HistoryData.length; j++)
                    length += HistoryData[j].getLength();
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

        public GroupAccountDetailsReplyPacket(byte[] bytes, int[] i) throws MalformedDataException 
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(bytes, i, packetEnd, null);
        }

        @Override
		public void FromBytes(byte[] bytes, int[] i, int[] packetEnd, byte[] zeroBuffer) throws MalformedDataException
        {
            header.FromBytes(bytes, i, packetEnd);
            if (header.Zerocoded && zeroBuffer != null)
            {
                packetEnd[0] = Helpers.ZeroDecode(bytes, packetEnd[0] + 1, zeroBuffer) - 1;
                bytes = zeroBuffer;
            }
            AgentData.FromBytes(bytes, i);
            MoneyData.FromBytes(bytes, i);
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(HistoryData == null || HistoryData.length != -1) {
                HistoryData = new HistoryDataBlock[count];
                for(int j = 0; j < count; j++)
                { HistoryData[j] = new HistoryDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { HistoryData[j].FromBytes(bytes, i); }
        }

        public GroupAccountDetailsReplyPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd) throws MalformedDataException
        {
            this.header =  header;
            AgentData.FromBytes(bytes, i);
            MoneyData.FromBytes(bytes, i);
            int count = Utils.ubyteToInt(bytes[i[0]++]);
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
            length += MoneyData.getLength();
            length++;
            for (int j = 0; j < HistoryData.length; j++) { length += HistoryData[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            MoneyData.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)HistoryData.length;
            for (int j = 0; j < HistoryData.length; j++) { HistoryData[j].ToBytes(bytes, i); }
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            List<byte[]> packets = new ArrayList<byte[]>();
            int[] i = new int[]{0};
            int fixedLength = 10;

            byte[] ackBytes = null;
            int[] acksLength = new int[]{0};
            if (header.AckList != null && header.AckList.length > 0) {
                header.AppendedAcks = true;
                ackBytes = new byte[header.AckList.length * 4 + 1];
                header.AcksToBytes(ackBytes, acksLength);
            }

            fixedLength += AgentData.getLength();
            fixedLength += MoneyData.getLength();
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

              i[0] =HistoryDataStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < HistoryData.length) {
                    int blockLength = HistoryData[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++HistoryDataCount;
                    }
                    else { break; }
                    i[0]++;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)HistoryDataCount;
                for (i[0] = HistoryDataStart; i[0] < HistoryDataStart + HistoryDataCount; i[0]++) { HistoryData[i[0]].ToBytes(packet, length); }
                HistoryDataStart += HistoryDataCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                HistoryDataStart < HistoryData.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
