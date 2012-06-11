package com.ngt.jopenmetaverse.shared.protocol;


    public final class DeRezAckPacket extends Packet
    {
        /// <exclude/>
        public final class TransactionDataBlock extends PacketBlock
        {
            public UUID TransactionID;
            public bool Success;

            @Override
			public int getLength()
            {
                get
                {
                    return 17;
                }
            }

            public TransactionDataBlock() { }
            public TransactionDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    TransactionID.FromBytes(bytes, i); i += 16;
                    Success = (bytes[i++] != 0) ? (bool)true : (bool)false;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                TransactionID.ToBytes(bytes, i); i += 16;
                bytes[i++] = (byte)((Success) ? 1 : 0);
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 10;
                length += TransactionData.length;
                return length;
            }
        }
        public TransactionDataBlock TransactionData;

        public DeRezAckPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.DeRezAck;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 292;
            header.Reliable = true;
            TransactionData = new TransactionDataBlock();
        }

        public DeRezAckPacket(byte[] bytes, int[] i) 
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
            TransactionData.FromBytes(bytes, i);
        }

        public DeRezAckPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            TransactionData.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += TransactionData.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            TransactionData.ToBytes(bytes, i);
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