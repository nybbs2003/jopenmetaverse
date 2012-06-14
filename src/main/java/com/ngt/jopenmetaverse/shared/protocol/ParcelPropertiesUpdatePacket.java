package com.ngt.jopenmetaverse.shared.protocol;

import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.types.Vector3;
import com.ngt.jopenmetaverse.shared.util.Utils;


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
                                {
                    return 32;
                }
            }

            public AgentDataBlock() { }
            public AgentDataBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    AgentID.FromBytes(bytes, i[0]); i[0] += 16;
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
        public final class ParcelDataBlock extends PacketBlock
        {
            public int LocalID;
            public long Flags;
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

            @Override
			public int getLength()
            {
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
                    LocalID = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    Flags = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    ParcelFlags = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    SalePrice = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    length = bytes[i[0]++];
                    Name = new byte[length];
                    Utils.arraycopy(bytes, i[0], Name, 0, length); i[0] +=  length;
                    length = bytes[i[0]++];
                    Desc = new byte[length];
                    Utils.arraycopy(bytes, i[0], Desc, 0, length); i[0] +=  length;
                    length = bytes[i[0]++];
                    MusicURL = new byte[length];
                    Utils.arraycopy(bytes, i[0], MusicURL, 0, length); i[0] +=  length;
                    length = bytes[i[0]++];
                    MediaURL = new byte[length];
                    Utils.arraycopy(bytes, i[0], MediaURL, 0, length); i[0] +=  length;
                    MediaID.FromBytes(bytes, i[0]); i[0] += 16;
                    MediaAutoScale = (byte)bytes[i[0]++];
                    GroupID.FromBytes(bytes, i[0]); i[0] += 16;
                    PassPrice = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    PassHours = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    Category = (byte)bytes[i[0]++];
                    AuthBuyerID.FromBytes(bytes, i[0]); i[0] += 16;
                    SnapshotID.FromBytes(bytes, i[0]); i[0] += 16;
                    UserLocation.fromBytes(bytes, i[0]); i[0] += 12;
                    UserLookAt.fromBytes(bytes, i[0]); i[0] += 12;
                    LandingType = (byte)bytes[i[0]++];
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.intToBytes(LocalID, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(Flags, bytes, i[0]); i[0] += 4;
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
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += AgentData.getLength();
                length += ParcelData.getLength();
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

        public ParcelPropertiesUpdatePacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            AgentData.FromBytes(bytes, i);
            ParcelData.FromBytes(bytes, i);
        }

        public ParcelPropertiesUpdatePacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd) throws MalformedDataException
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
            length += ParcelData.getLength();
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
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