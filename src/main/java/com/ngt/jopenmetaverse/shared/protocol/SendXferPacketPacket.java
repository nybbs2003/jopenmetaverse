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
import java.util.ArrayList;
import java.util.List;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;
	public final class SendXferPacketPacket extends Packet
    {
        /// <exclude/>
        public static final class XferIDBlock extends PacketBlock
        {
            public BigInteger ID;
            public long Packet;

            @Override
			public int getLength()
            {
                                {
                    return 12;
                }
            }

            public XferIDBlock() { }
            public XferIDBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ID = Utils.bytesToULongLit(bytes, i[0]); i[0] += 8;
                    Packet = Utils.bytesToUIntLit(bytes, i[0]); i[0] += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException(Utils.getExceptionStackTraceAsString(e));
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.ulongToBytesLit(ID, bytes, i[0]); i[0] += 8;
                Utils.uintToBytesLit(Packet, bytes, i[0]); i[0] += 4;
            }

        }

        /// <exclude/>
        public static final class DataPacketBlock extends PacketBlock
        {
            public byte[] Data;

            @Override
			public int getLength()
            {
                                {
                    int length = 2;
                    if (Data != null) { length += Data.length; }
                    return length;
                }
            }

            public DataPacketBlock() { }
            public DataPacketBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    length = Utils.bytesToUInt16Lit(bytes, i[0]); i[0] += 2;
                    Data = new byte[length];
                    Utils.arraycopy(bytes, i[0], Data, 0, length); i[0] +=  length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException(Utils.getExceptionStackTraceAsString(e));
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = (byte)(Data.length % 256);
                bytes[i[0]++] = (byte)((Data.length >> 8) % 256);
                Utils.arraycopy(Data, 0, bytes, i[0], Data.length); i[0] +=  Data.length;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 7;
                length += XferID.getLength();
                length += DataPacket.getLength();
                return length;
            }
        }
        public XferIDBlock XferID;
        public DataPacketBlock DataPacket;

        public SendXferPacketPacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.SendXferPacket;
            this.header =  new Header();
            header.Frequency = PacketFrequency.High;
            header.ID = 18;
            header.Reliable = true;
            XferID = new XferIDBlock();
            DataPacket = new DataPacketBlock();
        }

        public SendXferPacketPacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            XferID.FromBytes(bytes, i);
            DataPacket.FromBytes(bytes, i);
        }

        public SendXferPacketPacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
		{
		this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
		public void FromBytes(Header header, byte[] bytes, int[] i, int[] packetEnd) throws MalformedDataException
        {
            this.header =  header;
            XferID.FromBytes(bytes, i);
            DataPacket.FromBytes(bytes, i);
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 7;
            length += XferID.getLength();
            length += DataPacket.getLength();
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            XferID.ToBytes(bytes, i);
            DataPacket.ToBytes(bytes, i);
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
