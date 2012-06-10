package com.ngt.jopenmetaverse.shared.protocol;


    public final class GroupRoleDataReplyPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;

            @Override
			public int getLength()
            {
                get
                {
                    return 16;
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
            }

        }

        /// <exclude/>
        public final class GroupDataBlock extends PacketBlock
        {
            public UUID GroupID;
            public UUID RequestID;
            public int RoleCount;

            @Override
			public int getLength()
            {
                get
                {
                    return 36;
                }
            }

            public GroupDataBlock() { }
            public GroupDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    GroupID.FromBytes(bytes, i); i += 16;
                    RequestID.FromBytes(bytes, i); i += 16;
                    RoleCount = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                GroupID.ToBytes(bytes, i); i += 16;
                RequestID.ToBytes(bytes, i); i += 16;
                Utils.IntToBytes(RoleCount, bytes, i); i += 4;
            }

        }

        /// <exclude/>
        public final class RoleDataBlock extends PacketBlock
        {
            public UUID RoleID;
            public byte[] Name;
            public byte[] Title;
            public byte[] Description;
            public ulong Powers;
            public uint Members;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 31;
                    if (Name != null) { length += Name.length; }
                    if (Title != null) { length += Title.length; }
                    if (Description != null) { length += Description.length; }
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
                    Buffer.BlockCopy(bytes, i, Name, 0, length); i += length;
                    length = bytes[i++];
                    Title = new byte[length];
                    Buffer.BlockCopy(bytes, i, Title, 0, length); i += length;
                    length = bytes[i++];
                    Description = new byte[length];
                    Buffer.BlockCopy(bytes, i, Description, 0, length); i += length;
                    Powers = (ulong)((ulong)bytes[i++] + ((ulong)bytes[i++] << 8) + ((ulong)bytes[i++] << 16) + ((ulong)bytes[i++] << 24) + ((ulong)bytes[i++] << 32) + ((ulong)bytes[i++] << 40) + ((ulong)bytes[i++] << 48) + ((ulong)bytes[i++] << 56));
                    Members = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
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
                Buffer.BlockCopy(Name, 0, bytes, i, Name.length); i += Name.length;
                bytes[i++] = (byte)Title.length;
                Buffer.BlockCopy(Title, 0, bytes, i, Title.length); i += Title.length;
                bytes[i++] = (byte)Description.length;
                Buffer.BlockCopy(Description, 0, bytes, i, Description.length); i += Description.length;
                Utils.UInt64ToBytes(Powers, bytes, i); i += 8;
                Utils.UIntToBytes(Members, bytes, i); i += 4;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 11;
                length += AgentData.getLength();
                length += GroupData.length;
                for (int j = 0; j < RoleData.length; j++)
                    length += RoleData[j].length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public GroupDataBlock GroupData;
        public RoleDataBlock[] RoleData;

        public GroupRoleDataReplyPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.GroupRoleDataReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 372;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            GroupData = new GroupDataBlock();
            RoleData = null;
        }

        public GroupRoleDataReplyPacket(byte[] bytes, int[] i) 
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
            GroupData.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(RoleData == null || RoleData.length != -1) {
                RoleData = new RoleDataBlock[count];
                for(int j = 0; j < count; j++)
                { RoleData[j] = new RoleDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { RoleData[j].FromBytes(bytes, i); }
        }

        public GroupRoleDataReplyPacket(Header head, byte[] bytes, int[] i)
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
            GroupData.FromBytes(bytes, i);
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
            length += GroupData.length;
            length++;
            for (int j = 0; j < RoleData.length; j++) { length += RoleData[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            GroupData.ToBytes(bytes, i);
            bytes[i++] = (byte)RoleData.length;
            for (int j = 0; j < RoleData.length; j++) { RoleData[j].ToBytes(bytes, i); }
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
            fixedLength += GroupData.length;
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AgentData.ToBytes(fixedBytes, i);
            GroupData.ToBytes(fixedBytes, i);
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
                Buffer.BlockCopy(fixedBytes, 0, packet, 0, length);
                if (packets.Count > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)RoleDataCount;
                for (i = RoleDataStart; i < RoleDataStart + RoleDataCount; i++) { RoleData[i].ToBytes(packet, ref length); }
                RoleDataStart += RoleDataCount;

                if (acksLength > 0) {
                    Buffer.BlockCopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.Add(packet);
            } while (
                RoleDataStart < RoleData.length);

            return packets.ToArray();
        }
    }

    /// <exclude/>
