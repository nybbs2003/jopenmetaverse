package com.ngt.jopenmetaverse.shared.protocol;

import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.types.Vector3;
import com.ngt.jopenmetaverse.shared.util.Utils;


    public final class ParcelPropertiesPacket extends Packet
    {
        public final class ParcelDataBlock extends PacketBlock
        {
            public int RequestResult;
            public int SequenceID;
            public boolean SnapSelection;
            public int SelfCount;
            public int OtherCount;
            public int PublicCount;
            public int LocalID;
            public UUID OwnerID;
            public boolean IsGroupOwned;
            public long AuctionID;
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
            public long ParcelFlags;
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
            public boolean RegionPushOverride;
            public boolean RegionDenyAnonymous;
            public boolean RegionDenyIdentified;
            public boolean RegionDenyTransacted;

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
            public ParcelDataBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    RequestResult = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    SequenceID = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    SnapSelection = (bytes[i[0]++] != 0) ? true : false;
                    SelfCount = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    OtherCount = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    PublicCount = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    LocalID = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    OwnerID.FromBytes(bytes, i[0]); i[0] += 16;
                    IsGroupOwned = (bytes[i[0]++] != 0) ? true : false;
                    AuctionID = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    ClaimDate = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    ClaimPrice = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    RentPrice = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    AABBMin.fromBytes(bytes, i[0]); i[0] += 12;
                    AABBMax.fromBytes(bytes, i[0]); i[0] += 12;
                    length = Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    Bitmap = new byte[length];
                    Utils.arraycopy(bytes, i[0], Bitmap, 0, length); i[0] +=  length;
                    Area = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    Status = (byte)bytes[i[0]++];
                    SimWideMaxPrims = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    SimWideTotalPrims = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    MaxPrims = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    TotalPrims = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    OwnerPrims = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    GroupPrims = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    OtherPrims = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    SelectedPrims = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    ParcelPrimBonus = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    OtherCleanTime = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    ParcelFlags = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    SalePrice = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    Name = new byte[length];
                    Utils.arraycopy(bytes, i[0], Name, 0, length); i[0] +=  length;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    Desc = new byte[length];
                    Utils.arraycopy(bytes, i[0], Desc, 0, length); i[0] +=  length;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    MusicURL = new byte[length];
                    Utils.arraycopy(bytes, i[0], MusicURL, 0, length); i[0] +=  length;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    MediaURL = new byte[length];
                    Utils.arraycopy(bytes, i[0], MediaURL, 0, length); i[0] +=  length;
                    MediaID.FromBytes(bytes, i[0]); i[0] += 16;
                    MediaAutoScale = (byte)bytes[i[0]++];
                    GroupID.FromBytes(bytes, i[0]); i[0] += 16;
                    PassPrice = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    PassHours = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    Category = (byte)bytes[i[0]++];
                    AuthBuyerID.FromBytes(bytes, i[0]); i[0] += 16;
                    SnapshotID.FromBytes(bytes, i[0]); i[0] += 16;
                    UserLocation.fromBytes(bytes, i[0]); i[0] += 12;
                    UserLookAt.fromBytes(bytes, i[0]); i[0] += 12;
                    LandingType = (byte)bytes[i[0]++];
                    RegionPushOverride = (bytes[i[0]++] != 0) ? true : false;
                    RegionDenyAnonymous = (bytes[i[0]++] != 0) ? true : false;
                    RegionDenyIdentified = (bytes[i[0]++] != 0) ? true : false;
                    RegionDenyTransacted = (bytes[i[0]++] != 0) ? true : false;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.intToBytes(RequestResult, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(SequenceID, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = (byte)((SnapSelection) ? 1 : 0);
                Utils.intToBytes(SelfCount, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(OtherCount, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(PublicCount, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(LocalID, bytes, i[0]); i[0] += 4;
                OwnerID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)((IsGroupOwned) ? 1 : 0);
                Utils.uintToBytes(AuctionID, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(ClaimDate, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(ClaimPrice, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(RentPrice, bytes, i[0]); i[0] += 4;
                AABBMin.toBytes(bytes, i[0]); i[0] += 12;
                AABBMax.toBytes(bytes, i[0]); i[0] += 12;
                bytes[i[0]++] = (byte)(Bitmap.length % 256);
                bytes[i[0]++] = (byte)((Bitmap.length >> 8) % 256);
                Utils.arraycopy(Bitmap, 0, bytes, i[0], Bitmap.length); i[0] +=  Bitmap.length;
                Utils.intToBytes(Area, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = Status;
                Utils.intToBytes(SimWideMaxPrims, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(SimWideTotalPrims, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(MaxPrims, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(TotalPrims, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(OwnerPrims, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(GroupPrims, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(OtherPrims, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(SelectedPrims, bytes, i[0]); i[0] += 4;
                Utils.floatToBytes(ParcelPrimBonus, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(OtherCleanTime, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(ParcelFlags, bytes, i[0]); i[0] += 4;
                Utils.intToBytes(SalePrice, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = (byte)Name.length;
                Utils.arraycopy(Name, 0, bytes, i[0], Name.length); i[0] +=  Name.length;
                bytes[i[0]++] = (byte)Desc.length;
                Utils.arraycopy(Desc, 0, bytes, i[0], Desc.length); i[0] +=  Desc.length;
                bytes[i[0]++] = (byte)MusicURL.length;
                Utils.arraycopy(MusicURL, 0, bytes, i[0], MusicURL.length); i[0] +=  MusicURL.length;
                bytes[i[0]++] = (byte)MediaURL.length;
                Utils.arraycopy(MediaURL, 0, bytes, i[0], MediaURL.length); i[0] +=  MediaURL.length;
                MediaID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = MediaAutoScale;
                GroupID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.intToBytes(PassPrice, bytes, i[0]); i[0] += 4;
                Utils.floatToBytes(PassHours, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = Category;
                AuthBuyerID.ToBytes(bytes, i[0]); i[0] += 16;
                SnapshotID.ToBytes(bytes, i[0]); i[0] += 16;
                UserLocation.toBytes(bytes, i[0]); i[0] += 12;
                UserLookAt.toBytes(bytes, i[0]); i[0] += 12;
                bytes[i[0]++] = LandingType;
                bytes[i[0]++] = (byte)((RegionPushOverride) ? 1 : 0);
                bytes[i[0]++] = (byte)((RegionDenyAnonymous) ? 1 : 0);
                bytes[i[0]++] = (byte)((RegionDenyIdentified) ? 1 : 0);
                bytes[i[0]++] = (byte)((RegionDenyTransacted) ? 1 : 0);
            }

        }

        public final class AgeVerificationBlockBlock extends PacketBlock
        {
            public boolean RegionDenyAgeUnverified;

            @Override
			public int getLength()
            {
                    return 1;
            }

            public AgeVerificationBlockBlock() { }
            public AgeVerificationBlockBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    RegionDenyAgeUnverified = (bytes[i[0]++] != 0) ? true : false;
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
                length += ParcelData.getLength();
                length += AgeVerificationBlock.getLength();
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

        public ParcelPropertiesPacket(byte[] bytes, int[] i) throws MalformedDataException 
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(bytes, i, packetEnd, null);
        }

        @Override
		public void FromBytes(byte[] bytes, int[] i, int[] packetEnd, byte[] zeroBuffer) throws MalformedDataException
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

        public ParcelPropertiesPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd) throws MalformedDataException
        {
            this.header =  header;
            ParcelData.FromBytes(bytes, i);
            AgeVerificationBlock.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 7;
            length += ParcelData.getLength();
            length += AgeVerificationBlock.getLength();
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
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