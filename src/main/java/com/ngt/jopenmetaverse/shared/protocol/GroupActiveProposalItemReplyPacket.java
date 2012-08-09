package com.ngt.jopenmetaverse.shared.protocol;

import java.util.ArrayList;
import java.util.List;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;
	public final class GroupActiveProposalItemReplyPacket extends Packet
    {
        /// <exclude/>
        public static final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID = new UUID();
            public UUID GroupID = new UUID();

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
                    throw new MalformedDataException(Utils.getExceptionStackTraceAsString(e));
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
        public static final class TransactionDataBlock extends PacketBlock
        {
            public UUID TransactionID = new UUID();
            public long TotalNumItems;

            @Override
			public int getLength()
            {
                                {
                    return 20;
                }
            }

            public TransactionDataBlock() { }
            public TransactionDataBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    TransactionID.FromBytes(bytes, i[0]); i[0] += 16;
                    TotalNumItems = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException(Utils.getExceptionStackTraceAsString(e));
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                TransactionID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.uintToBytes(TotalNumItems, bytes, i[0]); i[0] += 4;
            }

        }

        /// <exclude/>
        public static final class ProposalDataBlock extends PacketBlock
        {
            public UUID VoteID = new UUID();
            public UUID VoteInitiator = new UUID();
		/** Unsigned Byte */ 
		public byte[] TerseDateID;
		/** Unsigned Byte */ 
		public byte[] StartDateTime;
		/** Unsigned Byte */ 
		public byte[] EndDateTime;
            public boolean AlreadyVoted;
		/** Unsigned Byte */ 
		public byte[] VoteCast;
            public float Majority;
            public int Quorum;
		/** Unsigned Byte */ 
		public byte[] ProposalText;

            @Override
			public int getLength()
            {
                                {
                    int length = 46;
                    if (TerseDateID != null) { length += TerseDateID.length; }
                    if (StartDateTime != null) { length += StartDateTime.length; }
                    if (EndDateTime != null) { length += EndDateTime.length; }
                    if (VoteCast != null) { length += VoteCast.length; }
                    if (ProposalText != null) { length += ProposalText.length; }
                    return length;
                }
            }

            public ProposalDataBlock() { }
            public ProposalDataBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    VoteID.FromBytes(bytes, i[0]); i[0] += 16;
                    VoteInitiator.FromBytes(bytes, i[0]); i[0] += 16;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    TerseDateID = new byte[length];
                    Utils.arraycopy(bytes, i[0], TerseDateID, 0, length); i[0] +=  length;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    StartDateTime = new byte[length];
                    Utils.arraycopy(bytes, i[0], StartDateTime, 0, length); i[0] +=  length;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    EndDateTime = new byte[length];
                    Utils.arraycopy(bytes, i[0], EndDateTime, 0, length); i[0] +=  length;
                    AlreadyVoted = (bytes[i[0]++] != 0) ? true : false;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    VoteCast = new byte[length];
                    Utils.arraycopy(bytes, i[0], VoteCast, 0, length); i[0] +=  length;
                    Majority = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    Quorum = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    ProposalText = new byte[length];
                    Utils.arraycopy(bytes, i[0], ProposalText, 0, length); i[0] +=  length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException(Utils.getExceptionStackTraceAsString(e));
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                VoteID.ToBytes(bytes, i[0]); i[0] += 16;
                VoteInitiator.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)TerseDateID.length;
                Utils.arraycopy(TerseDateID, 0, bytes, i[0], TerseDateID.length); i[0] +=  TerseDateID.length;
                bytes[i[0]++] = (byte)StartDateTime.length;
                Utils.arraycopy(StartDateTime, 0, bytes, i[0], StartDateTime.length); i[0] +=  StartDateTime.length;
                bytes[i[0]++] = (byte)EndDateTime.length;
                Utils.arraycopy(EndDateTime, 0, bytes, i[0], EndDateTime.length); i[0] +=  EndDateTime.length;
                bytes[i[0]++] = (byte)((AlreadyVoted) ? 1 : 0);
                bytes[i[0]++] = (byte)VoteCast.length;
                Utils.arraycopy(VoteCast, 0, bytes, i[0], VoteCast.length); i[0] +=  VoteCast.length;
                Utils.floatToBytes(Majority, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(Quorum, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = (byte)ProposalText.length;
                Utils.arraycopy(ProposalText, 0, bytes, i[0], ProposalText.length); i[0] +=  ProposalText.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += AgentData.getLength();
                length += TransactionData.getLength();
                for (int j = 0; j < ProposalData.length; j++)
                    length += ProposalData[j].getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public TransactionDataBlock TransactionData;
        public ProposalDataBlock[] ProposalData;

        public GroupActiveProposalItemReplyPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.GroupActiveProposalItemReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 360;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            TransactionData = new TransactionDataBlock();
            ProposalData = null;
        }

        public GroupActiveProposalItemReplyPacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            TransactionData.FromBytes(bytes, i);
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(ProposalData == null || ProposalData.length != -1) {
                ProposalData = new ProposalDataBlock[count];
                for(int j = 0; j < count; j++)
                { ProposalData[j] = new ProposalDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ProposalData[j].FromBytes(bytes, i); }
        }

        public GroupActiveProposalItemReplyPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
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
            TransactionData.FromBytes(bytes, i);
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(ProposalData == null || ProposalData.length != count) {
                ProposalData = new ProposalDataBlock[count];
                for(int j = 0; j < count; j++)
                { ProposalData[j] = new ProposalDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ProposalData[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += TransactionData.getLength();
            length++;
            for (int j = 0; j < ProposalData.length; j++) { length += ProposalData[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            TransactionData.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)ProposalData.length;
            for (int j = 0; j < ProposalData.length; j++) { ProposalData[j].ToBytes(bytes, i); }
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
            fixedLength += TransactionData.getLength();
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AgentData.ToBytes(fixedBytes, i);
            TransactionData.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int ProposalDataStart = 0;
            do
            {
                int variableLength = 0;
                int ProposalDataCount = 0;

              i[0] =ProposalDataStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < ProposalData.length) {
                    int blockLength = ProposalData[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++ProposalDataCount;
                    }
                    else { break; }
                    i[0]++;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)ProposalDataCount;
                for (i[0] = ProposalDataStart; i[0] < ProposalDataStart + ProposalDataCount; i[0]++) { ProposalData[i[0]].ToBytes(packet, length); }
                ProposalDataStart += ProposalDataCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                ProposalDataStart < ProposalData.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
