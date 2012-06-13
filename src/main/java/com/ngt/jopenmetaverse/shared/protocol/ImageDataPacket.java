package com.ngt.jopenmetaverse.shared.protocol;


    public final class ImageDataPacket extends Packet
    {
        /// <exclude/>
        public final class ImageIDBlock extends PacketBlock
        {
            public UUID ID;
            public byte Codec;
            public long Size;
            public ushort Packets;

            @Override
			public int getLength()
            {
                                {
                    return 23;
                }
            }

            public ImageIDBlock() { }
            public ImageIDBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ID.FromBytes(bytes, i[0]); i[0] += 16;
                    Codec = (byte)bytes[i[0]++];
                    Size = Utils.bytesToUInt(bytes); i[0] += 4;
                    Packets = (ushort)(bytes[i[0]++] + (bytes[i[0]++] << 8));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = Codec;
                Utils.uintToBytes(Size, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = (byte)(Packets % 256);
                bytes[i[0]++] = (byte)((Packets >> 8) % 256);
            }

        }

        /// <exclude/>
        public final class ImageDataBlock extends PacketBlock
        {
            public byte[] Data;

            @Override
			public int getLength()
            {
                                {
                    int length = 2;
                    if (Data != null) { length += Data.length; }
                    return length;
                }
            }

            public ImageDataBlock() { }
            public ImageDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    length = (bytes[i[0]++] + (bytes[i[0]++] << 8));
                    Data = new byte[length];
                    Utils.arraycopy(bytes, i[0], Data, 0, length); i[0] +=  length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = (byte)(Data.length % 256);
                bytes[i[0]++] = (byte)((Data.length >> 8) % 256);
                Utils.arraycopy(Data, 0, bytes, i[0], Data.length); i[0] +=  Data.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 7;
                length += ImageID.length;
                length += ImageData.length;
                return length;
            }
        }
        public ImageIDBlock ImageID;
        public ImageDataBlock ImageData;

        public ImageDataPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.ImageData;
            this.header =  new Header();
            header.Frequency = PacketFrequency.High;
            header.ID = 9;
            header.Reliable = true;
            ImageID = new ImageIDBlock();
            ImageData = new ImageDataBlock();
        }

        public ImageDataPacket(byte[] bytes, int[] i) 
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
            ImageID.FromBytes(bytes, i);
            ImageData.FromBytes(bytes, i);
        }

        public ImageDataPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            ImageID.FromBytes(bytes, i);
            ImageData.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 7;
            length += ImageID.length;
            length += ImageData.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            ImageID.ToBytes(bytes, i);
            ImageData.ToBytes(bytes, i);
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
