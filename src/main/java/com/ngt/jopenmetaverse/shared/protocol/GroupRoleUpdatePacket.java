package com.ngt.jopenmetaverse.shared.protocol;


    public final class GroupRoleUpdatePacket extends Packet
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
                    SessionID.FromBytes(bytes, i[0]); i[0] += 16;
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
                AgentID.ToBytes(bytes, i[0]); i[0] += 16;
                SessionID.ToBytes(bytes, i[0]); i[0] += 16;
                GroupID.ToBytes(bytes, i); i += 16;
            }

        }

        /// <exclude/>
        public final class RoleDataBlock extends PacketBlock
        {
            public UUID RoleID;
            public byte[] Name;
            public byte[] Description;
            public byte[] Title;
            public ulong Powers;
            public byte UpdateType;

            @Override
			public int getLength()
            {
                                {
                    int length = 28;
                    if (Name != null) { length += Name.length; }
                    if (Description != null) { length += Description.length; }
                    if (Title != null) { length += Title.length; }
                    return length;
                }
            }

            public RoleDataBlock() { }
            public RoleDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    RoleID.FromBytes(bytes, i); i += 16;
                    length = bytes[i++];
                    Name = new byte[length];
                    Utils.arraycopy(bytes, i, Name, 0, length); i += length;
                    length = bytes[i++];
                    Description = new byte[length];
                    Utils.arraycopy(bytes, i, Description, 0, length); i += length;
                    length = bytes[i++];
                    Title = new byte[length];
                    Utils.arraycopy(bytes, i, Title, 0, length); i += length;
                    Powers = (ulong)((ulong)bytes[i++] + ((ulong)bytes[i++] << 8) + ((ulong)bytes[i++] << 16) + ((ulong)bytes[i++] << 24) + ((ulong)bytes[i++] << 32) + ((ulong)bytes[i++] << 40) + ((ulong)bytes[i++] << 48) + ((ulong)bytes[i++] << 56));
                    UpdateType = (byte)bytes[i++];
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
                bytes[i++] = (byte)Name.length;
                Utils.arraycopy(Name, 0, bytes, i, Name.length); i += Name.length;
                bytes[i++] = (byte)Description.length;
                Utils.arraycopy(Description, 0, bytes, i, Description.length); i += Description.length;
                bytes[i++] = (byte)Title.length;
                Utils.arraycopy(Title, 0, bytes, i, Title.length); i += Title.length;
                Utils.UInt64ToBytes(Powers, bytes, i); i += 8;
                bytes[i++] = UpdateType;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += AgentData.getLength();
                for (int j = 0; j < RoleData.length; j++)
                    length += RoleData[j].length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public RoleDataBlock[] RoleData;

        public GroupRoleUpdatePacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.GroupRoleUpdate;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 378;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            RoleData = null;
        }

        public GroupRoleUpdatePacket(byte[] bytes, int[] i) 
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
                packetEnd[0] = Helpers.ZeroDecode(bytes, packetEnd[0] + 1, zeroBuffer) - 1;
                bytes = zeroBuffer;
            }
            AgentData.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(RoleData == null || RoleData.length != -1) {
                RoleData = new RoleDataBlock[count];
                for(int j = 0; j < count; j++)
                { RoleData[j] = new RoleDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { RoleData[j].FromBytes(bytes, i); }
        }

        public GroupRoleUpdatePacket(Header head, byte[] bytes, int[] i)
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
            if(RoleData == null || RoleData.length != count) {
                RoleData = new RoleDataBlock[count];
                for(int j = 0; j < count; j++)
                { RoleData[j] = new RoleDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { RoleData[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length++;
            for (int j = 0; j < RoleData.length; j++) { length += RoleData[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            bytes[i++] = (byte)RoleData.length;
            for (int j = 0; j < RoleData.length; j++) { RoleData[j].ToBytes(bytes, i); }
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            List<byte[]> packets = new ArrayList<byte[]>();
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

            int RoleDataStart = 0;
            do
            {
                int variableLength = 0;
                int RoleDataCount = 0;

                i = RoleDataStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < RoleData.length) {
                    int blockLength = RoleData[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++RoleDataCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Utils.arraycopy(fixedBytes, 0, packet, 0, length);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)RoleDataCount;
                for (i = RoleDataStart; i < RoleDataStart + RoleDataCount; i++) { RoleData[i].ToBytes(packet, ref length); }
                RoleDataStart += RoleDataCount;

                if (acksLength > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.add(packet);
            } while (
                RoleDataStart < RoleData.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
