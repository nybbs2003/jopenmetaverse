/**
 * A library to interact with Virtual Worlds such as OpenSim
 * Copyright (C) 2012  Jitendra Chauhan, Email: jitendra.chauhan@gmail.com
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.ngt.jopenmetaverse.shared.protocol;

import java.math.BigInteger;

import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.types.Vector3;
import com.ngt.jopenmetaverse.shared.util.Utils;


    public final class CrossedRegionPacket extends Packet
    {
        /// <exclude/>
        public static final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID = new UUID();
            public UUID SessionID = new UUID();

            @Override
			public int getLength()
            {
                                {
                    return 32;
                }
            }

            public AgentDataBlock() { }
            public AgentDataBlock(byte[] bytes, int[] i) throws MalformedDataException
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
                    throw new MalformedDataException(Utils.getExceptionStackTraceAsString(e));
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
        public static final class RegionDataBlock extends PacketBlock
        {
            public long SimIP;
            /**
             * Unsigned Short
             * Only two of the least significant bytes should be used
             */
            public int SimPort;
            public BigInteger RegionHandle;
		/** Unsigned Byte */ 
		public byte[] SeedCapability;

            @Override
			public int getLength()
            {
                                {
                    int length = 16;
                    if (SeedCapability != null) { length += SeedCapability.length; }
                    return length;
                }
            }

            public RegionDataBlock() { }
            public RegionDataBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    SimIP = Utils.bytesToUIntLit(bytes, i[0]); i[0] += 4;
                    SimPort = (int)Utils.bytesToUInt16Lit(bytes, i[0]); i[0] += 2;
                    RegionHandle = Utils.bytesToULongLit(bytes, i[0]); i[0] += 8;
                    length = Utils.bytesToUInt16Lit(bytes, i[0]); i[0] += 2;
                    SeedCapability = new byte[length];
                    Utils.arraycopy(bytes, i[0], SeedCapability, 0, length); i[0] +=  length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException(Utils.getExceptionStackTraceAsString(e));
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.uintToBytesLit(SimIP, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = (byte)((SimPort >> 8) % 256);
                bytes[i[0]++] = (byte)(SimPort % 256);
                Utils.ulongToBytesLit(RegionHandle, bytes, i[0]); i[0] += 8;
                bytes[i[0]++] = (byte)(SeedCapability.length % 256);
                bytes[i[0]++] = (byte)((SeedCapability.length >> 8) % 256);
                Utils.arraycopy(SeedCapability, 0, bytes, i[0], SeedCapability.length); i[0] +=  SeedCapability.length;
            }

        }

        /// <exclude/>
        public static final class InfoBlock extends PacketBlock
        {
            public Vector3 Position = new Vector3();
            public Vector3 LookAt = new Vector3();

            @Override
			public int getLength()
            {
                                {
                    return 24;
                }
            }

            public InfoBlock() { }
            public InfoBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    Position.fromBytesLit(bytes, i[0]); i[0] += 12;
                    LookAt.fromBytesLit(bytes, i[0]); i[0] += 12;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException(Utils.getExceptionStackTraceAsString(e));
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Position.toBytesLit(bytes, i[0]); i[0] += 12;
                LookAt.toBytesLit(bytes, i[0]); i[0] += 12;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 8;
                length += AgentData.getLength();
                length += RegionData.getLength();
                length += Info.getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public RegionDataBlock RegionData;
        public InfoBlock Info;

        public CrossedRegionPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.CrossedRegion;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Medium;
            header.ID = 7;
            header.Reliable = true;
            AgentData = new AgentDataBlock();
            RegionData = new RegionDataBlock();
            Info = new InfoBlock();
        }

        public CrossedRegionPacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            RegionData.FromBytes(bytes, i);
            Info.FromBytes(bytes, i);
        }

        public CrossedRegionPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
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
            RegionData.FromBytes(bytes, i);
            Info.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 8;
            length += AgentData.getLength();
            length += RegionData.getLength();
            length += Info.getLength();
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            RegionData.ToBytes(bytes, i);
            Info.ToBytes(bytes, i);
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
