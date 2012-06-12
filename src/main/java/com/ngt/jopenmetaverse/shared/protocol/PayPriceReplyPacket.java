package com.ngt.jopenmetaverse.shared.protocol;


    public final class PayPriceReplyPacket extends Packet
    {
        /// <exclude/>
        public final class ObjectDataBlock extends PacketBlock
        {
            public UUID ObjectID;
            public int DefaultPayPrice;

            @Override
			public int getLength()
            {
                                {
                    return 20;
                }
            }

            public ObjectDataBlock() { }
            public ObjectDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ObjectID.FromBytes(bytes, i); i += 16;
                    DefaultPayPrice = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ObjectID.ToBytes(bytes, i); i += 16;
                Utils.IntToBytes(DefaultPayPrice, bytes, i); i += 4;
            }

        }

        /// <exclude/>
        public final class ButtonDataBlock extends PacketBlock
        {
            public int PayButton;

            @Override
			public int getLength()
            {
                                {
                    return 4;
                }
            }

            public ButtonDataBlock() { }
            public ButtonDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    PayButton = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.IntToBytes(PayButton, bytes, i); i += 4;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += ObjectData.length;
                for (int j = 0; j < ButtonData.length; j++)
                    length += ButtonData[j].length;
                return length;
            }
        }
        public ObjectDataBlock ObjectData;
        public ButtonDataBlock[] ButtonData;

        public PayPriceReplyPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.PayPriceReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 162;
            header.Reliable = true;
            ObjectData = new ObjectDataBlock();
            ButtonData = null;
        }

        public PayPriceReplyPacket(byte[] bytes, int[] i) 
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
            ObjectData.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(ButtonData == null || ButtonData.length != -1) {
                ButtonData = new ButtonDataBlock[count];
                for(int j = 0; j < count; j++)
                { ButtonData[j] = new ButtonDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ButtonData[j].FromBytes(bytes, i); }
        }

        public PayPriceReplyPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            ObjectData.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(ButtonData == null || ButtonData.length != count) {
                ButtonData = new ButtonDataBlock[count];
                for(int j = 0; j < count; j++)
                { ButtonData[j] = new ButtonDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ButtonData[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += ObjectData.length;
            length++;
            for (int j = 0; j < ButtonData.length; j++) { length += ButtonData[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            ObjectData.ToBytes(bytes, i);
            bytes[i++] = (byte)ButtonData.length;
            for (int j = 0; j < ButtonData.length; j++) { ButtonData[j].ToBytes(bytes, i); }
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

            fixedLength += ObjectData.length;
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            ObjectData.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int ButtonDataStart = 0;
            do
            {
                int variableLength = 0;
                int ButtonDataCount = 0;

                i = ButtonDataStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < ButtonData.length) {
                    int blockLength = ButtonData[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++ButtonDataCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Utils.arraycopy(fixedBytes, 0, packet, 0, length);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)ButtonDataCount;
                for (i = ButtonDataStart; i < ButtonDataStart + ButtonDataCount; i++) { ButtonData[i].ToBytes(packet, ref length); }
                ButtonDataStart += ButtonDataCount;

                if (acksLength > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.add(packet);
            } while (
                ButtonDataStart < ButtonData.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
