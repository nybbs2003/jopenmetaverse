package com.ngt.jopenmetaverse.shared.protocol;


    public final class AgentUpdatePacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID SessionID;
            public Quaternion BodyRotation;
            public Quaternion HeadRotation;
            public byte State;
            public Vector3 CameraCenter;
            public Vector3 CameraAtAxis;
            public Vector3 CameraLeftAxis;
            public Vector3 CameraUpAxis;
            public float Far;
            public uint ControlFlags;
            public byte Flags;

            @Override
			public int getLength()
            {
                                {
                    return 114;
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
                    SessionID.FromBytes(bytes, i[0]); i[0] += 16;
                    BodyRotation.FromBytes(bytes, i, true); i += 12;
                    HeadRotation.FromBytes(bytes, i, true); i += 12;
                    State = (byte)bytes[i++];
                    CameraCenter.FromBytes(bytes, i); i += 12;
                    CameraAtAxis.FromBytes(bytes, i); i += 12;
                    CameraLeftAxis.FromBytes(bytes, i); i += 12;
                    CameraUpAxis.FromBytes(bytes, i); i += 12;
                    Far = Utils.BytesToFloat(bytes, i); i += 4;
                    ControlFlags = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Flags = (byte)bytes[i++];
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                AgentID.ToBytes(bytes, i[0]); i[0] += 16;
                SessionID.ToBytes(bytes, i[0]); i[0] += 16;
                BodyRotation.ToBytes(bytes, i); i += 12;
                HeadRotation.ToBytes(bytes, i); i += 12;
                bytes[i++] = State;
                CameraCenter.ToBytes(bytes, i); i += 12;
                CameraAtAxis.ToBytes(bytes, i); i += 12;
                CameraLeftAxis.ToBytes(bytes, i); i += 12;
                CameraUpAxis.ToBytes(bytes, i); i += 12;
                Utils.FloatToBytes(Far, bytes, i); i += 4;
                Utils.UIntToBytes(ControlFlags, bytes, i); i += 4;
                bytes[i++] = Flags;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 7;
                length += AgentData.getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;

        public AgentUpdatePacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.AgentUpdate;
            this.header =  new Header();
            header.Frequency = PacketFrequency.High;
            header.ID = 4;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
        }

        public AgentUpdatePacket(byte[] bytes, int[] i) 
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
            AgentData.FromBytes(bytes, i);
        }

        public AgentUpdatePacket(Header head, byte[] bytes, int[] i)
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
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 7;
            length += AgentData.getLength();
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
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
