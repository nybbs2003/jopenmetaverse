package com.ngt.jopenmetaverse.shared.protocol;


    public final class MapBlockRequestPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID SessionID;
            public uint Flags;
            public uint EstateID;
            public bool Godlike;

            @Override
			public int getLength()
            {
                get
                {
                    return 41;
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
                    SessionID.FromBytes(bytes, i); i += 16;
                    Flags = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    EstateID = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Godlike = (bytes[i++] != 0) ? (bool)true : (bool)false;
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
                SessionID.ToBytes(bytes, i); i += 16;
                Utils.UIntToBytes(Flags, bytes, i); i += 4;
                Utils.UIntToBytes(EstateID, bytes, i); i += 4;
                bytes[i++] = (byte)((Godlike) ? 1 : 0);
            }

        }

        /// <exclude/>
        public final class PositionDataBlock extends PacketBlock
        {
            public ushort MinX;
            public ushort MaxX;
            public ushort MinY;
            public ushort MaxY;

            @Override
			public int getLength()
            {
                get
                {
                    return 8;
                }
            }

            public PositionDataBlock() { }
            public PositionDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    MinX = (ushort)(bytes[i++] + (bytes[i++] << 8));
                    MaxX = (ushort)(bytes[i++] + (bytes[i++] << 8));
                    MinY = (ushort)(bytes[i++] + (bytes[i++] << 8));
                    MaxY = (ushort)(bytes[i++] + (bytes[i++] << 8));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i++] = (byte)(MinX % 256);
                bytes[i++] = (byte)((MinX >> 8) % 256);
                bytes[i++] = (byte)(MaxX % 256);
                bytes[i++] = (byte)((MaxX >> 8) % 256);
                bytes[i++] = (byte)(MinY % 256);
                bytes[i++] = (byte)((MinY >> 8) % 256);
                bytes[i++] = (byte)(MaxY % 256);
                bytes[i++] = (byte)((MaxY >> 8) % 256);
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 10;
                length += AgentData.getLength();
                length += PositionData.length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public PositionDataBlock PositionData;

        public MapBlockRequestPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.MapBlockRequest;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 407;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            PositionData = new PositionDataBlock();
        }

        public MapBlockRequestPacket(byte[] bytes, int[] i) 
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
            AgentData.FromBytes(bytes, i);
            PositionData.FromBytes(bytes, i);
        }

        public MapBlockRequestPacket(Header head, byte[] bytes, int[] i)
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
            PositionData.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += PositionData.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            PositionData.ToBytes(bytes, i);
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