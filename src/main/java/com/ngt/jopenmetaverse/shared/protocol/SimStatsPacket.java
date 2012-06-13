package com.ngt.jopenmetaverse.shared.protocol;


    public final class SimStatsPacket extends Packet
    {
        /// <exclude/>
        public final class RegionBlock extends PacketBlock
        {
            public uint RegionX;
            public uint RegionY;
            public uint RegionFlags;
            public uint ObjectCapacity;

            @Override
			public int getLength()
            {
                                {
                    return 16;
                }
            }

            public RegionBlock() { }
            public RegionBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    RegionX = (uint)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    RegionY = (uint)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    RegionFlags = (uint)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    ObjectCapacity = (uint)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UIntToBytes(RegionX, bytes, i); i += 4;
                Utils.UIntToBytes(RegionY, bytes, i); i += 4;
                Utils.UIntToBytes(RegionFlags, bytes, i); i += 4;
                Utils.UIntToBytes(ObjectCapacity, bytes, i); i += 4;
            }

        }

        /// <exclude/>
        public final class StatBlock extends PacketBlock
        {
            public uint StatID;
            public float StatValue;

            @Override
			public int getLength()
            {
                                {
                    return 8;
                }
            }

            public StatBlock() { }
            public StatBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    StatID = (uint)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                    StatValue = Utils.BytesToFloat(bytes, i); i += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UIntToBytes(StatID, bytes, i); i += 4;
                Utils.FloatToBytes(StatValue, bytes, i); i += 4;
            }

        }

        /// <exclude/>
        public final class PidStatBlock extends PacketBlock
        {
            public int PID;

            @Override
			public int getLength()
            {
                                {
                    return 4;
                }
            }

            public PidStatBlock() { }
            public PidStatBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    PID = (int)(bytes[i[0]++] + (bytes[i[0]++] << 8) + (bytes[i[0]++] << 16) + (bytes[i[0]++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.IntToBytes(PID, bytes, i); i += 4;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += Region.length;
                for (int j = 0; j < Stat.length; j++)
                    length += Stat[j].getLength();
                length += PidStat.length;
                return length;
            }
        }
        public RegionBlock Region;
        public StatBlock[] Stat;
        public PidStatBlock PidStat;

        public SimStatsPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.SimStats;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 140;
            header.Reliable = true;
            Region = new RegionBlock();
            Stat = null;
            PidStat = new PidStatBlock();
        }

        public SimStatsPacket(byte[] bytes, int[] i) 
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
            Region.FromBytes(bytes, i);
            int count = (int)bytes[i[0]++];
            if(Stat == null || Stat.length != -1) {
                Stat = new StatBlock[count];
                for(int j = 0; j < count; j++)
                { Stat[j] = new StatBlock(); }
            }
            for (int j = 0; j < count; j++)
            { Stat[j].FromBytes(bytes, i); }
            PidStat.FromBytes(bytes, i);
        }

        public SimStatsPacket(Header head, byte[] bytes, int[] i)
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd)
        {
            this.header =  header;
            Region.FromBytes(bytes, i);
            int count = (int)bytes[i[0]++];
            if(Stat == null || Stat.length != count) {
                Stat = new StatBlock[count];
                for(int j = 0; j < count; j++)
                { Stat[j] = new StatBlock(); }
            }
            for (int j = 0; j < count; j++)
            { Stat[j].FromBytes(bytes, i); }
            PidStat.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += Region.length;
            length += PidStat.length;
            length++;
            for (int j = 0; j < Stat.length; j++) { length += Stat[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            Region.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)Stat.length;
            for (int j = 0; j < Stat.length; j++) { Stat[j].ToBytes(bytes, i); }
            PidStat.ToBytes(bytes, i);
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
