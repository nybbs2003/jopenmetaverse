package com.ngt.jopenmetaverse.shared.protocol;

import java.math.BigInteger;

import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.types.Vector3;
import com.ngt.jopenmetaverse.shared.util.Utils;


    public final class AgentMovementCompletePacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID SessionID;

            @Override
			public int getLength()
            {
                                {
                    return 32;
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
            }

        }

        /// <exclude/>
        public final class DataBlock extends PacketBlock
        {
            public Vector3 Position;
            public Vector3 LookAt;
            /**
             * Unsigned Long, only 8 bytes should be used and stored
             */
            public BigInteger RegionHandle;
            public long Timestamp;

            @Override
			public int getLength()
            {
                                {
                    return 36;
                }
            }

            public DataBlock() { }
            public DataBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    Position.fromBytes(bytes, i[0]); i[0] += 12;
                    LookAt.fromBytes(bytes, i[0]); i[0] += 12;
                    RegionHandle = Utils.bytesToULong(bytes, i[0]); i[0] += 8;
                    Timestamp = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Position.fromBytes(bytes, i[0]); i[0] += 12;
                LookAt.fromBytes(bytes, i[0]); i[0] += 12;
                Utils.ulongToBytes(RegionHandle, bytes, i[0]); i[0] += 8;
                Utils.uintToBytes(Timestamp, bytes, i[0]); i[0] += 4;
            }

        }

        /// <exclude/>
        public final class SimDataBlock extends PacketBlock
        {
            public byte[] ChannelVersion;

            @Override
			public int getLength()
            {
                                {
                    int length = 2;
                    if (ChannelVersion != null) { length += ChannelVersion.length; }
                    return length;
                }
            }

            public SimDataBlock() { }
            public SimDataBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    length = Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    ChannelVersion = new byte[length];
                    Utils.arraycopy(bytes, i[0], ChannelVersion, 0, length); i[0] +=  length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = (byte)(ChannelVersion.length % 256);
                bytes[i[0]++] = (byte)((ChannelVersion.length >> 8) % 256);
                Utils.arraycopy(ChannelVersion, 0, bytes, i[0], ChannelVersion.length); i[0] +=  ChannelVersion.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += AgentData.getLength();
                length += Data.getLength();
                length += SimData.getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public DataBlock Data;
        public SimDataBlock SimData;

        public AgentMovementCompletePacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.AgentMovementComplete;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 250;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            Data = new DataBlock();
            SimData = new SimDataBlock();
        }

        public AgentMovementCompletePacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            Data.FromBytes(bytes, i);
            SimData.FromBytes(bytes, i);
        }

        public AgentMovementCompletePacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
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
            Data.FromBytes(bytes, i);
            SimData.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += Data.getLength();
            length += SimData.getLength();
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            Data.ToBytes(bytes, i);
            SimData.ToBytes(bytes, i);
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
