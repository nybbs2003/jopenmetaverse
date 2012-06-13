package com.ngt.jopenmetaverse.shared.protocol;


    public final class ParcelPropertiesPacket extends Packet
    {
        /// <exclude/>
        public final class ParcelDataBlock extends PacketBlock
        {
            public int RequestResult;
            public int SequenceID;
            public bool SnapSelection;
            public int SelfCount;
            public int OtherCount;
            public int PublicCount;
            public int LocalID;
            public UUID OwnerID;
            public bool IsGroupOwned;
            public uint AuctionID;
            public int ClaimDate;
            public int ClaimPrice;
            public int RentPrice;
            public Vector3 AABBMin;
            public Vector3 AABBMax;
            public byte[] Bitmap;
            public int Area;
            public byte Status;
            public int SimWideMaxPrims;
            public int SimWideTotalPrims;
            public int MaxPrims;
            public int TotalPrims;
            public int OwnerPrims;
            public int GroupPrims;
            public int OtherPrims;
            public int SelectedPrims;
            public float ParcelPrimBonus;
            public int OtherCleanTime;
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
            public bool RegionPushOverride;
            public bool RegionDenyAnonymous;
            public bool RegionDenyIdentified;
            public bool RegionDenyTransacted;

            @Override
			public int getLength()
            {
                                {
                    int length = 244;
                    if (Bitmap != null) { length += Bitmap.length; }
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
                    RequestResult = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    SequenceID = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    SnapSelection = (bytes[i[0]++] != 0) ? (bool)true : (bool)false;
                    SelfCount = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    OtherCount = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    PublicCount = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    LocalID = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    OwnerID.FromBytes(bytes, i[0]); i[0] += 16;
                    IsGroupOwned = (bytes[i[0]++] != 0) ? (bool)true : (bool)false;
                    AuctionID = (uint)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    ClaimDate = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    ClaimPrice = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    RentPrice = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    AABBMin.FromBytes(bytes, i[0]); i[0] += 12;
                    AABBMax.FromBytes(bytes, i[0]); i[0] += 12;
                    length = (bytes[i[0]++] + (bytes[i[0]++] << 8));
                    Bitmap = new byte[length];
                    Utils.arraycopy(bytes, i, Bitmap, 0, length); i[0] +=  length;
                    Area = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    Status = (byte)bytes[i[0]++];
                    SimWideMaxPrims = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    SimWideTotalPrims = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    MaxPrims = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    TotalPrims = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    OwnerPrims = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    GroupPrims = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    OtherPrims = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    SelectedPrims = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    ParcelPrimBonus = Utils.BytesToFloat(bytes, i); i += 4;
                    OtherCleanTime = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    ParcelFlags = (uint)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    SalePrice = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    length = bytes[i[0]++];
                    Name = new byte[length];
                    Utils.arraycopy(bytes, i, Name, 0, length); i[0] +=  length;
                    length = bytes[i[0]++];
                    Desc = new byte[length];
                    Utils.arraycopy(bytes, i, Desc, 0, length); i[0] +=  length;
                    length = bytes[i[0]++];
                    MusicURL = new byte[length];
                    Utils.arraycopy(bytes, i, MusicURL, 0, length); i[0] +=  length;
                    length = bytes[i[0]++];
                    MediaURL = new byte[length];
                    Utils.arraycopy(bytes, i, MediaURL, 0, length); i[0] +=  length;
                    MediaID.FromBytes(bytes, i[0]); i[0] += 16;
                    MediaAutoScale = (byte)bytes[i[0]++];
                    GroupID.FromBytes(bytes, i[0]); i[0] += 16;
                    PassPrice = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    PassHours = Utils.BytesToFloat(bytes, i); i += 4;
                    Category = (byte)bytes[i[0]++];
                    AuthBuyerID.FromBytes(bytes, i[0]); i[0] += 16;
                    SnapshotID.FromBytes(bytes, i[0]); i[0] += 16;
                    UserLocation.FromBytes(bytes, i[0]); i[0] += 12;
                    UserLookAt.FromBytes(bytes, i[0]); i[0] += 12;
                    LandingType = (byte)bytes[i[0]++];
                    RegionPushOverride = (bytes[i[0]++] != 0) ? (bool)true : (bool)false;
                    RegionDenyAnonymous = (bytes[i[0]++] != 0) ? (bool)true : (bool)false;
                    RegionDenyIdentified = (bytes[i[0]++] != 0) ? (bool)true : (bool)false;
                    RegionDenyTransacted = (bytes[i[0]++] != 0) ? (bool)true : (bool)false;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.IntToBytes(RequestResult, bytes, i); i += 4;
                Utils.IntToBytes(SequenceID, bytes, i); i += 4;
                bytes[i[0]++] = (byte)((SnapSelection) ? 1 : 0);
                Utils.IntToBytes(SelfCount, bytes, i); i += 4;
                Utils.IntToBytes(OtherCount, bytes, i); i += 4;
                Utils.IntToBytes(PublicCount, bytes, i); i += 4;
                Utils.IntToBytes(LocalID, bytes, i); i += 4;
                OwnerID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)((IsGroupOwned) ? 1 : 0);
                Utils.UIntToBytes(AuctionID, bytes, i); i += 4;
                Utils.IntToBytes(ClaimDate, bytes, i); i += 4;
                Utils.IntToBytes(ClaimPrice, bytes, i); i += 4;
                Utils.IntToBytes(RentPrice, bytes, i); i += 4;
                AABBMin.ToBytes(bytes, i[0]); i[0] += 12;
                AABBMax.ToBytes(bytes, i[0]); i[0] += 12;
                bytes[i[0]++] = (byte)(Bitmap.length % 256);
                bytes[i[0]++] = (byte)((Bitmap.length >> 8) % 256);
                Utils.arraycopy(Bitmap, 0, bytes, i, Bitmap.length); i[0] +=  Bitmap.length;
                Utils.IntToBytes(Area, bytes, i); i += 4;
                bytes[i[0]++] = Status;
                Utils.IntToBytes(SimWideMaxPrims, bytes, i); i += 4;
                Utils.IntToBytes(SimWideTotalPrims, bytes, i); i += 4;
                Utils.IntToBytes(MaxPrims, bytes, i); i += 4;
                Utils.IntToBytes(TotalPrims, bytes, i); i += 4;
                Utils.IntToBytes(OwnerPrims, bytes, i); i += 4;
                Utils.IntToBytes(GroupPrims, bytes, i); i += 4;
                Utils.IntToBytes(OtherPrims, bytes, i); i += 4;
                Utils.IntToBytes(SelectedPrims, bytes, i); i += 4;
                Utils.FloatToBytes(ParcelPrimBonus, bytes, i); i += 4;
                Utils.IntToBytes(OtherCleanTime, bytes, i); i += 4;
                Utils.UIntToBytes(ParcelFlags, bytes, i); i += 4;
                Utils.IntToBytes(SalePrice, bytes, i); i += 4;
                bytes[i[0]++] = (byte)Name.length;
                Utils.arraycopy(Name, 0, bytes, i, Name.length); i[0] +=  Name.length;
                bytes[i[0]++] = (byte)Desc.length;
                Utils.arraycopy(Desc, 0, bytes, i, Desc.length); i[0] +=  Desc.length;
                bytes[i[0]++] = (byte)MusicURL.length;
                Utils.arraycopy(MusicURL, 0, bytes, i, MusicURL.length); i[0] +=  MusicURL.length;
                bytes[i[0]++] = (byte)MediaURL.length;
                Utils.arraycopy(MediaURL, 0, bytes, i, MediaURL.length); i[0] +=  MediaURL.length;
                MediaID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = MediaAutoScale;
                GroupID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.IntToBytes(PassPrice, bytes, i); i += 4;
                Utils.FloatToBytes(PassHours, bytes, i); i += 4;
                bytes[i[0]++] = Category;
                AuthBuyerID.ToBytes(bytes, i[0]); i[0] += 16;
                SnapshotID.ToBytes(bytes, i[0]); i[0] += 16;
                UserLocation.ToBytes(bytes, i[0]); i[0] += 12;
                UserLookAt.ToBytes(bytes, i[0]); i[0] += 12;
                bytes[i[0]++] = LandingType;
                bytes[i[0]++] = (byte)((RegionPushOverride) ? 1 : 0);
                bytes[i[0]++] = (byte)((RegionDenyAnonymous) ? 1 : 0);
                bytes[i[0]++] = (byte)((RegionDenyIdentified) ? 1 : 0);
                bytes[i[0]++] = (byte)((RegionDenyTransacted) ? 1 : 0);
            }

        }

        /// <exclude/>
        public final class AgeVerificationBlockBlock extends PacketBlock
        {
            public bool RegionDenyAgeUnverified;

            @Override
			public int getLength()
            {
                                {
                    return 1;
                }
            }

            public AgeVerificationBlockBlock() { }
            public AgeVerificationBlockBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    RegionDenyAgeUnverified = (bytes[i[0]++] != 0) ? (bool)true : (bool)false;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = (byte)((RegionDenyAgeUnverified) ? 1 : 0);
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 7;
                length += ParcelData.length;
                length += AgeVerificationBlock.length;
                return length;
            }
        }
        public ParcelDataBlock ParcelData;
        public AgeVerificationBlockBlock AgeVerificationBlock;

        public ParcelPropertiesPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.ParcelProperties;
            this.header =  new Header();
            header.Frequency = PacketFrequency.High;
            header.ID = 23;
            header.Reliable = true;
            header.Zerocoded = true;
            ParcelData = new ParcelDataBlock();
            AgeVerificationBlock = new AgeVerificationBlockBlock();
        }

        public ParcelPropertiesPacket(byte[] bytes, int[] i) 
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
            ParcelData.FromBytes(bytes, i);
            AgeVerificationBlock.FromBytes(bytes, i);
        }

        public ParcelPropertiesPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            ParcelData.FromBytes(bytes, i);
            AgeVerificationBlock.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 7;
            length += ParcelData.length;
            length += AgeVerificationBlock.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            ParcelData.ToBytes(bytes, i);
            AgeVerificationBlock.ToBytes(bytes, i);
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
