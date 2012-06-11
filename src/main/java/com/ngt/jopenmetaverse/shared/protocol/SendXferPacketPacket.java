package com.ngt.jopenmetaverse.shared.protocol;


    public final class SendXferPacketPacket extends Packet
    {
        /// <exclude/>
        public final class XferIDBlock extends PacketBlock
        {
            public ulong ID;
            public uint Packet;

            @Override
			public int getLength()
            {
                get
                {
                    return 12;
                }
            }

            public XferIDBlock() { }
            public XferIDBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ID = (ulong)((ulong)bytes[i++] + ((ulong)bytes[i++] << 8) + ((ulong)bytes[i++] << 16) + ((ulong)bytes[i++] << 24) + ((ulong)bytes[i++] << 32) + ((ulong)bytes[i++] << 40) + ((ulong)bytes[i++] << 48) + ((ulong)bytes[i++] << 56));
                    Packet = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UInt64ToBytes(ID, bytes, i); i += 8;
                Utils.UIntToBytes(Packet, bytes, i); i += 4;
            }

        }

        /// <exclude/>
        public final class DataPacketBlock extends PacketBlock
        {
            public byte[] Data;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 2;
                    if (Data != null) { length += Data.getLength(); }
                    return length;
                }
            }

            public DataPacketBlock() { }
            public DataPacketBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
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
                length += XferID.length;
                length += DataPacket.length;
                return length;
            }
        }
        public XferIDBlock XferID;
        public DataPacketBlock DataPacket;

        public SendXferPacketPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.SendXferPacket;
            this.header =  new Header();
            header.Frequency = PacketFrequency.High;
            header.ID = 18;
            header.Reliable = true;
            XferID = new XferIDBlock();
            DataPacket = new DataPacketBlock();
        }

        public SendXferPacketPacket(byte[] bytes, int[] i) 
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
            XferID.FromBytes(bytes, i);
            DataPacket.FromBytes(bytes, i);
        }

        public SendXferPacketPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            XferID.FromBytes(bytes, i);
            DataPacket.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 7;
            length += XferID.length;
            length += DataPacket.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            XferID.ToBytes(bytes, i);
            DataPacket.ToBytes(bytes, i);
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