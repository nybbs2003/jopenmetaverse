package com.ngt.jopenmetaverse.shared.protocol;


    public final class ParcelAccessListReplyPacket extends Packet
    {
        /// <exclude/>
        public final class DataBlock extends PacketBlock
        {
            public UUID AgentID;
            public int SequenceID;
            public uint Flags;
            public int LocalID;

            @Override
			public int getLength()
            {
                                {
                    return 28;
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
                    AgentID.FromBytes(bytes, i); i += 16;
                    SequenceID = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Flags = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    LocalID = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
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
                Utils.IntToBytes(SequenceID, bytes, i); i += 4;
                Utils.UIntToBytes(Flags, bytes, i); i += 4;
                Utils.IntToBytes(LocalID, bytes, i); i += 4;
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
                        {
                int length = 11;
                length += Data.getLength();
                for (int j = 0; j < List.length; j++)
                    length += List[j].length;
                return length;
            }
        }
        public DataBlock Data;
        public ListBlock[] List;

        public ParcelAccessListReplyPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.ParcelAccessListReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 216;
            header.Reliable = true;
            header.Zerocoded = true;
            Data = new DataBlock();
            List = null;
        }

        public ParcelAccessListReplyPacket(byte[] bytes, int[] i) 
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

        public ParcelAccessListReplyPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
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
            length += Data.getLength();
            length++;
            for (int j = 0; j < List.length; j++) { length += List[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            Data.ToBytes(bytes, i);
            bytes[i++] = (byte)List.length;
            for (int j = 0; j < List.length; j++) { List[j].ToBytes(bytes, i); }
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

            fixedLength += Data.getLength();
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
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
                Utils.arraycopy(fixedBytes, 0, packet, 0, length);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)ListCount;
                for (i = ListStart; i < ListStart + ListCount; i++) { List[i].ToBytes(packet, ref length); }
                ListStart += ListCount;

                if (acksLength > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.add(packet);
            } while (
                ListStart < List.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
