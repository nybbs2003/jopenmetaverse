package com.ngt.jopenmetaverse.shared.protocol;

import java.math.BigInteger;

import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;


    public final class AgentDataUpdatePacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public byte[] FirstName;
            public byte[] LastName;
            public byte[] GroupTitle;
            public UUID ActiveGroupID;
            public BigInteger GroupPowers;
            public byte[] GroupName;

            @Override
			public int getLength()
            {
                                {
                    int length = 44;
                    if (FirstName != null) { length += FirstName.length; }
                    if (LastName != null) { length += LastName.length; }
                    if (GroupTitle != null) { length += GroupTitle.length; }
                    if (GroupName != null) { length += GroupName.length; }
                    return length;
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
                int length;
                try
                {
                    AgentID.FromBytes(bytes, i[0]); i[0] += 16;
                    length = bytes[i[0]++];
                    FirstName = new byte[length];
                    Utils.arraycopy(bytes, i[0], FirstName, 0, length); i[0] +=  length;
                    length = bytes[i[0]++];
                    LastName = new byte[length];
                    Utils.arraycopy(bytes, i[0], LastName, 0, length); i[0] +=  length;
                    length = bytes[i[0]++];
                    GroupTitle = new byte[length];
                    Utils.arraycopy(bytes, i[0], GroupTitle, 0, length); i[0] +=  length;
                    ActiveGroupID.FromBytes(bytes, i[0]); i[0] += 16;
                    GroupPowers = new BigInteger(bytes);
                    length = bytes[i[0]++];
                    GroupName = new byte[length];
                    Utils.arraycopy(bytes, i[0], GroupName, 0, length); i[0] +=  length;
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
                bytes[i[0]++] = (byte)FirstName.length;
                Utils.arraycopy(FirstName, 0, bytes, i[0], FirstName.length); i[0] +=  FirstName.length;
                bytes[i[0]++] = (byte)LastName.length;
                Utils.arraycopy(LastName, 0, bytes, i[0], LastName.length); i[0] +=  LastName.length;
                bytes[i[0]++] = (byte)GroupTitle.length;
                Utils.arraycopy(GroupTitle, 0, bytes, i[0], GroupTitle.length); i[0] +=  GroupTitle.length;
                ActiveGroupID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.ulongToBytes(GroupPowers, bytes, i[0]); i[0] += 8;
                bytes[i[0]++] = (byte)GroupName.length;
                Utils.arraycopy(GroupName, 0, bytes, i[0], GroupName.length); i[0] +=  GroupName.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += AgentData.getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;

        public AgentDataUpdatePacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.AgentDataUpdate;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 387;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
        }

        public AgentDataUpdatePacket(byte[] bytes, int[] i) throws MalformedDataException 
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

        public AgentDataUpdatePacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
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
            int length = 10;
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
