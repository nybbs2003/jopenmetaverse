package com.ngt.jopenmetaverse.shared.protocol;


    public final class AttachedSoundPacket extends Packet
    {
        /// <exclude/>
        public final class DataBlockBlock extends PacketBlock
        {
            public UUID SoundID;
            public UUID ObjectID;
            public UUID OwnerID;
            public float Gain;
            public byte Flags;

            @Override
			public int getLength()
            {
                                {
                    return 53;
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
                try
                {
                    SoundID.FromBytes(bytes, i[0]); i[0] += 16;
                    ObjectID.FromBytes(bytes, i[0]); i[0] += 16;
                    OwnerID.FromBytes(bytes, i[0]); i[0] += 16;
                    Gain = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    Flags = (byte)bytes[i[0]++];
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                SoundID.ToBytes(bytes, i[0]); i[0] += 16;
                ObjectID.ToBytes(bytes, i[0]); i[0] += 16;
                OwnerID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.floatToBytes(Gain, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = Flags;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 8;
                length += DataBlock.length;
                return length;
            }
        }
        public DataBlockBlock DataBlock;

        public AttachedSoundPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.AttachedSound;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Medium;
            header.ID = 13;
            header.Reliable = true;
            DataBlock = new DataBlockBlock();
        }

        public AttachedSoundPacket(byte[] bytes, int[] i) 
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
        }

        public AttachedSoundPacket(Header head, byte[] bytes, int[] i)
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
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 8;
            length += DataBlock.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            DataBlock.ToBytes(bytes, i);
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
