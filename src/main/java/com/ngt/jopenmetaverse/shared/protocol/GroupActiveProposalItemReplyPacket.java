package com.ngt.jopenmetaverse.shared.protocol;


    public final class GroupActiveProposalItemReplyPacket extends Packet
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
        public final class TransactionDataBlock extends PacketBlock
        {
            public UUID TransactionID;
            public uint TotalNumItems;

            @Override
			public int getLength()
            {
                                {
                    return 20;
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
                    TotalNumItems = (uint)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
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
                Utils.UIntToBytes(TotalNumItems, bytes, i); i += 4;
            }

        }

        /// <exclude/>
        public final class ProposalDataBlock extends PacketBlock
        {
            public UUID VoteID;
            public UUID VoteInitiator;
            public byte[] TerseDateID;
            public byte[] StartDateTime;
            public byte[] EndDateTime;
            public bool AlreadyVoted;
            public byte[] VoteCast;
            public float Majority;
            public int Quorum;
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
            public ProposalDataBlock(byte[] bytes, int[] i)
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
                    length = bytes[i[0]++];
                    TerseDateID = new byte[length];
                    Utils.arraycopy(bytes, i, TerseDateID, 0, length); i[0] +=  length;
                    length = bytes[i[0]++];
                    StartDateTime = new byte[length];
                    Utils.arraycopy(bytes, i, StartDateTime, 0, length); i[0] +=  length;
                    length = bytes[i[0]++];
                    EndDateTime = new byte[length];
                    Utils.arraycopy(bytes, i, EndDateTime, 0, length); i[0] +=  length;
                    AlreadyVoted = (bytes[i[0]++] != 0) ? (bool)true : (bool)false;
                    length = bytes[i[0]++];
                    VoteCast = new byte[length];
                    Utils.arraycopy(bytes, i, VoteCast, 0, length); i[0] +=  length;
                    Majority = Utils.BytesToFloat(bytes, i); i += 4;
                    Quorum = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    length = bytes[i[0]++];
                    ProposalText = new byte[length];
                    Utils.arraycopy(bytes, i, ProposalText, 0, length); i[0] +=  length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                VoteID.ToBytes(bytes, i[0]); i[0] += 16;
                VoteInitiator.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)TerseDateID.length;
                Utils.arraycopy(TerseDateID, 0, bytes, i, TerseDateID.length); i[0] +=  TerseDateID.length;
                bytes[i[0]++] = (byte)StartDateTime.length;
                Utils.arraycopy(StartDateTime, 0, bytes, i, StartDateTime.length); i[0] +=  StartDateTime.length;
                bytes[i[0]++] = (byte)EndDateTime.length;
                Utils.arraycopy(EndDateTime, 0, bytes, i, EndDateTime.length); i[0] +=  EndDateTime.length;
                bytes[i[0]++] = (byte)((AlreadyVoted) ? 1 : 0);
                bytes[i[0]++] = (byte)VoteCast.length;
                Utils.arraycopy(VoteCast, 0, bytes, i, VoteCast.length); i[0] +=  VoteCast.length;
                Utils.FloatToBytes(Majority, bytes, i); i += 4;
                Utils.IntToBytes(Quorum, bytes, i); i += 4;
                bytes[i[0]++] = (byte)ProposalText.length;
                Utils.arraycopy(ProposalText, 0, bytes, i, ProposalText.length); i[0] +=  ProposalText.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += AgentData.getLength();
                length += TransactionData.length;
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

        public GroupActiveProposalItemReplyPacket(byte[] bytes, int[] i) 
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
            int count = (int)bytes[i[0]++];
            if(ProposalData == null || ProposalData.length != -1) {
                ProposalData = new ProposalDataBlock[count];
                for(int j = 0; j < count; j++)
                { ProposalData[j] = new ProposalDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ProposalData[j].FromBytes(bytes, i); }
        }

        public GroupActiveProposalItemReplyPacket(Header head, byte[] bytes, int[] i)
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
            int count = (int)bytes[i[0]++];
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
            length += TransactionData.length;
            length++;
            for (int j = 0; j < ProposalData.length; j++) { length += ProposalData[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
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
            int i = 0;
            int fixedLength = 10;

            byte[] ackBytes = null;
            int[] acksLength = new int[]{0};
            if (header.AckList != null && header.AckList.length > 0) {
                header.AppendedAcks = true;
                ackBytes = new byte[header.AckList.length * 4 + 1];
                header.AcksToBytes(ackBytes, acksLength);
            }

            fixedLength += AgentData.getLength();
            fixedLength += TransactionData.length;
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

                i = ProposalDataStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i < ProposalData.length) {
                    int blockLength = ProposalData[i].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++ProposalDataCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)ProposalDataCount;
                for (i = ProposalDataStart; i < ProposalDataStart + ProposalDataCount; i++) { ProposalData[i].ToBytes(packet, length); }
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
