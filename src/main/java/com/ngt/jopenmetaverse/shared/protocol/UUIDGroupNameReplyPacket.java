package com.ngt.jopenmetaverse.shared.protocol;


    public final class UUIDGroupNameReplyPacket extends Packet
    {
        /// <exclude/>
        public final class UUIDNameBlockBlock extends PacketBlock
        {
            public UUID ID;
            public byte[] GroupName;

            @Override
			public int getLength()
            {
                                {
                    int length = 17;
                    if (GroupName != null) { length += GroupName.length; }
                    return length;
                }
            }

            public UUIDNameBlockBlock() { }
            public UUIDNameBlockBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    ID.FromBytes(bytes, i[0]); i[0] += 16;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
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
                ID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)GroupName.length;
                Utils.arraycopy(GroupName, 0, bytes, i[0], GroupName.length); i[0] +=  GroupName.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                for (int j = 0; j < UUIDNameBlock.length; j++)
                    length += UUIDNameBlock[j].getLength();
                return length;
            }
        }
        public UUIDNameBlockBlock[] UUIDNameBlock;

        public UUIDGroupNameReplyPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.UUIDGroupNameReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 238;
            header.Reliable = true;
            UUIDNameBlock = null;
        }

        public UUIDGroupNameReplyPacket(byte[] bytes, int[] i) 
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
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(UUIDNameBlock == null || UUIDNameBlock.length != -1) {
                UUIDNameBlock = new UUIDNameBlockBlock[count];
                for(int j = 0; j < count; j++)
                { UUIDNameBlock[j] = new UUIDNameBlockBlock(); }
            }
            for (int j = 0; j < count; j++)
            { UUIDNameBlock[j].FromBytes(bytes, i); }
        }

        public UUIDGroupNameReplyPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(UUIDNameBlock == null || UUIDNameBlock.length != count) {
                UUIDNameBlock = new UUIDNameBlockBlock[count];
                for(int j = 0; j < count; j++)
                { UUIDNameBlock[j] = new UUIDNameBlockBlock(); }
            }
            for (int j = 0; j < count; j++)
            { UUIDNameBlock[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length++;
            for (int j = 0; j < UUIDNameBlock.length; j++) { length += UUIDNameBlock[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)UUIDNameBlock.length;
            for (int j = 0; j < UUIDNameBlock.length; j++) { UUIDNameBlock[j].ToBytes(bytes, i); }
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            List<byte[]> packets = new ArrayList<byte[]>();
            int[] i = new int[]{0};
            int fixedLength = 10;

            byte[] ackBytes = null;
            int[] acksLength = new int[]{0};
            if (header.AckList != null && header.AckList.length > 0) {
                header.AppendedAcks = true;
                ackBytes = new byte[header.AckList.length * 4 + 1];
                header.AcksToBytes(ackBytes, acksLength);
            }

            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int UUIDNameBlockStart = 0;
            do
            {
                int variableLength = 0;
                int UUIDNameBlockCount = 0;

              i[0] =UUIDNameBlockStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < UUIDNameBlock.length) {
                    int blockLength = UUIDNameBlock[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++UUIDNameBlockCount;
                    }
                    else { break; }
                    i[0]++;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)UUIDNameBlockCount;
                for (i[0] = UUIDNameBlockStart; i[0] < UUIDNameBlockStart + UUIDNameBlockCount; i[0]++) { UUIDNameBlock[i[0]].ToBytes(packet, length); }
                UUIDNameBlockStart += UUIDNameBlockCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                UUIDNameBlockStart < UUIDNameBlock.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
