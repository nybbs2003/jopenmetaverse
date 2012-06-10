package com.ngt.jopenmetaverse.shared.protocol;


    public final class GroupRoleChangesPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID SessionID;
            public UUID GroupID;

            @Override
			public int getLength()
            {
                get
                {
                    return 48;
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
                    SessionID.FromBytes(bytes, i); i += 16;
                    GroupID.FromBytes(bytes, i); i += 16;
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
                SessionID.ToBytes(bytes, i); i += 16;
                GroupID.ToBytes(bytes, i); i += 16;
            }

        }

        /// <exclude/>
        public final class RoleChangeBlock extends PacketBlock
        {
            public UUID RoleID;
            public UUID MemberID;
            public uint Change;

            @Override
			public int getLength()
            {
                get
                {
                    return 36;
                }
            }

            public RoleChangeBlock() { }
            public RoleChangeBlock(byte[] bytes, int[] i)
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
                    Change = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
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
                Utils.UIntToBytes(Change, bytes, i); i += 4;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 11;
                length += AgentData.getLength();
                for (int j = 0; j < RoleChange.length; j++)
                    length += RoleChange[j].length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public RoleChangeBlock[] RoleChange;

        public GroupRoleChangesPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.GroupRoleChanges;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 342;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            RoleChange = null;
        }

        public GroupRoleChangesPacket(byte[] bytes, int[] i) 
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
            if(RoleChange == null || RoleChange.length != -1) {
                RoleChange = new RoleChangeBlock[count];
                for(int j = 0; j < count; j++)
                { RoleChange[j] = new RoleChangeBlock(); }
            }
            for (int j = 0; j < count; j++)
            { RoleChange[j].FromBytes(bytes, i); }
        }

        public GroupRoleChangesPacket(Header head, byte[] bytes, int[] i)
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
            if(RoleChange == null || RoleChange.length != count) {
                RoleChange = new RoleChangeBlock[count];
                for(int j = 0; j < count; j++)
                { RoleChange[j] = new RoleChangeBlock(); }
            }
            for (int j = 0; j < count; j++)
            { RoleChange[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length++;
            for (int j = 0; j < RoleChange.length; j++) { length += RoleChange[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            bytes[i++] = (byte)RoleChange.length;
            for (int j = 0; j < RoleChange.length; j++) { RoleChange[j].ToBytes(bytes, i); }
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

            int RoleChangeStart = 0;
            do
            {
                int variableLength = 0;
                int RoleChangeCount = 0;

                i = RoleChangeStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < RoleChange.length) {
                    int blockLength = RoleChange[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++RoleChangeCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Buffer.BlockCopy(fixedBytes, 0, packet, 0, length);
                if (packets.Count > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)RoleChangeCount;
                for (i = RoleChangeStart; i < RoleChangeStart + RoleChangeCount; i++) { RoleChange[i].ToBytes(packet, ref length); }
                RoleChangeStart += RoleChangeCount;

                if (acksLength > 0) {
                    Buffer.BlockCopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.Add(packet);
            } while (
                RoleChangeStart < RoleChange.length);

            return packets.ToArray();
        }
    }

    /// <exclude/>
