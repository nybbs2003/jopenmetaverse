package com.ngt.jopenmetaverse.shared.protocol;


    public final class InviteGroupRequestPacket extends Packet
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
                    GroupID.FromBytes(bytes, i[0]); i[0] += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                GroupID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        /// <exclude/>
        public final class InviteDataBlock extends PacketBlock
        {
            public UUID InviteeID;
            public UUID RoleID;

            @Override
			public int getLength()
            {
                                {
                    return 32;
                }
            }

            public InviteDataBlock() { }
            public InviteDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    InviteeID.FromBytes(bytes, i[0]); i[0] += 16;
                    RoleID.FromBytes(bytes, i[0]); i[0] += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                InviteeID.ToBytes(bytes, i[0]); i[0] += 16;
                RoleID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += AgentData.getLength();
                length += GroupData.length;
                for (int j = 0; j < InviteData.length; j++)
                    length += InviteData[j].getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public GroupDataBlock GroupData;
        public InviteDataBlock[] InviteData;

        public InviteGroupRequestPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.InviteGroupRequest;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 349;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            GroupData = new GroupDataBlock();
            InviteData = null;
        }

        public InviteGroupRequestPacket(byte[] bytes, int[] i) 
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
            GroupData.FromBytes(bytes, i);
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(InviteData == null || InviteData.length != -1) {
                InviteData = new InviteDataBlock[count];
                for(int j = 0; j < count; j++)
                { InviteData[j] = new InviteDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { InviteData[j].FromBytes(bytes, i); }
        }

        public InviteGroupRequestPacket(Header head, byte[] bytes, int[] i)
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
            GroupData.FromBytes(bytes, i);
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(InviteData == null || InviteData.length != count) {
                InviteData = new InviteDataBlock[count];
                for(int j = 0; j < count; j++)
                { InviteData[j] = new InviteDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { InviteData[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += GroupData.length;
            length++;
            for (int j = 0; j < InviteData.length; j++) { length += InviteData[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            GroupData.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)InviteData.length;
            for (int j = 0; j < InviteData.length; j++) { InviteData[j].ToBytes(bytes, i); }
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
            fixedLength += GroupData.length;
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AgentData.ToBytes(fixedBytes, i);
            GroupData.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int InviteDataStart = 0;
            do
            {
                int variableLength = 0;
                int InviteDataCount = 0;

              i[0] =InviteDataStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < InviteData.length) {
                    int blockLength = InviteData[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++InviteDataCount;
                    }
                    else { break; }
                    i[0]++;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)InviteDataCount;
                for (i[0] = InviteDataStart; i[0] < InviteDataStart + InviteDataCount; i[0]++) { InviteData[i[0]].ToBytes(packet, length); }
                InviteDataStart += InviteDataCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                InviteDataStart < InviteData.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
