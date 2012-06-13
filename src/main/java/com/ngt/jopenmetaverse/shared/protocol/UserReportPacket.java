package com.ngt.jopenmetaverse.shared.protocol;


    public final class UserReportPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID SessionID;

            @Override
			public int getLength()
            {
                                {
                    return 32;
                }
            }

            public AgentDataBlock() { }
            public AgentDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    AgentID.FromBytes(bytes, i[0]); i[0] += 16;
                    SessionID.FromBytes(bytes, i[0]); i[0] += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                AgentID.ToBytes(bytes, i[0]); i[0] += 16;
                SessionID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        /// <exclude/>
        public final class ReportDataBlock extends PacketBlock
        {
            public byte ReportType;
            public byte Category;
            public Vector3 Position;
            public byte CheckFlags;
            public UUID ScreenshotID;
            public UUID ObjectID;
            public UUID AbuserID;
            public byte[] AbuseRegionName;
            public UUID AbuseRegionID;
            public byte[] Summary;
            public byte[] Details;
            public byte[] VersionString;

            @Override
			public int getLength()
            {
                                {
                    int length = 84;
                    if (AbuseRegionName != null) { length += AbuseRegionName.length; }
                    if (Summary != null) { length += Summary.length; }
                    if (Details != null) { length += Details.length; }
                    if (VersionString != null) { length += VersionString.length; }
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
                    ReportType = (byte)bytes[i[0]++];
                    Category = (byte)bytes[i[0]++];
                    Position.FromBytes(bytes, i[0]); i[0] += 12;
                    CheckFlags = (byte)bytes[i[0]++];
                    ScreenshotID.FromBytes(bytes, i[0]); i[0] += 16;
                    ObjectID.FromBytes(bytes, i[0]); i[0] += 16;
                    AbuserID.FromBytes(bytes, i[0]); i[0] += 16;
                    length = bytes[i[0]++];
                    AbuseRegionName = new byte[length];
                    Utils.arraycopy(bytes, i[0], AbuseRegionName, 0, length); i[0] +=  length;
                    AbuseRegionID.FromBytes(bytes, i[0]); i[0] += 16;
                    length = bytes[i[0]++];
                    Summary = new byte[length];
                    Utils.arraycopy(bytes, i[0], Summary, 0, length); i[0] +=  length;
                    length = (bytes[i[0]++] + (bytes[i[0]++] << 8));
                    Details = new byte[length];
                    Utils.arraycopy(bytes, i[0], Details, 0, length); i[0] +=  length;
                    length = bytes[i[0]++];
                    VersionString = new byte[length];
                    Utils.arraycopy(bytes, i[0], VersionString, 0, length); i[0] +=  length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = ReportType;
                bytes[i[0]++] = Category;
                Position.ToBytes(bytes, i[0]); i[0] += 12;
                bytes[i[0]++] = CheckFlags;
                ScreenshotID.ToBytes(bytes, i[0]); i[0] += 16;
                ObjectID.ToBytes(bytes, i[0]); i[0] += 16;
                AbuserID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)AbuseRegionName.length;
                Utils.arraycopy(AbuseRegionName, 0, bytes, i[0], AbuseRegionName.length); i[0] +=  AbuseRegionName.length;
                AbuseRegionID.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = (byte)Summary.length;
                Utils.arraycopy(Summary, 0, bytes, i[0], Summary.length); i[0] +=  Summary.length;
                bytes[i[0]++] = (byte)(Details.length % 256);
                bytes[i[0]++] = (byte)((Details.length >> 8) % 256);
                Utils.arraycopy(Details, 0, bytes, i[0], Details.length); i[0] +=  Details.length;
                bytes[i[0]++] = (byte)VersionString.length;
                Utils.arraycopy(VersionString, 0, bytes, i[0], VersionString.length); i[0] +=  VersionString.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 10;
                length += AgentData.getLength();
                length += ReportData.length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public ReportDataBlock ReportData;

        public UserReportPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.UserReport;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 133;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            ReportData = new ReportDataBlock();
        }

        public UserReportPacket(byte[] bytes, int[] i) 
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
            AgentData.FromBytes(bytes, i);
            ReportData.FromBytes(bytes, i);
        }

        public UserReportPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            AgentData.FromBytes(bytes, i);
            ReportData.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += ReportData.length;
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            ReportData.ToBytes(bytes, i);
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            return new byte[][] { ToBytes() };
        }
    }

    /// <exclude/>
