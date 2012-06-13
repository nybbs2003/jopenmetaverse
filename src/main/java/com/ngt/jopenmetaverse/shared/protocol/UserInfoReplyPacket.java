package com.ngt.jopenmetaverse.shared.protocol;


    public final class UserInfoReplyPacket extends Packet
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
        public final class UserDataBlock extends PacketBlock
        {
            public bool IMViaEMail;
            public byte[] DirectoryVisibility;
            public byte[] EMail;

            @Override
			public int getLength()
            {
                                {
                    int length = 4;
                    if (DirectoryVisibility != null) { length += DirectoryVisibility.length; }
                    if (EMail != null) { length += EMail.length; }
                    return length;
                }
            }

            public UserDataBlock() { }
            public UserDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    IMViaEMail = (bytes[i[0]++] != 0) ? (bool)true : (bool)false;
                    length = bytes[i[0]++];
                    DirectoryVisibility = new byte[length];
                    Utils.arraycopy(bytes, i[0], DirectoryVisibility, 0, length); i[0] +=  length;
                    length = (bytes[i[0]++] + (bytes[i[0]++] << 8));
                    EMail = new byte[length];
                    Utils.arraycopy(bytes, i[0], EMail, 0, length); i[0] +=  length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = (byte)((IMViaEMail) ? 1 : 0);
                bytes[i[0]++] = (byte)DirectoryVisibility.length;
                Utils.arraycopy(DirectoryVisibility, 0, bytes, i[0], DirectoryVisibility.length); i[0] +=  DirectoryVisibility.length;
                bytes[i[0]++] = (byte)(EMail.length % 256);
                bytes[i[0]++] = (byte)((EMail.length >> 8) % 256);
                Utils.arraycopy(EMail, 0, bytes, i[0], EMail.length); i[0] +=  EMail.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += AgentData.getLength();
                length += UserData.length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public UserDataBlock UserData;

        public UserInfoReplyPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.UserInfoReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 400;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            UserData = new UserDataBlock();
        }

        public UserInfoReplyPacket(byte[] bytes, int[] i) 
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
            UserData.FromBytes(bytes, i);
        }

        public UserInfoReplyPacket(Header head, byte[] bytes, int[] i)
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
            UserData.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += UserData.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            UserData.ToBytes(bytes, i);
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
