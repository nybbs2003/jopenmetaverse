package com.ngt.jopenmetaverse.shared.protocol;


    public final class DirPlacesReplyPacket extends Packet
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
            public UUID ParcelID;
            public byte[] Name;
            public bool ForSale;
            public bool Auction;
            public float Dwell;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 23;
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
                    Buffer.BlockCopy(bytes, i, Name, 0, length); i += length;
                    ForSale = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    Auction = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    Dwell = Utils.BytesToFloat(bytes, i); i += 4;
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
                Buffer.BlockCopy(Name, 0, bytes, i, Name.length); i += Name.length;
                bytes[i++] = (byte)((ForSale) ? 1 : 0);
                bytes[i++] = (byte)((Auction) ? 1 : 0);
                Utils.FloatToBytes(Dwell, bytes, i); i += 4;
            }

        }

        /// <exclude/>
        public final class StatusDataBlock extends PacketBlock
        {
            public uint Status;

            @Override
			public int getLength()
            {
                get
                {
                    return 4;
                }
            }

            public StatusDataBlock() { }
            public StatusDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    Status = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UIntToBytes(Status, bytes, i); i += 4;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 13;
                length += AgentData.getLength();
                for (int j = 0; j < QueryData.length; j++)
                    length += QueryData[j].length;
                for (int j = 0; j < QueryReplies.length; j++)
                    length += QueryReplies[j].length;
                for (int j = 0; j < StatusData.length; j++)
                    length += StatusData[j].length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public QueryDataBlock[] QueryData;
        public QueryRepliesBlock[] QueryReplies;
        public StatusDataBlock[] StatusData;

        public DirPlacesReplyPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.DirPlacesReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 35;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            QueryData = null;
            QueryReplies = null;
            StatusData = null;
        }

        public DirPlacesReplyPacket(byte[] bytes, int[] i) 
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
            int count = (int)bytes[i++];
            if(QueryData == null || QueryData.length != -1) {
                QueryData = new QueryDataBlock[count];
                for(int j = 0; j < count; j++)
                { QueryData[j] = new QueryDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { QueryData[j].FromBytes(bytes, i); }
            count = (int)bytes[i++];
            if(QueryReplies == null || QueryReplies.length != -1) {
                QueryReplies = new QueryRepliesBlock[count];
                for(int j = 0; j < count; j++)
                { QueryReplies[j] = new QueryRepliesBlock(); }
            }
            for (int j = 0; j < count; j++)
            { QueryReplies[j].FromBytes(bytes, i); }
            count = (int)bytes[i++];
            if(StatusData == null || StatusData.length != -1) {
                StatusData = new StatusDataBlock[count];
                for(int j = 0; j < count; j++)
                { StatusData[j] = new StatusDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { StatusData[j].FromBytes(bytes, i); }
        }

        public DirPlacesReplyPacket(Header head, byte[] bytes, int[] i)
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
            int count = (int)bytes[i++];
            if(QueryData == null || QueryData.length != count) {
                QueryData = new QueryDataBlock[count];
                for(int j = 0; j < count; j++)
                { QueryData[j] = new QueryDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { QueryData[j].FromBytes(bytes, i); }
            count = (int)bytes[i++];
            if(QueryReplies == null || QueryReplies.length != count) {
                QueryReplies = new QueryRepliesBlock[count];
                for(int j = 0; j < count; j++)
                { QueryReplies[j] = new QueryRepliesBlock(); }
            }
            for (int j = 0; j < count; j++)
            { QueryReplies[j].FromBytes(bytes, i); }
            count = (int)bytes[i++];
            if(StatusData == null || StatusData.length != count) {
                StatusData = new StatusDataBlock[count];
                for(int j = 0; j < count; j++)
                { StatusData[j] = new StatusDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { StatusData[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length++;
            for (int j = 0; j < QueryData.length; j++) { length += QueryData[j].length; }
            length++;
            for (int j = 0; j < QueryReplies.length; j++) { length += QueryReplies[j].length; }
            length++;
            for (int j = 0; j < StatusData.length; j++) { length += StatusData[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            bytes[i++] = (byte)QueryData.length;
            for (int j = 0; j < QueryData.length; j++) { QueryData[j].ToBytes(bytes, i); }
            bytes[i++] = (byte)QueryReplies.length;
            for (int j = 0; j < QueryReplies.length; j++) { QueryReplies[j].ToBytes(bytes, i); }
            bytes[i++] = (byte)StatusData.length;
            for (int j = 0; j < StatusData.length; j++) { StatusData[j].ToBytes(bytes, i); }
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
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AgentData.ToBytes(fixedBytes, i);
            fixedLength += 3;

            int QueryDataStart = 0;
            int QueryRepliesStart = 0;
            int StatusDataStart = 0;
            do
            {
                int variableLength = 0;
                int QueryDataCount = 0;
                int QueryRepliesCount = 0;
                int StatusDataCount = 0;

                i = QueryDataStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < QueryData.length) {
                    int blockLength = QueryData[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++QueryDataCount;
                    }
                    else { break; }
                    ++i;
                }

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

                i = StatusDataStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < StatusData.length) {
                    int blockLength = StatusData[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++StatusDataCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Buffer.BlockCopy(fixedBytes, 0, packet, 0, length);
                if (packets.Count > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)QueryDataCount;
                for (i = QueryDataStart; i < QueryDataStart + QueryDataCount; i++) { QueryData[i].ToBytes(packet, ref length); }
                QueryDataStart += QueryDataCount;

                packet[length++] = (byte)QueryRepliesCount;
                for (i = QueryRepliesStart; i < QueryRepliesStart + QueryRepliesCount; i++) { QueryReplies[i].ToBytes(packet, ref length); }
                QueryRepliesStart += QueryRepliesCount;

                packet[length++] = (byte)StatusDataCount;
                for (i = StatusDataStart; i < StatusDataStart + StatusDataCount; i++) { StatusData[i].ToBytes(packet, ref length); }
                StatusDataStart += StatusDataCount;

                if (acksLength > 0) {
                    Buffer.BlockCopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.Add(packet);
            } while (
                QueryDataStart < QueryData.length ||
                QueryRepliesStart < QueryReplies.length ||
                StatusDataStart < StatusData.length);

            return packets.ToArray();
        }
    }

    /// <exclude/>
