package com.ngt.jopenmetaverse.shared.protocol;


    public final class ParcelAccessListUpdatePacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID SessionID;

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
                    SessionID.FromBytes(bytes, i[0]); i[0] += 16;
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
                SessionID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        /// <exclude/>
        public final class DataBlock extends PacketBlock
        {
            public long Flags;
            public int LocalID;
            public UUID TransactionID;
            public int SequenceID;
            public int Sections;

            @Override
			public int getLength()
            {
                                {
                    return 32;
                }
            }

            public DataBlock() { }
            public DataBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    Flags = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    LocalID = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    TransactionID.FromBytes(bytes, i[0]); i[0] += 16;
                    SequenceID = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    Sections = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.uintToBytes(Flags, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(LocalID, bytes, i[0]); i[0] += 4;
                TransactionID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.intToBytes(SequenceID, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(Sections, bytes, i[0]); i[0] += 4;
            }

        }

        /// <exclude/>
        public final class ListBlock extends PacketBlock
        {
            public UUID ID;
            public int Time;
            public long Flags;

            @Override
			public int getLength()
            {
                                {
                    return 24;
                }
            }

            public ListBlock() { }
            public ListBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ID.FromBytes(bytes, i[0]); i[0] += 16;
                    Time = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    Flags = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.intToBytes(Time, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(Flags, bytes, i[0]); i[0] += 4;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += AgentData.getLength();
                length += Data.length;
                for (int j = 0; j < List.length; j++)
                    length += List[j].getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public DataBlock Data;
        public ListBlock[] List;

        public ParcelAccessListUpdatePacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.ParcelAccessListUpdate;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 217;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            Data = new DataBlock();
            List = null;
        }

        public ParcelAccessListUpdatePacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            Data.FromBytes(bytes, i);
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(List == null || List.length != -1) {
                List = new ListBlock[count];
                for(int j = 0; j < count; j++)
                { List[j] = new ListBlock(); }
            }
            for (int j = 0; j < count; j++)
            { List[j].FromBytes(bytes, i); }
        }

        public ParcelAccessListUpdatePacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
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
            Data.FromBytes(bytes, i);
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(List == null || List.length != count) {
                List = new ListBlock[count];
                for(int j = 0; j < count; j++)
                { List[j] = new ListBlock(); }
            }
            for (int j = 0; j < count; j++)
            { List[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += Data.length;
            length++;
            for (int j = 0; j < List.length; j++) { length += List[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            Data.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)List.length;
            for (int j = 0; j < List.length; j++) { List[j].ToBytes(bytes, i); }
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
            fixedLength += Data.length;
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AgentData.ToBytes(fixedBytes, i);
            Data.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int ListStart = 0;
            do
            {
                int variableLength = 0;
                int ListCount = 0;

              i[0] =ListStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < List.length) {
                    int blockLength = List[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++ListCount;
                    }
                    else { break; }
                    i[0]++;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)ListCount;
                for (i[0] = ListStart; i[0] < ListStart + ListCount; i[0]++) { List[i[0]].ToBytes(packet, length); }
                ListStart += ListCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                ListStart < List.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
