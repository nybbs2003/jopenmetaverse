package com.ngt.jopenmetaverse.shared.protocol;


    public final class FindAgentPacket extends Packet
    {
        /// <exclude/>
        public final class AgentBlockBlock extends PacketBlock
        {
            public UUID Hunter;
            public UUID Prey;
            public long SpaceIP;

            @Override
			public int getLength()
            {
                                {
                    return 36;
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
                    Hunter.FromBytes(bytes, i[0]); i[0] += 16;
                    Prey.FromBytes(bytes, i[0]); i[0] += 16;
                    SpaceIP = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Hunter.ToBytes(bytes, i[0]); i[0] += 16;
                Prey.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.uintToBytes(SpaceIP, bytes, i[0]); i[0] += 4;
            }

        }

        /// <exclude/>
        public final class LocationBlockBlock extends PacketBlock
        {
            public double GlobalX;
            public double GlobalY;

            @Override
			public int getLength()
            {
                                {
                    return 16;
                }
            }

            public LocationBlockBlock() { }
            public LocationBlockBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    GlobalX = Utils.BytesToDouble(bytes, i[0]); i[0] += 8;
                    GlobalY = Utils.BytesToDouble(bytes, i[0]); i[0] += 8;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.DoubleToBytes(GlobalX, bytes, i[0]); i[0] += 8;
                Utils.DoubleToBytes(GlobalY, bytes, i[0]); i[0] += 8;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += AgentBlock.length;
                for (int j = 0; j < LocationBlock.length; j++)
                    length += LocationBlock[j].getLength();
                return length;
            }
        }
        public AgentBlockBlock AgentBlock;
        public LocationBlockBlock[] LocationBlock;

        public FindAgentPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.FindAgent;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 256;
            header.Reliable = true;
            AgentBlock = new AgentBlockBlock();
            LocationBlock = null;
        }

        public FindAgentPacket(byte[] bytes, int[] i) 
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
            AgentBlock.FromBytes(bytes, i);
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(LocationBlock == null || LocationBlock.length != -1) {
                LocationBlock = new LocationBlockBlock[count];
                for(int j = 0; j < count; j++)
                { LocationBlock[j] = new LocationBlockBlock(); }
            }
            for (int j = 0; j < count; j++)
            { LocationBlock[j].FromBytes(bytes, i); }
        }

        public FindAgentPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd) throws MalformedDataException
        {
            this.header =  header;
            AgentBlock.FromBytes(bytes, i);
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(LocationBlock == null || LocationBlock.length != count) {
                LocationBlock = new LocationBlockBlock[count];
                for(int j = 0; j < count; j++)
                { LocationBlock[j] = new LocationBlockBlock(); }
            }
            for (int j = 0; j < count; j++)
            { LocationBlock[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentBlock.length;
            length++;
            for (int j = 0; j < LocationBlock.length; j++) { length += LocationBlock[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentBlock.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)LocationBlock.length;
            for (int j = 0; j < LocationBlock.length; j++) { LocationBlock[j].ToBytes(bytes, i); }
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

            fixedLength += AgentBlock.length;
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AgentBlock.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int LocationBlockStart = 0;
            do
            {
                int variableLength = 0;
                int LocationBlockCount = 0;

              i[0] =LocationBlockStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < LocationBlock.length) {
                    int blockLength = LocationBlock[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++LocationBlockCount;
                    }
                    else { break; }
                    i[0]++;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)LocationBlockCount;
                for (i[0] = LocationBlockStart; i[0] < LocationBlockStart + LocationBlockCount; i[0]++) { LocationBlock[i[0]].ToBytes(packet, length); }
                LocationBlockStart += LocationBlockCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                LocationBlockStart < LocationBlock.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
