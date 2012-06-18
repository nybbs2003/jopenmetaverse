package com.ngt.jopenmetaverse.shared.protocol;

import java.util.ArrayList;
import java.util.List;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;
	public final class SimStatsPacket extends Packet
    {
        /// <exclude/>
        public static final class RegionBlock extends PacketBlock
        {
            public long RegionX;
            public long RegionY;
            public long RegionFlags;
            public long ObjectCapacity;

            @Override
			public int getLength()
            {
                                {
                    return 16;
                }
            }

            public RegionBlock() { }
            public RegionBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    RegionX = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    RegionY = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    RegionFlags = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    ObjectCapacity = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.uintToBytes(RegionX, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(RegionY, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(RegionFlags, bytes, i[0]); i[0] += 4;
                Utils.uintToBytes(ObjectCapacity, bytes, i[0]); i[0] += 4;
            }

        }

        /// <exclude/>
        public static final class StatBlock extends PacketBlock
        {
            public long StatID;
            public float StatValue;

            @Override
			public int getLength()
            {
                                {
                    return 8;
                }
            }

            public StatBlock() { }
            public StatBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    StatID = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    StatValue = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.uintToBytes(StatID, bytes, i[0]); i[0] += 4;
                Utils.floatToBytes(StatValue, bytes, i[0]); i[0] += 4;
            }

        }

        /// <exclude/>
        public static final class PidStatBlock extends PacketBlock
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
            public PidStatBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    PID = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.intToBytes(PID, bytes, i[0]); i[0] += 4;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += Region.getLength();
                for (int j = 0; j < Stat.length; j++)
                    length += Stat[j].getLength();
                length += PidStat.getLength();
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

        public SimStatsPacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            Region.FromBytes(bytes, i);
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(Stat == null || Stat.length != -1) {
                Stat = new StatBlock[count];
                for(int j = 0; j < count; j++)
                { Stat[j] = new StatBlock(); }
            }
            for (int j = 0; j < count; j++)
            { Stat[j].FromBytes(bytes, i); }
            PidStat.FromBytes(bytes, i);
        }

        public SimStatsPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd) throws MalformedDataException
        {
            this.header =  header;
            Region.FromBytes(bytes, i);
            int count = Utils.ubyteToInt(bytes[i[0]++]);
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
            length += Region.getLength();
            length += PidStat.getLength();
            length++;
            for (int j = 0; j < Stat.length; j++) { length += Stat[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
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
