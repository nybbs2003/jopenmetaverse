package com.ngt.jopenmetaverse.shared.protocol;


    public final class ParcelPropertiesUpdatePacket extends Packet
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
        public final class ParcelDataBlock extends PacketBlock
        {
            public int LocalID;
            public uint Flags;
            public uint ParcelFlags;
            public int SalePrice;
            public byte[] Name;
            public byte[] Desc;
            public byte[] MusicURL;
            public byte[] MediaURL;
            public UUID MediaID;
            public byte MediaAutoScale;
            public UUID GroupID;
            public int PassPrice;
            public float PassHours;
            public byte Category;
            public UUID AuthBuyerID;
            public UUID SnapshotID;
            public Vector3 UserLocation;
            public Vector3 UserLookAt;
            public byte LandingType;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 119;
                    if (Name != null) { length += Name.length; }
                    if (Desc != null) { length += Desc.length; }
                    if (MusicURL != null) { length += MusicURL.length; }
                    if (MediaURL != null) { length += MediaURL.length; }
                    return length;
                }
            }

            public ParcelDataBlock() { }
            public ParcelDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    LocalID = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Flags = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    ParcelFlags = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    SalePrice = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    length = bytes[i++];
                    Name = new byte[length];
                    Buffer.BlockCopy(bytes, i, Name, 0, length); i += length;
                    length = bytes[i++];
                    Desc = new byte[length];
                    Buffer.BlockCopy(bytes, i, Desc, 0, length); i += length;
                    length = bytes[i++];
                    MusicURL = new byte[length];
                    Buffer.BlockCopy(bytes, i, MusicURL, 0, length); i += length;
                    length = bytes[i++];
                    MediaURL = new byte[length];
                    Buffer.BlockCopy(bytes, i, MediaURL, 0, length); i += length;
                    MediaID.FromBytes(bytes, i); i += 16;
                    MediaAutoScale = (byte)bytes[i++];
                    GroupID.FromBytes(bytes, i); i += 16;
                    PassPrice = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    PassHours = Utils.BytesToFloat(bytes, i); i += 4;
                    Category = (byte)bytes[i++];
                    AuthBuyerID.FromBytes(bytes, i); i += 16;
                    SnapshotID.FromBytes(bytes, i); i += 16;
                    UserLocation.FromBytes(bytes, i); i += 12;
                    UserLookAt.FromBytes(bytes, i); i += 12;
                    LandingType = (byte)bytes[i++];
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.IntToBytes(LocalID, bytes, i); i += 4;
                Utils.UIntToBytes(Flags, bytes, i); i += 4;
                Utils.UIntToBytes(ParcelFlags, bytes, i); i += 4;
                Utils.IntToBytes(SalePrice, bytes, i); i += 4;
                bytes[i++] = (byte)Name.length;
                Buffer.BlockCopy(Name, 0, bytes, i, Name.length); i += Name.length;
                bytes[i++] = (byte)Desc.length;
                Buffer.BlockCopy(Desc, 0, bytes, i, Desc.length); i += Desc.length;
                bytes[i++] = (byte)MusicURL.length;
                Buffer.BlockCopy(MusicURL, 0, bytes, i, MusicURL.length); i += MusicURL.length;
                bytes[i++] = (byte)MediaURL.length;
                Buffer.BlockCopy(MediaURL, 0, bytes, i, MediaURL.length); i += MediaURL.length;
                MediaID.ToBytes(bytes, i); i += 16;
                bytes[i++] = MediaAutoScale;
                GroupID.ToBytes(bytes, i); i += 16;
                Utils.IntToBytes(PassPrice, bytes, i); i += 4;
                Utils.FloatToBytes(PassHours, bytes, i); i += 4;
                bytes[i++] = Category;
                AuthBuyerID.ToBytes(bytes, i); i += 16;
                SnapshotID.ToBytes(bytes, i); i += 16;
                UserLocation.ToBytes(bytes, i); i += 12;
                UserLookAt.ToBytes(bytes, i); i += 12;
                bytes[i++] = LandingType;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 10;
                length += AgentData.getLength();
                length += ParcelData.length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public ParcelDataBlock ParcelData;

        public ParcelPropertiesUpdatePacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.ParcelPropertiesUpdate;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 198;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            ParcelData = new ParcelDataBlock();
        }

        public ParcelPropertiesUpdatePacket(byte[] bytes, int[] i) 
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
            ParcelData.FromBytes(bytes, i);
        }

        public ParcelPropertiesUpdatePacket(Header head, byte[] bytes, int[] i)
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
            ParcelData.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += ParcelData.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            ParcelData.ToBytes(bytes, i);
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
