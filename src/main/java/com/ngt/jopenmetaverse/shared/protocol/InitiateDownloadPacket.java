package com.ngt.jopenmetaverse.shared.protocol;


    public final class InitiateDownloadPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;

            @Override
			public int getLength()
            {
                get
                {
                    return 16;
                }
            }

            public AgentDataBlock() { }
            public AgentDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    AgentID.FromBytes(bytes, i); i += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                AgentID.ToBytes(bytes, i); i += 16;
            }

        }

        /// <exclude/>
        public final class FileDataBlock extends PacketBlock
        {
            public byte[] SimFilename;
            public byte[] ViewerFilename;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 2;
                    if (SimFilename != null) { length += SimFilename.length; }
                    if (ViewerFilename != null) { length += ViewerFilename.length; }
                    return length;
                }
            }

            public FileDataBlock() { }
            public FileDataBlock(byte[] bytes, int[] i)
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
                    SimFilename = new byte[length];
                    Buffer.BlockCopy(bytes, i, SimFilename, 0, length); i += length;
                    length = bytes[i++];
                    ViewerFilename = new byte[length];
                    Buffer.BlockCopy(bytes, i, ViewerFilename, 0, length); i += length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i++] = (byte)SimFilename.length;
                Buffer.BlockCopy(SimFilename, 0, bytes, i, SimFilename.length); i += SimFilename.length;
                bytes[i++] = (byte)ViewerFilename.length;
                Buffer.BlockCopy(ViewerFilename, 0, bytes, i, ViewerFilename.length); i += ViewerFilename.length;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 10;
                length += AgentData.getLength();
                length += FileData.length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public FileDataBlock FileData;

        public InitiateDownloadPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.InitiateDownload;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 403;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            FileData = new FileDataBlock();
        }

        public InitiateDownloadPacket(byte[] bytes, int[] i) 
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
            AgentData.FromBytes(bytes, i);
            FileData.FromBytes(bytes, i);
        }

        public InitiateDownloadPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            AgentData.FromBytes(bytes, i);
            FileData.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += FileData.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            FileData.ToBytes(bytes, i);
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