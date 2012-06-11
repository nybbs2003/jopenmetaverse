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
                get
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
                    AvatarID.FromBytes(bytes, i); i += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                AgentID.ToBytes(bytes, i); i += 16;
                AvatarID.ToBytes(bytes, i); i += 16;
            }

        }

        /// <exclude/>
        public final class GroupDataBlock extends PacketBlock
        {
            public ulong GroupPowers;
            public bool AcceptNotices;
            public byte[] GroupTitle;
            public UUID GroupID;
            public byte[] GroupName;
            public UUID GroupInsigniaID;

            @Override
			public int getLength()
            {
                get
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
                    GroupPowers = (ulong)((ulong)bytes[i++] + ((ulong)bytes[i++] << 8) + ((ulong)bytes[i++] << 16) + ((ulong)bytes[i++] << 24) + ((ulong)bytes[i++] << 32) + ((ulong)bytes[i++] << 40) + ((ulong)bytes[i++] << 48) + ((ulong)bytes[i++] << 56));
                    AcceptNotices = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    length = bytes[i++];
                    GroupTitle = new byte[length];
                    Buffer.BlockCopy(bytes, i, GroupTitle, 0, length); i += length;
                    GroupID.FromBytes(bytes, i); i += 16;
                    length = bytes[i++];
                    GroupName = new byte[length];
                    Buffer.BlockCopy(bytes, i, GroupName, 0, length); i += length;
                    GroupInsigniaID.FromBytes(bytes, i); i += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UInt64ToBytes(GroupPowers, bytes, i); i += 8;
                bytes[i++] = (byte)((AcceptNotices) ? 1 : 0);
                bytes[i++] = (byte)GroupTitle.length;
                Buffer.BlockCopy(GroupTitle, 0, bytes, i, GroupTitle.length); i += GroupTitle.length;
                GroupID.ToBytes(bytes, i); i += 16;
                bytes[i++] = (byte)GroupName.length;
                Buffer.BlockCopy(GroupName, 0, bytes, i, GroupName.length); i += GroupName.length;
                GroupInsigniaID.ToBytes(bytes, i); i += 16;
            }

        }

        /// <exclude/>
        public final class NewGroupDataBlock extends PacketBlock
        {
            public bool ListInProfile;

            @Override
			public int getLength()
            {
                get
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
                    ListInProfile = (bytes[i++] != 0) ? (bool)true : (bool)false;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i++] = (byte)((ListInProfile) ? 1 : 0);
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 11;
                length += AgentData.getLength();
                for (int j = 0; j < GroupData.length; j++)
                    length += GroupData[j].length;
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
                packetEnd = Helpers.ZeroDecode(bytes, packetEnd + 1, zeroBuffer) - 1;
                bytes = zeroBuffer;
            }
            AgentData.FromBytes(bytes, i);
            int count = (int)bytes[i++];
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
            int count = (int)bytes[i++];
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
            for (int j = 0; j < GroupData.length; j++) { length += GroupData[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            bytes[i++] = (byte)GroupData.length;
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