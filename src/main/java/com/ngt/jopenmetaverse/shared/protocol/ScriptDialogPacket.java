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
                    ObjectID.FromBytes(bytes, i); i += 16;
                    length = bytes[i++];
                    FirstName = new byte[length];
                    Utils.arraycopy(bytes, i, FirstName, 0, length); i += length;
                    length = bytes[i++];
                    LastName = new byte[length];
                    Utils.arraycopy(bytes, i, LastName, 0, length); i += length;
                    length = bytes[i++];
                    ObjectName = new byte[length];
                    Utils.arraycopy(bytes, i, ObjectName, 0, length); i += length;
                    length = (bytes[i++] + (bytes[i++] << 8));
                    Message = new byte[length];
                    Utils.arraycopy(bytes, i, Message, 0, length); i += length;
                    ChatChannel = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    ImageID.FromBytes(bytes, i); i += 16;
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
                bytes[i++] = (byte)FirstName.length;
                Utils.arraycopy(FirstName, 0, bytes, i, FirstName.length); i += FirstName.length;
                bytes[i++] = (byte)LastName.length;
                Utils.arraycopy(LastName, 0, bytes, i, LastName.length); i += LastName.length;
                bytes[i++] = (byte)ObjectName.length;
                Utils.arraycopy(ObjectName, 0, bytes, i, ObjectName.length); i += ObjectName.length;
                bytes[i++] = (byte)(Message.length % 256);
                bytes[i++] = (byte)((Message.length >> 8) % 256);
                Utils.arraycopy(Message, 0, bytes, i, Message.length); i += Message.length;
                Utils.IntToBytes(ChatChannel, bytes, i); i += 4;
                ImageID.ToBytes(bytes, i); i += 16;
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
                    length = bytes[i++];
                    ButtonLabel = new byte[length];
                    Utils.arraycopy(bytes, i, ButtonLabel, 0, length); i += length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i++] = (byte)ButtonLabel.length;
                Utils.arraycopy(ButtonLabel, 0, bytes, i, ButtonLabel.length); i += ButtonLabel.length;
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
                    OwnerID.FromBytes(bytes, i); i += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                OwnerID.ToBytes(bytes, i); i += 16;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 12;
                length += Data.getLength();
                for (int j = 0; j < Buttons.length; j++)
                    length += Buttons[j].length;
                for (int j = 0; j < OwnerData.length; j++)
                    length += OwnerData[j].length;
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
            int count = (int)bytes[i++];
            if(Buttons == null || Buttons.length != -1) {
                Buttons = new ButtonsBlock[count];
                for(int j = 0; j < count; j++)
                { Buttons[j] = new ButtonsBlock(); }
            }
            for (int j = 0; j < count; j++)
            { Buttons[j].FromBytes(bytes, i); }
            count = (int)bytes[i++];
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
            int count = (int)bytes[i++];
            if(Buttons == null || Buttons.length != count) {
                Buttons = new ButtonsBlock[count];
                for(int j = 0; j < count; j++)
                { Buttons[j] = new ButtonsBlock(); }
            }
            for (int j = 0; j < count; j++)
            { Buttons[j].FromBytes(bytes, i); }
            count = (int)bytes[i++];
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
            length += Data.getLength();
            length++;
            for (int j = 0; j < Buttons.length; j++) { length += Buttons[j].length; }
            length++;
            for (int j = 0; j < OwnerData.length; j++) { length += OwnerData[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            Data.ToBytes(bytes, i);
            bytes[i++] = (byte)Buttons.length;
            for (int j = 0; j < Buttons.length; j++) { Buttons[j].ToBytes(bytes, i); }
            bytes[i++] = (byte)OwnerData.length;
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
            fixedLength += 2;

            int ButtonsStart = 0;
            int OwnerDataStart = 0;
            do
            {
                int variableLength = 0;
                int ButtonsCount = 0;
                int OwnerDataCount = 0;

                i = ButtonsStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < Buttons.length) {
                    int blockLength = Buttons[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++ButtonsCount;
                    }
                    else { break; }
                    ++i;
                }

                i = OwnerDataStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < OwnerData.length) {
                    int blockLength = OwnerData[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++OwnerDataCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Utils.arraycopy(fixedBytes, 0, packet, 0, length);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)ButtonsCount;
                for (i = ButtonsStart; i < ButtonsStart + ButtonsCount; i++) { Buttons[i].ToBytes(packet, ref length); }
                ButtonsStart += ButtonsCount;

                packet[length++] = (byte)OwnerDataCount;
                for (i = OwnerDataStart; i < OwnerDataStart + OwnerDataCount; i++) { OwnerData[i].ToBytes(packet, ref length); }
                OwnerDataStart += OwnerDataCount;

                if (acksLength > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.add(packet);
            } while (
                ButtonsStart < Buttons.length ||
                OwnerDataStart < OwnerData.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
