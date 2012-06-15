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
                                {
                    return 48;
                }
            }

            public AgentDataBlock() { }
            public AgentDataBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    AgentID.FromBytes(bytes, i[0]); i[0] += 16;
                    SessionID.FromBytes(bytes, i[0]); i[0] += 16;
                    GroupID.FromBytes(bytes, i[0]); i[0] += 16;
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
                GroupID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        /// <exclude/>
        public final class ObjectDataBlock extends PacketBlock
        {
            public byte PCode;
            public byte Material;
            public long AddFlags;
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
                    PCode = (byte)bytes[i[0]++];
                    Material = (byte)bytes[i[0]++];
                    AddFlags = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    PathCurve = (byte)bytes[i[0]++];
                    ProfileCurve = (byte)bytes[i[0]++];
                    PathBegin = (int)Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    PathEnd = (int)Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    PathScaleX = (byte)bytes[i[0]++];
                    PathScaleY = (byte)bytes[i[0]++];
                    PathShearX = (byte)bytes[i[0]++];
                    PathShearY = (byte)bytes[i[0]++];
                    PathTwist = (sbyte)bytes[i[0]++];
                    PathTwistBegin = (sbyte)bytes[i[0]++];
                    PathRadiusOffset = (sbyte)bytes[i[0]++];
                    PathTaperX = (sbyte)bytes[i[0]++];
                    PathTaperY = (sbyte)bytes[i[0]++];
                    PathRevolutions = (byte)bytes[i[0]++];
                    PathSkew = (sbyte)bytes[i[0]++];
                    ProfileBegin = (int)Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    ProfileEnd = (int)Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    ProfileHollow = (int)Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    BypassRaycast = (byte)bytes[i[0]++];
                    RayStart.FromBytes(bytes, i[0]); i[0] += 12;
                    RayEnd.FromBytes(bytes, i[0]); i[0] += 12;
                    RayTargetID.FromBytes(bytes, i[0]); i[0] += 16;
                    RayEndIsIntersection = (byte)bytes[i[0]++];
                    Scale.FromBytes(bytes, i[0]); i[0] += 12;
                    Rotation.FromBytes(bytes, i[0], true); i[0] += 12;
                    State = (byte)bytes[i[0]++];
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = PCode;
                bytes[i[0]++] = Material;
                Utils.uintToBytes(AddFlags, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = PathCurve;
                bytes[i[0]++] = ProfileCurve;
                bytes[i[0]++] = (byte)(PathBegin % 256);
                bytes[i[0]++] = (byte)((PathBegin >> 8) % 256);
                bytes[i[0]++] = (byte)(PathEnd % 256);
                bytes[i[0]++] = (byte)((PathEnd >> 8) % 256);
                bytes[i[0]++] = PathScaleX;
                bytes[i[0]++] = PathScaleY;
                bytes[i[0]++] = PathShearX;
                bytes[i[0]++] = PathShearY;
                bytes[i[0]++] = (byte)PathTwist;
                bytes[i[0]++] = (byte)PathTwistBegin;
                bytes[i[0]++] = (byte)PathRadiusOffset;
                bytes[i[0]++] = (byte)PathTaperX;
                bytes[i[0]++] = (byte)PathTaperY;
                bytes[i[0]++] = PathRevolutions;
                bytes[i[0]++] = (byte)PathSkew;
                bytes[i[0]++] = (byte)(ProfileBegin % 256);
                bytes[i[0]++] = (byte)((ProfileBegin >> 8) % 256);
                bytes[i[0]++] = (byte)(ProfileEnd % 256);
                bytes[i[0]++] = (byte)((ProfileEnd >> 8) % 256);
                bytes[i[0]++] = (byte)(ProfileHollow % 256);
                bytes[i[0]++] = (byte)((ProfileHollow >> 8) % 256);
                bytes[i[0]++] = BypassRaycast;
                RayStart.ToBytes(bytes, i[0]); i[0] += 12;
                RayEnd.ToBytes(bytes, i[0]); i[0] += 12;
                RayTargetID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = RayEndIsIntersection;
                Scale.ToBytes(bytes, i[0]); i[0] += 12;
                Rotation.ToBytes(bytes, i[0]); i[0] += 12;
                bytes[i[0]++] = State;
            }

        }

        @Override
			public int getLength()
        {
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
		public void FromBytes(byte[] bytes, int[] i, int[] packetEnd, byte[] zeroBuffer) throws MalformedDataException
        {
            header.FromBytes(bytes, i, packetEnd);
            if (header.Zerocoded && zeroBuffer != null)
            {
                packetEnd[0] = Helpers.ZeroDecode(bytes, packetEnd[0] + 1, zeroBuffer) - 1;
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
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd) throws MalformedDataException
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
            int[] i = new int[]{0};
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
