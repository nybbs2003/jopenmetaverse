package com.ngt.jopenmetaverse.shared.protocol;


    public final class AvatarSitResponsePacket extends Packet
    {
        /// <exclude/>
        public final class SitObjectBlock extends PacketBlock
        {
            public UUID ID;

            @Override
			public int getLength()
            {
                get
                {
                    return 16;
                }
            }

            public SitObjectBlock() { }
            public SitObjectBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ID.FromBytes(bytes, i); i += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ID.ToBytes(bytes, i); i += 16;
            }

        }

        /// <exclude/>
        public final class SitTransformBlock extends PacketBlock
        {
            public bool AutoPilot;
            public Vector3 SitPosition;
            public Quaternion SitRotation;
            public Vector3 CameraEyeOffset;
            public Vector3 CameraAtOffset;
            public bool ForceMouselook;

            @Override
			public int getLength()
            {
                get
                {
                    return 50;
                }
            }

            public SitTransformBlock() { }
            public SitTransformBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    AutoPilot = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    SitPosition.FromBytes(bytes, i); i += 12;
                    SitRotation.FromBytes(bytes, i, true); i += 12;
                    CameraEyeOffset.FromBytes(bytes, i); i += 12;
                    CameraAtOffset.FromBytes(bytes, i); i += 12;
                    ForceMouselook = (bytes[i++] != 0) ? (bool)true : (bool)false;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i++] = (byte)((AutoPilot) ? 1 : 0);
                SitPosition.ToBytes(bytes, i); i += 12;
                SitRotation.ToBytes(bytes, i); i += 12;
                CameraEyeOffset.ToBytes(bytes, i); i += 12;
                CameraAtOffset.ToBytes(bytes, i); i += 12;
                bytes[i++] = (byte)((ForceMouselook) ? 1 : 0);
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 7;
                length += SitObject.length;
                length += SitTransform.length;
                return length;
            }
        }
        public SitObjectBlock SitObject;
        public SitTransformBlock SitTransform;

        public AvatarSitResponsePacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.AvatarSitResponse;
            this.header =  new Header();
            header.Frequency = PacketFrequency.High;
            header.ID = 21;
            header.Reliable = true;
            header.Zerocoded = true;
            SitObject = new SitObjectBlock();
            SitTransform = new SitTransformBlock();
        }

        public AvatarSitResponsePacket(byte[] bytes, int[] i) 
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
            SitObject.FromBytes(bytes, i);
            SitTransform.FromBytes(bytes, i);
        }

        public AvatarSitResponsePacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            SitObject.FromBytes(bytes, i);
            SitTransform.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 7;
            length += SitObject.length;
            length += SitTransform.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            SitObject.ToBytes(bytes, i);
            SitTransform.ToBytes(bytes, i);
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
