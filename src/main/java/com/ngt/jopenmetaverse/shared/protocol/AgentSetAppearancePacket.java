package com.ngt.jopenmetaverse.shared.protocol;


    public final class AgentSetAppearancePacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID SessionID;
            public uint SerialNum;
            public Vector3 Size;

            @Override
			public int getLength()
            {
                                {
                    return 48;
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
                    SerialNum = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Size.FromBytes(bytes, i); i += 12;
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
                Utils.UIntToBytes(SerialNum, bytes, i); i += 4;
                Size.ToBytes(bytes, i); i += 12;
            }

        }

        /// <exclude/>
        public final class WearableDataBlock extends PacketBlock
        {
            public UUID CacheID;
            public byte TextureIndex;

            @Override
			public int getLength()
            {
                                {
                    return 17;
                }
            }

            public WearableDataBlock() { }
            public WearableDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    CacheID.FromBytes(bytes, i); i += 16;
                    TextureIndex = (byte)bytes[i++];
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                CacheID.ToBytes(bytes, i); i += 16;
                bytes[i++] = TextureIndex;
            }

        }

        /// <exclude/>
        public final class ObjectDataBlock extends PacketBlock
        {
            public byte[] TextureEntry;

            @Override
			public int getLength()
            {
                                {
                    int length = 2;
                    if (TextureEntry != null) { length += TextureEntry.length; }
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
                    length = (bytes[i++] + (bytes[i++] << 8));
                    TextureEntry = new byte[length];
                    Utils.arraycopy(bytes, i, TextureEntry, 0, length); i += length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i++] = (byte)(TextureEntry.length % 256);
                bytes[i++] = (byte)((TextureEntry.length >> 8) % 256);
                Utils.arraycopy(TextureEntry, 0, bytes, i, TextureEntry.length); i += TextureEntry.length;
            }

        }

        /// <exclude/>
        public final class VisualParamBlock extends PacketBlock
        {
            public byte ParamValue;

            @Override
			public int getLength()
            {
                                {
                    return 1;
                }
            }

            public VisualParamBlock() { }
            public VisualParamBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ParamValue = (byte)bytes[i++];
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i++] = ParamValue;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 12;
                length += AgentData.getLength();
                for (int j = 0; j < WearableData.length; j++)
                    length += WearableData[j].length;
                length += ObjectData.length;
                for (int j = 0; j < VisualParam.length; j++)
                    length += VisualParam[j].length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public WearableDataBlock[] WearableData;
        public ObjectDataBlock ObjectData;
        public VisualParamBlock[] VisualParam;

        public AgentSetAppearancePacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.AgentSetAppearance;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 84;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            WearableData = null;
            ObjectData = new ObjectDataBlock();
            VisualParam = null;
        }

        public AgentSetAppearancePacket(byte[] bytes, int[] i) 
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
            int count = (int)bytes[i++];
            if(WearableData == null || WearableData.length != -1) {
                WearableData = new WearableDataBlock[count];
                for(int j = 0; j < count; j++)
                { WearableData[j] = new WearableDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { WearableData[j].FromBytes(bytes, i); }
            ObjectData.FromBytes(bytes, i);
            count = (int)bytes[i++];
            if(VisualParam == null || VisualParam.length != -1) {
                VisualParam = new VisualParamBlock[count];
                for(int j = 0; j < count; j++)
                { VisualParam[j] = new VisualParamBlock(); }
            }
            for (int j = 0; j < count; j++)
            { VisualParam[j].FromBytes(bytes, i); }
        }

        public AgentSetAppearancePacket(Header head, byte[] bytes, int[] i)
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
            int count = (int)bytes[i++];
            if(WearableData == null || WearableData.length != count) {
                WearableData = new WearableDataBlock[count];
                for(int j = 0; j < count; j++)
                { WearableData[j] = new WearableDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { WearableData[j].FromBytes(bytes, i); }
            ObjectData.FromBytes(bytes, i);
            count = (int)bytes[i++];
            if(VisualParam == null || VisualParam.length != count) {
                VisualParam = new VisualParamBlock[count];
                for(int j = 0; j < count; j++)
                { VisualParam[j] = new VisualParamBlock(); }
            }
            for (int j = 0; j < count; j++)
            { VisualParam[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += ObjectData.length;
            length++;
            for (int j = 0; j < WearableData.length; j++) { length += WearableData[j].length; }
            length++;
            for (int j = 0; j < VisualParam.length; j++) { length += VisualParam[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            bytes[i++] = (byte)WearableData.length;
            for (int j = 0; j < WearableData.length; j++) { WearableData[j].ToBytes(bytes, i); }
            ObjectData.ToBytes(bytes, i);
            bytes[i++] = (byte)VisualParam.length;
            for (int j = 0; j < VisualParam.length; j++) { VisualParam[j].ToBytes(bytes, i); }
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
