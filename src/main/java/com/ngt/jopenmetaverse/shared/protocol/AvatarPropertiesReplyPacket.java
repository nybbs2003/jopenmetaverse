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
            public long Flags;

            @Override
			public int getLength()
            {
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
                    ImageID.FromBytes(bytes, i[0]); i[0] += 16;
                    FLImageID.FromBytes(bytes, i[0]); i[0] += 16;
                    PartnerID.FromBytes(bytes, i[0]); i[0] += 16;
                    length = Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    AboutText = new byte[length];
                    Utils.arraycopy(bytes, i[0], AboutText, 0, length); i[0] +=  length;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    FLAboutText = new byte[length];
                    Utils.arraycopy(bytes, i[0], FLAboutText, 0, length); i[0] +=  length;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    BornOn = new byte[length];
                    Utils.arraycopy(bytes, i[0], BornOn, 0, length); i[0] +=  length;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    ProfileURL = new byte[length];
                    Utils.arraycopy(bytes, i[0], ProfileURL, 0, length); i[0] +=  length;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    CharterMember = new byte[length];
                    Utils.arraycopy(bytes, i[0], CharterMember, 0, length); i[0] +=  length;
                    Flags = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ImageID.ToBytes(bytes, i[0]); i[0] += 16;
                FLImageID.ToBytes(bytes, i[0]); i[0] += 16;
                PartnerID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)(AboutText.length % 256);
                bytes[i[0]++] = (byte)((AboutText.length >> 8) % 256);
                Utils.arraycopy(AboutText, 0, bytes, i[0], AboutText.length); i[0] +=  AboutText.length;
                bytes[i[0]++] = (byte)FLAboutText.length;
                Utils.arraycopy(FLAboutText, 0, bytes, i[0], FLAboutText.length); i[0] +=  FLAboutText.length;
                bytes[i[0]++] = (byte)BornOn.length;
                Utils.arraycopy(BornOn, 0, bytes, i[0], BornOn.length); i[0] +=  BornOn.length;
                bytes[i[0]++] = (byte)ProfileURL.length;
                Utils.arraycopy(ProfileURL, 0, bytes, i[0], ProfileURL.length); i[0] +=  ProfileURL.length;
                bytes[i[0]++] = (byte)CharterMember.length;
                Utils.arraycopy(CharterMember, 0, bytes, i[0], CharterMember.length); i[0] +=  CharterMember.length;
                Utils.uintToBytes(Flags, bytes, i[0]); i[0] += 4;
            }

        }

        @Override
			public int getLength()
        {
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
                packetEnd[0] = Helpers.ZeroDecode(bytes, packetEnd[0] + 1, zeroBuffer) - 1;
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
            int[] i = new int[]{0};
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
