package com.ngt.jopenmetaverse.shared.protocol;


    public final class TeleportLocalPacket extends Packet
    {
        /// <exclude/>
        public final class InfoBlock extends PacketBlock
        {
            public UUID AgentID;
            public uint LocationID;
            public Vector3 Position;
            public Vector3 LookAt;
            public uint TeleportFlags;

            @Override
			public int getLength()
            {
                                {
                    return 48;
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
                try
                {
                    AgentID.FromBytes(bytes, i[0]); i[0] += 16;
                    LocationID = (uint)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    Position.FromBytes(bytes, i[0]); i[0] += 12;
                    LookAt.FromBytes(bytes, i[0]); i[0] += 12;
                    TeleportFlags = (uint)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
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
                Utils.UIntToBytes(LocationID, bytes, i[0]); i[0] += 4;
                Position.ToBytes(bytes, i[0]); i[0] += 12;
                LookAt.ToBytes(bytes, i[0]); i[0] += 12;
                Utils.UIntToBytes(TeleportFlags, bytes, i[0]); i[0] += 4;
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

        public TeleportLocalPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.TeleportLocal;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 64;
            header.Reliable = true;
            Info = new InfoBlock();
        }

        public TeleportLocalPacket(byte[] bytes, int[] i) 
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
            Info.FromBytes(bytes, i);
        }

        public TeleportLocalPacket(Header head, byte[] bytes, int[] i)
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
