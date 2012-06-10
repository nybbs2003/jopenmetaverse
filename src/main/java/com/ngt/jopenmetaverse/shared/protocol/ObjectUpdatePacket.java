package com.ngt.jopenmetaverse.shared.protocol;


    public final class ObjectUpdatePacket extends Packet
    {
        /// <exclude/>
        public final class RegionDataBlock extends PacketBlock
        {
            public ulong RegionHandle;
            public ushort TimeDilation;

            @Override
			public int getLength()
            {
                get
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
                    RegionHandle = (ulong)((ulong)bytes[i++] + ((ulong)bytes[i++] << 8) + ((ulong)bytes[i++] << 16) + ((ulong)bytes[i++] << 24) + ((ulong)bytes[i++] << 32) + ((ulong)bytes[i++] << 40) + ((ulong)bytes[i++] << 48) + ((ulong)bytes[i++] << 56));
                    TimeDilation = (ushort)(bytes[i++] + (bytes[i++] << 8));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UInt64ToBytes(RegionHandle, bytes, i); i += 8;
                bytes[i++] = (byte)(TimeDilation % 256);
                bytes[i++] = (byte)((TimeDilation >> 8) % 256);
            }

        }

        /// <exclude/>
        public final class ObjectDataBlock extends PacketBlock
        {
            public uint ID;
            public byte State;
            public UUID FullID;
            public uint CRC;
            public byte PCode;
            public byte Material;
            public byte ClickAction;
            public Vector3 Scale;
            public byte[] ObjectData;
            public uint ParentID;
            public uint UpdateFlags;
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
                get
                {
                    int length = 153;
                    if (ObjectData != null) { length += ObjectData.length; }
                    if (TextureEntry != null) { length += TextureEntry.length; }
                    if (TextureAnim != null) { length += TextureAnim.length; }
                    if (NameValue != null) { length += NameValue.length; }
                    if (Data != null) { length += Data.getLength(); }
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
                    ID = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    State = (byte)bytes[i++];
                    FullID.FromBytes(bytes, i); i += 16;
                    CRC = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    PCode = (byte)bytes[i++];
                    Material = (byte)bytes[i++];
                    ClickAction = (byte)bytes[i++];
                    Scale.FromBytes(bytes, i); i += 12;
                    length = bytes[i++];
                    ObjectData = new byte[length];
                    Buffer.BlockCopy(bytes, i, ObjectData, 0, length); i += length;
                    ParentID = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    UpdateFlags = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    PathCurve = (byte)bytes[i++];
                    ProfileCurve = (byte)bytes[i++];
                    PathBegin = (ushort)(bytes[i++] + (bytes[i++] << 8));
                    PathEnd = (ushort)(bytes[i++] + (bytes[i++] << 8));
                    PathScaleX = (byte)bytes[i++];
                    PathScaleY = (byte)bytes[i++];
                    PathShearX = (byte)bytes[i++];
                    PathShearY = (byte)bytes[i++];
                    PathTwist = (sbyte)bytes[i++];
                    PathTwistBegin = (sbyte)bytes[i++];
                    PathRadiusOffset = (sbyte)bytes[i++];
                    PathTaperX = (sbyte)bytes[i++];
                    PathTaperY = (sbyte)bytes[i++];
                    PathRevolutions = (byte)bytes[i++];
                    PathSkew = (sbyte)bytes[i++];
                    ProfileBegin = (ushort)(bytes[i++] + (bytes[i++] << 8));
                    ProfileEnd = (ushort)(bytes[i++] + (bytes[i++] << 8));
                    ProfileHollow = (ushort)(bytes[i++] + (bytes[i++] << 8));
                    length = (bytes[i++] + (bytes[i++] << 8));
                    TextureEntry = new byte[length];
                    Buffer.BlockCopy(bytes, i, TextureEntry, 0, length); i += length;
                    length = bytes[i++];
                    TextureAnim = new byte[length];
                    Buffer.BlockCopy(bytes, i, TextureAnim, 0, length); i += length;
                    length = (bytes[i++] + (bytes[i++] << 8));
                    NameValue = new byte[length];
                    Buffer.BlockCopy(bytes, i, NameValue, 0, length); i += length;
                    length = (bytes[i++] + (bytes[i++] << 8));
                    Data = new byte[length];
                    Buffer.BlockCopy(bytes, i, Data, 0, length); i += length;
                    length = bytes[i++];
                    Text = new byte[length];
                    Buffer.BlockCopy(bytes, i, Text, 0, length); i += length;
                    TextColor = new byte[4];
                    Buffer.BlockCopy(bytes, i, TextColor, 0, 4); i += 4;
                    length = bytes[i++];
                    MediaURL = new byte[length];
                    Buffer.BlockCopy(bytes, i, MediaURL, 0, length); i += length;
                    length = bytes[i++];
                    PSBlock = new byte[length];
                    Buffer.BlockCopy(bytes, i, PSBlock, 0, length); i += length;
                    length = bytes[i++];
                    ExtraParams = new byte[length];
                    Buffer.BlockCopy(bytes, i, ExtraParams, 0, length); i += length;
                    Sound.FromBytes(bytes, i); i += 16;
                    OwnerID.FromBytes(bytes, i); i += 16;
                    Gain = Utils.BytesToFloat(bytes, i); i += 4;
                    Flags = (byte)bytes[i++];
                    Radius = Utils.BytesToFloat(bytes, i); i += 4;
                    JointType = (byte)bytes[i++];
                    JointPivot.FromBytes(bytes, i); i += 12;
                    JointAxisOrAnchor.FromBytes(bytes, i); i += 12;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UIntToBytes(ID, bytes, i); i += 4;
                bytes[i++] = State;
                FullID.ToBytes(bytes, i); i += 16;
                Utils.UIntToBytes(CRC, bytes, i); i += 4;
                bytes[i++] = PCode;
                bytes[i++] = Material;
                bytes[i++] = ClickAction;
                Scale.ToBytes(bytes, i); i += 12;
                bytes[i++] = (byte)ObjectData.length;
                Buffer.BlockCopy(ObjectData, 0, bytes, i, ObjectData.length); i += ObjectData.length;
                Utils.UIntToBytes(ParentID, bytes, i); i += 4;
                Utils.UIntToBytes(UpdateFlags, bytes, i); i += 4;
                bytes[i++] = PathCurve;
                bytes[i++] = ProfileCurve;
                bytes[i++] = (byte)(PathBegin % 256);
                bytes[i++] = (byte)((PathBegin >> 8) % 256);
                bytes[i++] = (byte)(PathEnd % 256);
                bytes[i++] = (byte)((PathEnd >> 8) % 256);
                bytes[i++] = PathScaleX;
                bytes[i++] = PathScaleY;
                bytes[i++] = PathShearX;
                bytes[i++] = PathShearY;
                bytes[i++] = (byte)PathTwist;
                bytes[i++] = (byte)PathTwistBegin;
                bytes[i++] = (byte)PathRadiusOffset;
                bytes[i++] = (byte)PathTaperX;
                bytes[i++] = (byte)PathTaperY;
                bytes[i++] = PathRevolutions;
                bytes[i++] = (byte)PathSkew;
                bytes[i++] = (byte)(ProfileBegin % 256);
                bytes[i++] = (byte)((ProfileBegin >> 8) % 256);
                bytes[i++] = (byte)(ProfileEnd % 256);
                bytes[i++] = (byte)((ProfileEnd >> 8) % 256);
                bytes[i++] = (byte)(ProfileHollow % 256);
                bytes[i++] = (byte)((ProfileHollow >> 8) % 256);
                bytes[i++] = (byte)(TextureEntry.length % 256);
                bytes[i++] = (byte)((TextureEntry.length >> 8) % 256);
                Buffer.BlockCopy(TextureEntry, 0, bytes, i, TextureEntry.length); i += TextureEntry.length;
                bytes[i++] = (byte)TextureAnim.length;
                Buffer.BlockCopy(TextureAnim, 0, bytes, i, TextureAnim.length); i += TextureAnim.length;
                bytes[i++] = (byte)(NameValue.length % 256);
                bytes[i++] = (byte)((NameValue.length >> 8) % 256);
                Buffer.BlockCopy(NameValue, 0, bytes, i, NameValue.length); i += NameValue.length;
                bytes[i++] = (byte)(Data.length % 256);
                bytes[i++] = (byte)((Data.length >> 8) % 256);
                Buffer.BlockCopy(Data, 0, bytes, i, Data.getLength()); i += Data.getLength();
                bytes[i++] = (byte)Text.length;
                Buffer.BlockCopy(Text, 0, bytes, i, Text.length); i += Text.length;
                Buffer.BlockCopy(TextColor, 0, bytes, i, 4);i += 4;
                bytes[i++] = (byte)MediaURL.length;
                Buffer.BlockCopy(MediaURL, 0, bytes, i, MediaURL.length); i += MediaURL.length;
                bytes[i++] = (byte)PSBlock.length;
                Buffer.BlockCopy(PSBlock, 0, bytes, i, PSBlock.length); i += PSBlock.length;
                bytes[i++] = (byte)ExtraParams.length;
                Buffer.BlockCopy(ExtraParams, 0, bytes, i, ExtraParams.length); i += ExtraParams.length;
                Sound.ToBytes(bytes, i); i += 16;
                OwnerID.ToBytes(bytes, i); i += 16;
                Utils.FloatToBytes(Gain, bytes, i); i += 4;
                bytes[i++] = Flags;
                Utils.FloatToBytes(Radius, bytes, i); i += 4;
                bytes[i++] = JointType;
                JointPivot.ToBytes(bytes, i); i += 12;
                JointAxisOrAnchor.ToBytes(bytes, i); i += 12;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 8;
                length += RegionData.length;
                for (int j = 0; j < ObjectData.length; j++)
                    length += ObjectData[j].length;
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
                packetEnd = Helpers.ZeroDecode(bytes, packetEnd + 1, zeroBuffer) - 1;
                bytes = zeroBuffer;
            }
            RegionData.FromBytes(bytes, i);
            int count = (int)bytes[i++];
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
            int count = (int)bytes[i++];
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
            for (int j = 0; j < ObjectData.length; j++) { length += ObjectData[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            RegionData.ToBytes(bytes, i);
            bytes[i++] = (byte)ObjectData.length;
            for (int j = 0; j < ObjectData.length; j++) { ObjectData[j].ToBytes(bytes, i); }
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            System.Collections.Generic.List<byte[]> packets = new System.Collections.Generic.List<byte[]>();
            int i = 0;
            int fixedLength = 7;

            byte[] ackBytes = null;
            int acksLength = 0;
            if (header.AckList != null && header.AckList.length > 0) {
                header.AppendedAcks = true;
                ackBytes = new byte[header.AckList.length * 4 + 1];
                header.AcksToBytes(ackBytes, ref acksLength);
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

                i = ObjectDataStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < ObjectData.length) {
                    int blockLength = ObjectData[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++ObjectDataCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Buffer.BlockCopy(fixedBytes, 0, packet, 0, length);
                if (packets.Count > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)ObjectDataCount;
                for (i = ObjectDataStart; i < ObjectDataStart + ObjectDataCount; i++) { ObjectData[i].ToBytes(packet, ref length); }
                ObjectDataStart += ObjectDataCount;

                if (acksLength > 0) {
                    Buffer.BlockCopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.Add(packet);
            } while (
                ObjectDataStart < ObjectData.length);

            return packets.ToArray();
        }
    }

    /// <exclude/>
