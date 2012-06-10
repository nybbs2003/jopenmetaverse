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
        public final class TransactionDataBlock extends PacketBlock
        {
            public UUID TransactionID;
            public uint TotalNumItems;

            @Override
			public int getLength()
            {
                get
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
                    TransactionID.FromBytes(bytes, i); i += 16;
                    TotalNumItems = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                TransactionID.ToBytes(bytes, i); i += 16;
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
                get
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
                    VoteID.FromBytes(bytes, i); i += 16;
                    VoteInitiator.FromBytes(bytes, i); i += 16;
                    length = bytes[i++];
                    TerseDateID = new byte[length];
                    Buffer.BlockCopy(bytes, i, TerseDateID, 0, length); i += length;
                    length = bytes[i++];
                    StartDateTime = new byte[length];
                    Buffer.BlockCopy(bytes, i, StartDateTime, 0, length); i += length;
                    length = bytes[i++];
                    EndDateTime = new byte[length];
                    Buffer.BlockCopy(bytes, i, EndDateTime, 0, length); i += length;
                    AlreadyVoted = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    length = bytes[i++];
                    VoteCast = new byte[length];
                    Buffer.BlockCopy(bytes, i, VoteCast, 0, length); i += length;
                    Majority = Utils.BytesToFloat(bytes, i); i += 4;
                    Quorum = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    length = bytes[i++];
                    ProposalText = new byte[length];
                    Buffer.BlockCopy(bytes, i, ProposalText, 0, length); i += length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                VoteID.ToBytes(bytes, i); i += 16;
                VoteInitiator.ToBytes(bytes, i); i += 16;
                bytes[i++] = (byte)TerseDateID.length;
                Buffer.BlockCopy(TerseDateID, 0, bytes, i, TerseDateID.length); i += TerseDateID.length;
                bytes[i++] = (byte)StartDateTime.length;
                Buffer.BlockCopy(StartDateTime, 0, bytes, i, StartDateTime.length); i += StartDateTime.length;
                bytes[i++] = (byte)EndDateTime.length;
                Buffer.BlockCopy(EndDateTime, 0, bytes, i, EndDateTime.length); i += EndDateTime.length;
                bytes[i++] = (byte)((AlreadyVoted) ? 1 : 0);
                bytes[i++] = (byte)VoteCast.length;
                Buffer.BlockCopy(VoteCast, 0, bytes, i, VoteCast.length); i += VoteCast.length;
                Utils.FloatToBytes(Majority, bytes, i); i += 4;
                Utils.IntToBytes(Quorum, bytes, i); i += 4;
                bytes[i++] = (byte)ProposalText.length;
                Buffer.BlockCopy(ProposalText, 0, bytes, i, ProposalText.length); i += ProposalText.length;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 11;
                length += AgentData.getLength();
                length += TransactionData.length;
                for (int j = 0; j < ProposalData.length; j++)
                    length += ProposalData[j].length;
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
                packetEnd = Helpers.ZeroDecode(bytes, packetEnd + 1, zeroBuffer) - 1;
                bytes = zeroBuffer;
            }
            AgentData.FromBytes(bytes, i);
            TransactionData.FromBytes(bytes, i);
            int count = (int)bytes[i++];
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
            int count = (int)bytes[i++];
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
            for (int j = 0; j < ProposalData.length; j++) { length += ProposalData[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            TransactionData.ToBytes(bytes, i);
            bytes[i++] = (byte)ProposalData.length;
            for (int j = 0; j < ProposalData.length; j++) { ProposalData[j].ToBytes(bytes, i); }
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            System.Collections.Generic.List<byte[]> packets = new System.Collections.Generic.List<byte[]>();
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
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < ProposalData.length) {
                    int blockLength = ProposalData[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++ProposalDataCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Buffer.BlockCopy(fixedBytes, 0, packet, 0, length);
                if (packets.Count > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)ProposalDataCount;
                for (i = ProposalDataStart; i < ProposalDataStart + ProposalDataCount; i++) { ProposalData[i].ToBytes(packet, ref length); }
                ProposalDataStart += ProposalDataCount;

                if (acksLength > 0) {
                    Buffer.BlockCopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.Add(packet);
            } while (
                ProposalDataStart < ProposalData.length);

            return packets.ToArray();
        }
    }

    /// <exclude/>
