package com.ngt.jopenmetaverse.shared.protocol;


    public final class AgentWearablesUpdatePacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID SessionID;
            public uint SerialNum;

            @Override
			public int getLength()
            {
                                {
                    return 36;
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
                    SerialNum = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
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
                Utils.UIntToBytes(SerialNum, bytes, i); i += 4;
            }

        }

        /// <exclude/>
        public final class WearableDataBlock extends PacketBlock
        {
            public UUID ItemID;
            public UUID AssetID;
            public byte WearableType;

            @Override
			public int getLength()
            {
                                {
                    return 33;
                }
            }

            public WearableDataBlock() { }
            public WearableDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ItemID.FromBytes(bytes, i); i += 16;
                    AssetID.FromBytes(bytes, i); i += 16;
                    WearableType = (byte)bytes[i++];
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ItemID.ToBytes(bytes, i); i += 16;
                AssetID.ToBytes(bytes, i); i += 16;
                bytes[i++] = WearableType;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += AgentData.getLength();
                for (int j = 0; j < WearableData.length; j++)
                    length += WearableData[j].length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public WearableDataBlock[] WearableData;

        public AgentWearablesUpdatePacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.AgentWearablesUpdate;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 382;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            WearableData = null;
        }

        public AgentWearablesUpdatePacket(byte[] bytes, int[] i) 
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
            if(WearableData == null || WearableData.length != -1) {
                WearableData = new WearableDataBlock[count];
                for(int j = 0; j < count; j++)
                { WearableData[j] = new WearableDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { WearableData[j].FromBytes(bytes, i); }
        }

        public AgentWearablesUpdatePacket(Header head, byte[] bytes, int[] i)
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
            if(WearableData == null || WearableData.length != count) {
                WearableData = new WearableDataBlock[count];
                for(int j = 0; j < count; j++)
                { WearableData[j] = new WearableDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { WearableData[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length++;
            for (int j = 0; j < WearableData.length; j++) { length += WearableData[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            bytes[i++] = (byte)WearableData.length;
            for (int j = 0; j < WearableData.length; j++) { WearableData[j].ToBytes(bytes, i); }
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

            int WearableDataStart = 0;
            do
            {
                int variableLength = 0;
                int WearableDataCount = 0;

                i = WearableDataStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < WearableData.length) {
                    int blockLength = WearableData[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++WearableDataCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Utils.arraycopy(fixedBytes, 0, packet, 0, length);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)WearableDataCount;
                for (i = WearableDataStart; i < WearableDataStart + WearableDataCount; i++) { WearableData[i].ToBytes(packet, ref length); }
                WearableDataStart += WearableDataCount;

                if (acksLength > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.add(packet);
            } while (
                WearableDataStart < WearableData.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
