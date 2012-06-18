package com.ngt.jopenmetaverse.shared.protocol;

import com.ngt.jopenmetaverse.shared.types.Quaternion;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.types.Vector3;


    public final class AvatarSitResponsePacket extends Packet
    {
        /// <exclude/>
        public static final class SitObjectBlock extends PacketBlock
        {
            public UUID ID;

            @Override
			public int getLength()
            {
                                {
                    return 16;
                }
            }

            public SitObjectBlock() { }
            public SitObjectBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ID.FromBytes(bytes, i[0]); i[0] += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        /// <exclude/>
        public static final class SitTransformBlock extends PacketBlock
        {
            public boolean AutoPilot;
            public Vector3 SitPosition;
            public Quaternion SitRotation;
            public Vector3 CameraEyeOffset;
            public Vector3 CameraAtOffset;
            public boolean ForceMouselook;

            @Override
			public int getLength()
            {
                                {
                    return 50;
                }
            }

            public SitTransformBlock() { }
            public SitTransformBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    AutoPilot = (bytes[i[0]++] != 0) ? true : false;
                    SitPosition.fromBytes(bytes, i[0]); i[0] += 12;
                    SitRotation.fromBytes(bytes, i[0], true); i[0] += 12;
                    CameraEyeOffset.fromBytes(bytes, i[0]); i[0] += 12;
                    CameraAtOffset.fromBytes(bytes, i[0]); i[0] += 12;
                    ForceMouselook = (bytes[i[0]++] != 0) ? true : false;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = (byte)((AutoPilot) ? 1 : 0);
                SitPosition.toBytes(bytes, i[0]); i[0] += 12;
                SitRotation.toBytes(bytes, i[0]); i[0] += 12;
                CameraEyeOffset.toBytes(bytes, i[0]); i[0] += 12;
                CameraAtOffset.toBytes(bytes, i[0]); i[0] += 12;
                bytes[i[0]++] = (byte)((ForceMouselook) ? 1 : 0);
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 7;
                length += SitObject.getLength();
                length += SitTransform.getLength();
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

        public AvatarSitResponsePacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            SitObject.FromBytes(bytes, i);
            SitTransform.FromBytes(bytes, i);
        }

        public AvatarSitResponsePacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd) throws MalformedDataException
        {
            this.header =  header;
            SitObject.FromBytes(bytes, i);
            SitTransform.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 7;
            length += SitObject.getLength();
            length += SitTransform.getLength();
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
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
