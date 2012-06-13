package com.ngt.jopenmetaverse.shared.protocol;


    public final class ReportAutosaveCrashPacket extends Packet
    {
        /// <exclude/>
        public final class AutosaveDataBlock extends PacketBlock
        {
            public int PID;
            public int Status;

            @Override
			public int getLength()
            {
                                {
                    return 8;
                }
            }

            public AutosaveDataBlock() { }
            public AutosaveDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    PID = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    Status = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.IntToBytes(PID, bytes, i); i += 4;
                Utils.IntToBytes(Status, bytes, i); i += 4;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += AutosaveData.length;
                return length;
            }
        }
        public AutosaveDataBlock AutosaveData;

        public ReportAutosaveCrashPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.ReportAutosaveCrash;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 128;
            header.Reliable = true;
            AutosaveData = new AutosaveDataBlock();
        }

        public ReportAutosaveCrashPacket(byte[] bytes, int[] i) 
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
            AutosaveData.FromBytes(bytes, i);
        }

        public ReportAutosaveCrashPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            AutosaveData.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AutosaveData.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AutosaveData.ToBytes(bytes, i);
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
