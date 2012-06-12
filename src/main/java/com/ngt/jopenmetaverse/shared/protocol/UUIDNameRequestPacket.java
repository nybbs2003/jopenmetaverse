package com.ngt.jopenmetaverse.shared.protocol;


    public final class UUIDNameRequestPacket extends Packet
    {
        /// <exclude/>
        public final class UUIDNameBlockBlock extends PacketBlock
        {
            public UUID ID;

            @Override
			public int getLength()
            {
                                {
                    return 16;
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
                try
                {
                    ID.FromBytes(bytes, i); i += 16;
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
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                for (int j = 0; j < UUIDNameBlock.length; j++)
                    length += UUIDNameBlock[j].length;
                return length;
            }
        }
        public UUIDNameBlockBlock[] UUIDNameBlock;

        public UUIDNameRequestPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.UUIDNameRequest;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 235;
            header.Reliable = true;
            UUIDNameBlock = null;
        }

        public UUIDNameRequestPacket(byte[] bytes, int[] i) 
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
            int count = (int)bytes[i++];
            if(UUIDNameBlock == null || UUIDNameBlock.length != -1) {
                UUIDNameBlock = new UUIDNameBlockBlock[count];
                for(int j = 0; j < count; j++)
                { UUIDNameBlock[j] = new UUIDNameBlockBlock(); }
            }
            for (int j = 0; j < count; j++)
            { UUIDNameBlock[j].FromBytes(bytes, i); }
        }

        public UUIDNameRequestPacket(Header head, byte[] bytes, int[] i)
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
            for (int j = 0; j < UUIDNameBlock.length; j++) { length += UUIDNameBlock[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            bytes[i++] = (byte)UUIDNameBlock.length;
            for (int j = 0; j < UUIDNameBlock.length; j++) { UUIDNameBlock[j].ToBytes(bytes, i); }
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

            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int UUIDNameBlockStart = 0;
            do
            {
                int variableLength = 0;
                int UUIDNameBlockCount = 0;

                i = UUIDNameBlockStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < UUIDNameBlock.length) {
                    int blockLength = UUIDNameBlock[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++UUIDNameBlockCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Utils.arraycopy(fixedBytes, 0, packet, 0, length);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)UUIDNameBlockCount;
                for (i = UUIDNameBlockStart; i < UUIDNameBlockStart + UUIDNameBlockCount; i++) { UUIDNameBlock[i].ToBytes(packet, ref length); }
                UUIDNameBlockStart += UUIDNameBlockCount;

                if (acksLength > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.add(packet);
            } while (
                UUIDNameBlockStart < UUIDNameBlock.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
