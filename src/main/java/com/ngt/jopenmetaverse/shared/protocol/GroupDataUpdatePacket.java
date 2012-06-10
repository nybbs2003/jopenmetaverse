package com.ngt.jopenmetaverse.shared.protocol;


    public final class GroupDataUpdatePacket extends Packet
    {
        /// <exclude/>
        public final class AgentGroupDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID GroupID;
            public ulong AgentPowers;
            public byte[] GroupTitle;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 41;
                    if (GroupTitle != null) { length += GroupTitle.length; }
                    return length;
                }
            }

            public AgentGroupDataBlock() { }
            public AgentGroupDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    AgentID.FromBytes(bytes, i); i += 16;
                    GroupID.FromBytes(bytes, i); i += 16;
                    AgentPowers = (ulong)((ulong)bytes[i++] + ((ulong)bytes[i++] << 8) + ((ulong)bytes[i++] << 16) + ((ulong)bytes[i++] << 24) + ((ulong)bytes[i++] << 32) + ((ulong)bytes[i++] << 40) + ((ulong)bytes[i++] << 48) + ((ulong)bytes[i++] << 56));
                    length = bytes[i++];
                    GroupTitle = new byte[length];
                    Buffer.BlockCopy(bytes, i, GroupTitle, 0, length); i += length;
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
                Utils.UInt64ToBytes(AgentPowers, bytes, i); i += 8;
                bytes[i++] = (byte)GroupTitle.length;
                Buffer.BlockCopy(GroupTitle, 0, bytes, i, GroupTitle.length); i += GroupTitle.length;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 11;
                for (int j = 0; j < AgentGroupData.length; j++)
                    length += AgentGroupData[j].length;
                return length;
            }
        }
        public AgentGroupDataBlock[] AgentGroupData;

        public GroupDataUpdatePacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.GroupDataUpdate;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 388;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentGroupData = null;
        }

        public GroupDataUpdatePacket(byte[] bytes, int[] i) 
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
            int count = (int)bytes[i++];
            if(AgentGroupData == null || AgentGroupData.length != -1) {
                AgentGroupData = new AgentGroupDataBlock[count];
                for(int j = 0; j < count; j++)
                { AgentGroupData[j] = new AgentGroupDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { AgentGroupData[j].FromBytes(bytes, i); }
        }

        public GroupDataUpdatePacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            int count = (int)bytes[i++];
            if(AgentGroupData == null || AgentGroupData.length != count) {
                AgentGroupData = new AgentGroupDataBlock[count];
                for(int j = 0; j < count; j++)
                { AgentGroupData[j] = new AgentGroupDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { AgentGroupData[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length++;
            for (int j = 0; j < AgentGroupData.length; j++) { length += AgentGroupData[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            bytes[i++] = (byte)AgentGroupData.length;
            for (int j = 0; j < AgentGroupData.length; j++) { AgentGroupData[j].ToBytes(bytes, i); }
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

            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int AgentGroupDataStart = 0;
            do
            {
                int variableLength = 0;
                int AgentGroupDataCount = 0;

                i = AgentGroupDataStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < AgentGroupData.length) {
                    int blockLength = AgentGroupData[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++AgentGroupDataCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Buffer.BlockCopy(fixedBytes, 0, packet, 0, length);
                if (packets.Count > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)AgentGroupDataCount;
                for (i = AgentGroupDataStart; i < AgentGroupDataStart + AgentGroupDataCount; i++) { AgentGroupData[i].ToBytes(packet, ref length); }
                AgentGroupDataStart += AgentGroupDataCount;

                if (acksLength > 0) {
                    Buffer.BlockCopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.Add(packet);
            } while (
                AgentGroupDataStart < AgentGroupData.length);

            return packets.ToArray();
        }
    }

    /// <exclude/>
