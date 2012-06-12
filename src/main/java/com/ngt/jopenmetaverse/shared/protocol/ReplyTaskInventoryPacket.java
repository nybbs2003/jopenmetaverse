package com.ngt.jopenmetaverse.shared.protocol;


    public final class ReplyTaskInventoryPacket extends Packet
    {
        /// <exclude/>
        public final class InventoryDataBlock extends PacketBlock
        {
            public UUID TaskID;
            public short Serial;
            public byte[] Filename;

            @Override
			public int getLength()
            {
                                {
                    int length = 19;
                    if (Filename != null) { length += Filename.length; }
                    return length;
                }
            }

            public InventoryDataBlock() { }
            public InventoryDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    TaskID.FromBytes(bytes, i); i += 16;
                    Serial = (short)(bytes[i++] + (bytes[i++] << 8));
                    length = bytes[i++];
                    Filename = new byte[length];
                    Utils.arraycopy(bytes, i, Filename, 0, length); i += length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                TaskID.ToBytes(bytes, i); i += 16;
                bytes[i++] = (byte)(Serial % 256);
                bytes[i++] = (byte)((Serial >> 8) % 256);
                bytes[i++] = (byte)Filename.length;
                Utils.arraycopy(Filename, 0, bytes, i, Filename.length); i += Filename.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += InventoryData.length;
                return length;
            }
        }
        public InventoryDataBlock InventoryData;

        public ReplyTaskInventoryPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.ReplyTaskInventory;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 290;
            header.Reliable = true;
            header.Zerocoded = true;
            InventoryData = new InventoryDataBlock();
        }

        public ReplyTaskInventoryPacket(byte[] bytes, int[] i) 
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
            InventoryData.FromBytes(bytes, i);
        }

        public ReplyTaskInventoryPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            InventoryData.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += InventoryData.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            InventoryData.ToBytes(bytes, i);
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
