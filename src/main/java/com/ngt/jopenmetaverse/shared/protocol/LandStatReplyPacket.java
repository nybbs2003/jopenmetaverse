package com.ngt.jopenmetaverse.shared.protocol;


    public final class LandStatReplyPacket extends Packet
    {
        /// <exclude/>
        public final class RequestDataBlock extends PacketBlock
        {
            public uint ReportType;
            public uint RequestFlags;
            public uint TotalObjectCount;

            @Override
			public int getLength()
            {
                                {
                    return 12;
                }
            }

            public RequestDataBlock() { }
            public RequestDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ReportType = (uint)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    RequestFlags = (uint)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    TotalObjectCount = (uint)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UIntToBytes(ReportType, bytes, i); i += 4;
                Utils.UIntToBytes(RequestFlags, bytes, i); i += 4;
                Utils.UIntToBytes(TotalObjectCount, bytes, i); i += 4;
            }

        }

        /// <exclude/>
        public final class ReportDataBlock extends PacketBlock
        {
            public uint TaskLocalID;
            public UUID TaskID;
            public float LocationX;
            public float LocationY;
            public float LocationZ;
            public float Score;
            public byte[] TaskName;
            public byte[] OwnerName;

            @Override
			public int getLength()
            {
                                {
                    int length = 38;
                    if (TaskName != null) { length += TaskName.length; }
                    if (OwnerName != null) { length += OwnerName.length; }
                    return length;
                }
            }

            public ReportDataBlock() { }
            public ReportDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    TaskLocalID = (uint)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    TaskID.FromBytes(bytes, i[0]); i[0] += 16;
                    LocationX = Utils.BytesToFloat(bytes, i); i += 4;
                    LocationY = Utils.BytesToFloat(bytes, i); i += 4;
                    LocationZ = Utils.BytesToFloat(bytes, i); i += 4;
                    Score = Utils.BytesToFloat(bytes, i); i += 4;
                    length = bytes[i[0]++];
                    TaskName = new byte[length];
                    Utils.arraycopy(bytes, i, TaskName, 0, length); i[0] +=  length;
                    length = bytes[i[0]++];
                    OwnerName = new byte[length];
                    Utils.arraycopy(bytes, i, OwnerName, 0, length); i[0] +=  length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UIntToBytes(TaskLocalID, bytes, i); i += 4;
                TaskID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.FloatToBytes(LocationX, bytes, i); i += 4;
                Utils.FloatToBytes(LocationY, bytes, i); i += 4;
                Utils.FloatToBytes(LocationZ, bytes, i); i += 4;
                Utils.FloatToBytes(Score, bytes, i); i += 4;
                bytes[i[0]++] = (byte)TaskName.length;
                Utils.arraycopy(TaskName, 0, bytes, i, TaskName.length); i[0] +=  TaskName.length;
                bytes[i[0]++] = (byte)OwnerName.length;
                Utils.arraycopy(OwnerName, 0, bytes, i, OwnerName.length); i[0] +=  OwnerName.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += RequestData.length;
                for (int j = 0; j < ReportData.length; j++)
                    length += ReportData[j].getLength();
                return length;
            }
        }
        public RequestDataBlock RequestData;
        public ReportDataBlock[] ReportData;

        public LandStatReplyPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.LandStatReply;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 422;
            header.Reliable = true;
            RequestData = new RequestDataBlock();
            ReportData = null;
        }

        public LandStatReplyPacket(byte[] bytes, int[] i) 
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
            RequestData.FromBytes(bytes, i);
            int count = (int)bytes[i[0]++];
            if(ReportData == null || ReportData.length != -1) {
                ReportData = new ReportDataBlock[count];
                for(int j = 0; j < count; j++)
                { ReportData[j] = new ReportDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ReportData[j].FromBytes(bytes, i); }
        }

        public LandStatReplyPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            RequestData.FromBytes(bytes, i);
            int count = (int)bytes[i[0]++];
            if(ReportData == null || ReportData.length != count) {
                ReportData = new ReportDataBlock[count];
                for(int j = 0; j < count; j++)
                { ReportData[j] = new ReportDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ReportData[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += RequestData.length;
            length++;
            for (int j = 0; j < ReportData.length; j++) { length += ReportData[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            RequestData.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)ReportData.length;
            for (int j = 0; j < ReportData.length; j++) { ReportData[j].ToBytes(bytes, i); }
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

            fixedLength += RequestData.length;
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            RequestData.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int ReportDataStart = 0;
            do
            {
                int variableLength = 0;
                int ReportDataCount = 0;

                i = ReportDataStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i < ReportData.length) {
                    int blockLength = ReportData[i].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++ReportDataCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)ReportDataCount;
                for (i = ReportDataStart; i < ReportDataStart + ReportDataCount; i++) { ReportData[i].ToBytes(packet, length); }
                ReportDataStart += ReportDataCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                ReportDataStart < ReportData.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
