package com.ngt.jopenmetaverse.shared.protocol;


    public final class GroupNoticesListReplyPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID GroupID;

            @Override
			public int getLength()
            {
                get
                {
                    return 32;
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
            }

        }

        /// <exclude/>
        public final class DataBlock extends PacketBlock
        {
            public UUID NoticeID;
            public uint Timestamp;
            public byte[] FromName;
            public byte[] Subject;
            public bool HasAttachment;
            public byte AssetType;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 26;
                    if (FromName != null) { length += FromName.length; }
                    if (Subject != null) { length += Subject.length; }
                    return length;
                }
            }

            public DataBlock() { }
            public DataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    NoticeID.FromBytes(bytes, i); i += 16;
                    Timestamp = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    length = (bytes[i++] + (bytes[i++] << 8));
                    FromName = new byte[length];
                    Buffer.BlockCopy(bytes, i, FromName, 0, length); i += length;
                    length = (bytes[i++] + (bytes[i++] << 8));
                    Subject = new byte[length];
                    Buffer.BlockCopy(bytes, i, Subject, 0, length); i += length;
                    HasAttachment = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    AssetType = (byte)bytes[i++];
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                NoticeID.ToBytes(bytes, i); i += 16;
                Utils.UIntToBytes(Timestamp, bytes, i); i += 4;
                bytes[i++] = (byte)(FromName.length % 256);
                bytes[i++] = (byte)((FromName.length >> 8) % 256);
                Buffer.BlockCopy(FromName, 0, bytes, i, FromName.length); i += FromName.length;
                bytes[i++] = (byte)(Subject.length % 256);
                bytes[i++] = (byte)((Subject.length >> 8) % 256);
                Buffer.BlockCopy(Subject, 0, bytes, i, Subject.length); i += Subject.length;
                bytes[i++] = (byte)((HasAttachment) ? 1 : 0);
                bytes[i++] = AssetType;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 11;
                length += AgentData.getLength();
                for (int j = 0; j < Data.getLength(); j++)
                    length += Data[j].length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public DataBlock[] Data;

        public GroupNoticesListReplyPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.GroupNoticesListReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 59;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            Data = null;
        }

        public GroupNoticesListReplyPacket(byte[] bytes, int[] i) 
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
            if(Data == null || Data.getLength() != -1) {
                Data = new DataBlock[count];
                for(int j = 0; j < count; j++)
                { Data[j] = new DataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { Data[j].FromBytes(bytes, i); }
        }

        public GroupNoticesListReplyPacket(Header head, byte[] bytes, int[] i)
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
            if(Data == null || Data.getLength() != count) {
                Data = new DataBlock[count];
                for(int j = 0; j < count; j++)
                { Data[j] = new DataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { Data[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length++;
            for (int j = 0; j < Data.getLength(); j++) { length += Data[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            bytes[i++] = (byte)Data.length;
            for (int j = 0; j < Data.getLength(); j++) { Data[j].ToBytes(bytes, i); }
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

            int DataStart = 0;
            do
            {
                int variableLength = 0;
                int DataCount = 0;

                i = DataStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < Data.getLength()) {
                    int blockLength = Data[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++DataCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Buffer.BlockCopy(fixedBytes, 0, packet, 0, length);
                if (packets.Count > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)DataCount;
                for (i = DataStart; i < DataStart + DataCount; i++) { Data[i].ToBytes(packet, ref length); }
                DataStart += DataCount;

                if (acksLength > 0) {
                    Buffer.BlockCopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.Add(packet);
            } while (
                DataStart < Data.getLength());

            return packets.ToArray();
        }
    }

    /// <exclude/>