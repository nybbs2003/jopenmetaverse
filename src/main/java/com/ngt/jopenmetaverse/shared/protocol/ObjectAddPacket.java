package com.ngt.jopenmetaverse.shared.protocol;

import java.util.ArrayList;
import java.util.List;

import com.ngt.jopenmetaverse.shared.types.Quaternion;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.types.Vector3;
import com.ngt.jopenmetaverse.shared.util.Utils;
	public final class ObjectAddPacket extends Packet
    {
        /// <exclude/>
        public static final class AgentDataBlock extends PacketBlock
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
        public static final class ObjectDataBlock extends PacketBlock
        {
            public byte PCode;
		/** Unsigned Byte */ 
		public byte Material;
            public long AddFlags;
		/** Unsigned Byte */ 
		public byte PathCurve;
		/** Unsigned Byte */ 
		public byte ProfileCurve;
            
            /**Unsigned Short **/
            public int PathBegin;
            /**Unsigned Short **/
            public int PathEnd;
		/** Unsigned Byte */ 
		public byte PathScaleX;
		/** Unsigned Byte */ 
		public byte PathScaleY;
		/** Unsigned Byte */ 
		public byte PathShearX;
		/** Unsigned Byte */ 
		public byte PathShearY;
		/** Signed Byte */ 
		public byte PathTwist;
		/** Signed Byte */ 
		public byte PathTwistBegin;
		/** Signed Byte */ 
		public byte PathRadiusOffset;
		/** Signed Byte */ 
		public byte PathTaperX;
		/** Signed Byte */ 
		public byte PathTaperY;
		/** Unsigned Byte */ 
		public byte PathRevolutions;
		/** Signed Byte */ 
		public byte PathSkew;
            
            /**Unsigned Short **/
            public int ProfileBegin;
            /**Unsigned Short **/
            public int ProfileEnd;
            /**Unsigned Short **/
            public int ProfileHollow;
		/** Unsigned Byte */ 
		public byte BypassRaycast;
            public Vector3 RayStart;
            public Vector3 RayEnd;
            public UUID RayTargetID;
		/** Unsigned Byte */ 
		public byte RayEndIsIntersection;
            public Vector3 Scale;
            public Quaternion Rotation;
		/** Unsigned Byte */ 
		public byte State;

            @Override
			public int getLength()
            {
                                {
                    return 96;
                }
            }

            public ObjectDataBlock() { }
            public ObjectDataBlock(byte[] bytes, int[] i) throws MalformedDataException
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
                    PathTwist = (byte)bytes[i[0]++];
                    PathTwistBegin = (byte)bytes[i[0]++];
                    PathRadiusOffset = (byte)bytes[i[0]++];
                    PathTaperX = (byte)bytes[i[0]++];
                    PathTaperY = (byte)bytes[i[0]++];
                    PathRevolutions = (byte)bytes[i[0]++];
                    PathSkew = (byte)bytes[i[0]++];
                    ProfileBegin = (int)Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    ProfileEnd = (int)Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    ProfileHollow = (int)Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    BypassRaycast = (byte)bytes[i[0]++];
                    RayStart.fromBytes(bytes, i[0]); i[0] += 12;
                    RayEnd.fromBytes(bytes, i[0]); i[0] += 12;
                    RayTargetID.FromBytes(bytes, i[0]); i[0] += 16;
                    RayEndIsIntersection = (byte)bytes[i[0]++];
                    Scale.fromBytes(bytes, i[0]); i[0] += 12;
                    Rotation.fromBytes(bytes, i[0], true); i[0] += 12;
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
                RayStart.toBytes(bytes, i[0]); i[0] += 12;
                RayEnd.toBytes(bytes, i[0]); i[0] += 12;
                RayTargetID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = RayEndIsIntersection;
                Scale.toBytes(bytes, i[0]); i[0] += 12;
                Rotation.toBytes(bytes, i[0]); i[0] += 12;
                bytes[i[0]++] = State;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 8;
                length += AgentData.getLength();
                length += ObjectData.getLength();
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

        public ObjectAddPacket(byte[] bytes, int[] i) throws MalformedDataException 
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

        public ObjectAddPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
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
            length += ObjectData.getLength();
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
