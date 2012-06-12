package com.ngt.jopenmetaverse.shared.protocol;


    public final class TransferRequestPacket extends Packet
    {
        /// <exclude/>
        public final class TransferInfoBlock extends PacketBlock
        {
            public UUID TransferID;
            public int ChannelType;
            public int SourceType;
            public float Priority;
            public byte[] Params;

            @Override
			public int getLength()
            {
                                {
                    int length = 30;
                    if (Params != null) { length += Params.length; }
                    return length;
                }
            }

            public TransferInfoBlock() { }
            public TransferInfoBlock(byte[] bytes, int[] i)
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
                    SourceType = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Priority = Utils.BytesToFloat(bytes, i); i += 4;
                    length = (bytes[i++] + (bytes[i++] << 8));
                    Params = new byte[length];
                    Utils.arraycopy(bytes, i, Params, 0, length); i += length;
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
                Utils.IntToBytes(SourceType, bytes, i); i += 4;
                Utils.FloatToBytes(Priority, bytes, i); i += 4;
                bytes[i++] = (byte)(Params.length % 256);
                bytes[i++] = (byte)((Params.length >> 8) % 256);
                Utils.arraycopy(Params, 0, bytes, i, Params.length); i += Params.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += TransferInfo.length;
                return length;
            }
        }
        public TransferInfoBlock TransferInfo;

        public TransferRequestPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.TransferRequest;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 153;
            header.Reliable = true;
            header.Zerocoded = true;
            TransferInfo = new TransferInfoBlock();
        }

        public TransferRequestPacket(byte[] bytes, int[] i) 
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
            TransferInfo.FromBytes(bytes, i);
        }

        public TransferRequestPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            TransferInfo.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += TransferInfo.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            TransferInfo.ToBytes(bytes, i);
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
