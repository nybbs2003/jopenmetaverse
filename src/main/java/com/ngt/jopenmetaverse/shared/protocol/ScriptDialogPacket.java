package com.ngt.jopenmetaverse.shared.protocol;


    public final class ScriptDialogPacket extends Packet
    {
        /// <exclude/>
        public final class DataBlock extends PacketBlock
        {
            public UUID ObjectID;
            public byte[] FirstName;
            public byte[] LastName;
            public byte[] ObjectName;
            public byte[] Message;
            public int ChatChannel;
            public UUID ImageID;

            @Override
			public int getLength()
            {
                                {
                    int length = 41;
                    if (FirstName != null) { length += FirstName.length; }
                    if (LastName != null) { length += LastName.length; }
                    if (ObjectName != null) { length += ObjectName.length; }
                    if (Message != null) { length += Message.length; }
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
                    ObjectID.FromBytes(bytes, i[0]); i[0] += 16;
                    length = bytes[i[0]++];
                    FirstName = new byte[length];
                    Utils.arraycopy(bytes, i, FirstName, 0, length); i[0] +=  length;
                    length = bytes[i[0]++];
                    LastName = new byte[length];
                    Utils.arraycopy(bytes, i, LastName, 0, length); i[0] +=  length;
                    length = bytes[i[0]++];
                    ObjectName = new byte[length];
                    Utils.arraycopy(bytes, i, ObjectName, 0, length); i[0] +=  length;
                    length = (bytes[i[0]++] + (bytes[i[0]++] << 8));
                    Message = new byte[length];
                    Utils.arraycopy(bytes, i, Message, 0, length); i[0] +=  length;
                    ChatChannel = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    ImageID.FromBytes(bytes, i[0]); i[0] += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ObjectID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)FirstName.length;
                Utils.arraycopy(FirstName, 0, bytes, i, FirstName.length); i[0] +=  FirstName.length;
                bytes[i[0]++] = (byte)LastName.length;
                Utils.arraycopy(LastName, 0, bytes, i, LastName.length); i[0] +=  LastName.length;
                bytes[i[0]++] = (byte)ObjectName.length;
                Utils.arraycopy(ObjectName, 0, bytes, i, ObjectName.length); i[0] +=  ObjectName.length;
                bytes[i[0]++] = (byte)(Message.length % 256);
                bytes[i[0]++] = (byte)((Message.length >> 8) % 256);
                Utils.arraycopy(Message, 0, bytes, i, Message.length); i[0] +=  Message.length;
                Utils.IntToBytes(ChatChannel, bytes, i); i += 4;
                ImageID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        /// <exclude/>
        public final class ButtonsBlock extends PacketBlock
        {
            public byte[] ButtonLabel;

            @Override
			public int getLength()
            {
                                {
                    int length = 1;
                    if (ButtonLabel != null) { length += ButtonLabel.length; }
                    return length;
                }
            }

            public ButtonsBlock() { }
            public ButtonsBlock(byte[] bytes, int[] i)
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
                    ButtonLabel = new byte[length];
                    Utils.arraycopy(bytes, i, ButtonLabel, 0, length); i[0] +=  length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = (byte)ButtonLabel.length;
                Utils.arraycopy(ButtonLabel, 0, bytes, i, ButtonLabel.length); i[0] +=  ButtonLabel.length;
            }

        }

        /// <exclude/>
        public final class OwnerDataBlock extends PacketBlock
        {
            public UUID OwnerID;

            @Override
			public int getLength()
            {
                                {
                    return 16;
                }
            }

            public OwnerDataBlock() { }
            public OwnerDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    OwnerID.FromBytes(bytes, i[0]); i[0] += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                OwnerID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 12;
                length += Data.length;
                for (int j = 0; j < Buttons.length; j++)
                    length += Buttons[j].getLength();
                for (int j = 0; j < OwnerData.length; j++)
                    length += OwnerData[j].getLength();
                return length;
            }
        }
        public DataBlock Data;
        public ButtonsBlock[] Buttons;
        public OwnerDataBlock[] OwnerData;

        public ScriptDialogPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.ScriptDialog;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 190;
            header.Reliable = true;
            header.Zerocoded = true;
            Data = new DataBlock();
            Buttons = null;
            OwnerData = null;
        }

        public ScriptDialogPacket(byte[] bytes, int[] i) 
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
            int count = (int)bytes[i[0]++];
            if(Buttons == null || Buttons.length != -1) {
                Buttons = new ButtonsBlock[count];
                for(int j = 0; j < count; j++)
                { Buttons[j] = new ButtonsBlock(); }
            }
            for (int j = 0; j < count; j++)
            { Buttons[j].FromBytes(bytes, i); }
            count = (int)bytes[i[0]++];
            if(OwnerData == null || OwnerData.length != -1) {
                OwnerData = new OwnerDataBlock[count];
                for(int j = 0; j < count; j++)
                { OwnerData[j] = new OwnerDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { OwnerData[j].FromBytes(bytes, i); }
        }

        public ScriptDialogPacket(Header head, byte[] bytes, int[] i)
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
            int count = (int)bytes[i[0]++];
            if(Buttons == null || Buttons.length != count) {
                Buttons = new ButtonsBlock[count];
                for(int j = 0; j < count; j++)
                { Buttons[j] = new ButtonsBlock(); }
            }
            for (int j = 0; j < count; j++)
            { Buttons[j].FromBytes(bytes, i); }
            count = (int)bytes[i[0]++];
            if(OwnerData == null || OwnerData.length != count) {
                OwnerData = new OwnerDataBlock[count];
                for(int j = 0; j < count; j++)
                { OwnerData[j] = new OwnerDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { OwnerData[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += Data.length;
            length++;
            for (int j = 0; j < Buttons.length; j++) { length += Buttons[j].getLength(); }
            length++;
            for (int j = 0; j < OwnerData.length; j++) { length += OwnerData[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            Data.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)Buttons.length;
            for (int j = 0; j < Buttons.length; j++) { Buttons[j].ToBytes(bytes, i); }
            bytes[i[0]++] = (byte)OwnerData.length;
            for (int j = 0; j < OwnerData.length; j++) { OwnerData[j].ToBytes(bytes, i); }
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

            fixedLength += Data.length;
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            Data.ToBytes(fixedBytes, i);
            fixedLength += 2;

            int ButtonsStart = 0;
            int OwnerDataStart = 0;
            do
            {
                int variableLength = 0;
                int ButtonsCount = 0;
                int OwnerDataCount = 0;

                i = ButtonsStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i < Buttons.length) {
                    int blockLength = Buttons[i].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++ButtonsCount;
                    }
                    else { break; }
                    ++i;
                }

                i = OwnerDataStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i < OwnerData.length) {
                    int blockLength = OwnerData[i].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++OwnerDataCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)ButtonsCount;
                for (i = ButtonsStart; i < ButtonsStart + ButtonsCount; i++) { Buttons[i].ToBytes(packet, length); }
                ButtonsStart += ButtonsCount;

                packet[length[0]++] = (byte)OwnerDataCount;
                for (i = OwnerDataStart; i < OwnerDataStart + OwnerDataCount; i++) { OwnerData[i].ToBytes(packet, length); }
                OwnerDataStart += OwnerDataCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                ButtonsStart < Buttons.length ||
                OwnerDataStart < OwnerData.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
