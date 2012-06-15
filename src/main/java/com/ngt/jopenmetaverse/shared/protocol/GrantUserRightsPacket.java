package com.ngt.jopenmetaverse.shared.protocol;


    public final class GrantUserRightsPacket extends Packet
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
        public final class RightsBlock extends PacketBlock
        {
            public UUID AgentRelated;
            public int RelatedRights;

            @Override
			public int getLength()
            {
                                {
                    return 20;
                }
            }

            public RightsBlock() { }
            public RightsBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    AgentRelated.FromBytes(bytes, i[0]); i[0] += 16;
                    RelatedRights = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                AgentRelated.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.intToBytes(RelatedRights, bytes, i[0]); i[0] += 4;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += AgentData.getLength();
                for (int j = 0; j < Rights.length; j++)
                    length += Rights[j].getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public RightsBlock[] Rights;

        public GrantUserRightsPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.GrantUserRights;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 320;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            Rights = null;
        }

        public GrantUserRightsPacket(byte[] bytes, int[] i) 
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
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(Rights == null || Rights.length != -1) {
                Rights = new RightsBlock[count];
                for(int j = 0; j < count; j++)
                { Rights[j] = new RightsBlock(); }
            }
            for (int j = 0; j < count; j++)
            { Rights[j].FromBytes(bytes, i); }
        }

        public GrantUserRightsPacket(Header head, byte[] bytes, int[] i)
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
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(Rights == null || Rights.length != count) {
                Rights = new RightsBlock[count];
                for(int j = 0; j < count; j++)
                { Rights[j] = new RightsBlock(); }
            }
            for (int j = 0; j < count; j++)
            { Rights[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length++;
            for (int j = 0; j < Rights.length; j++) { length += Rights[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)Rights.length;
            for (int j = 0; j < Rights.length; j++) { Rights[j].ToBytes(bytes, i); }
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
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AgentData.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int RightsStart = 0;
            do
            {
                int variableLength = 0;
                int RightsCount = 0;

              i[0] =RightsStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < Rights.length) {
                    int blockLength = Rights[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++RightsCount;
                    }
                    else { break; }
                    i[0]++;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)RightsCount;
                for (i[0] = RightsStart; i[0] < RightsStart + RightsCount; i[0]++) { Rights[i[0]].ToBytes(packet, length); }
                RightsStart += RightsCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                RightsStart < Rights.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
