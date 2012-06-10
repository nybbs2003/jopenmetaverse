package com.ngt.jopenmetaverse.shared.protocol;


    public final class ParcelAccessListUpdatePacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID SessionID;

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
                    SessionID.FromBytes(bytes, i); i += 16;
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
            }

        }

        /// <exclude/>
        public final class DataBlock extends PacketBlock
        {
            public uint Flags;
            public int LocalID;
            public UUID TransactionID;
            public int SequenceID;
            public int Sections;

            @Override
			public int getLength()
            {
                get
                {
                    return 32;
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
                try
                {
                    Flags = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    LocalID = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    TransactionID.FromBytes(bytes, i); i += 16;
                    SequenceID = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Sections = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UIntToBytes(Flags, bytes, i); i += 4;
                Utils.IntToBytes(LocalID, bytes, i); i += 4;
                TransactionID.ToBytes(bytes, i); i += 16;
                Utils.IntToBytes(SequenceID, bytes, i); i += 4;
                Utils.IntToBytes(Sections, bytes, i); i += 4;
            }

        }

        /// <exclude/>
        public final class ListBlock extends PacketBlock
        {
            public UUID ID;
            public int Time;
            public uint Flags;

            @Override
			public int getLength()
            {
                get
                {
                    return 24;
                }
            }

            public ListBlock() { }
            public ListBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ID.FromBytes(bytes, i); i += 16;
                    Time = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Flags = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ID.ToBytes(bytes, i); i += 16;
                Utils.IntToBytes(Time, bytes, i); i += 4;
                Utils.UIntToBytes(Flags, bytes, i); i += 4;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 11;
                length += AgentData.getLength();
                length += Data.getLength();
                for (int j = 0; j < List.length; j++)
                    length += List[j].length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public DataBlock Data;
        public ListBlock[] List;

        public ParcelAccessListUpdatePacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.ParcelAccessListUpdate;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 217;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            Data = new DataBlock();
            List = null;
        }

        public ParcelAccessListUpdatePacket(byte[] bytes, int[] i) 
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
            Data.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(List == null || List.length != -1) {
                List = new ListBlock[count];
                for(int j = 0; j < count; j++)
                { List[j] = new ListBlock(); }
            }
            for (int j = 0; j < count; j++)
            { List[j].FromBytes(bytes, i); }
        }

        public ParcelAccessListUpdatePacket(Header head, byte[] bytes, int[] i)
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
            Data.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(List == null || List.length != count) {
                List = new ListBlock[count];
                for(int j = 0; j < count; j++)
                { List[j] = new ListBlock(); }
            }
            for (int j = 0; j < count; j++)
            { List[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += Data.getLength();
            length++;
            for (int j = 0; j < List.length; j++) { length += List[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            Data.ToBytes(bytes, i);
            bytes[i++] = (byte)List.length;
            for (int j = 0; j < List.length; j++) { List[j].ToBytes(bytes, i); }
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
            fixedLength += Data.getLength();
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AgentData.ToBytes(fixedBytes, i);
            Data.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int ListStart = 0;
            do
            {
                int variableLength = 0;
                int ListCount = 0;

                i = ListStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < List.length) {
                    int blockLength = List[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++ListCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Buffer.BlockCopy(fixedBytes, 0, packet, 0, length);
                if (packets.Count > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)ListCount;
                for (i = ListStart; i < ListStart + ListCount; i++) { List[i].ToBytes(packet, ref length); }
                ListStart += ListCount;

                if (acksLength > 0) {
                    Buffer.BlockCopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.Add(packet);
            } while (
                ListStart < List.length);

            return packets.ToArray();
        }
    }

    /// <exclude/>
