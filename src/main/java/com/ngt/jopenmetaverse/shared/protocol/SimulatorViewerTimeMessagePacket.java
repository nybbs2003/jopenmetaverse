package com.ngt.jopenmetaverse.shared.protocol;


    public final class SimulatorViewerTimeMessagePacket extends Packet
    {
        /// <exclude/>
        public final class TimeInfoBlock extends PacketBlock
        {
            public BigInteger UsecSinceStart;
            public long SecPerDay;
            public long SecPerYear;
            public Vector3 SunDirection;
            public float SunPhase;
            public Vector3 SunAngVelocity;

            @Override
			public int getLength()
            {
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
                    UsecSinceStart = Utils.bytesToULong(bytes, i[0]); i[0] += 8;
                    SecPerDay = Utils.bytesToUInt(bytes); i[0] += 4;
                    SecPerYear = Utils.bytesToUInt(bytes); i[0] += 4;
                    SunDirection.FromBytes(bytes, i[0]); i[0] += 12;
                    SunPhase = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    SunAngVelocity.FromBytes(bytes, i[0]); i[0] += 12;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.ulongToBytes(UsecSinceStart, bytes, i[0]); i[0] += 8;
                Utils.uintToBytes(SecPerDay, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(SecPerYear, bytes, i[0]); i[0] += 4;
                SunDirection.ToBytes(bytes, i[0]); i[0] += 12;
                Utils.floatToBytes(SunPhase, bytes, i[0]); i[0] += 4;
                SunAngVelocity.ToBytes(bytes, i[0]); i[0] += 12;
            }

        }

        @Override
			public int getLength()
        {
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
                packetEnd[0] = Helpers.ZeroDecode(bytes, packetEnd[0] + 1, zeroBuffer) - 1;
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
            int[] i = new int[]{0};
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
