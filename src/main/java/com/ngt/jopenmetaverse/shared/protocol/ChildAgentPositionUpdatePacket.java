package com.ngt.jopenmetaverse.shared.protocol;

import java.math.BigInteger;

import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.types.Vector3;
import com.ngt.jopenmetaverse.shared.util.Utils;


    public final class ChildAgentPositionUpdatePacket extends Packet
    {
        /// <exclude/>
        public static final class AgentDataBlock extends PacketBlock
        {
            public BigInteger RegionHandle;
            public long ViewerCircuitCode;
            public UUID AgentID = new UUID();
            public UUID SessionID = new UUID();
            public Vector3 AgentPos = new Vector3();
            public Vector3 AgentVel = new Vector3();
            public Vector3 Center = new Vector3();
            public Vector3 Size = new Vector3();
            public Vector3 AtAxis = new Vector3();
            public Vector3 LeftAxis = new Vector3();
            public Vector3 UpAxis = new Vector3();
            public boolean ChangedGrid;

            @Override
			public int getLength()
            {
                                {
                    return 129;
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
                    RegionHandle = Utils.bytesToULongLit(bytes, i[0]); i[0] += 8;
                    ViewerCircuitCode = Utils.bytesToUIntLit(bytes, i[0]); i[0] += 4;
                    AgentID.FromBytes(bytes, i[0]); i[0] += 16;
                    SessionID.FromBytes(bytes, i[0]); i[0] += 16;
                    AgentPos.fromBytesLit(bytes, i[0]); i[0] += 12;
                    AgentVel.fromBytesLit(bytes, i[0]); i[0] += 12;
                    Center.fromBytesLit(bytes, i[0]); i[0] += 12;
                    Size.fromBytesLit(bytes, i[0]); i[0] += 12;
                    AtAxis.fromBytesLit(bytes, i[0]); i[0] += 12;
                    LeftAxis.fromBytesLit(bytes, i[0]); i[0] += 12;
                    UpAxis.fromBytesLit(bytes, i[0]); i[0] += 12;
                    ChangedGrid = (bytes[i[0]++] != 0) ? true : false;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException(Utils.getExceptionStackTraceAsString(e));
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.ulongToBytesLit(RegionHandle, bytes, i[0]); i[0] += 8;
                Utils.uintToBytesLit(ViewerCircuitCode, bytes, i[0]); i[0] += 4;
                AgentID.ToBytes(bytes, i[0]); i[0] += 16;
                SessionID.ToBytes(bytes, i[0]); i[0] += 16;
                AgentPos.toBytesLit(bytes, i[0]); i[0] += 12;
                AgentVel.toBytesLit(bytes, i[0]); i[0] += 12;
                Center.toBytesLit(bytes, i[0]); i[0] += 12;
                Size.toBytesLit(bytes, i[0]); i[0] += 12;
                AtAxis.toBytesLit(bytes, i[0]); i[0] += 12;
                LeftAxis.toBytesLit(bytes, i[0]); i[0] += 12;
                UpAxis.toBytesLit(bytes, i[0]); i[0] += 12;
                bytes[i[0]++] = (byte)((ChangedGrid) ? 1 : 0);
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

        public ChildAgentPositionUpdatePacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.ChildAgentPositionUpdate;
            this.header =  new Header();
            header.Frequency = PacketFrequency.High;
            header.ID = 27;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
        }

        public ChildAgentPositionUpdatePacket(byte[] bytes, int[] i) throws MalformedDataException 
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

        public ChildAgentPositionUpdatePacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
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
