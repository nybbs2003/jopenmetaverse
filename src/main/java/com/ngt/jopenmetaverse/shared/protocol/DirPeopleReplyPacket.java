package com.ngt.jopenmetaverse.shared.protocol;


    public final class DirPeopleReplyPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;

            @Override
			public int getLength()
            {
                get
                {
                    return 16;
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
            }

        }

        /// <exclude/>
        public final class QueryDataBlock extends PacketBlock
        {
            public UUID QueryID;

            @Override
			public int getLength()
            {
                get
                {
                    return 16;
                }
            }

            public QueryDataBlock() { }
            public QueryDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    QueryID.FromBytes(bytes, i); i += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                QueryID.ToBytes(bytes, i); i += 16;
            }

        }

        /// <exclude/>
        public final class QueryRepliesBlock extends PacketBlock
        {
            public UUID AgentID;
            public byte[] FirstName;
            public byte[] LastName;
            public byte[] Group;
            public bool Online;
            public int Reputation;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 24;
                    if (FirstName != null) { length += FirstName.length; }
                    if (LastName != null) { length += LastName.length; }
                    if (Group != null) { length += Group.length; }
                    return length;
                }
            }

            public QueryRepliesBlock() { }
            public QueryRepliesBlock(byte[] bytes, int[] i)
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
                    length = bytes[i++];
                    FirstName = new byte[length];
                    Buffer.BlockCopy(bytes, i, FirstName, 0, length); i += length;
                    length = bytes[i++];
                    LastName = new byte[length];
                    Buffer.BlockCopy(bytes, i, LastName, 0, length); i += length;
                    length = bytes[i++];
                    Group = new byte[length];
                    Buffer.BlockCopy(bytes, i, Group, 0, length); i += length;
                    Online = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    Reputation = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
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
                bytes[i++] = (byte)FirstName.length;
                Buffer.BlockCopy(FirstName, 0, bytes, i, FirstName.length); i += FirstName.length;
                bytes[i++] = (byte)LastName.length;
                Buffer.BlockCopy(LastName, 0, bytes, i, LastName.length); i += LastName.length;
                bytes[i++] = (byte)Group.length;
                Buffer.BlockCopy(Group, 0, bytes, i, Group.length); i += Group.length;
                bytes[i++] = (byte)((Online) ? 1 : 0);
                Utils.IntToBytes(Reputation, bytes, i); i += 4;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 11;
                length += AgentData.getLength();
                length += QueryData.length;
                for (int j = 0; j < QueryReplies.length; j++)
                    length += QueryReplies[j].length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public QueryDataBlock QueryData;
        public QueryRepliesBlock[] QueryReplies;

        public DirPeopleReplyPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.DirPeopleReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 36;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            QueryData = new QueryDataBlock();
            QueryReplies = null;
        }

        public DirPeopleReplyPacket(byte[] bytes, int[] i) 
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
            QueryData.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(QueryReplies == null || QueryReplies.length != -1) {
                QueryReplies = new QueryRepliesBlock[count];
                for(int j = 0; j < count; j++)
                { QueryReplies[j] = new QueryRepliesBlock(); }
            }
            for (int j = 0; j < count; j++)
            { QueryReplies[j].FromBytes(bytes, i); }
        }

        public DirPeopleReplyPacket(Header head, byte[] bytes, int[] i)
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
            QueryData.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(QueryReplies == null || QueryReplies.length != count) {
                QueryReplies = new QueryRepliesBlock[count];
                for(int j = 0; j < count; j++)
                { QueryReplies[j] = new QueryRepliesBlock(); }
            }
            for (int j = 0; j < count; j++)
            { QueryReplies[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += QueryData.length;
            length++;
            for (int j = 0; j < QueryReplies.length; j++) { length += QueryReplies[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            QueryData.ToBytes(bytes, i);
            bytes[i++] = (byte)QueryReplies.length;
            for (int j = 0; j < QueryReplies.length; j++) { QueryReplies[j].ToBytes(bytes, i); }
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
            fixedLength += QueryData.length;
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AgentData.ToBytes(fixedBytes, i);
            QueryData.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int QueryRepliesStart = 0;
            do
            {
                int variableLength = 0;
                int QueryRepliesCount = 0;

                i = QueryRepliesStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < QueryReplies.length) {
                    int blockLength = QueryReplies[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++QueryRepliesCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Buffer.BlockCopy(fixedBytes, 0, packet, 0, length);
                if (packets.Count > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)QueryRepliesCount;
                for (i = QueryRepliesStart; i < QueryRepliesStart + QueryRepliesCount; i++) { QueryReplies[i].ToBytes(packet, ref length); }
                QueryRepliesStart += QueryRepliesCount;

                if (acksLength > 0) {
                    Buffer.BlockCopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.Add(packet);
            } while (
                QueryRepliesStart < QueryReplies.length);

            return packets.ToArray();
        }
    }

    /// <exclude/>
