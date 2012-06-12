package com.ngt.jopenmetaverse.shared.protocol;


    public final class EjectGroupMemberRequestPacket extends Packet
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
        public final class GroupDataBlock extends PacketBlock
        {
            public UUID GroupID;

            @Override
			public int getLength()
            {
                                {
                    return 16;
                }
            }

            public GroupDataBlock() { }
            public GroupDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
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
                GroupID.ToBytes(bytes, i); i += 16;
            }

        }

        /// <exclude/>
        public final class EjectDataBlock extends PacketBlock
        {
            public UUID EjecteeID;

            @Override
			public int getLength()
            {
                                {
                    return 16;
                }
            }

            public EjectDataBlock() { }
            public EjectDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    EjecteeID.FromBytes(bytes, i); i += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                EjecteeID.ToBytes(bytes, i); i += 16;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += AgentData.getLength();
                length += GroupData.length;
                for (int j = 0; j < EjectData.length; j++)
                    length += EjectData[j].length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public GroupDataBlock GroupData;
        public EjectDataBlock[] EjectData;

        public EjectGroupMemberRequestPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.EjectGroupMemberRequest;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 345;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            GroupData = new GroupDataBlock();
            EjectData = null;
        }

        public EjectGroupMemberRequestPacket(byte[] bytes, int[] i) 
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
            GroupData.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(EjectData == null || EjectData.length != -1) {
                EjectData = new EjectDataBlock[count];
                for(int j = 0; j < count; j++)
                { EjectData[j] = new EjectDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { EjectData[j].FromBytes(bytes, i); }
        }

        public EjectGroupMemberRequestPacket(Header head, byte[] bytes, int[] i)
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
            GroupData.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(EjectData == null || EjectData.length != count) {
                EjectData = new EjectDataBlock[count];
                for(int j = 0; j < count; j++)
                { EjectData[j] = new EjectDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { EjectData[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += GroupData.length;
            length++;
            for (int j = 0; j < EjectData.length; j++) { length += EjectData[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            GroupData.ToBytes(bytes, i);
            bytes[i++] = (byte)EjectData.length;
            for (int j = 0; j < EjectData.length; j++) { EjectData[j].ToBytes(bytes, i); }
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
            fixedLength += GroupData.length;
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AgentData.ToBytes(fixedBytes, i);
            GroupData.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int EjectDataStart = 0;
            do
            {
                int variableLength = 0;
                int EjectDataCount = 0;

                i = EjectDataStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < EjectData.length) {
                    int blockLength = EjectData[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++EjectDataCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Utils.arraycopy(fixedBytes, 0, packet, 0, length);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)EjectDataCount;
                for (i = EjectDataStart; i < EjectDataStart + EjectDataCount; i++) { EjectData[i].ToBytes(packet, ref length); }
                EjectDataStart += EjectDataCount;

                if (acksLength > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.add(packet);
            } while (
                EjectDataStart < EjectData.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
