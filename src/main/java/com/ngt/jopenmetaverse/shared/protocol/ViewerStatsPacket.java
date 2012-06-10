package com.ngt.jopenmetaverse.shared.protocol;


    public final class ViewerStatsPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID SessionID;
            public uint IP;
            public uint StartTime;
            public float RunTime;
            public float SimFPS;
            public float FPS;
            public byte AgentsInView;
            public float Ping;
            public double MetersTraveled;
            public int RegionsVisited;
            public uint SysRAM;
            public byte[] SysOS;
            public byte[] SysCPU;
            public byte[] SysGPU;

            @Override
			public int getLength()
            {
                get
                {
                    int length = 76;
                    if (SysOS != null) { length += SysOS.length; }
                    if (SysCPU != null) { length += SysCPU.length; }
                    if (SysGPU != null) { length += SysGPU.length; }
                    return length;
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
                int length;
                try
                {
                    AgentID.FromBytes(bytes, i); i += 16;
                    SessionID.FromBytes(bytes, i); i += 16;
                    IP = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    StartTime = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    RunTime = Utils.BytesToFloat(bytes, i); i += 4;
                    SimFPS = Utils.BytesToFloat(bytes, i); i += 4;
                    FPS = Utils.BytesToFloat(bytes, i); i += 4;
                    AgentsInView = (byte)bytes[i++];
                    Ping = Utils.BytesToFloat(bytes, i); i += 4;
                    MetersTraveled = Utils.BytesToDouble(bytes, i); i += 8;
                    RegionsVisited = (int)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    SysRAM = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    length = bytes[i++];
                    SysOS = new byte[length];
                    Buffer.BlockCopy(bytes, i, SysOS, 0, length); i += length;
                    length = bytes[i++];
                    SysCPU = new byte[length];
                    Buffer.BlockCopy(bytes, i, SysCPU, 0, length); i += length;
                    length = bytes[i++];
                    SysGPU = new byte[length];
                    Buffer.BlockCopy(bytes, i, SysGPU, 0, length); i += length;
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
                SessionID.ToBytes(bytes, i); i += 16;
                Utils.UIntToBytes(IP, bytes, i); i += 4;
                Utils.UIntToBytes(StartTime, bytes, i); i += 4;
                Utils.FloatToBytes(RunTime, bytes, i); i += 4;
                Utils.FloatToBytes(SimFPS, bytes, i); i += 4;
                Utils.FloatToBytes(FPS, bytes, i); i += 4;
                bytes[i++] = AgentsInView;
                Utils.FloatToBytes(Ping, bytes, i); i += 4;
                Utils.DoubleToBytes(MetersTraveled, bytes, i); i += 8;
                Utils.IntToBytes(RegionsVisited, bytes, i); i += 4;
                Utils.UIntToBytes(SysRAM, bytes, i); i += 4;
                bytes[i++] = (byte)SysOS.length;
                Buffer.BlockCopy(SysOS, 0, bytes, i, SysOS.length); i += SysOS.length;
                bytes[i++] = (byte)SysCPU.length;
                Buffer.BlockCopy(SysCPU, 0, bytes, i, SysCPU.length); i += SysCPU.length;
                bytes[i++] = (byte)SysGPU.length;
                Buffer.BlockCopy(SysGPU, 0, bytes, i, SysGPU.length); i += SysGPU.length;
            }

        }

        /// <exclude/>
        public final class DownloadTotalsBlock extends PacketBlock
        {
            public uint World;
            public uint Objects;
            public uint Textures;

            @Override
			public int getLength()
            {
                get
                {
                    return 12;
                }
            }

            public DownloadTotalsBlock() { }
            public DownloadTotalsBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    World = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Objects = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Textures = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UIntToBytes(World, bytes, i); i += 4;
                Utils.UIntToBytes(Objects, bytes, i); i += 4;
                Utils.UIntToBytes(Textures, bytes, i); i += 4;
            }

        }

        /// <exclude/>
        public final class NetStatsBlock extends PacketBlock
        {
            public uint Bytes;
            public uint Packets;
            public uint Compressed;
            public uint Savings;

            @Override
			public int getLength()
            {
                get
                {
                    return 16;
                }
            }

            public NetStatsBlock() { }
            public NetStatsBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    Bytes = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Packets = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Compressed = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Savings = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UIntToBytes(Bytes, bytes, i); i += 4;
                Utils.UIntToBytes(Packets, bytes, i); i += 4;
                Utils.UIntToBytes(Compressed, bytes, i); i += 4;
                Utils.UIntToBytes(Savings, bytes, i); i += 4;
            }

        }

        /// <exclude/>
        public final class FailStatsBlock extends PacketBlock
        {
            public uint SendPacket;
            public uint Dropped;
            public uint Resent;
            public uint FailedResends;
            public uint OffCircuit;
            public uint Invalid;

            @Override
			public int getLength()
            {
                get
                {
                    return 24;
                }
            }

            public FailStatsBlock() { }
            public FailStatsBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    SendPacket = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Dropped = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Resent = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    FailedResends = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    OffCircuit = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Invalid = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UIntToBytes(SendPacket, bytes, i); i += 4;
                Utils.UIntToBytes(Dropped, bytes, i); i += 4;
                Utils.UIntToBytes(Resent, bytes, i); i += 4;
                Utils.UIntToBytes(FailedResends, bytes, i); i += 4;
                Utils.UIntToBytes(OffCircuit, bytes, i); i += 4;
                Utils.UIntToBytes(Invalid, bytes, i); i += 4;
            }

        }

        /// <exclude/>
        public final class MiscStatsBlock extends PacketBlock
        {
            public uint Type;
            public double Value;

            @Override
			public int getLength()
            {
                get
                {
                    return 12;
                }
            }

            public MiscStatsBlock() { }
            public MiscStatsBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    Type = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    Value = Utils.BytesToDouble(bytes, i); i += 8;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UIntToBytes(Type, bytes, i); i += 4;
                Utils.DoubleToBytes(Value, bytes, i); i += 8;
            }

        }

        @Override
			public int getLength()
        {
            get
            {
                int length = 11;
                length += AgentData.getLength();
                length += DownloadTotals.length;
                for (int j = 0; j < 2; j++)
                    length += NetStats[j].length;
                length += FailStats.length;
                for (int j = 0; j < MiscStats.length; j++)
                    length += MiscStats[j].length;
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public DownloadTotalsBlock DownloadTotals;
        public NetStatsBlock[] NetStats;
        public FailStatsBlock FailStats;
        public MiscStatsBlock[] MiscStats;

        public ViewerStatsPacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.ViewerStats;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 131;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            DownloadTotals = new DownloadTotalsBlock();
            NetStats = new NetStatsBlock[2];
            FailStats = new FailStatsBlock();
            MiscStats = null;
        }

        public ViewerStatsPacket(byte[] bytes, int[] i) 
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
            AgentData.FromBytes(bytes, i);
            DownloadTotals.FromBytes(bytes, i);
            if(NetStats == null || NetStats.length != 2) {
                NetStats = new NetStatsBlock[2];
                for(int j = 0; j < 2; j++)
                { NetStats[j] = new NetStatsBlock(); }
            }
            for (int j = 0; j < 2; j++)
            { NetStats[j].FromBytes(bytes, i); }
            FailStats.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(MiscStats == null || MiscStats.length != -1) {
                MiscStats = new MiscStatsBlock[count];
                for(int j = 0; j < count; j++)
                { MiscStats[j] = new MiscStatsBlock(); }
            }
            for (int j = 0; j < count; j++)
            { MiscStats[j].FromBytes(bytes, i); }
        }

        public ViewerStatsPacket(Header head, byte[] bytes, int[] i)
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
            DownloadTotals.FromBytes(bytes, i);
            if(NetStats == null || NetStats.length != 2) {
                NetStats = new NetStatsBlock[2];
                for(int j = 0; j < 2; j++)
                { NetStats[j] = new NetStatsBlock(); }
            }
            for (int j = 0; j < 2; j++)
            { NetStats[j].FromBytes(bytes, i); }
            FailStats.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(MiscStats == null || MiscStats.length != count) {
                MiscStats = new MiscStatsBlock[count];
                for(int j = 0; j < count; j++)
                { MiscStats[j] = new MiscStatsBlock(); }
            }
            for (int j = 0; j < count; j++)
            { MiscStats[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += DownloadTotals.length;
            length += FailStats.length;
            for (int j = 0; j < 2; j++) { length += NetStats[j].length; }
            length++;
            for (int j = 0; j < MiscStats.length; j++) { length += MiscStats[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            DownloadTotals.ToBytes(bytes, i);
            for (int j = 0; j < 2; j++) { NetStats[j].ToBytes(bytes, i); }
            FailStats.ToBytes(bytes, i);
            bytes[i++] = (byte)MiscStats.length;
            for (int j = 0; j < MiscStats.length; j++) { MiscStats[j].ToBytes(bytes, i); }
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

            fixedLength += AgentData.getLength();
            fixedLength += DownloadTotals.length;
            for (int j = 0; j < 2; j++) { fixedLength += NetStats[j].length; }
            fixedLength += FailStats.length;
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AgentData.ToBytes(fixedBytes, i);
            DownloadTotals.ToBytes(fixedBytes, i);
            for (int j = 0; j < 2; j++) { NetStats[j].ToBytes(fixedBytes, i); }
            FailStats.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int MiscStatsStart = 0;
            do
            {
                int variableLength = 0;
                int MiscStatsCount = 0;

                i = MiscStatsStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < MiscStats.length) {
                    int blockLength = MiscStats[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++MiscStatsCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Buffer.BlockCopy(fixedBytes, 0, packet, 0, length);
                if (packets.Count > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)MiscStatsCount;
                for (i = MiscStatsStart; i < MiscStatsStart + MiscStatsCount; i++) { MiscStats[i].ToBytes(packet, ref length); }
                MiscStatsStart += MiscStatsCount;

                if (acksLength > 0) {
                    Buffer.BlockCopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.Add(packet);
            } while (
                MiscStatsStart < MiscStats.length);

            return packets.ToArray();
        }
    }

    /// <exclude/>
