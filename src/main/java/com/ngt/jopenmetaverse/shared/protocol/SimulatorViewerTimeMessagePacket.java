package com.ngt.jopenmetaverse.shared.protocol;


    public final class SimulatorViewerTimeMessagePacket extends Packet
    {
        /// <exclude/>
        public final class TimeInfoBlock extends PacketBlock
        {
            public ulong UsecSinceStart;
            public uint SecPerDay;
            public uint SecPerYear;
            public Vector3 SunDirection;
            public float SunPhase;
            public Vector3 SunAngVelocity;

            @Override
			public int getLength()
            {
                get
                {
                    return 44;
                }
            }

            public TimeInfoBlock() { }
            public TimeInfoBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    UsecSinceStart = (ulong)((ulong)bytes[i++] + ((ulong)bytes[i++] << 8) + ((ulong)bytes[i++] << 16) + ((ulong)bytes[i++] << 24) + ((ulong)bytes[i++] << 32) + ((ulong)bytes[i++] << 40) + ((ulong)bytes[i++] << 48) + ((ulong)bytes[i++] << 56));
                    SecPerDay = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    SecPerYear = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    SunDirection.FromBytes(bytes, i); i += 12;
                    SunPhase = Utils.BytesToFloat(bytes, i); i += 4;
                    SunAngVelocity.FromBytes(bytes, i); i += 12;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UInt64ToBytes(UsecSinceStart, bytes, i); i += 8;
                Utils.UIntToBytes(SecPerDay, bytes, i); i += 4;
                Utils.UIntToBytes(SecPerYear, bytes, i); i += 4;
                SunDirection.ToBytes(bytes, i); i += 12;
                Utils.FloatToBytes(SunPhase, bytes, i); i += 4;
                SunAngVelocity.ToBytes(bytes, i); i += 12;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 10;
                length += TimeInfo.length;
                return length;
            }
        }
        public TimeInfoBlock TimeInfo;

        public SimulatorViewerTimeMessagePacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.SimulatorViewerTimeMessage;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 150;
            header.Reliable = true;
            TimeInfo = new TimeInfoBlock();
        }

        public SimulatorViewerTimeMessagePacket(byte[] bytes, int[] i) 
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
            TimeInfo.FromBytes(bytes, i);
        }

        public SimulatorViewerTimeMessagePacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            TimeInfo.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += TimeInfo.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            TimeInfo.ToBytes(bytes, i);
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
