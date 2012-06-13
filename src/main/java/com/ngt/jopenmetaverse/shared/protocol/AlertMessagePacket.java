package com.ngt.jopenmetaverse.shared.protocol;


    public final class AlertMessagePacket extends Packet
    {
        /// <exclude/>
        public final class AlertDataBlock extends PacketBlock
        {
            public byte[] Message;

            @Override
			public int getLength()
            {
                                {
                    int length = 1;
                    if (Message != null) { length += Message.length; }
                    return length;
                }
            }

            public AlertDataBlock() { }
            public AlertDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    length = bytes[i[0]++];
                    Message = new byte[length];
                    Utils.arraycopy(bytes, i, Message, 0, length); i[0] +=  length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = (byte)Message.length;
                Utils.arraycopy(Message, 0, bytes, i, Message.length); i[0] +=  Message.length;
            }

        }

        /// <exclude/>
        public final class AlertInfoBlock extends PacketBlock
        {
            public byte[] Message;
            public byte[] ExtraParams;

            @Override
			public int getLength()
            {
                                {
                    int length = 2;
                    if (Message != null) { length += Message.length; }
                    if (ExtraParams != null) { length += ExtraParams.length; }
                    return length;
                }
            }

            public AlertInfoBlock() { }
            public AlertInfoBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    length = bytes[i[0]++];
                    Message = new byte[length];
                    Utils.arraycopy(bytes, i, Message, 0, length); i[0] +=  length;
                    length = bytes[i[0]++];
                    ExtraParams = new byte[length];
                    Utils.arraycopy(bytes, i, ExtraParams, 0, length); i[0] +=  length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = (byte)Message.length;
                Utils.arraycopy(Message, 0, bytes, i, Message.length); i[0] +=  Message.length;
                bytes[i[0]++] = (byte)ExtraParams.length;
                Utils.arraycopy(ExtraParams, 0, bytes, i, ExtraParams.length); i[0] +=  ExtraParams.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += AlertData.getLength();
                for (int j = 0; j < AlertInfo.length; j++)
                    length += AlertInfo[j].getLength();
                return length;
            }
        }
        public AlertDataBlock AlertData;
        public AlertInfoBlock[] AlertInfo;

        public AlertMessagePacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.AlertMessage;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 134;
            header.Reliable = true;
            AlertData = new AlertDataBlock();
            AlertInfo = null;
        }

        public AlertMessagePacket(byte[] bytes, int[] i) 
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
            AlertData.FromBytes(bytes, i);
            int count = (int)bytes[i[0]++];
            if(AlertInfo == null || AlertInfo.length != -1) {
                AlertInfo = new AlertInfoBlock[count];
                for(int j = 0; j < count; j++)
                { AlertInfo[j] = new AlertInfoBlock(); }
            }
            for (int j = 0; j < count; j++)
            { AlertInfo[j].FromBytes(bytes, i); }
        }

        public AlertMessagePacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            AlertData.FromBytes(bytes, i);
            int count = (int)bytes[i[0]++];
            if(AlertInfo == null || AlertInfo.length != count) {
                AlertInfo = new AlertInfoBlock[count];
                for(int j = 0; j < count; j++)
                { AlertInfo[j] = new AlertInfoBlock(); }
            }
            for (int j = 0; j < count; j++)
            { AlertInfo[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AlertData.getLength();
            length++;
            for (int j = 0; j < AlertInfo.length; j++) { length += AlertInfo[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AlertData.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)AlertInfo.length;
            for (int j = 0; j < AlertInfo.length; j++) { AlertInfo[j].ToBytes(bytes, i); }
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
            int[] acksLength = new int[]{0};
            if (header.AckList != null && header.AckList.length > 0) {
                header.AppendedAcks = true;
                ackBytes = new byte[header.AckList.length * 4 + 1];
                header.AcksToBytes(ackBytes, acksLength);
            }

            fixedLength += AlertData.length;
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AlertData.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int AlertInfoStart = 0;
            do
            {
                int variableLength = 0;
                int AlertInfoCount = 0;

                i = AlertInfoStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i < AlertInfo.length) {
                    int blockLength = AlertInfo[i].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++AlertInfoCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)AlertInfoCount;
                for (i = AlertInfoStart; i < AlertInfoStart + AlertInfoCount; i++) { AlertInfo[i].ToBytes(packet, length); }
                AlertInfoStart += AlertInfoCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                AlertInfoStart < AlertInfo.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
