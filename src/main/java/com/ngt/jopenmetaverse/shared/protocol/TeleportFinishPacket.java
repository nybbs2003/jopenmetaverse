package com.ngt.jopenmetaverse.shared.protocol;


    public final class TeleportFinishPacket extends Packet
    {
        /// <exclude/>
        public final class InfoBlock extends PacketBlock
        {
            public UUID AgentID;
            public long LocationID;
            public long SimIP;
            public ushort SimPort;
            public BigInteger RegionHandle;
            public byte[] SeedCapability;
            public byte SimAccess;
            public long TeleportFlags;

            @Override
			public int getLength()
            {
                                {
                    int length = 41;
                    if (SeedCapability != null) { length += SeedCapability.length; }
                    return length;
                }
            }

            public InfoBlock() { }
            public InfoBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    AgentID.FromBytes(bytes, i[0]); i[0] += 16;
                    LocationID = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    SimIP = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    SimPort = (int)Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    RegionHandle = Utils.bytesToULong(bytes, i[0]); i[0] += 8;
                    length = Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    SeedCapability = new byte[length];
                    Utils.arraycopy(bytes, i[0], SeedCapability, 0, length); i[0] +=  length;
                    SimAccess = (byte)bytes[i[0]++];
                    TeleportFlags = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
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
                Utils.uintToBytes(LocationID, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(SimIP, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = (byte)((SimPort >> 8) % 256);
                bytes[i[0]++] = (byte)(SimPort % 256);
                Utils.ulongToBytes(RegionHandle, bytes, i[0]); i[0] += 8;
                bytes[i[0]++] = (byte)(SeedCapability.length % 256);
                bytes[i[0]++] = (byte)((SeedCapability.length >> 8) % 256);
                Utils.arraycopy(SeedCapability, 0, bytes, i[0], SeedCapability.length); i[0] +=  SeedCapability.length;
                bytes[i[0]++] = SimAccess;
                Utils.uintToBytes(TeleportFlags, bytes, i[0]); i[0] += 4;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += Info.length;
                return length;
            }
        }
        public InfoBlock Info;

        public TeleportFinishPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.TeleportFinish;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 69;
            header.Reliable = true;
            Info = new InfoBlock();
        }

        public TeleportFinishPacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            Info.FromBytes(bytes, i);
        }

        public TeleportFinishPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd) throws MalformedDataException
        {
            this.header =  header;
            Info.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += Info.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            Info.ToBytes(bytes, i);
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
