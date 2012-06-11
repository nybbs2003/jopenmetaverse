package com.ngt.jopenmetaverse.shared.protocol;


    public final class TeleportFinishPacket extends Packet
    {
        /// <exclude/>
        public final class InfoBlock extends PacketBlock
        {
            public UUID AgentID;
            public uint LocationID;
            public uint SimIP;
            public ushort SimPort;
            public ulong RegionHandle;
            public byte[] SeedCapability;
            public byte SimAccess;
            public uint TeleportFlags;

            @Override
			public int getLength()
            {
                get
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
                    AgentID.FromBytes(bytes, i); i += 16;
                    LocationID = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    SimIP = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    SimPort = (ushort)((bytes[i++] << 8) + bytes[i++]);
                    RegionHandle = (ulong)((ulong)bytes[i++] + ((ulong)bytes[i++] << 8) + ((ulong)bytes[i++] << 16) + ((ulong)bytes[i++] << 24) + ((ulong)bytes[i++] << 32) + ((ulong)bytes[i++] << 40) + ((ulong)bytes[i++] << 48) + ((ulong)bytes[i++] << 56));
                    length = (bytes[i++] + (bytes[i++] << 8));
                    SeedCapability = new byte[length];
                    Buffer.BlockCopy(bytes, i, SeedCapability, 0, length); i += length;
                    SimAccess = (byte)bytes[i++];
                    TeleportFlags = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
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
                Utils.UIntToBytes(LocationID, bytes, i); i += 4;
                Utils.UIntToBytes(SimIP, bytes, i); i += 4;
                bytes[i++] = (byte)((SimPort >> 8) % 256);
                bytes[i++] = (byte)(SimPort % 256);
                Utils.UInt64ToBytes(RegionHandle, bytes, i); i += 8;
                bytes[i++] = (byte)(SeedCapability.length % 256);
                bytes[i++] = (byte)((SeedCapability.length >> 8) % 256);
                Buffer.BlockCopy(SeedCapability, 0, bytes, i, SeedCapability.length); i += SeedCapability.length;
                bytes[i++] = SimAccess;
                Utils.UIntToBytes(TeleportFlags, bytes, i); i += 4;
            }

        }

        @Override
			public int getLength()
        {
            get
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

        public TeleportFinishPacket(byte[] bytes, int[] i) 
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
            Info.FromBytes(bytes, i);
        }

        public TeleportFinishPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
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
            int i = 0;
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