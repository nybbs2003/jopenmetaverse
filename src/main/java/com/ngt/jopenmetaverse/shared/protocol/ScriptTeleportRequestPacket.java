package com.ngt.jopenmetaverse.shared.protocol;


    public final class ScriptTeleportRequestPacket extends Packet
    {
        /// <exclude/>
        public final class DataBlock extends PacketBlock
        {
            public byte[] ObjectName;
            public byte[] SimName;
            public Vector3 SimPosition;
            public Vector3 LookAt;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 26;
                    if (ObjectName != null) { length += ObjectName.length; }
                    if (SimName != null) { length += SimName.length; }
                    return length;
                }
            }

            public DataBlock() { }
            public DataBlock(byte[] bytes, int[] i)
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
                    ObjectName = new byte[length];
                    Buffer.BlockCopy(bytes, i, ObjectName, 0, length); i += length;
                    length = bytes[i++];
                    SimName = new byte[length];
                    Buffer.BlockCopy(bytes, i, SimName, 0, length); i += length;
                    SimPosition.FromBytes(bytes, i); i += 12;
                    LookAt.FromBytes(bytes, i); i += 12;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i++] = (byte)ObjectName.length;
                Buffer.BlockCopy(ObjectName, 0, bytes, i, ObjectName.length); i += ObjectName.length;
                bytes[i++] = (byte)SimName.length;
                Buffer.BlockCopy(SimName, 0, bytes, i, SimName.length); i += SimName.length;
                SimPosition.ToBytes(bytes, i); i += 12;
                LookAt.ToBytes(bytes, i); i += 12;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 10;
                length += Data.getLength();
                return length;
            }
        }
        public DataBlock Data;

        public ScriptTeleportRequestPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.ScriptTeleportRequest;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 195;
            header.Reliable = true;
            Data = new DataBlock();
        }

        public ScriptTeleportRequestPacket(byte[] bytes, int[] i) 
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
            Data.FromBytes(bytes, i);
        }

        public ScriptTeleportRequestPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            Data.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += Data.getLength();
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            Data.ToBytes(bytes, i);
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
