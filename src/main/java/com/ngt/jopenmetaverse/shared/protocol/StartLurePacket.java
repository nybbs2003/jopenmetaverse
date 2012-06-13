package com.ngt.jopenmetaverse.shared.protocol;


    public final class StartLurePacket extends Packet
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
        public final class InfoBlock extends PacketBlock
        {
            public byte LureType;
            public byte[] Message;

            @Override
			public int getLength()
            {
                                {
                    int length = 2;
                    if (Message != null) { length += Message.length; }
                    return length;
                }
            }

            public InfoBlock() { }
            public InfoBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    LureType = (byte)bytes[i[0]++];
                    length = bytes[i[0]++];
                    Message = new byte[length];
                    Utils.arraycopy(bytes, i[0], Message, 0, length); i[0] +=  length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = LureType;
                bytes[i[0]++] = (byte)Message.length;
                Utils.arraycopy(Message, 0, bytes, i[0], Message.length); i[0] +=  Message.length;
            }

        }

        /// <exclude/>
        public final class TargetDataBlock extends PacketBlock
        {
            public UUID TargetID;

            @Override
			public int getLength()
            {
                                {
                    return 16;
                }
            }

            public TargetDataBlock() { }
            public TargetDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    TargetID.FromBytes(bytes, i[0]); i[0] += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                TargetID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += AgentData.getLength();
                length += Info.length;
                for (int j = 0; j < TargetData.length; j++)
                    length += TargetData[j].getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public InfoBlock Info;
        public TargetDataBlock[] TargetData;

        public StartLurePacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.StartLure;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 70;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            Info = new InfoBlock();
            TargetData = null;
        }

        public StartLurePacket(byte[] bytes, int[] i) 
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
            Info.FromBytes(bytes, i);
            int count = (int)bytes[i[0]++];
            if(TargetData == null || TargetData.length != -1) {
                TargetData = new TargetDataBlock[count];
                for(int j = 0; j < count; j++)
                { TargetData[j] = new TargetDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { TargetData[j].FromBytes(bytes, i); }
        }

        public StartLurePacket(Header head, byte[] bytes, int[] i)
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
            Info.FromBytes(bytes, i);
            int count = (int)bytes[i[0]++];
            if(TargetData == null || TargetData.length != count) {
                TargetData = new TargetDataBlock[count];
                for(int j = 0; j < count; j++)
                { TargetData[j] = new TargetDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { TargetData[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += Info.length;
            length++;
            for (int j = 0; j < TargetData.length; j++) { length += TargetData[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            Info.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)TargetData.length;
            for (int j = 0; j < TargetData.length; j++) { TargetData[j].ToBytes(bytes, i); }
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
            fixedLength += Info.length;
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AgentData.ToBytes(fixedBytes, i);
            Info.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int TargetDataStart = 0;
            do
            {
                int variableLength = 0;
                int TargetDataCount = 0;

              i[0] =TargetDataStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < TargetData.length) {
                    int blockLength = TargetData[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++TargetDataCount;
                    }
                    else { break; }
                    i[0]++;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)TargetDataCount;
                for (i[0] = TargetDataStart; i[0] < TargetDataStart + TargetDataCount; i[0]++) { TargetData[i[0]].ToBytes(packet, length); }
                TargetDataStart += TargetDataCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                TargetDataStart < TargetData.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
