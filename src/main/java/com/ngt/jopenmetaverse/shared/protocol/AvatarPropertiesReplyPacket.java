package com.ngt.jopenmetaverse.shared.protocol;


    public final class AvatarPropertiesReplyPacket extends Packet
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
        public final class PropertiesDataBlock extends PacketBlock
        {
            public UUID ImageID;
            public UUID FLImageID;
            public UUID PartnerID;
            public byte[] AboutText;
            public byte[] FLAboutText;
            public byte[] BornOn;
            public byte[] ProfileURL;
            public byte[] CharterMember;
            public uint Flags;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 58;
                    if (AboutText != null) { length += AboutText.length; }
                    if (FLAboutText != null) { length += FLAboutText.length; }
                    if (BornOn != null) { length += BornOn.length; }
                    if (ProfileURL != null) { length += ProfileURL.length; }
                    if (CharterMember != null) { length += CharterMember.length; }
                    return length;
                }
            }

            public PropertiesDataBlock() { }
            public PropertiesDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    ImageID.FromBytes(bytes, i); i += 16;
                    FLImageID.FromBytes(bytes, i); i += 16;
                    PartnerID.FromBytes(bytes, i); i += 16;
                    length = (bytes[i++] + (bytes[i++] << 8));
                    AboutText = new byte[length];
                    Buffer.BlockCopy(bytes, i, AboutText, 0, length); i += length;
                    length = bytes[i++];
                    FLAboutText = new byte[length];
                    Buffer.BlockCopy(bytes, i, FLAboutText, 0, length); i += length;
                    length = bytes[i++];
                    BornOn = new byte[length];
                    Buffer.BlockCopy(bytes, i, BornOn, 0, length); i += length;
                    length = bytes[i++];
                    ProfileURL = new byte[length];
                    Buffer.BlockCopy(bytes, i, ProfileURL, 0, length); i += length;
                    length = bytes[i++];
                    CharterMember = new byte[length];
                    Buffer.BlockCopy(bytes, i, CharterMember, 0, length); i += length;
                    Flags = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ImageID.ToBytes(bytes, i); i += 16;
                FLImageID.ToBytes(bytes, i); i += 16;
                PartnerID.ToBytes(bytes, i); i += 16;
                bytes[i++] = (byte)(AboutText.length % 256);
                bytes[i++] = (byte)((AboutText.length >> 8) % 256);
                Buffer.BlockCopy(AboutText, 0, bytes, i, AboutText.length); i += AboutText.length;
                bytes[i++] = (byte)FLAboutText.length;
                Buffer.BlockCopy(FLAboutText, 0, bytes, i, FLAboutText.length); i += FLAboutText.length;
                bytes[i++] = (byte)BornOn.length;
                Buffer.BlockCopy(BornOn, 0, bytes, i, BornOn.length); i += BornOn.length;
                bytes[i++] = (byte)ProfileURL.length;
                Buffer.BlockCopy(ProfileURL, 0, bytes, i, ProfileURL.length); i += ProfileURL.length;
                bytes[i++] = (byte)CharterMember.length;
                Buffer.BlockCopy(CharterMember, 0, bytes, i, CharterMember.length); i += CharterMember.length;
                Utils.UIntToBytes(Flags, bytes, i); i += 4;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 10;
                length += AgentData.getLength();
                length += PropertiesData.length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public PropertiesDataBlock PropertiesData;

        public AvatarPropertiesReplyPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.AvatarPropertiesReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 171;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            PropertiesData = new PropertiesDataBlock();
        }

        public AvatarPropertiesReplyPacket(byte[] bytes, int[] i) 
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
            PropertiesData.FromBytes(bytes, i);
        }

        public AvatarPropertiesReplyPacket(Header head, byte[] bytes, int[] i)
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
            PropertiesData.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += PropertiesData.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            PropertiesData.ToBytes(bytes, i);
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
