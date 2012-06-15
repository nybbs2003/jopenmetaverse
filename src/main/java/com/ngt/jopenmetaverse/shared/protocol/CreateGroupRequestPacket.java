package com.ngt.jopenmetaverse.shared.protocol;

import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;


    public final class CreateGroupRequestPacket extends Packet
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
        public final class GroupDataBlock extends PacketBlock
        {
            public byte[] Name;
            public byte[] Charter;
            public boolean ShowInList;
            public UUID InsigniaID;
            public int MembershipFee;
            public boolean OpenEnrollment;
            public boolean AllowPublish;
            public boolean MaturePublish;

            @Override
			public int getLength()
            {
                                {
                    int length = 27;
                    if (Name != null) { length += Name.length; }
                    if (Charter != null) { length += Charter.length; }
                    return length;
                }
            }

            public GroupDataBlock() { }
            public GroupDataBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    Name = new byte[length];
                    Utils.arraycopy(bytes, i[0], Name, 0, length); i[0] +=  length;
                    length = Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    Charter = new byte[length];
                    Utils.arraycopy(bytes, i[0], Charter, 0, length); i[0] +=  length;
                    ShowInList = (bytes[i[0]++] != 0) ? true : false;
                    InsigniaID.FromBytes(bytes, i[0]); i[0] += 16;
                    MembershipFee = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    OpenEnrollment = (bytes[i[0]++] != 0) ? true : false;
                    AllowPublish = (bytes[i[0]++] != 0) ? true : false;
                    MaturePublish = (bytes[i[0]++] != 0) ? true : false;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = (byte)Name.length;
                Utils.arraycopy(Name, 0, bytes, i[0], Name.length); i[0] +=  Name.length;
                bytes[i[0]++] = (byte)(Charter.length % 256);
                bytes[i[0]++] = (byte)((Charter.length >> 8) % 256);
                Utils.arraycopy(Charter, 0, bytes, i[0], Charter.length); i[0] +=  Charter.length;
                bytes[i[0]++] = (byte)((ShowInList) ? 1 : 0);
                InsigniaID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.intToBytes(MembershipFee, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = (byte)((OpenEnrollment) ? 1 : 0);
                bytes[i[0]++] = (byte)((AllowPublish) ? 1 : 0);
                bytes[i[0]++] = (byte)((MaturePublish) ? 1 : 0);
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += AgentData.getLength();
                length += GroupData.getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public GroupDataBlock GroupData;

        public CreateGroupRequestPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.CreateGroupRequest;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 339;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            GroupData = new GroupDataBlock();
        }

        public CreateGroupRequestPacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            GroupData.FromBytes(bytes, i);
        }

        public CreateGroupRequestPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
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
            GroupData.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += GroupData.getLength();
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            GroupData.ToBytes(bytes, i);
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
