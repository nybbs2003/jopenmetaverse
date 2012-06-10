package com.ngt.jopenmetaverse.shared.protocol;


    public final class TeleportFailedPacket extends Packet
    {
        /// <exclude/>
        public final class InfoBlock extends PacketBlock
        {
            public UUID AgentID;
            public byte[] Reason;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 17;
                    if (Reason != null) { length += Reason.length; }
                    return length;
                }
            }

            public InfoBlock() { }
            public InfoBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    AgentID.FromBytes(bytes, i); i += 16;
                    length = bytes[i++];
                    Reason = new byte[length];
                    Buffer.BlockCopy(bytes, i, Reason, 0, length); i += length;
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
                bytes[i++] = (byte)Reason.length;
                Buffer.BlockCopy(Reason, 0, bytes, i, Reason.length); i += Reason.length;
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
                get
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
                    length = bytes[i++];
                    Message = new byte[length];
                    Buffer.BlockCopy(bytes, i, Message, 0, length); i += length;
                    length = bytes[i++];
                    ExtraParams = new byte[length];
                    Buffer.BlockCopy(bytes, i, ExtraParams, 0, length); i += length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i++] = (byte)Message.length;
                Buffer.BlockCopy(Message, 0, bytes, i, Message.length); i += Message.length;
                bytes[i++] = (byte)ExtraParams.length;
                Buffer.BlockCopy(ExtraParams, 0, bytes, i, ExtraParams.length); i += ExtraParams.length;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 11;
                length += Info.length;
                for (int j = 0; j < AlertInfo.length; j++)
                    length += AlertInfo[j].length;
                return length;
            }
        }
        public InfoBlock Info;
        public AlertInfoBlock[] AlertInfo;

        public TeleportFailedPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.TeleportFailed;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 74;
            header.Reliable = true;
            Info = new InfoBlock();
            AlertInfo = null;
        }

        public TeleportFailedPacket(byte[] bytes, int[] i) 
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
            Info.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(AlertInfo == null || AlertInfo.length != -1) {
                AlertInfo = new AlertInfoBlock[count];
                for(int j = 0; j < count; j++)
                { AlertInfo[j] = new AlertInfoBlock(); }
            }
            for (int j = 0; j < count; j++)
            { AlertInfo[j].FromBytes(bytes, i); }
        }

        public TeleportFailedPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            Info.FromBytes(bytes, i);
            int count = (int)bytes[i++];
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
            length += Info.length;
            length++;
            for (int j = 0; j < AlertInfo.length; j++) { length += AlertInfo[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            Info.ToBytes(bytes, i);
            bytes[i++] = (byte)AlertInfo.length;
            for (int j = 0; j < AlertInfo.length; j++) { AlertInfo[j].ToBytes(bytes, i); }
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

            fixedLength += Info.length;
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            Info.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int AlertInfoStart = 0;
            do
            {
                int variableLength = 0;
                int AlertInfoCount = 0;

                i = AlertInfoStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < AlertInfo.length) {
                    int blockLength = AlertInfo[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++AlertInfoCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Buffer.BlockCopy(fixedBytes, 0, packet, 0, length);
                if (packets.Count > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)AlertInfoCount;
                for (i = AlertInfoStart; i < AlertInfoStart + AlertInfoCount; i++) { AlertInfo[i].ToBytes(packet, ref length); }
                AlertInfoStart += AlertInfoCount;

                if (acksLength > 0) {
                    Buffer.BlockCopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.Add(packet);
            } while (
                AlertInfoStart < AlertInfo.length);

            return packets.ToArray();
        }
    }

    /// <exclude/>
