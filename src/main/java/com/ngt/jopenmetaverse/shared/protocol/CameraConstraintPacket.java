package com.ngt.jopenmetaverse.shared.protocol;

import com.ngt.jopenmetaverse.shared.types.Vector4;


    public final class CameraConstraintPacket extends Packet
    {
        /// <exclude/>
        public static final class CameraCollidePlaneBlock extends PacketBlock
        {
            public Vector4 Plane;

            @Override
			public int getLength()
            {
                                {
                    return 16;
                }
            }

            public CameraCollidePlaneBlock() { }
            public CameraCollidePlaneBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    Plane.fromBytes(bytes, i[0]); i[0] += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException(Utils.getExceptionStackTraceAsString(e));
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Plane.toBytes(bytes, i[0]); i[0] += 16;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 7;
                length += CameraCollidePlane.getLength();
                return length;
            }
        }
        public CameraCollidePlaneBlock CameraCollidePlane;

        public CameraConstraintPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.CameraConstraint;
            this.header =  new Header();
            header.Frequency = PacketFrequency.High;
            header.ID = 22;
            header.Reliable = true;
            header.Zerocoded = true;
            CameraCollidePlane = new CameraCollidePlaneBlock();
        }

        public CameraConstraintPacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            CameraCollidePlane.FromBytes(bytes, i);
        }

        public CameraConstraintPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd) throws MalformedDataException
        {
            this.header =  header;
            CameraCollidePlane.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 7;
            length += CameraCollidePlane.getLength();
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            CameraCollidePlane.ToBytes(bytes, i);
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
