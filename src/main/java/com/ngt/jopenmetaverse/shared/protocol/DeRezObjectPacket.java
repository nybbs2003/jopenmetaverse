package com.ngt.jopenmetaverse.shared.protocol;


    public final class DeRezObjectPacket extends Packet
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
        public final class AgentBlockBlock extends PacketBlock
        {
            public UUID GroupID;
            public byte Destination;
            public UUID DestinationID;
            public UUID TransactionID;
            public byte PacketCount;
            public byte PacketNumber;

            @Override
			public int getLength()
            {
                                {
                    return 51;
                }
            }

            public AgentBlockBlock() { }
            public AgentBlockBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    GroupID.FromBytes(bytes, i); i += 16;
                    Destination = (byte)bytes[i++];
                    DestinationID.FromBytes(bytes, i); i += 16;
                    TransactionID.FromBytes(bytes, i); i += 16;
                    PacketCount = (byte)bytes[i++];
                    PacketNumber = (byte)bytes[i++];
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
                bytes[i++] = Destination;
                DestinationID.ToBytes(bytes, i); i += 16;
                TransactionID.ToBytes(bytes, i); i += 16;
                bytes[i++] = PacketCount;
                bytes[i++] = PacketNumber;
            }

        }

        /// <exclude/>
        public final class ObjectDataBlock extends PacketBlock
        {
            public uint ObjectLocalID;

            @Override
			public int getLength()
            {
                                {
                    return 4;
                }
            }

            public ObjectDataBlock() { }
            public ObjectDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ObjectLocalID = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UIntToBytes(ObjectLocalID, bytes, i); i += 4;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += AgentData.getLength();
                length += AgentBlock.length;
                for (int j = 0; j < ObjectData.length; j++)
                    length += ObjectData[j].length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public AgentBlockBlock AgentBlock;
        public ObjectDataBlock[] ObjectData;

        public DeRezObjectPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.DeRezObject;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 291;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            AgentBlock = new AgentBlockBlock();
            ObjectData = null;
        }

        public DeRezObjectPacket(byte[] bytes, int[] i) 
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
            AgentBlock.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(ObjectData == null || ObjectData.length != -1) {
                ObjectData = new ObjectDataBlock[count];
                for(int j = 0; j < count; j++)
                { ObjectData[j] = new ObjectDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ObjectData[j].FromBytes(bytes, i); }
        }

        public DeRezObjectPacket(Header head, byte[] bytes, int[] i)
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
            AgentBlock.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(ObjectData == null || ObjectData.length != count) {
                ObjectData = new ObjectDataBlock[count];
                for(int j = 0; j < count; j++)
                { ObjectData[j] = new ObjectDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ObjectData[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += AgentBlock.length;
            length++;
            for (int j = 0; j < ObjectData.length; j++) { length += ObjectData[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            AgentBlock.ToBytes(bytes, i);
            bytes[i++] = (byte)ObjectData.length;
            for (int j = 0; j < ObjectData.length; j++) { ObjectData[j].ToBytes(bytes, i); }
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
            fixedLength += AgentBlock.length;
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AgentData.ToBytes(fixedBytes, i);
            AgentBlock.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int ObjectDataStart = 0;
            do
            {
                int variableLength = 0;
                int ObjectDataCount = 0;

                i = ObjectDataStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < ObjectData.length) {
                    int blockLength = ObjectData[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++ObjectDataCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Utils.arraycopy(fixedBytes, 0, packet, 0, length);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)ObjectDataCount;
                for (i = ObjectDataStart; i < ObjectDataStart + ObjectDataCount; i++) { ObjectData[i].ToBytes(packet, ref length); }
                ObjectDataStart += ObjectDataCount;

                if (acksLength > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.add(packet);
            } while (
                ObjectDataStart < ObjectData.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
