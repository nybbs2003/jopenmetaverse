package com.ngt.jopenmetaverse.shared.protocol;


    public final class GroupRoleDataReplyPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;

            @Override
			public int getLength()
            {
                                {
                    return 16;
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

        /// <exclude/>
        public final class GroupDataBlock extends PacketBlock
        {
            public UUID GroupID;
            public UUID RequestID;
            public int RoleCount;

            @Override
			public int getLength()
            {
                                {
                    return 36;
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
                    RequestID.FromBytes(bytes, i[0]); i[0] += 16;
                    RoleCount = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
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
                RequestID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.intToBytes(RoleCount, bytes, i[0]); i[0] += 4;
            }

        }

        /// <exclude/>
        public final class RoleDataBlock extends PacketBlock
        {
            public UUID RoleID;
            public byte[] Name;
            public byte[] Title;
            public byte[] Description;
            public BigInteger Powers;
            public long Members;

            @Override
			public int getLength()
            {
                                {
                    int length = 31;
                    if (Name != null) { length += Name.length; }
                    if (Title != null) { length += Title.length; }
                    if (Description != null) { length += Description.length; }
                    return length;
                }
            }

            public RoleDataBlock() { }
            public RoleDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    RoleID.FromBytes(bytes, i[0]); i[0] += 16;
                    length = bytes[i[0]++];
                    Name = new byte[length];
                    Utils.arraycopy(bytes, i[0], Name, 0, length); i[0] +=  length;
                    length = bytes[i[0]++];
                    Title = new byte[length];
                    Utils.arraycopy(bytes, i[0], Title, 0, length); i[0] +=  length;
                    length = bytes[i[0]++];
                    Description = new byte[length];
                    Utils.arraycopy(bytes, i[0], Description, 0, length); i[0] +=  length;
                    Powers = Utils.bytesToULong(bytes, i[0]); i[0] += 8;
                    Members = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                RoleID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)Name.length;
                Utils.arraycopy(Name, 0, bytes, i[0], Name.length); i[0] +=  Name.length;
                bytes[i[0]++] = (byte)Title.length;
                Utils.arraycopy(Title, 0, bytes, i[0], Title.length); i[0] +=  Title.length;
                bytes[i[0]++] = (byte)Description.length;
                Utils.arraycopy(Description, 0, bytes, i[0], Description.length); i[0] +=  Description.length;
                Utils.ulongToBytes(Powers, bytes, i[0]); i[0] += 8;
                Utils.uintToBytes(Members, bytes, i[0]); i[0] += 4;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += AgentData.getLength();
                length += GroupData.length;
                for (int j = 0; j < RoleData.length; j++)
                    length += RoleData[j].getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public GroupDataBlock GroupData;
        public RoleDataBlock[] RoleData;

        public GroupRoleDataReplyPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.GroupRoleDataReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 372;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            GroupData = new GroupDataBlock();
            RoleData = null;
        }

        public GroupRoleDataReplyPacket(byte[] bytes, int[] i) 
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
            int count = (int)bytes[i[0]++];
            if(RoleData == null || RoleData.length != -1) {
                RoleData = new RoleDataBlock[count];
                for(int j = 0; j < count; j++)
                { RoleData[j] = new RoleDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { RoleData[j].FromBytes(bytes, i); }
        }

        public GroupRoleDataReplyPacket(Header head, byte[] bytes, int[] i)
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
            int count = (int)bytes[i[0]++];
            if(RoleData == null || RoleData.length != count) {
                RoleData = new RoleDataBlock[count];
                for(int j = 0; j < count; j++)
                { RoleData[j] = new RoleDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { RoleData[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += GroupData.length;
            length++;
            for (int j = 0; j < RoleData.length; j++) { length += RoleData[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            GroupData.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)RoleData.length;
            for (int j = 0; j < RoleData.length; j++) { RoleData[j].ToBytes(bytes, i); }
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

            int RoleDataStart = 0;
            do
            {
                int variableLength = 0;
                int RoleDataCount = 0;

              i[0] =RoleDataStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < RoleData.length) {
                    int blockLength = RoleData[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++RoleDataCount;
                    }
                    else { break; }
                    i[0]++;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)RoleDataCount;
                for (i[0] = RoleDataStart; i[0] < RoleDataStart + RoleDataCount; i[0]++) { RoleData[i[0]].ToBytes(packet, length); }
                RoleDataStart += RoleDataCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                RoleDataStart < RoleData.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
