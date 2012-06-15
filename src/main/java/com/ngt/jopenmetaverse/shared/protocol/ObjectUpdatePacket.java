package com.ngt.jopenmetaverse.shared.protocol;


    public final class ObjectUpdatePacket extends Packet
    {
        /// <exclude/>
        public final class RegionDataBlock extends PacketBlock
        {
            public BigInteger RegionHandle;
            public ushort TimeDilation;

            @Override
			public int getLength()
            {
                                {
                    return 10;
                }
            }

            public RegionDataBlock() { }
            public RegionDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    RegionHandle = Utils.bytesToULong(bytes, i[0]); i[0] += 8;
                    TimeDilation = (ushort)Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.ulongToBytes(RegionHandle, bytes, i[0]); i[0] += 8;
                bytes[i[0]++] = (byte)(TimeDilation % 256);
                bytes[i[0]++] = (byte)((TimeDilation >> 8) % 256);
            }

        }

        /// <exclude/>
        public final class ObjectDataBlock extends PacketBlock
        {
            public long ID;
            public byte State;
            public UUID FullID;
            public long CRC;
            public byte PCode;
            public byte Material;
            public byte ClickAction;
            public Vector3 Scale;
            public byte[] ObjectData;
            public long ParentID;
            public long UpdateFlags;
            public byte PathCurve;
            public byte ProfileCurve;
            public ushort PathBegin;
            public ushort PathEnd;
            public byte PathScaleX;
            public byte PathScaleY;
            public byte PathShearX;
            public byte PathShearY;
            public sbyte PathTwist;
            public sbyte PathTwistBegin;
            public sbyte PathRadiusOffset;
            public sbyte PathTaperX;
            public sbyte PathTaperY;
            public byte PathRevolutions;
            public sbyte PathSkew;
            public ushort ProfileBegin;
            public ushort ProfileEnd;
            public ushort ProfileHollow;
            public byte[] TextureEntry;
            public byte[] TextureAnim;
            public byte[] NameValue;
            public byte[] Data;
            public byte[] Text;
            public byte[] TextColor;
            public byte[] MediaURL;
            public byte[] PSBlock;
            public byte[] ExtraParams;
            public UUID Sound;
            public UUID OwnerID;
            public float Gain;
            public byte Flags;
            public float Radius;
            public byte JointType;
            public Vector3 JointPivot;
            public Vector3 JointAxisOrAnchor;

            @Override
			public int getLength()
            {
                                {
                    int length = 153;
                    if (ObjectData != null) { length += ObjectData.length; }
                    if (TextureEntry != null) { length += TextureEntry.length; }
                    if (TextureAnim != null) { length += TextureAnim.length; }
                    if (NameValue != null) { length += NameValue.length; }
                    if (Data != null) { length += Data.length; }
                    if (Text != null) { length += Text.length; }
                    if (MediaURL != null) { length += MediaURL.length; }
                    if (PSBlock != null) { length += PSBlock.length; }
                    if (ExtraParams != null) { length += ExtraParams.length; }
                    return length;
                }
            }

            public ObjectDataBlock() { }
            public ObjectDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    ID = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    State = (byte)bytes[i[0]++];
                    FullID.FromBytes(bytes, i[0]); i[0] += 16;
                    CRC = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    PCode = (byte)bytes[i[0]++];
                    Material = (byte)bytes[i[0]++];
                    ClickAction = (byte)bytes[i[0]++];
                    Scale.FromBytes(bytes, i[0]); i[0] += 12;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    ObjectData = new byte[length];
                    Utils.arraycopy(bytes, i[0], ObjectData, 0, length); i[0] +=  length;
                    ParentID = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    UpdateFlags = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    PathCurve = (byte)bytes[i[0]++];
                    ProfileCurve = (byte)bytes[i[0]++];
                    PathBegin = (ushort)Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    PathEnd = (ushort)Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    PathScaleX = (byte)bytes[i[0]++];
                    PathScaleY = (byte)bytes[i[0]++];
                    PathShearX = (byte)bytes[i[0]++];
                    PathShearY = (byte)bytes[i[0]++];
                    PathTwist = (sbyte)bytes[i[0]++];
                    PathTwistBegin = (sbyte)bytes[i[0]++];
                    PathRadiusOffset = (sbyte)bytes[i[0]++];
                    PathTaperX = (sbyte)bytes[i[0]++];
                    PathTaperY = (sbyte)bytes[i[0]++];
                    PathRevolutions = (byte)bytes[i[0]++];
                    PathSkew = (sbyte)bytes[i[0]++];
                    ProfileBegin = (ushort)Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    ProfileEnd = (ushort)Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    ProfileHollow = (ushort)Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    length = Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    TextureEntry = new byte[length];
                    Utils.arraycopy(bytes, i[0], TextureEntry, 0, length); i[0] +=  length;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    TextureAnim = new byte[length];
                    Utils.arraycopy(bytes, i[0], TextureAnim, 0, length); i[0] +=  length;
                    length = Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    NameValue = new byte[length];
                    Utils.arraycopy(bytes, i[0], NameValue, 0, length); i[0] +=  length;
                    length = Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    Data = new byte[length];
                    Utils.arraycopy(bytes, i[0], Data, 0, length); i[0] +=  length;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    Text = new byte[length];
                    Utils.arraycopy(bytes, i[0], Text, 0, length); i[0] +=  length;
                    TextColor = new byte[4];
                    Utils.arraycopy(bytes, i[0], TextColor, 0, 4); i += 4;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    MediaURL = new byte[length];
                    Utils.arraycopy(bytes, i[0], MediaURL, 0, length); i[0] +=  length;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    PSBlock = new byte[length];
                    Utils.arraycopy(bytes, i[0], PSBlock, 0, length); i[0] +=  length;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    ExtraParams = new byte[length];
                    Utils.arraycopy(bytes, i[0], ExtraParams, 0, length); i[0] +=  length;
                    Sound.FromBytes(bytes, i[0]); i[0] += 16;
                    OwnerID.FromBytes(bytes, i[0]); i[0] += 16;
                    Gain = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    Flags = (byte)bytes[i[0]++];
                    Radius = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    JointType = (byte)bytes[i[0]++];
                    JointPivot.FromBytes(bytes, i[0]); i[0] += 12;
                    JointAxisOrAnchor.FromBytes(bytes, i[0]); i[0] += 12;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.uintToBytes(ID, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = State;
                FullID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.uintToBytes(CRC, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = PCode;
                bytes[i[0]++] = Material;
                bytes[i[0]++] = ClickAction;
                Scale.ToBytes(bytes, i[0]); i[0] += 12;
                bytes[i[0]++] = (byte)ObjectData.length;
                Utils.arraycopy(ObjectData, 0, bytes, i[0], ObjectData.length); i[0] +=  ObjectData.length;
                Utils.uintToBytes(ParentID, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(UpdateFlags, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = PathCurve;
                bytes[i[0]++] = ProfileCurve;
                bytes[i[0]++] = (byte)(PathBegin % 256);
                bytes[i[0]++] = (byte)((PathBegin >> 8) % 256);
                bytes[i[0]++] = (byte)(PathEnd % 256);
                bytes[i[0]++] = (byte)((PathEnd >> 8) % 256);
                bytes[i[0]++] = PathScaleX;
                bytes[i[0]++] = PathScaleY;
                bytes[i[0]++] = PathShearX;
                bytes[i[0]++] = PathShearY;
                bytes[i[0]++] = (byte)PathTwist;
                bytes[i[0]++] = (byte)PathTwistBegin;
                bytes[i[0]++] = (byte)PathRadiusOffset;
                bytes[i[0]++] = (byte)PathTaperX;
                bytes[i[0]++] = (byte)PathTaperY;
                bytes[i[0]++] = PathRevolutions;
                bytes[i[0]++] = (byte)PathSkew;
                bytes[i[0]++] = (byte)(ProfileBegin % 256);
                bytes[i[0]++] = (byte)((ProfileBegin >> 8) % 256);
                bytes[i[0]++] = (byte)(ProfileEnd % 256);
                bytes[i[0]++] = (byte)((ProfileEnd >> 8) % 256);
                bytes[i[0]++] = (byte)(ProfileHollow % 256);
                bytes[i[0]++] = (byte)((ProfileHollow >> 8) % 256);
                bytes[i[0]++] = (byte)(TextureEntry.length % 256);
                bytes[i[0]++] = (byte)((TextureEntry.length >> 8) % 256);
                Utils.arraycopy(TextureEntry, 0, bytes, i[0], TextureEntry.length); i[0] +=  TextureEntry.length;
                bytes[i[0]++] = (byte)TextureAnim.length;
                Utils.arraycopy(TextureAnim, 0, bytes, i[0], TextureAnim.length); i[0] +=  TextureAnim.length;
                bytes[i[0]++] = (byte)(NameValue.length % 256);
                bytes[i[0]++] = (byte)((NameValue.length >> 8) % 256);
                Utils.arraycopy(NameValue, 0, bytes, i[0], NameValue.length); i[0] +=  NameValue.length;
                bytes[i[0]++] = (byte)(Data.length % 256);
                bytes[i[0]++] = (byte)((Data.length >> 8) % 256);
                Utils.arraycopy(Data, 0, bytes, i[0], Data.length); i[0] +=  Data.length;
                bytes[i[0]++] = (byte)Text.length;
                Utils.arraycopy(Text, 0, bytes, i[0], Text.length); i[0] +=  Text.length;
                Utils.arraycopy(TextColor, 0, bytes, i[0], 4);i += 4;
                bytes[i[0]++] = (byte)MediaURL.length;
                Utils.arraycopy(MediaURL, 0, bytes, i[0], MediaURL.length); i[0] +=  MediaURL.length;
                bytes[i[0]++] = (byte)PSBlock.length;
                Utils.arraycopy(PSBlock, 0, bytes, i[0], PSBlock.length); i[0] +=  PSBlock.length;
                bytes[i[0]++] = (byte)ExtraParams.length;
                Utils.arraycopy(ExtraParams, 0, bytes, i[0], ExtraParams.length); i[0] +=  ExtraParams.length;
                Sound.ToBytes(bytes, i[0]); i[0] += 16;
                OwnerID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.floatToBytes(Gain, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = Flags;
                Utils.floatToBytes(Radius, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = JointType;
                JointPivot.ToBytes(bytes, i[0]); i[0] += 12;
                JointAxisOrAnchor.ToBytes(bytes, i[0]); i[0] += 12;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 8;
                length += RegionData.length;
                for (int j = 0; j < ObjectData.length; j++)
                    length += ObjectData[j].getLength();
                return length;
            }
        }
        public RegionDataBlock RegionData;
        public ObjectDataBlock[] ObjectData;

        public ObjectUpdatePacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.ObjectUpdate;
            this.header =  new Header();
            header.Frequency = PacketFrequency.High;
            header.ID = 12;
            header.Reliable = true;
            header.Zerocoded = true;
            RegionData = new RegionDataBlock();
            ObjectData = null;
        }

        public ObjectUpdatePacket(byte[] bytes, int[] i) 
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
            RegionData.FromBytes(bytes, i);
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(ObjectData == null || ObjectData.length != -1) {
                ObjectData = new ObjectDataBlock[count];
                for(int j = 0; j < count; j++)
                { ObjectData[j] = new ObjectDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ObjectData[j].FromBytes(bytes, i); }
        }

        public ObjectUpdatePacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            RegionData.FromBytes(bytes, i);
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(ObjectData == null || ObjectData.length != count) {
                ObjectData = new ObjectDataBlock[count];
                for(int j = 0; j < count; j++)
                { ObjectData[j] = new ObjectDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ObjectData[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 7;
            length += RegionData.length;
            length++;
            for (int j = 0; j < ObjectData.length; j++) { length += ObjectData[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            RegionData.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)ObjectData.length;
            for (int j = 0; j < ObjectData.length; j++) { ObjectData[j].ToBytes(bytes, i); }
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            List<byte[]> packets = new ArrayList<byte[]>();
            int[] i = new int[]{0};
            int fixedLength = 7;

            byte[] ackBytes = null;
            int[] acksLength = new int[]{0};
            if (header.AckList != null && header.AckList.length > 0) {
                header.AppendedAcks = true;
                ackBytes = new byte[header.AckList.length * 4 + 1];
                header.AcksToBytes(ackBytes, acksLength);
            }

            fixedLength += RegionData.length;
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            RegionData.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int ObjectDataStart = 0;
            do
            {
                int variableLength = 0;
                int ObjectDataCount = 0;

              i[0] =ObjectDataStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < ObjectData.length) {
                    int blockLength = ObjectData[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++ObjectDataCount;
                    }
                    else { break; }
                    i[0]++;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)ObjectDataCount;
                for (i[0] = ObjectDataStart; i[0] < ObjectDataStart + ObjectDataCount; i[0]++) { ObjectData[i[0]].ToBytes(packet, length); }
                ObjectDataStart += ObjectDataCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                ObjectDataStart < ObjectData.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
