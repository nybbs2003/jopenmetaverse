package com.ngt.jopenmetaverse.shared.protocol;


    public final class GroupVoteHistoryItemReplyPacket extends Packet
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
        public final class HistoryItemDataBlock extends PacketBlock
        {
            public UUID VoteID;
            public byte[] TerseDateID;
            public byte[] StartDateTime;
            public byte[] EndDateTime;
            public UUID VoteInitiator;
            public byte[] VoteType;
            public byte[] VoteResult;
            public float Majority;
            public int Quorum;
            public byte[] ProposalText;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 47;
                    if (TerseDateID != null) { length += TerseDateID.length; }
                    if (StartDateTime != null) { length += StartDateTime.length; }
                    if (EndDateTime != null) { length += EndDateTime.length; }
                    if (VoteType != null) { length += VoteType.length; }
                    if (VoteResult != null) { length += VoteResult.length; }
                    if (ProposalText != null) { length += ProposalText.length; }
                    return length;
                }
            }

            public HistoryItemDataBlock() { }
            public HistoryItemDataBlock(byte[] bytes, int[] i)
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
                    length = bytes[i++];
                    TerseDateID = new byte[length];
                    Buffer.BlockCopy(bytes, i, TerseDateID, 0, length); i += length;
                    length = bytes[i++];
                    StartDateTime = new byte[length];
                    Buffer.BlockCopy(bytes, i, StartDateTime, 0, length); i += length;
                    length = bytes[i++];
                    EndDateTime = new byte[length];
                    Buffer.BlockCopy(bytes, i, EndDateTime, 0, length); i += length;
                    VoteInitiator.FromBytes(bytes, i); i += 16;
                    length = bytes[i++];
                    VoteType = new byte[length];
                    Buffer.BlockCopy(bytes, i, VoteType, 0, length); i += length;
                    length = bytes[i++];
                    VoteResult = new byte[length];
                    Buffer.BlockCopy(bytes, i, VoteResult, 0, length); i += length;
                    Majority = Utils.BytesToFloat(bytes, i); i += 4;
                    Quorum = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    length = (bytes[i++] + (bytes[i++] << 8));
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
                bytes[i++] = (byte)TerseDateID.length;
                Buffer.BlockCopy(TerseDateID, 0, bytes, i, TerseDateID.length); i += TerseDateID.length;
                bytes[i++] = (byte)StartDateTime.length;
                Buffer.BlockCopy(StartDateTime, 0, bytes, i, StartDateTime.length); i += StartDateTime.length;
                bytes[i++] = (byte)EndDateTime.length;
                Buffer.BlockCopy(EndDateTime, 0, bytes, i, EndDateTime.length); i += EndDateTime.length;
                VoteInitiator.ToBytes(bytes, i); i += 16;
                bytes[i++] = (byte)VoteType.length;
                Buffer.BlockCopy(VoteType, 0, bytes, i, VoteType.length); i += VoteType.length;
                bytes[i++] = (byte)VoteResult.length;
                Buffer.BlockCopy(VoteResult, 0, bytes, i, VoteResult.length); i += VoteResult.length;
                Utils.FloatToBytes(Majority, bytes, i); i += 4;
                Utils.IntToBytes(Quorum, bytes, i); i += 4;
                bytes[i++] = (byte)(ProposalText.length % 256);
                bytes[i++] = (byte)((ProposalText.length >> 8) % 256);
                Buffer.BlockCopy(ProposalText, 0, bytes, i, ProposalText.length); i += ProposalText.length;
            }

        }

        /// <exclude/>
        public final class VoteItemBlock extends PacketBlock
        {
            public UUID CandidateID;
            public byte[] VoteCast;
            public int NumVotes;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 21;
                    if (VoteCast != null) { length += VoteCast.length; }
                    return length;
                }
            }

            public VoteItemBlock() { }
            public VoteItemBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    CandidateID.FromBytes(bytes, i); i += 16;
                    length = bytes[i++];
                    VoteCast = new byte[length];
                    Buffer.BlockCopy(bytes, i, VoteCast, 0, length); i += length;
                    NumVotes = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                CandidateID.ToBytes(bytes, i); i += 16;
                bytes[i++] = (byte)VoteCast.length;
                Buffer.BlockCopy(VoteCast, 0, bytes, i, VoteCast.length); i += VoteCast.length;
                Utils.IntToBytes(NumVotes, bytes, i); i += 4;
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
                length += HistoryItemData.length;
                for (int j = 0; j < VoteItem.length; j++)
                    length += VoteItem[j].length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public TransactionDataBlock TransactionData;
        public HistoryItemDataBlock HistoryItemData;
        public VoteItemBlock[] VoteItem;

        public GroupVoteHistoryItemReplyPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.GroupVoteHistoryItemReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 362;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            TransactionData = new TransactionDataBlock();
            HistoryItemData = new HistoryItemDataBlock();
            VoteItem = null;
        }

        public GroupVoteHistoryItemReplyPacket(byte[] bytes, int[] i) 
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
            HistoryItemData.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(VoteItem == null || VoteItem.length != -1) {
                VoteItem = new VoteItemBlock[count];
                for(int j = 0; j < count; j++)
                { VoteItem[j] = new VoteItemBlock(); }
            }
            for (int j = 0; j < count; j++)
            { VoteItem[j].FromBytes(bytes, i); }
        }

        public GroupVoteHistoryItemReplyPacket(Header head, byte[] bytes, int[] i)
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
            HistoryItemData.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(VoteItem == null || VoteItem.length != count) {
                VoteItem = new VoteItemBlock[count];
                for(int j = 0; j < count; j++)
                { VoteItem[j] = new VoteItemBlock(); }
            }
            for (int j = 0; j < count; j++)
            { VoteItem[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += TransactionData.length;
            length += HistoryItemData.length;
            length++;
            for (int j = 0; j < VoteItem.length; j++) { length += VoteItem[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            TransactionData.ToBytes(bytes, i);
            HistoryItemData.ToBytes(bytes, i);
            bytes[i++] = (byte)VoteItem.length;
            for (int j = 0; j < VoteItem.length; j++) { VoteItem[j].ToBytes(bytes, i); }
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
            fixedLength += HistoryItemData.length;
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AgentData.ToBytes(fixedBytes, i);
            TransactionData.ToBytes(fixedBytes, i);
            HistoryItemData.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int VoteItemStart = 0;
            do
            {
                int variableLength = 0;
                int VoteItemCount = 0;

                i = VoteItemStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < VoteItem.length) {
                    int blockLength = VoteItem[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++VoteItemCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Buffer.BlockCopy(fixedBytes, 0, packet, 0, length);
                if (packets.Count > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)VoteItemCount;
                for (i = VoteItemStart; i < VoteItemStart + VoteItemCount; i++) { VoteItem[i].ToBytes(packet, ref length); }
                VoteItemStart += VoteItemCount;

                if (acksLength > 0) {
                    Buffer.BlockCopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.Add(packet);
            } while (
                VoteItemStart < VoteItem.length);

            return packets.ToArray();
        }
    }

    /// <exclude/>
