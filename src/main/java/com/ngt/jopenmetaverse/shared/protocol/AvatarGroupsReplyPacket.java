package com.ngt.jopenmetaverse.shared.protocol;


    public final class AvatarGroupsReplyPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID AvatarID;

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
                    AvatarID.FromBytes(bytes, i[0]); i[0] += 16;
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
                AvatarID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        /// <exclude/>
        public final class GroupDataBlock extends PacketBlock
        {
            public BigInteger GroupPowers;
            public bool AcceptNotices;
            public byte[] GroupTitle;
            public UUID GroupID;
            public byte[] GroupName;
            public UUID GroupInsigniaID;

            @Override
			public int getLength()
            {
                                {
                    int length = 43;
                    if (GroupTitle != null) { length += GroupTitle.length; }
                    if (GroupName != null) { length += GroupName.length; }
                    return length;
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
                int length;
                try
                {
                    GroupPowers = new BigInteger(bytes);
                    AcceptNotices = (bytes[i[0]++] != 0) ? (bool)true : (bool)false;
                    length = bytes[i[0]++];
                    GroupTitle = new byte[length];
                    Utils.arraycopy(bytes, i[0], GroupTitle, 0, length); i[0] +=  length;
                    GroupID.FromBytes(bytes, i[0]); i[0] += 16;
                    length = bytes[i[0]++];
                    GroupName = new byte[length];
                    Utils.arraycopy(bytes, i[0], GroupName, 0, length); i[0] +=  length;
                    GroupInsigniaID.FromBytes(bytes, i[0]); i[0] += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.ulongToBytes(GroupPowers, bytes, i[0]); i[0] += 8;
                bytes[i[0]++] = (byte)((AcceptNotices) ? 1 : 0);
                bytes[i[0]++] = (byte)GroupTitle.length;
                Utils.arraycopy(GroupTitle, 0, bytes, i[0], GroupTitle.length); i[0] +=  GroupTitle.length;
                GroupID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)GroupName.length;
                Utils.arraycopy(GroupName, 0, bytes, i[0], GroupName.length); i[0] +=  GroupName.length;
                GroupInsigniaID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        /// <exclude/>
        public final class NewGroupDataBlock extends PacketBlock
        {
            public bool ListInProfile;

            @Override
			public int getLength()
            {
                                {
                    return 1;
                }
            }

            public NewGroupDataBlock() { }
            public NewGroupDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ListInProfile = (bytes[i[0]++] != 0) ? (bool)true : (bool)false;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = (byte)((ListInProfile) ? 1 : 0);
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += AgentData.getLength();
                for (int j = 0; j < GroupData.length; j++)
                    length += GroupData[j].getLength();
                length += NewGroupData.length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public GroupDataBlock[] GroupData;
        public NewGroupDataBlock NewGroupData;

        public AvatarGroupsReplyPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.AvatarGroupsReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 173;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            GroupData = null;
            NewGroupData = new NewGroupDataBlock();
        }

        public AvatarGroupsReplyPacket(byte[] bytes, int[] i) 
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
            int count = (int)bytes[i[0]++];
            if(GroupData == null || GroupData.length != -1) {
                GroupData = new GroupDataBlock[count];
                for(int j = 0; j < count; j++)
                { GroupData[j] = new GroupDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { GroupData[j].FromBytes(bytes, i); }
            NewGroupData.FromBytes(bytes, i);
        }

        public AvatarGroupsReplyPacket(Header head, byte[] bytes, int[] i)
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
            int count = (int)bytes[i[0]++];
            if(GroupData == null || GroupData.length != count) {
                GroupData = new GroupDataBlock[count];
                for(int j = 0; j < count; j++)
                { GroupData[j] = new GroupDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { GroupData[j].FromBytes(bytes, i); }
            NewGroupData.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += NewGroupData.length;
            length++;
            for (int j = 0; j < GroupData.length; j++) { length += GroupData[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)GroupData.length;
            for (int j = 0; j < GroupData.length; j++) { GroupData[j].ToBytes(bytes, i); }
            NewGroupData.ToBytes(bytes, i);
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            return new byte[][] { ToBytes() };
        }
    }

    /// <exclude/>
