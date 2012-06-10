package com.ngt.jopenmetaverse.shared.protocol;


    public final class PlacesReplyPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID QueryID;

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
                AgentID.ToBytes(bytes, i); i += 16;
                QueryID.ToBytes(bytes, i); i += 16;
            }

        }

        /// <exclude/>
        public final class TransactionDataBlock extends PacketBlock
        {
            public UUID TransactionID;

            @Override
		public int getLength()
            {
                get
                {
                    return 16;
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
            }

        }

        /// <exclude/>
        public final class QueryDataBlock extends PacketBlock
        {
            public UUID OwnerID;
            public byte[] Name;
            public byte[] Desc;
            public int ActualArea;
            public int BillableArea;
            public byte Flags;
            public float GlobalX;
            public float GlobalY;
            public float GlobalZ;
            public byte[] SimName;
            public UUID SnapshotID;
            public float Dwell;
            public int Price;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 64;
                    if (Name != null) { length += Name.length; }
                    if (Desc != null) { length += Desc.length; }
                    if (SimName != null) { length += SimName.length; }
                    return length;
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
                int length;
                try
                {
                    OwnerID.FromBytes(bytes, i); i += 16;
                    length = bytes[i++];
                    Name = new byte[length];
                    Buffer.BlockCopy(bytes, i, Name, 0, length); i += length;
                    length = bytes[i++];
                    Desc = new byte[length];
                    Buffer.BlockCopy(bytes, i, Desc, 0, length); i += length;
                    ActualArea = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    BillableArea = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Flags = (byte)bytes[i++];
                    GlobalX = Utils.BytesToFloat(bytes, i); i += 4;
                    GlobalY = Utils.BytesToFloat(bytes, i); i += 4;
                    GlobalZ = Utils.BytesToFloat(bytes, i); i += 4;
                    length = bytes[i++];
                    SimName = new byte[length];
                    Buffer.BlockCopy(bytes, i, SimName, 0, length); i += length;
                    SnapshotID.FromBytes(bytes, i); i += 16;
                    Dwell = Utils.BytesToFloat(bytes, i); i += 4;
                    Price = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                OwnerID.ToBytes(bytes, i); i += 16;
                bytes[i++] = (byte)Name.length;
                Buffer.BlockCopy(Name, 0, bytes, i, Name.length); i += Name.length;
                bytes[i++] = (byte)Desc.length;
                Buffer.BlockCopy(Desc, 0, bytes, i, Desc.length); i += Desc.length;
                Utils.IntToBytes(ActualArea, bytes, i); i += 4;
                Utils.IntToBytes(BillableArea, bytes, i); i += 4;
                bytes[i++] = Flags;
                Utils.FloatToBytes(GlobalX, bytes, i); i += 4;
                Utils.FloatToBytes(GlobalY, bytes, i); i += 4;
                Utils.FloatToBytes(GlobalZ, bytes, i); i += 4;
                bytes[i++] = (byte)SimName.length;
                Buffer.BlockCopy(SimName, 0, bytes, i, SimName.length); i += SimName.length;
                SnapshotID.ToBytes(bytes, i); i += 16;
                Utils.FloatToBytes(Dwell, bytes, i); i += 4;
                Utils.IntToBytes(Price, bytes, i); i += 4;
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
                for (int j = 0; j < QueryData.length; j++)
                    length += QueryData[j].length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public TransactionDataBlock TransactionData;
        public QueryDataBlock[] QueryData;

        public PlacesReplyPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.PlacesReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 30;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            TransactionData = new TransactionDataBlock();
            QueryData = null;
        }

        public PlacesReplyPacket(byte[] bytes, int[] i) 
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
            if(QueryData == null || QueryData.length != -1) {
                QueryData = new QueryDataBlock[count];
                for(int j = 0; j < count; j++)
                { QueryData[j] = new QueryDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { QueryData[j].FromBytes(bytes, i); }
        }

        public PlacesReplyPacket(Header head, byte[] bytes, int[] i)
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
            if(QueryData == null || QueryData.length != count) {
                QueryData = new QueryDataBlock[count];
                for(int j = 0; j < count; j++)
                { QueryData[j] = new QueryDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { QueryData[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += TransactionData.length;
            length++;
            for (int j = 0; j < QueryData.length; j++) { length += QueryData[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            TransactionData.ToBytes(bytes, i);
            bytes[i++] = (byte)QueryData.length;
            for (int j = 0; j < QueryData.length; j++) { QueryData[j].ToBytes(bytes, i); }
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

            int QueryDataStart = 0;
            do
            {
                int variableLength = 0;
                int QueryDataCount = 0;

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

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Buffer.BlockCopy(fixedBytes, 0, packet, 0, length);
                if (packets.Count > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)QueryDataCount;
                for (i = QueryDataStart; i < QueryDataStart + QueryDataCount; i++) { QueryData[i].ToBytes(packet, ref length); }
                QueryDataStart += QueryDataCount;

                if (acksLength > 0) {
                    Buffer.BlockCopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.Add(packet);
            } while (
                QueryDataStart < QueryData.length);

            return packets.ToArray();
        }
    }

    /// <exclude/>
