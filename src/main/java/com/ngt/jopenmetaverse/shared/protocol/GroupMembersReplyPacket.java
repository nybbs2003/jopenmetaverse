package com.ngt.jopenmetaverse.shared.protocol;


    public final class GroupMembersReplyPacket extends Packet
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
                    AgentID.FromBytes(bytes, i); i += 16;
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
            public int MemberCount;

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
                    GroupID.FromBytes(bytes, i); i += 16;
                    RequestID.FromBytes(bytes, i); i += 16;
                    MemberCount = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
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
                RequestID.ToBytes(bytes, i); i += 16;
                Utils.IntToBytes(MemberCount, bytes, i); i += 4;
            }

        }

        /// <exclude/>
        public final class MemberDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public int Contribution;
            public byte[] OnlineStatus;
            public ulong AgentPowers;
            public byte[] Title;
            public bool IsOwner;

            @Override
			public int getLength()
            {
                                {
                    int length = 31;
                    if (OnlineStatus != null) { length += OnlineStatus.length; }
                    if (Title != null) { length += Title.length; }
                    return length;
                }
            }

            public MemberDataBlock() { }
            public MemberDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    AgentID.FromBytes(bytes, i); i += 16;
                    Contribution = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    length = bytes[i++];
                    OnlineStatus = new byte[length];
                    Utils.arraycopy(bytes, i, OnlineStatus, 0, length); i += length;
                    AgentPowers = (ulong)((ulong)bytes[i++] + ((ulong)bytes[i++] << 8) + ((ulong)bytes[i++] << 16) + ((ulong)bytes[i++] << 24) + ((ulong)bytes[i++] << 32) + ((ulong)bytes[i++] << 40) + ((ulong)bytes[i++] << 48) + ((ulong)bytes[i++] << 56));
                    length = bytes[i++];
                    Title = new byte[length];
                    Utils.arraycopy(bytes, i, Title, 0, length); i += length;
                    IsOwner = (bytes[i++] != 0) ? (bool)true : (bool)false;
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
                Utils.IntToBytes(Contribution, bytes, i); i += 4;
                bytes[i++] = (byte)OnlineStatus.length;
                Utils.arraycopy(OnlineStatus, 0, bytes, i, OnlineStatus.length); i += OnlineStatus.length;
                Utils.UInt64ToBytes(AgentPowers, bytes, i); i += 8;
                bytes[i++] = (byte)Title.length;
                Utils.arraycopy(Title, 0, bytes, i, Title.length); i += Title.length;
                bytes[i++] = (byte)((IsOwner) ? 1 : 0);
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += AgentData.getLength();
                length += GroupData.length;
                for (int j = 0; j < MemberData.length; j++)
                    length += MemberData[j].length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public GroupDataBlock GroupData;
        public MemberDataBlock[] MemberData;

        public GroupMembersReplyPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.GroupMembersReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 367;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            GroupData = new GroupDataBlock();
            MemberData = null;
        }

        public GroupMembersReplyPacket(byte[] bytes, int[] i) 
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
            if(MemberData == null || MemberData.length != -1) {
                MemberData = new MemberDataBlock[count];
                for(int j = 0; j < count; j++)
                { MemberData[j] = new MemberDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { MemberData[j].FromBytes(bytes, i); }
        }

        public GroupMembersReplyPacket(Header head, byte[] bytes, int[] i)
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
            if(MemberData == null || MemberData.length != count) {
                MemberData = new MemberDataBlock[count];
                for(int j = 0; j < count; j++)
                { MemberData[j] = new MemberDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { MemberData[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += GroupData.length;
            length++;
            for (int j = 0; j < MemberData.length; j++) { length += MemberData[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            GroupData.ToBytes(bytes, i);
            bytes[i++] = (byte)MemberData.length;
            for (int j = 0; j < MemberData.length; j++) { MemberData[j].ToBytes(bytes, i); }
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

            int MemberDataStart = 0;
            do
            {
                int variableLength = 0;
                int MemberDataCount = 0;

                i = MemberDataStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < MemberData.length) {
                    int blockLength = MemberData[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++MemberDataCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Utils.arraycopy(fixedBytes, 0, packet, 0, length);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)MemberDataCount;
                for (i = MemberDataStart; i < MemberDataStart + MemberDataCount; i++) { MemberData[i].ToBytes(packet, ref length); }
                MemberDataStart += MemberDataCount;

                if (acksLength > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.add(packet);
            } while (
                MemberDataStart < MemberData.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
