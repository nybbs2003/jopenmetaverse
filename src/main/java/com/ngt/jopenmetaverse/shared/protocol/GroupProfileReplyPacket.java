package com.ngt.jopenmetaverse.shared.protocol;


    public final class GroupProfileReplyPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;

            @Override
			public int getLength()
            {
                get
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
                AgentID.ToBytes(bytes, i); i += 16;
            }

        }

        /// <exclude/>
        public final class GroupDataBlock extends PacketBlock
        {
            public UUID GroupID;
            public byte[] Name;
            public byte[] Charter;
            public bool ShowInList;
            public byte[] MemberTitle;
            public ulong PowersMask;
            public UUID InsigniaID;
            public UUID FounderID;
            public int MembershipFee;
            public bool OpenEnrollment;
            public int Money;
            public int GroupMembershipCount;
            public int GroupRolesCount;
            public bool AllowPublish;
            public bool MaturePublish;
            public UUID OwnerRole;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 96;
                    if (Name != null) { length += Name.length; }
                    if (Charter != null) { length += Charter.length; }
                    if (MemberTitle != null) { length += MemberTitle.length; }
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
                    GroupID.FromBytes(bytes, i); i += 16;
                    length = bytes[i++];
                    Name = new byte[length];
                    Buffer.BlockCopy(bytes, i, Name, 0, length); i += length;
                    length = (bytes[i++] + (bytes[i++] << 8));
                    Charter = new byte[length];
                    Buffer.BlockCopy(bytes, i, Charter, 0, length); i += length;
                    ShowInList = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    length = bytes[i++];
                    MemberTitle = new byte[length];
                    Buffer.BlockCopy(bytes, i, MemberTitle, 0, length); i += length;
                    PowersMask = (ulong)((ulong)bytes[i++] + ((ulong)bytes[i++] << 8) + ((ulong)bytes[i++] << 16) + ((ulong)bytes[i++] << 24) + ((ulong)bytes[i++] << 32) + ((ulong)bytes[i++] << 40) + ((ulong)bytes[i++] << 48) + ((ulong)bytes[i++] << 56));
                    InsigniaID.FromBytes(bytes, i); i += 16;
                    FounderID.FromBytes(bytes, i); i += 16;
                    MembershipFee = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    OpenEnrollment = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    Money = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    GroupMembershipCount = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    GroupRolesCount = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    AllowPublish = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    MaturePublish = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    OwnerRole.FromBytes(bytes, i); i += 16;
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
                bytes[i++] = (byte)Name.length;
                Buffer.BlockCopy(Name, 0, bytes, i, Name.length); i += Name.length;
                bytes[i++] = (byte)(Charter.length % 256);
                bytes[i++] = (byte)((Charter.length >> 8) % 256);
                Buffer.BlockCopy(Charter, 0, bytes, i, Charter.length); i += Charter.length;
                bytes[i++] = (byte)((ShowInList) ? 1 : 0);
                bytes[i++] = (byte)MemberTitle.length;
                Buffer.BlockCopy(MemberTitle, 0, bytes, i, MemberTitle.length); i += MemberTitle.length;
                Utils.UInt64ToBytes(PowersMask, bytes, i); i += 8;
                InsigniaID.ToBytes(bytes, i); i += 16;
                FounderID.ToBytes(bytes, i); i += 16;
                Utils.IntToBytes(MembershipFee, bytes, i); i += 4;
                bytes[i++] = (byte)((OpenEnrollment) ? 1 : 0);
                Utils.IntToBytes(Money, bytes, i); i += 4;
                Utils.IntToBytes(GroupMembershipCount, bytes, i); i += 4;
                Utils.IntToBytes(GroupRolesCount, bytes, i); i += 4;
                bytes[i++] = (byte)((AllowPublish) ? 1 : 0);
                bytes[i++] = (byte)((MaturePublish) ? 1 : 0);
                OwnerRole.ToBytes(bytes, i); i += 16;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 10;
                length += AgentData.getLength();
                length += GroupData.length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public GroupDataBlock GroupData;

        public GroupProfileReplyPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.GroupProfileReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 352;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            GroupData = new GroupDataBlock();
        }

        public GroupProfileReplyPacket(byte[] bytes, int[] i) 
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
            GroupData.FromBytes(bytes, i);
        }

        public GroupProfileReplyPacket(Header head, byte[] bytes, int[] i)
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
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += GroupData.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            GroupData.ToBytes(bytes, i);
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
