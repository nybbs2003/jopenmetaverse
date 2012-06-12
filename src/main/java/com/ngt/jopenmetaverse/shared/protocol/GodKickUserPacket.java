package com.ngt.jopenmetaverse.shared.protocol;


    public final class GodKickUserPacket extends Packet
    {
        /// <exclude/>
        public final class UserInfoBlock extends PacketBlock
        {
            public UUID GodID;
            public UUID GodSessionID;
            public UUID AgentID;
            public uint KickFlags;
            public byte[] Reason;

            @Override
			public int getLength()
            {
                                {
                    int length = 54;
                    if (Reason != null) { length += Reason.length; }
                    return length;
                }
            }

            public UserInfoBlock() { }
            public UserInfoBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    GodID.FromBytes(bytes, i); i += 16;
                    GodSessionID.FromBytes(bytes, i[0]); i[0] += 16;
                    AgentID.FromBytes(bytes, i); i += 16;
                    KickFlags = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    length = (bytes[i++] + (bytes[i++] << 8));
                    Reason = new byte[length];
                    Utils.arraycopy(bytes, i, Reason, 0, length); i += length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                GodID.ToBytes(bytes, i); i += 16;
                GodSessionID.ToBytes(bytes, i[0]); i[0] += 16;
                AgentID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.UIntToBytes(KickFlags, bytes, i); i += 4;
                bytes[i++] = (byte)(Reason.length % 256);
                bytes[i++] = (byte)((Reason.length >> 8) % 256);
                Utils.arraycopy(Reason, 0, bytes, i, Reason.length); i += Reason.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += UserInfo.length;
                return length;
            }
        }
        public UserInfoBlock UserInfo;

        public GodKickUserPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.GodKickUser;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 165;
            header.Reliable = true;
            UserInfo = new UserInfoBlock();
        }

        public GodKickUserPacket(byte[] bytes, int[] i) 
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
            UserInfo.FromBytes(bytes, i);
        }

        public GodKickUserPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            UserInfo.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += UserInfo.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            UserInfo.ToBytes(bytes, i);
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
