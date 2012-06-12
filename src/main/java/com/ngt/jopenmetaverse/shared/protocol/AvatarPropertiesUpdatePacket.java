package com.ngt.jopenmetaverse.shared.protocol;


    public final class AvatarPropertiesUpdatePacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID SessionID;

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
                    AgentID.FromBytes(bytes, i); i += 16;
                    SessionID.FromBytes(bytes, i[0]); i[0] += 16;
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
                SessionID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        /// <exclude/>
        public final class PropertiesDataBlock extends PacketBlock
        {
            public UUID ImageID;
            public UUID FLImageID;
            public byte[] AboutText;
            public byte[] FLAboutText;
            public bool AllowPublish;
            public bool MaturePublish;
            public byte[] ProfileURL;

            @Override
			public int getLength()
            {
                                {
                    int length = 38;
                    if (AboutText != null) { length += AboutText.length; }
                    if (FLAboutText != null) { length += FLAboutText.length; }
                    if (ProfileURL != null) { length += ProfileURL.length; }
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
                    length = (bytes[i++] + (bytes[i++] << 8));
                    AboutText = new byte[length];
                    Utils.arraycopy(bytes, i, AboutText, 0, length); i += length;
                    length = bytes[i++];
                    FLAboutText = new byte[length];
                    Utils.arraycopy(bytes, i, FLAboutText, 0, length); i += length;
                    AllowPublish = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    MaturePublish = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    length = bytes[i++];
                    ProfileURL = new byte[length];
                    Utils.arraycopy(bytes, i, ProfileURL, 0, length); i += length;
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
                bytes[i++] = (byte)(AboutText.length % 256);
                bytes[i++] = (byte)((AboutText.length >> 8) % 256);
                Utils.arraycopy(AboutText, 0, bytes, i, AboutText.length); i += AboutText.length;
                bytes[i++] = (byte)FLAboutText.length;
                Utils.arraycopy(FLAboutText, 0, bytes, i, FLAboutText.length); i += FLAboutText.length;
                bytes[i++] = (byte)((AllowPublish) ? 1 : 0);
                bytes[i++] = (byte)((MaturePublish) ? 1 : 0);
                bytes[i++] = (byte)ProfileURL.length;
                Utils.arraycopy(ProfileURL, 0, bytes, i, ProfileURL.length); i += ProfileURL.length;
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

        public AvatarPropertiesUpdatePacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.AvatarPropertiesUpdate;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 174;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            PropertiesData = new PropertiesDataBlock();
        }

        public AvatarPropertiesUpdatePacket(byte[] bytes, int[] i) 
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

        public AvatarPropertiesUpdatePacket(Header head, byte[] bytes, int[] i)
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
