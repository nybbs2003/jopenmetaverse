package com.ngt.jopenmetaverse.shared.protocol;

import java.util.ArrayList;
import java.util.List;

import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;


public final class ViewerStatsPacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID;
            public UUID SessionID;
            public long IP;
            public long StartTime;
            public float RunTime;
            public float SimFPS;
            public float FPS;
		/** Unsigned Byte */ 
		public byte AgentsInView;
            public float Ping;
            public double MetersTraveled;
            public int RegionsVisited;
            public long SysRAM;
		/** Unsigned Byte */ 
		public byte[] SysOS;
		/** Unsigned Byte */ 
		public byte[] SysCPU;
		/** Unsigned Byte */ 
		public byte[] SysGPU;

            @Override
			public int getLength()
            {
                    int length = 76;
                    if (SysOS != null) { length += SysOS.length; }
                    if (SysCPU != null) { length += SysCPU.length; }
                    if (SysGPU != null) { length += SysGPU.length; }
                    return length;
            }

            public AgentDataBlock() { }
            public AgentDataBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    AgentID.FromBytes(bytes, i[0]); i[0] += 16;
                    SessionID.FromBytes(bytes, i[0]); i[0] += 16;
                    IP = (long)Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    StartTime = (long)Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    RunTime = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    SimFPS = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    FPS = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    AgentsInView = (byte)bytes[i[0]++];
                    Ping = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    MetersTraveled = Utils.bytesToDouble(bytes, i[0]); i[0] += 8;
                    RegionsVisited = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    SysRAM = (long)Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    SysOS = new byte[length];
                   
                    Utils.arraycopy(bytes, i[0], SysOS, 0, length); i[0] += length;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    SysCPU = new byte[length];
                    Utils.arraycopy(bytes, i[0], SysCPU, 0, length); i[0] += length;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    SysGPU = new byte[length];
                    Utils.arraycopy(bytes, i[0], SysGPU, 0, length); i[0] += length;
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
                Utils.uintToBytes(IP, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(StartTime, bytes, i[0]); i[0] += 4;
                Utils.floatToBytes(RunTime, bytes, i[0]); i[0] += 4;
                Utils.floatToBytes(SimFPS, bytes, i[0]); i[0] += 4;
                Utils.floatToBytes(FPS, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = AgentsInView;
                Utils.floatToBytes(Ping, bytes, i[0]); i[0] += 4;
                Utils.doubleToBytes(MetersTraveled, bytes, i[0]); i[0] += 8;
                Utils.intToBytes(RegionsVisited, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(SysRAM, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = (byte)SysOS.length;
                Utils.arraycopy(SysOS, 0, bytes, i[0], SysOS.length); i[0] += SysOS.length;
                bytes[i[0]++] = (byte)SysCPU.length;
                Utils.arraycopy(SysCPU, 0, bytes, i[0], SysCPU.length); i[0] += SysCPU.length;
                bytes[i[0]++] = (byte)SysGPU.length;
                Utils.arraycopy(SysGPU, 0, bytes, i[0], SysGPU.length); i[0] += SysGPU.length;
            }

        }

        /// <exclude/>
        public final class DownloadTotalsBlock extends PacketBlock
        {
            public long World;
            public long Objects;
            public long Textures;

            @Override
			public int getLength()
            {
                    return 12;
            }

            public DownloadTotalsBlock() { }
            public DownloadTotalsBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    World = (long)Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    Objects = (long)Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    Textures = (long)Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.uintToBytes(World, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(Objects, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(Textures, bytes, i[0]); i[0] += 4;
            }

        }

        /// <exclude/>
        public final class NetStatsBlock extends PacketBlock
        {
            public long Bytes;
            public long Packets;
            public long Compressed;
            public long Savings;

            @Override
			public int getLength()
            {
                    return 16;
            }

            public NetStatsBlock() { }
            public NetStatsBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    Bytes = (long)Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    Packets = (long)Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    Compressed = (long)Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    Savings = (long)Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.uintToBytes(Bytes, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(Packets, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(Compressed, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(Savings, bytes, i[0]); i[0] += 4;
            }

        }

        /// <exclude/>
        public final class FailStatsBlock extends PacketBlock
        {
            public long SendPacket;
            public long Dropped;
            public long Resent;
            public long FailedResends;
            public long OffCircuit;
            public long Invalid;

            @Override
			public int getLength()
            {
                    return 24;
            }

            public FailStatsBlock() { }
            public FailStatsBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    SendPacket = (long)Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    Dropped = (long)Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    Resent = (long)Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    FailedResends = (long)Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    OffCircuit = (long)Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    Invalid = (long)Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.uintToBytes(SendPacket, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(Dropped, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(Resent, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(FailedResends, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(OffCircuit, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(Invalid, bytes, i[0]); i[0] += 4;
            }

        }

        /// <exclude/>
        public final class MiscStatsBlock extends PacketBlock
        {
            public long Type;
            public double Value;

            @Override
			public int getLength()
            {
                    return 12;
            }

            public MiscStatsBlock() { }
            public MiscStatsBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    Type = (long)Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    Value = Utils.bytesToDouble(bytes, i[0]); i[0] += 8;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.uintToBytes(Type, bytes, i[0]); i[0] += 4;
                Utils.doubleToBytes(Value, bytes, i[0]); i[0] += 8;
            }

        }

        @Override
			public int getLength()
        {
                int length = 11;
                length += AgentData.getLength();
                length += DownloadTotals.getLength();
                for (int j = 0; j < 2; j++)
                    length += NetStats[j].getLength();
                length += FailStats.getLength();
                for (int j = 0; j < MiscStats.length; j++)
                    length += MiscStats[j].getLength();
                return length;
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

        public ViewerStatsPacket(byte[] bytes, int[] i) throws MalformedDataException 
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(bytes, i, packetEnd, null);
        }

        @Override
		public void FromBytes(byte[] bytes, int[] i, int[] packetEnd, byte[] zeroBuffer) throws MalformedDataException
        {
            header.FromBytes(bytes, i, packetEnd);
            if (header.Zerocoded && zeroBuffer != null)
            {
                packetEnd[0] = Helpers.ZeroDecode(bytes, packetEnd[0] + 1, zeroBuffer) - 1;
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
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(MiscStats == null || MiscStats.length != -1) {
                MiscStats = new MiscStatsBlock[count];
                for(int j = 0; j < count; j++)
                { MiscStats[j] = new MiscStatsBlock(); }
            }
            for (int j = 0; j < count; j++)
            { MiscStats[j].FromBytes(bytes, i); }
        }

        public ViewerStatsPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd) throws MalformedDataException
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
            int count = Utils.ubyteToInt(bytes[i[0]++]);
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
            length += DownloadTotals.getLength();
            length += FailStats.getLength();
            for (int j = 0; j < 2; j++) { length += NetStats[j].getLength(); }
            length++;
            for (int j = 0; j < MiscStats.length; j++) { length += MiscStats[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            DownloadTotals.ToBytes(bytes, i);
            for (int j = 0; j < 2; j++) { NetStats[j].ToBytes(bytes, i); }
            FailStats.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)MiscStats.length;
            for (int j = 0; j < MiscStats.length; j++) { MiscStats[j].ToBytes(bytes, i); }
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
        	List<byte[]> packets = new ArrayList<byte[]>();
            int[] i = new int[] {0};
            int fixedLength = 10;

            byte[] ackBytes = null;
            int[] acksLength = new int[]{0};
            if (header.AckList != null && header.AckList.length > 0) {
                header.AppendedAcks = true;
                ackBytes = new byte[header.AckList.length * 4 + 1];
                header.AcksToBytes(ackBytes, acksLength);
            }

            fixedLength += AgentData.getLength();
            fixedLength += DownloadTotals.getLength();
            for (int j = 0; j < 2; j++) { fixedLength += NetStats[j].getLength(); }
            fixedLength += FailStats.getLength();
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

                i[0] = MiscStatsStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < MiscStats.length) {
                    int blockLength = MiscStats[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++MiscStatsCount;
                    }
                    else { break; }
                    ++i[0];
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)MiscStatsCount;
                for (i[0] = MiscStatsStart; i[0] < MiscStatsStart + MiscStatsCount; i[0]++) { MiscStats[i[0]].ToBytes(packet, length); }
                MiscStatsStart += MiscStatsCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                MiscStatsStart < MiscStats.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
