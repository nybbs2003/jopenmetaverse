package com.ngt.jopenmetaverse.shared.protocol;


    public final class GroupRoleMembersReplyPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID GroupID;
            public UUID RequestID;
            public uint TotalPairs;

            @Override
			public int getLength()
            {
                get
                {
                    return 52;
                }
            }

            public AgentDataBlock() { }
            public AgentDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    AgentID.FromBytes(bytes, i); i += 16;
                    GroupID.FromBytes(bytes, i); i += 16;
                    RequestID.FromBytes(bytes, i); i += 16;
                    TotalPairs = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                AgentID.ToBytes(bytes, i); i += 16;
                GroupID.ToBytes(bytes, i); i += 16;
                RequestID.ToBytes(bytes, i); i += 16;
                Utils.UIntToBytes(TotalPairs, bytes, i); i += 4;
            }

        }

        /// <exclude/>
        public final class MemberDataBlock extends PacketBlock
        {
            public UUID RoleID;
            public UUID MemberID;

            @Override
			public int getLength()
            {
                get
                {
                    return 32;
                }
            }

            public MemberDataBlock() { }
            public MemberDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    RoleID.FromBytes(bytes, i); i += 16;
                    MemberID.FromBytes(bytes, i); i += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                RoleID.ToBytes(bytes, i); i += 16;
                MemberID.ToBytes(bytes, i); i += 16;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 11;
                length += AgentData.getLength();
                for (int j = 0; j < MemberData.length; j++)
                    length += MemberData[j].length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public MemberDataBlock[] MemberData;

        public GroupRoleMembersReplyPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.GroupRoleMembersReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 374;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            MemberData = null;
        }

        public GroupRoleMembersReplyPacket(byte[] bytes, int[] i) 
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
            AgentData.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(MemberData == null || MemberData.length != -1) {
                MemberData = new MemberDataBlock[count];
                for(int j = 0; j < count; j++)
                { MemberData[j] = new MemberDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { MemberData[j].FromBytes(bytes, i); }
        }

        public GroupRoleMembersReplyPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            AgentData.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(MemberData == null || MemberData.length != count) {
                MemberData = new MemberDataBlock[count];
                for(int j = 0; j < count; j++)
                { MemberData[j] = new MemberDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { MemberData[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length++;
            for (int j = 0; j < MemberData.length; j++) { length += MemberData[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            bytes[i++] = (byte)MemberData.length;
            for (int j = 0; j < MemberData.length; j++) { MemberData[j].ToBytes(bytes, i); }
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            System.Collections.Generic.List<byte[]> packets = new System.Collections.Generic.List<byte[]>();
            int i = 0;
            int fixedLength = 10;

            byte[] ackBytes = null;
            int acksLength = 0;
            if (header.AckList != null && header.AckList.length > 0) {
                header.AppendedAcks = true;
                ackBytes = new byte[header.AckList.length * 4 + 1];
                header.AcksToBytes(ackBytes, ref acksLength);
            }

            fixedLength += AgentData.getLength();
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AgentData.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int MemberDataStart = 0;
            do
            {
                int variableLength = 0;
                int MemberDataCount = 0;

                i = MemberDataStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < MemberData.length) {
                    int blockLength = MemberData[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++MemberDataCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Buffer.BlockCopy(fixedBytes, 0, packet, 0, length);
                if (packets.Count > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)MemberDataCount;
                for (i = MemberDataStart; i < MemberDataStart + MemberDataCount; i++) { MemberData[i].ToBytes(packet, ref length); }
                MemberDataStart += MemberDataCount;

                if (acksLength > 0) {
                    Buffer.BlockCopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.Add(packet);
            } while (
                MemberDataStart < MemberData.length);

            return packets.ToArray();
        }
    }

    /// <exclude/>
