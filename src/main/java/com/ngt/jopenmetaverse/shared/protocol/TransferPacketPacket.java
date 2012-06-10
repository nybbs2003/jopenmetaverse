package com.ngt.jopenmetaverse.shared.protocol;


    public final class TransferPacketPacket extends Packet
    {
        /// <exclude/>
        public final class TransferDataBlock extends PacketBlock
        {
            public UUID TransferID;
            public int ChannelType;
            public int Packet;
            public int Status;
            public byte[] Data;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 30;
                    if (Data != null) { length += Data.getLength(); }
                    return length;
                }
            }

            public TransferDataBlock() { }
            public TransferDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    TransferID.FromBytes(bytes, i); i += 16;
                    ChannelType = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Packet = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Status = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    length = (bytes[i++] + (bytes[i++] << 8));
                    Data = new byte[length];
                    Buffer.BlockCopy(bytes, i, Data, 0, length); i += length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                TransferID.ToBytes(bytes, i); i += 16;
                Utils.IntToBytes(ChannelType, bytes, i); i += 4;
                Utils.IntToBytes(Packet, bytes, i); i += 4;
                Utils.IntToBytes(Status, bytes, i); i += 4;
                bytes[i++] = (byte)(Data.length % 256);
                bytes[i++] = (byte)((Data.length >> 8) % 256);
                Buffer.BlockCopy(Data, 0, bytes, i, Data.getLength()); i += Data.getLength();
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 7;
                length += TransferData.length;
                return length;
            }
        }
        public TransferDataBlock TransferData;

        public TransferPacketPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.TransferPacket;
            this.header =  new Header();
            header.Frequency = PacketFrequency.High;
            header.ID = 17;
            header.Reliable = true;
            TransferData = new TransferDataBlock();
        }

        public TransferPacketPacket(byte[] bytes, int[] i) 
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
            TransferData.FromBytes(bytes, i);
        }

        public TransferPacketPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            TransferData.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 7;
            length += TransferData.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            TransferData.ToBytes(bytes, i);
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
