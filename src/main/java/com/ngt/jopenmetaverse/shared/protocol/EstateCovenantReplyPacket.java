package com.ngt.jopenmetaverse.shared.protocol;


    public final class EstateCovenantReplyPacket extends Packet
    {
        /// <exclude/>
        public final class DataBlock extends PacketBlock
        {
            public UUID CovenantID;
            public uint CovenantTimestamp;
            public byte[] EstateName;
            public UUID EstateOwnerID;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 37;
                    if (EstateName != null) { length += EstateName.length; }
                    return length;
                }
            }

            public DataBlock() { }
            public DataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    CovenantID.FromBytes(bytes, i); i += 16;
                    CovenantTimestamp = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    length = bytes[i++];
                    EstateName = new byte[length];
                    Buffer.BlockCopy(bytes, i, EstateName, 0, length); i += length;
                    EstateOwnerID.FromBytes(bytes, i); i += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                CovenantID.ToBytes(bytes, i); i += 16;
                Utils.UIntToBytes(CovenantTimestamp, bytes, i); i += 4;
                bytes[i++] = (byte)EstateName.length;
                Buffer.BlockCopy(EstateName, 0, bytes, i, EstateName.length); i += EstateName.length;
                EstateOwnerID.ToBytes(bytes, i); i += 16;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 10;
                length += Data.getLength();
                return length;
            }
        }
        public DataBlock Data;

        public EstateCovenantReplyPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.EstateCovenantReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 204;
            header.Reliable = true;
            Data = new DataBlock();
        }

        public EstateCovenantReplyPacket(byte[] bytes, int[] i) 
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
            Data.FromBytes(bytes, i);
        }

        public EstateCovenantReplyPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            Data.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += Data.getLength();
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            Data.ToBytes(bytes, i);
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