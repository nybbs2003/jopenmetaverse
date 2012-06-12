package com.ngt.jopenmetaverse.shared.protocol;


    public final class ClearFollowCamPropertiesPacket extends Packet
    {
        /// <exclude/>
        public final class ObjectDataBlock extends PacketBlock
        {
            public UUID ObjectID;

            @Override
			public int getLength()
            {
                                {
                    return 16;
                }
            }

            public ObjectDataBlock() { }
            public ObjectDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ObjectID.FromBytes(bytes, i); i += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ObjectID.ToBytes(bytes, i); i += 16;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += ObjectData.length;
                return length;
            }
        }
        public ObjectDataBlock ObjectData;

        public ClearFollowCamPropertiesPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.ClearFollowCamProperties;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 160;
            header.Reliable = true;
            ObjectData = new ObjectDataBlock();
        }

        public ClearFollowCamPropertiesPacket(byte[] bytes, int[] i) 
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
            ObjectData.FromBytes(bytes, i);
        }

        public ClearFollowCamPropertiesPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            ObjectData.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += ObjectData.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            ObjectData.ToBytes(bytes, i);
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
