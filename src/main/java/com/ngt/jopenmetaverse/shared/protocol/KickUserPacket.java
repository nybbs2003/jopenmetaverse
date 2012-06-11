package com.ngt.jopenmetaverse.shared.protocol;


    public final class KickUserPacket extends Packet
    {
        /// <exclude/>
        public final class TargetBlockBlock extends PacketBlock
        {
            public uint TargetIP;
            public ushort TargetPort;

            @Override
			public int getLength()
            {
                get
                {
                    return 6;
                }
            }

            public TargetBlockBlock() { }
            public TargetBlockBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    TargetIP = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    TargetPort = (ushort)((bytes[i++] << 8) + bytes[i++]);
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UIntToBytes(TargetIP, bytes, i); i += 4;
                bytes[i++] = (byte)((TargetPort >> 8) % 256);
                bytes[i++] = (byte)(TargetPort % 256);
            }

        }

        /// <exclude/>
        public final class UserInfoBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID SessionID;
            public byte[] Reason;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 34;
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
                    AgentID.FromBytes(bytes, i); i += 16;
                    SessionID.FromBytes(bytes, i); i += 16;
                    length = (bytes[i++] + (bytes[i++] << 8));
                    Reason = new byte[length];
                    Buffer.BlockCopy(bytes, i, Reason, 0, length); i += length;
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
                SessionID.ToBytes(bytes, i); i += 16;
                bytes[i++] = (byte)(Reason.length % 256);
                bytes[i++] = (byte)((Reason.length >> 8) % 256);
                Buffer.BlockCopy(Reason, 0, bytes, i, Reason.length); i += Reason.length;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 10;
                length += TargetBlock.length;
                length += UserInfo.length;
                return length;
            }
        }
        public TargetBlockBlock TargetBlock;
        public UserInfoBlock UserInfo;

        public KickUserPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.KickUser;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 163;
            header.Reliable = true;
            TargetBlock = new TargetBlockBlock();
            UserInfo = new UserInfoBlock();
        }

        public KickUserPacket(byte[] bytes, int[] i) 
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
            TargetBlock.FromBytes(bytes, i);
            UserInfo.FromBytes(bytes, i);
        }

        public KickUserPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            TargetBlock.FromBytes(bytes, i);
            UserInfo.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += TargetBlock.length;
            length += UserInfo.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            TargetBlock.ToBytes(bytes, i);
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