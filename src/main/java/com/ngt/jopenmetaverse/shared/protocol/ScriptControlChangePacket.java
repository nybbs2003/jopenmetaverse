package com.ngt.jopenmetaverse.shared.protocol;


    public final class ScriptControlChangePacket extends Packet
    {
        /// <exclude/>
        public final class DataBlock extends PacketBlock
        {
            public bool TakeControls;
            public uint Controls;
            public bool PassToAgent;

            @Override
			public int getLength()
            {
                                {
                    return 6;
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
                    TakeControls = (bytes[i[0]++] != 0) ? (bool)true : (bool)false;
                    Controls = (uint)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    PassToAgent = (bytes[i[0]++] != 0) ? (bool)true : (bool)false;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = (byte)((TakeControls) ? 1 : 0);
                Utils.UIntToBytes(Controls, bytes, i); i += 4;
                bytes[i[0]++] = (byte)((PassToAgent) ? 1 : 0);
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                for (int j = 0; j < Data.length; j++)
                    length += Data[j].getLength();
                return length;
            }
        }
        public DataBlock[] Data;

        public ScriptControlChangePacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.ScriptControlChange;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 189;
            header.Reliable = true;
            Data = null;
        }

        public ScriptControlChangePacket(byte[] bytes, int[] i) 
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
            int count = (int)bytes[i[0]++];
            if(Data == null || Data.length != -1) {
                Data = new DataBlock[count];
                for(int j = 0; j < count; j++)
                { Data[j] = new DataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { Data[j].FromBytes(bytes, i); }
        }

        public ScriptControlChangePacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            int count = (int)bytes[i[0]++];
            if(Data == null || Data.length != count) {
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
            length++;
            for (int j = 0; j < Data.length; j++) { length += Data[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)Data.length;
            for (int j = 0; j < Data.length; j++) { Data[j].ToBytes(bytes, i); }
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

            int DataStart = 0;
            do
            {
                int variableLength = 0;
                int DataCount = 0;

              i[0] =DataStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < Data.length) {
                    int blockLength = Data[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++DataCount;
                    }
                    else { break; }
                    i[0]++;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)DataCount;
                for (i[0] = DataStart; i[0] < DataStart + DataCount; i[0]++) { Data[i[0]].ToBytes(packet, length); }
                DataStart += DataCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                DataStart < Data.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
