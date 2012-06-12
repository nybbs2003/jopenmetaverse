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
            public ulong RegionHandle;
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
                    SourceID.FromBytes(bytes, i); i += 16;
                    RequestID.FromBytes(bytes, i); i += 16;
                    SearchID.FromBytes(bytes, i); i += 16;
                    SearchPos.FromBytes(bytes, i); i += 12;
                    SearchDir.FromBytes(bytes, i, true); i += 12;
                    length = bytes[i++];
                    SearchName = new byte[length];
                    Utils.arraycopy(bytes, i, SearchName, 0, length); i += length;
                    Type = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Range = Utils.BytesToFloat(bytes, i); i += 4;
                    Arc = Utils.BytesToFloat(bytes, i); i += 4;
                    RegionHandle = (ulong)((ulong)bytes[i++] + ((ulong)bytes[i++] << 8) + ((ulong)bytes[i++] << 16) + ((ulong)bytes[i++] << 24) + ((ulong)bytes[i++] << 32) + ((ulong)bytes[i++] << 40) + ((ulong)bytes[i++] << 48) + ((ulong)bytes[i++] << 56));
                    SearchRegions = (byte)bytes[i++];
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                SourceID.ToBytes(bytes, i); i += 16;
                RequestID.ToBytes(bytes, i); i += 16;
                SearchID.ToBytes(bytes, i); i += 16;
                SearchPos.ToBytes(bytes, i); i += 12;
                SearchDir.ToBytes(bytes, i); i += 12;
                bytes[i++] = (byte)SearchName.length;
                Utils.arraycopy(SearchName, 0, bytes, i, SearchName.length); i += SearchName.length;
                Utils.IntToBytes(Type, bytes, i); i += 4;
                Utils.FloatToBytes(Range, bytes, i); i += 4;
                Utils.FloatToBytes(Arc, bytes, i); i += 4;
                Utils.UInt64ToBytes(RegionHandle, bytes, i); i += 8;
                bytes[i++] = SearchRegions;
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
		public void FromBytes(byte[] bytes, int[] i, int[] packetEnd, byte[] zeroBuffer)
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
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
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
            int i = 0;
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
