package com.ngt.jopenmetaverse.shared.protocol;


    public final class MapItemReplyPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public uint Flags;

            @Override
			public int getLength()
            {
                                {
                    return 20;
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
                    Flags = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
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
                Utils.UIntToBytes(Flags, bytes, i); i += 4;
            }

        }

        /// <exclude/>
        public final class RequestDataBlock extends PacketBlock
        {
            public uint ItemType;

            @Override
			public int getLength()
            {
                                {
                    return 4;
                }
            }

            public RequestDataBlock() { }
            public RequestDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ItemType = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UIntToBytes(ItemType, bytes, i); i += 4;
            }

        }

        /// <exclude/>
        public final class DataBlock extends PacketBlock
        {
            public uint X;
            public uint Y;
            public UUID ID;
            public int Extra;
            public int Extra2;
            public byte[] Name;

            @Override
			public int getLength()
            {
                                {
                    int length = 33;
                    if (Name != null) { length += Name.length; }
                    return length;
                }
            }

            public DataBlock() { }
            public DataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    X = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Y = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    ID.FromBytes(bytes, i); i += 16;
                    Extra = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Extra2 = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    length = bytes[i++];
                    Name = new byte[length];
                    Utils.arraycopy(bytes, i, Name, 0, length); i += length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UIntToBytes(X, bytes, i); i += 4;
                Utils.UIntToBytes(Y, bytes, i); i += 4;
                ID.ToBytes(bytes, i); i += 16;
                Utils.IntToBytes(Extra, bytes, i); i += 4;
                Utils.IntToBytes(Extra2, bytes, i); i += 4;
                bytes[i++] = (byte)Name.length;
                Utils.arraycopy(Name, 0, bytes, i, Name.length); i += Name.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += AgentData.getLength();
                length += RequestData.length;
                for (int j = 0; j < Data.getLength(); j++)
                    length += Data[j].length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public RequestDataBlock RequestData;
        public DataBlock[] Data;

        public MapItemReplyPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.MapItemReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 411;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            RequestData = new RequestDataBlock();
            Data = null;
        }

        public MapItemReplyPacket(byte[] bytes, int[] i) 
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
            RequestData.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(Data == null || Data.getLength() != -1) {
                Data = new DataBlock[count];
                for(int j = 0; j < count; j++)
                { Data[j] = new DataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { Data[j].FromBytes(bytes, i); }
        }

        public MapItemReplyPacket(Header head, byte[] bytes, int[] i)
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
            RequestData.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(Data == null || Data.getLength() != count) {
                Data = new DataBlock[count];
                for(int j = 0; j < count; j++)
                { Data[j] = new DataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { Data[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += RequestData.length;
            length++;
            for (int j = 0; j < Data.getLength(); j++) { length += Data[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            RequestData.ToBytes(bytes, i);
            bytes[i++] = (byte)Data.length;
            for (int j = 0; j < Data.getLength(); j++) { Data[j].ToBytes(bytes, i); }
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
            fixedLength += RequestData.length;
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AgentData.ToBytes(fixedBytes, i);
            RequestData.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int DataStart = 0;
            do
            {
                int variableLength = 0;
                int DataCount = 0;

                i = DataStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < Data.getLength()) {
                    int blockLength = Data[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++DataCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Utils.arraycopy(fixedBytes, 0, packet, 0, length);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)DataCount;
                for (i = DataStart; i < DataStart + DataCount; i++) { Data[i].ToBytes(packet, ref length); }
                DataStart += DataCount;

                if (acksLength > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.add(packet);
            } while (
                DataStart < Data.getLength());

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
