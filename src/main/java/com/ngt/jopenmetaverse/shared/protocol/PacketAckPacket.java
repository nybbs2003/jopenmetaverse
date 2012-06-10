package com.ngt.jopenmetaverse.shared.protocol;


    public final class PacketAckPacket extends Packet
    {
        /// <exclude/>
        public final class PacketsBlock extends PacketBlock
        {
            public uint ID;

            @Override
			public int getLength()
            {
                get
                {
                    return 4;
                }
            }

            public PacketsBlock() { }
            public PacketsBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ID = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UIntToBytes(ID, bytes, i); i += 4;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 11;
                for (int j = 0; j < Packets.length; j++)
                    length += Packets[j].length;
                return length;
            }
        }
        public PacketsBlock[] Packets;

        public PacketAckPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.PacketAck;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 65531;
            header.Reliable = true;
            Packets = null;
        }

        public PacketAckPacket(byte[] bytes, int[] i) 
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
            if(Packets == null || Packets.length != -1) {
                Packets = new PacketsBlock[count];
                for(int j = 0; j < count; j++)
                { Packets[j] = new PacketsBlock(); }
            }
            for (int j = 0; j < count; j++)
            { Packets[j].FromBytes(bytes, i); }
        }

        public PacketAckPacket(Header head, byte[] bytes, int[] i)
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
            if(Packets == null || Packets.length != count) {
                Packets = new PacketsBlock[count];
                for(int j = 0; j < count; j++)
                { Packets[j] = new PacketsBlock(); }
            }
            for (int j = 0; j < count; j++)
            { Packets[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length++;
            for (int j = 0; j < Packets.length; j++) { length += Packets[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            bytes[i++] = (byte)Packets.length;
            for (int j = 0; j < Packets.length; j++) { Packets[j].ToBytes(bytes, i); }
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

            int PacketsStart = 0;
            do
            {
                int variableLength = 0;
                int PacketsCount = 0;

                i = PacketsStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < Packets.length) {
                    int blockLength = Packets[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++PacketsCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Buffer.BlockCopy(fixedBytes, 0, packet, 0, length);
                if (packets.Count > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)PacketsCount;
                for (i = PacketsStart; i < PacketsStart + PacketsCount; i++) { Packets[i].ToBytes(packet, ref length); }
                PacketsStart += PacketsCount;

                if (acksLength > 0) {
                    Buffer.BlockCopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.Add(packet);
            } while (
                PacketsStart < Packets.length);

            return packets.ToArray();
        }
    }

    /// <exclude/>
