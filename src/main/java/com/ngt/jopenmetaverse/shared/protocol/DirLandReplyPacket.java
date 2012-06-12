package com.ngt.jopenmetaverse.shared.protocol;


    public final class DirLandReplyPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;

            @Override
			public int getLength()
            {
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
                AgentID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        /// <exclude/>
        public final class QueryDataBlock extends PacketBlock
        {
            public UUID QueryID;

            @Override
			public int getLength()
            {
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
            public UUID ParcelID;
            public byte[] Name;
            public bool Auction;
            public bool ForSale;
            public int SalePrice;
            public int ActualArea;

            @Override
			public int getLength()
            {
                                {
                    int length = 27;
                    if (Name != null) { length += Name.length; }
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
                    ParcelID.FromBytes(bytes, i); i += 16;
                    length = bytes[i++];
                    Name = new byte[length];
                    Utils.arraycopy(bytes, i, Name, 0, length); i += length;
                    Auction = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    ForSale = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    SalePrice = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    ActualArea = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ParcelID.ToBytes(bytes, i); i += 16;
                bytes[i++] = (byte)Name.length;
                Utils.arraycopy(Name, 0, bytes, i, Name.length); i += Name.length;
                bytes[i++] = (byte)((Auction) ? 1 : 0);
                bytes[i++] = (byte)((ForSale) ? 1 : 0);
                Utils.IntToBytes(SalePrice, bytes, i); i += 4;
                Utils.IntToBytes(ActualArea, bytes, i); i += 4;
            }

        }

        @Override
			public int getLength()
        {
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

        public DirLandReplyPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.DirLandReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 50;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            QueryData = new QueryDataBlock();
            QueryReplies = null;
        }

        public DirLandReplyPacket(byte[] bytes, int[] i) 
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

        public DirLandReplyPacket(Header head, byte[] bytes, int[] i)
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
                Utils.arraycopy(fixedBytes, 0, packet, 0, length);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)QueryRepliesCount;
                for (i = QueryRepliesStart; i < QueryRepliesStart + QueryRepliesCount; i++) { QueryReplies[i].ToBytes(packet, ref length); }
                QueryRepliesStart += QueryRepliesCount;

                if (acksLength > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.add(packet);
            } while (
                QueryRepliesStart < QueryReplies.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
