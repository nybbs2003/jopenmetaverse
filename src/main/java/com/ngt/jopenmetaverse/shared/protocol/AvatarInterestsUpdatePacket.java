package com.ngt.jopenmetaverse.shared.protocol;


    public final class AvatarInterestsUpdatePacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID SessionID;

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
                    SessionID.FromBytes(bytes, i); i += 16;
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
            }

        }

        /// <exclude/>
        public final class PropertiesDataBlock extends PacketBlock
        {
            public uint WantToMask;
            public byte[] WantToText;
            public uint SkillsMask;
            public byte[] SkillsText;
            public byte[] LanguagesText;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 11;
                    if (WantToText != null) { length += WantToText.length; }
                    if (SkillsText != null) { length += SkillsText.length; }
                    if (LanguagesText != null) { length += LanguagesText.length; }
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
                    WantToMask = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    length = bytes[i++];
                    WantToText = new byte[length];
                    Buffer.BlockCopy(bytes, i, WantToText, 0, length); i += length;
                    SkillsMask = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    length = bytes[i++];
                    SkillsText = new byte[length];
                    Buffer.BlockCopy(bytes, i, SkillsText, 0, length); i += length;
                    length = bytes[i++];
                    LanguagesText = new byte[length];
                    Buffer.BlockCopy(bytes, i, LanguagesText, 0, length); i += length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UIntToBytes(WantToMask, bytes, i); i += 4;
                bytes[i++] = (byte)WantToText.length;
                Buffer.BlockCopy(WantToText, 0, bytes, i, WantToText.length); i += WantToText.length;
                Utils.UIntToBytes(SkillsMask, bytes, i); i += 4;
                bytes[i++] = (byte)SkillsText.length;
                Buffer.BlockCopy(SkillsText, 0, bytes, i, SkillsText.length); i += SkillsText.length;
                bytes[i++] = (byte)LanguagesText.length;
                Buffer.BlockCopy(LanguagesText, 0, bytes, i, LanguagesText.length); i += LanguagesText.length;
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

        public AvatarInterestsUpdatePacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.AvatarInterestsUpdate;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 175;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            PropertiesData = new PropertiesDataBlock();
        }

        public AvatarInterestsUpdatePacket(byte[] bytes, int[] i) 
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

        public AvatarInterestsUpdatePacket(Header head, byte[] bytes, int[] i)
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
