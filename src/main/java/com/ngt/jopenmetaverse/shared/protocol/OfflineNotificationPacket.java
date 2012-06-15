package com.ngt.jopenmetaverse.shared.protocol;


    public final class OfflineNotificationPacket extends Packet
    {
        /// <exclude/>
        public final class AgentBlockBlock extends PacketBlock
        {
            public UUID AgentID;

            @Override
			public int getLength()
            {
                                {
                    return 16;
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
                    AgentID.FromBytes(bytes, i[0]); i[0] += 16;
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

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                for (int j = 0; j < AgentBlock.length; j++)
                    length += AgentBlock[j].getLength();
                return length;
            }
        }
        public AgentBlockBlock[] AgentBlock;

        public OfflineNotificationPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.OfflineNotification;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 323;
            header.Reliable = true;
            AgentBlock = null;
        }

        public OfflineNotificationPacket(byte[] bytes, int[] i) 
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
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(AgentBlock == null || AgentBlock.length != -1) {
                AgentBlock = new AgentBlockBlock[count];
                for(int j = 0; j < count; j++)
                { AgentBlock[j] = new AgentBlockBlock(); }
            }
            for (int j = 0; j < count; j++)
            { AgentBlock[j].FromBytes(bytes, i); }
        }

        public OfflineNotificationPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(AgentBlock == null || AgentBlock.length != count) {
                AgentBlock = new AgentBlockBlock[count];
                for(int j = 0; j < count; j++)
                { AgentBlock[j] = new AgentBlockBlock(); }
            }
            for (int j = 0; j < count; j++)
            { AgentBlock[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length++;
            for (int j = 0; j < AgentBlock.length; j++) { length += AgentBlock[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)AgentBlock.length;
            for (int j = 0; j < AgentBlock.length; j++) { AgentBlock[j].ToBytes(bytes, i); }
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

            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int AgentBlockStart = 0;
            do
            {
                int variableLength = 0;
                int AgentBlockCount = 0;

              i[0] =AgentBlockStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < AgentBlock.length) {
                    int blockLength = AgentBlock[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++AgentBlockCount;
                    }
                    else { break; }
                    i[0]++;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)AgentBlockCount;
                for (i[0] = AgentBlockStart; i[0] < AgentBlockStart + AgentBlockCount; i[0]++) { AgentBlock[i[0]].ToBytes(packet, length); }
                AgentBlockStart += AgentBlockCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                AgentBlockStart < AgentBlock.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
