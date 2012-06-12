package com.ngt.jopenmetaverse.shared.protocol;


    public final class DisableSimulatorPacket extends Packet
    {
        @Override
			public int getLength()
        {
                        {
                int length = 10;
                return length;
            }
        }

        public DisableSimulatorPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.DisableSimulator;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 152;
            header.Reliable = true;
        }

        public DisableSimulatorPacket(byte[] bytes, int[] i) 
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
        }

        public DisableSimulatorPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
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
