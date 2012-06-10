package com.ngt.jopenmetaverse.shared.protocol;


    public final class ObjectAddPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID SessionID;
            public UUID GroupID;

            @Override
			public int getLength()
            {
                get
                {
                    return 48;
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
                    SessionID.FromBytes(bytes, i); i += 16;
                    GroupID.FromBytes(bytes, i); i += 16;
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
                SessionID.ToBytes(bytes, i); i += 16;
                GroupID.ToBytes(bytes, i); i += 16;
            }

        }

        /// <exclude/>
        public final class ObjectDataBlock extends PacketBlock
        {
            public byte PCode;
            public byte Material;
            public uint AddFlags;
            public byte PathCurve;
            public byte ProfileCurve;
            public ushort PathBegin;
            public ushort PathEnd;
            public byte PathScaleX;
            public byte PathScaleY;
            public byte PathShearX;
            public byte PathShearY;
            public sbyte PathTwist;
            public sbyte PathTwistBegin;
            public sbyte PathRadiusOffset;
            public sbyte PathTaperX;
            public sbyte PathTaperY;
            public byte PathRevolutions;
            public sbyte PathSkew;
            public ushort ProfileBegin;
            public ushort ProfileEnd;
            public ushort ProfileHollow;
            public byte BypassRaycast;
            public Vector3 RayStart;
            public Vector3 RayEnd;
            public UUID RayTargetID;
            public byte RayEndIsIntersection;
            public Vector3 Scale;
            public Quaternion Rotation;
            public byte State;

            @Override
			public int getLength()
            {
                get
                {
                    return 96;
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
                    PCode = (byte)bytes[i++];
                    Material = (byte)bytes[i++];
                    AddFlags = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    PathCurve = (byte)bytes[i++];
                    ProfileCurve = (byte)bytes[i++];
                    PathBegin = (ushort)(bytes[i++] + (bytes[i++] << 8));
                    PathEnd = (ushort)(bytes[i++] + (bytes[i++] << 8));
                    PathScaleX = (byte)bytes[i++];
                    PathScaleY = (byte)bytes[i++];
                    PathShearX = (byte)bytes[i++];
                    PathShearY = (byte)bytes[i++];
                    PathTwist = (sbyte)bytes[i++];
                    PathTwistBegin = (sbyte)bytes[i++];
                    PathRadiusOffset = (sbyte)bytes[i++];
                    PathTaperX = (sbyte)bytes[i++];
                    PathTaperY = (sbyte)bytes[i++];
                    PathRevolutions = (byte)bytes[i++];
                    PathSkew = (sbyte)bytes[i++];
                    ProfileBegin = (ushort)(bytes[i++] + (bytes[i++] << 8));
                    ProfileEnd = (ushort)(bytes[i++] + (bytes[i++] << 8));
                    ProfileHollow = (ushort)(bytes[i++] + (bytes[i++] << 8));
                    BypassRaycast = (byte)bytes[i++];
                    RayStart.FromBytes(bytes, i); i += 12;
                    RayEnd.FromBytes(bytes, i); i += 12;
                    RayTargetID.FromBytes(bytes, i); i += 16;
                    RayEndIsIntersection = (byte)bytes[i++];
                    Scale.FromBytes(bytes, i); i += 12;
                    Rotation.FromBytes(bytes, i, true); i += 12;
                    State = (byte)bytes[i++];
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i++] = PCode;
                bytes[i++] = Material;
                Utils.UIntToBytes(AddFlags, bytes, i); i += 4;
                bytes[i++] = PathCurve;
                bytes[i++] = ProfileCurve;
                bytes[i++] = (byte)(PathBegin % 256);
                bytes[i++] = (byte)((PathBegin >> 8) % 256);
                bytes[i++] = (byte)(PathEnd % 256);
                bytes[i++] = (byte)((PathEnd >> 8) % 256);
                bytes[i++] = PathScaleX;
                bytes[i++] = PathScaleY;
                bytes[i++] = PathShearX;
                bytes[i++] = PathShearY;
                bytes[i++] = (byte)PathTwist;
                bytes[i++] = (byte)PathTwistBegin;
                bytes[i++] = (byte)PathRadiusOffset;
                bytes[i++] = (byte)PathTaperX;
                bytes[i++] = (byte)PathTaperY;
                bytes[i++] = PathRevolutions;
                bytes[i++] = (byte)PathSkew;
                bytes[i++] = (byte)(ProfileBegin % 256);
                bytes[i++] = (byte)((ProfileBegin >> 8) % 256);
                bytes[i++] = (byte)(ProfileEnd % 256);
                bytes[i++] = (byte)((ProfileEnd >> 8) % 256);
                bytes[i++] = (byte)(ProfileHollow % 256);
                bytes[i++] = (byte)((ProfileHollow >> 8) % 256);
                bytes[i++] = BypassRaycast;
                RayStart.ToBytes(bytes, i); i += 12;
                RayEnd.ToBytes(bytes, i); i += 12;
                RayTargetID.ToBytes(bytes, i); i += 16;
                bytes[i++] = RayEndIsIntersection;
                Scale.ToBytes(bytes, i); i += 12;
                Rotation.ToBytes(bytes, i); i += 12;
                bytes[i++] = State;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 8;
                length += AgentData.getLength();
                length += ObjectData.length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public ObjectDataBlock ObjectData;

        public ObjectAddPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.ObjectAdd;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Medium;
            header.ID = 1;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            ObjectData = new ObjectDataBlock();
        }

        public ObjectAddPacket(byte[] bytes, int[] i) 
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
            ObjectData.FromBytes(bytes, i);
        }

        public ObjectAddPacket(Header head, byte[] bytes, int[] i)
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
            ObjectData.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 8;
            length += AgentData.getLength();
            length += ObjectData.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
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
