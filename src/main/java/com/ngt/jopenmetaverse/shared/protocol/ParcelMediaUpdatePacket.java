package com.ngt.jopenmetaverse.shared.protocol;


    public final class ParcelMediaUpdatePacket extends Packet
    {
        /// <exclude/>
        public final class DataBlockBlock extends PacketBlock
        {
            public byte[] MediaURL;
            public UUID MediaID;
            public byte MediaAutoScale;

            @Override
			public int getLength()
            {
                                {
                    int length = 18;
                    if (MediaURL != null) { length += MediaURL.length; }
                    return length;
                }
            }

            public DataBlockBlock() { }
            public DataBlockBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    length = bytes[i++];
                    MediaURL = new byte[length];
                    Utils.arraycopy(bytes, i, MediaURL, 0, length); i += length;
                    MediaID.FromBytes(bytes, i); i += 16;
                    MediaAutoScale = (byte)bytes[i++];
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i++] = (byte)MediaURL.length;
                Utils.arraycopy(MediaURL, 0, bytes, i, MediaURL.length); i += MediaURL.length;
                MediaID.ToBytes(bytes, i); i += 16;
                bytes[i++] = MediaAutoScale;
            }

        }

        /// <exclude/>
        public final class DataBlockExtendedBlock extends PacketBlock
        {
            public byte[] MediaType;
            public byte[] MediaDesc;
            public int MediaWidth;
            public int MediaHeight;
            public byte MediaLoop;

            @Override
			public int getLength()
            {
                                {
                    int length = 11;
                    if (MediaType != null) { length += MediaType.length; }
                    if (MediaDesc != null) { length += MediaDesc.length; }
                    return length;
                }
            }

            public DataBlockExtendedBlock() { }
            public DataBlockExtendedBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    length = bytes[i++];
                    MediaType = new byte[length];
                    Utils.arraycopy(bytes, i, MediaType, 0, length); i += length;
                    length = bytes[i++];
                    MediaDesc = new byte[length];
                    Utils.arraycopy(bytes, i, MediaDesc, 0, length); i += length;
                    MediaWidth = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    MediaHeight = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    MediaLoop = (byte)bytes[i++];
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i++] = (byte)MediaType.length;
                Utils.arraycopy(MediaType, 0, bytes, i, MediaType.length); i += MediaType.length;
                bytes[i++] = (byte)MediaDesc.length;
                Utils.arraycopy(MediaDesc, 0, bytes, i, MediaDesc.length); i += MediaDesc.length;
                Utils.IntToBytes(MediaWidth, bytes, i); i += 4;
                Utils.IntToBytes(MediaHeight, bytes, i); i += 4;
                bytes[i++] = MediaLoop;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += DataBlock.length;
                length += DataBlockExtended.length;
                return length;
            }
        }
        public DataBlockBlock DataBlock;
        public DataBlockExtendedBlock DataBlockExtended;

        public ParcelMediaUpdatePacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.ParcelMediaUpdate;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 420;
            header.Reliable = true;
            DataBlock = new DataBlockBlock();
            DataBlockExtended = new DataBlockExtendedBlock();
        }

        public ParcelMediaUpdatePacket(byte[] bytes, int[] i) 
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
            DataBlock.FromBytes(bytes, i);
            DataBlockExtended.FromBytes(bytes, i);
        }

        public ParcelMediaUpdatePacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            DataBlock.FromBytes(bytes, i);
            DataBlockExtended.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += DataBlock.length;
            length += DataBlockExtended.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            DataBlock.ToBytes(bytes, i);
            DataBlockExtended.ToBytes(bytes, i);
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
