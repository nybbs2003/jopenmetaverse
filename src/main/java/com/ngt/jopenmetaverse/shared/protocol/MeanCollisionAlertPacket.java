package com.ngt.jopenmetaverse.shared.protocol;


    public final class MeanCollisionAlertPacket extends Packet
    {
        /// <exclude/>
        public final class MeanCollisionBlock extends PacketBlock
        {
            public UUID Victim;
            public UUID Perp;
            public uint Time;
            public float Mag;
            public byte Type;

            @Override
			public int getLength()
            {
                get
                {
                    return 41;
                }
            }

            public MeanCollisionBlock() { }
            public MeanCollisionBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    Victim.FromBytes(bytes, i); i += 16;
                    Perp.FromBytes(bytes, i); i += 16;
                    Time = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Mag = Utils.BytesToFloat(bytes, i); i += 4;
                    Type = (byte)bytes[i++];
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Victim.ToBytes(bytes, i); i += 16;
                Perp.ToBytes(bytes, i); i += 16;
                Utils.UIntToBytes(Time, bytes, i); i += 4;
                Utils.FloatToBytes(Mag, bytes, i); i += 4;
                bytes[i++] = Type;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 11;
                for (int j = 0; j < MeanCollision.length; j++)
                    length += MeanCollision[j].length;
                return length;
            }
        }
        public MeanCollisionBlock[] MeanCollision;

        public MeanCollisionAlertPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.MeanCollisionAlert;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 136;
            header.Reliable = true;
            header.Zerocoded = true;
            MeanCollision = null;
        }

        public MeanCollisionAlertPacket(byte[] bytes, int[] i) 
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
            if(MeanCollision == null || MeanCollision.length != -1) {
                MeanCollision = new MeanCollisionBlock[count];
                for(int j = 0; j < count; j++)
                { MeanCollision[j] = new MeanCollisionBlock(); }
            }
            for (int j = 0; j < count; j++)
            { MeanCollision[j].FromBytes(bytes, i); }
        }

        public MeanCollisionAlertPacket(Header head, byte[] bytes, int[] i)
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
            if(MeanCollision == null || MeanCollision.length != count) {
                MeanCollision = new MeanCollisionBlock[count];
                for(int j = 0; j < count; j++)
                { MeanCollision[j] = new MeanCollisionBlock(); }
            }
            for (int j = 0; j < count; j++)
            { MeanCollision[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length++;
            for (int j = 0; j < MeanCollision.length; j++) { length += MeanCollision[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            bytes[i++] = (byte)MeanCollision.length;
            for (int j = 0; j < MeanCollision.length; j++) { MeanCollision[j].ToBytes(bytes, i); }
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

            int MeanCollisionStart = 0;
            do
            {
                int variableLength = 0;
                int MeanCollisionCount = 0;

                i = MeanCollisionStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < MeanCollision.length) {
                    int blockLength = MeanCollision[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++MeanCollisionCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Buffer.BlockCopy(fixedBytes, 0, packet, 0, length);
                if (packets.Count > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)MeanCollisionCount;
                for (i = MeanCollisionStart; i < MeanCollisionStart + MeanCollisionCount; i++) { MeanCollision[i].ToBytes(packet, ref length); }
                MeanCollisionStart += MeanCollisionCount;

                if (acksLength > 0) {
                    Buffer.BlockCopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.Add(packet);
            } while (
                MeanCollisionStart < MeanCollision.length);

            return packets.ToArray();
        }
    }

    /// <exclude/>
