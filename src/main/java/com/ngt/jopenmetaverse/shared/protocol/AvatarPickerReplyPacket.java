package com.ngt.jopenmetaverse.shared.protocol;


    public final class AvatarPickerReplyPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID QueryID;

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
                AgentID.ToBytes(bytes, i[0]); i[0] += 16;
                QueryID.ToBytes(bytes, i); i += 16;
            }

        }

        /// <exclude/>
        public final class DataBlock extends PacketBlock
        {
            public UUID AvatarID;
            public byte[] FirstName;
            public byte[] LastName;

            @Override
			public int getLength()
            {
                                {
                    int length = 18;
                    if (FirstName != null) { length += FirstName.length; }
                    if (LastName != null) { length += LastName.length; }
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
                    AvatarID.FromBytes(bytes, i); i += 16;
                    length = bytes[i++];
                    FirstName = new byte[length];
                    Utils.arraycopy(bytes, i, FirstName, 0, length); i += length;
                    length = bytes[i++];
                    LastName = new byte[length];
                    Utils.arraycopy(bytes, i, LastName, 0, length); i += length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                AvatarID.ToBytes(bytes, i); i += 16;
                bytes[i++] = (byte)FirstName.length;
                Utils.arraycopy(FirstName, 0, bytes, i, FirstName.length); i += FirstName.length;
                bytes[i++] = (byte)LastName.length;
                Utils.arraycopy(LastName, 0, bytes, i, LastName.length); i += LastName.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += AgentData.getLength();
                for (int j = 0; j < Data.getLength(); j++)
                    length += Data[j].length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public DataBlock[] Data;

        public AvatarPickerReplyPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.AvatarPickerReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 28;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            Data = null;
        }

        public AvatarPickerReplyPacket(byte[] bytes, int[] i) 
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
            int count = (int)bytes[i++];
            if(Data == null || Data.getLength() != -1) {
                Data = new DataBlock[count];
                for(int j = 0; j < count; j++)
                { Data[j] = new DataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { Data[j].FromBytes(bytes, i); }
        }

        public AvatarPickerReplyPacket(Header head, byte[] bytes, int[] i)
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
            length++;
            for (int j = 0; j < Data.getLength(); j++) { length += Data[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
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
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AgentData.ToBytes(fixedBytes, i);
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
