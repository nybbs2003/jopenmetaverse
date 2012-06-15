package com.ngt.jopenmetaverse.shared.protocol;

import com.ngt.jopenmetaverse.shared.types.Quaternion;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.types.Vector3;
import com.ngt.jopenmetaverse.shared.util.Utils;


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
            public long ControlFlags;
            public byte Flags;

            @Override
			public int getLength()
            {
                                {
                    return 114;
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
                    BodyRotation.fromBytes(bytes, i[0], true); i[0] += 12;
                    HeadRotation.fromBytes(bytes, i[0], true); i[0] += 12;
                    State = (byte)bytes[i[0]++];
                    CameraCenter.fromBytes(bytes, i[0]); i[0] += 12;
                    CameraAtAxis.fromBytes(bytes, i[0]); i[0] += 12;
                    CameraLeftAxis.fromBytes(bytes, i[0]); i[0] += 12;
                    CameraUpAxis.fromBytes(bytes, i[0]); i[0] += 12;
                    Far = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    ControlFlags = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    Flags = (byte)bytes[i[0]++];
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
                BodyRotation.toBytes(bytes, i[0]); i[0] += 12;
                HeadRotation.toBytes(bytes, i[0]); i[0] += 12;
                bytes[i[0]++] = State;
                CameraCenter.toBytes(bytes, i[0]); i[0] += 12;
                CameraAtAxis.toBytes(bytes, i[0]); i[0] += 12;
                CameraLeftAxis.toBytes(bytes, i[0]); i[0] += 12;
                CameraUpAxis.toBytes(bytes, i[0]); i[0] += 12;
                Utils.floatToBytes(Far, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(ControlFlags, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = Flags;
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

        public AgentUpdatePacket(byte[] bytes, int[] i) throws MalformedDataException 
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
        }

        public AgentUpdatePacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
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
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 7;
            length += AgentData.getLength();
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
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
