package com.ngt.jopenmetaverse.shared.protocol;


    public final class ScriptSensorRequestPacket extends Packet
    {
        /// <exclude/>
        public final class RequesterBlock extends PacketBlock
        {
            public UUID SourceID;
            public UUID RequestID;
            public UUID SearchID;
            public Vector3 SearchPos;
            public Quaternion SearchDir;
            public byte[] SearchName;
            public int Type;
            public float Range;
            public float Arc;
            public BigInteger RegionHandle;
            public byte SearchRegions;

            @Override
			public int getLength()
            {
                                {
                    int length = 94;
                    if (SearchName != null) { length += SearchName.length; }
                    return length;
                }
            }

            public RequesterBlock() { }
            public RequesterBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    SourceID.FromBytes(bytes, i[0]); i[0] += 16;
                    RequestID.FromBytes(bytes, i[0]); i[0] += 16;
                    SearchID.FromBytes(bytes, i[0]); i[0] += 16;
                    SearchPos.FromBytes(bytes, i[0]); i[0] += 12;
                    SearchDir.FromBytes(bytes, i[0], true); i[0] += 12;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    SearchName = new byte[length];
                    Utils.arraycopy(bytes, i[0], SearchName, 0, length); i[0] +=  length;
                    Type = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    Range = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    Arc = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    RegionHandle = Utils.bytesToULong(bytes, i[0]); i[0] += 8;
                    SearchRegions = (byte)bytes[i[0]++];
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                SourceID.ToBytes(bytes, i[0]); i[0] += 16;
                RequestID.ToBytes(bytes, i[0]); i[0] += 16;
                SearchID.ToBytes(bytes, i[0]); i[0] += 16;
                SearchPos.ToBytes(bytes, i[0]); i[0] += 12;
                SearchDir.ToBytes(bytes, i[0]); i[0] += 12;
                bytes[i[0]++] = (byte)SearchName.length;
                Utils.arraycopy(SearchName, 0, bytes, i[0], SearchName.length); i[0] +=  SearchName.length;
                Utils.intToBytes(Type, bytes, i[0]); i[0] += 4;
                Utils.floatToBytes(Range, bytes, i[0]); i[0] += 4;
                Utils.floatToBytes(Arc, bytes, i[0]); i[0] += 4;
                Utils.ulongToBytes(RegionHandle, bytes, i[0]); i[0] += 8;
                bytes[i[0]++] = SearchRegions;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += Requester.length;
                return length;
            }
        }
        public RequesterBlock Requester;

        public ScriptSensorRequestPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.ScriptSensorRequest;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 247;
            header.Reliable = true;
            header.Zerocoded = true;
            Requester = new RequesterBlock();
        }

        public ScriptSensorRequestPacket(byte[] bytes, int[] i) 
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
            Requester.FromBytes(bytes, i);
        }

        public ScriptSensorRequestPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd) throws MalformedDataException
        {
            this.header =  header;
            Requester.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += Requester.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            Requester.ToBytes(bytes, i);
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
