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

import com.ngt.jopenmetaverse.shared.util.Utils;

  /// <exclude/>
    public final class TestMessagePacket extends Packet
    {
        /// <exclude/>
        public static final class TestBlock1Block extends PacketBlock
        {
            public long test1;

            @Override
            public int getLength()
            {
                    return 4;
            }

            public TestBlock1Block() { }
            public TestBlock1Block(byte[] bytes, int i[]) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
            public void FromBytes(byte[] bytes, int i[]) throws MalformedDataException
            {
                try
                {
                    test1 = (long)Utils.bytesToUIntLit(bytes, i[0]); i[0] += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException(Utils.getExceptionStackTraceAsString(e));
                }
            }

            @Override
            public void ToBytes(byte[] bytes, int i[])
            {
                Utils.intToBytesLit((int)test1, bytes, i[0]); i[0] += 4;
            }

        }
        
        /// <exclude/>
        public static final class NeighborBlockBlock extends PacketBlock
        {
            public long test0;
            public long test1;
            public long test2;

            @Override
            public int getLength()
            {
                    return 12;
            }

            public NeighborBlockBlock() { }
            public NeighborBlockBlock(byte[] bytes, int i[]) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
            public void FromBytes(byte[] bytes, int i[]) throws MalformedDataException
            {
                try
                {
                    test0 = (long)Utils.bytesToUIntLit(bytes, i[0]); i[0] += 4;
                    test1 = (long)Utils.bytesToUIntLit(bytes, i[0]); i[0] += 4;
                    test2 = (long)Utils.bytesToUIntLit(bytes, i[0]); i[0] += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException(Utils.getExceptionStackTraceAsString(e));
                }
            }

            @Override
            public void ToBytes(byte[] bytes, int i[])
            {
                Utils.intToBytesLit((int)test0, bytes, i[0]); i[0] += 4;
                Utils.intToBytesLit((int)test1, bytes, i[0]); i[0] += 4;
                Utils.intToBytesLit((int)test2, bytes, i[0]); i[0] += 4;
            }

        }

        @Override
        public int getLength()
        {
                int length = 10;
                length += TestBlock1.getLength();
                for (int j = 0; j < 4; j++)
                    length += NeighborBlock[j].getLength();
                return length;
        }
        public TestBlock1Block TestBlock1;
        public NeighborBlockBlock[] NeighborBlock;

        public TestMessagePacket()
        {
            HasVariableBlocks = false;
            Type = PacketType.TestMessage;
            header = new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 1;
            header.Reliable = true;
            header.Zerocoded = true;
            TestBlock1 = new TestBlock1Block();
            NeighborBlock = new NeighborBlockBlock[4];
        }

        public TestMessagePacket(byte[] bytes, int i[]) throws MalformedDataException
        {
        	this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(bytes, i, packetEnd, null);
        }

        @Override
        public void FromBytes(byte[] bytes, int i[], int packetEnd[], byte[] zeroBuffer) throws MalformedDataException
        {
            header.FromBytes(bytes, i, packetEnd);
            if (header.Zerocoded && zeroBuffer != null)
            {
                packetEnd[0] = Helpers.ZeroDecode(bytes, packetEnd[0] + 1, zeroBuffer) - 1;
                bytes = zeroBuffer;
            }
            TestBlock1.FromBytes(bytes, i);
            if(NeighborBlock == null || NeighborBlock.length != 4) {
                NeighborBlock = new NeighborBlockBlock[4];
                for(int j = 0; j < 4; j++)
                { NeighborBlock[j] = new NeighborBlockBlock(); }
            }
            for (int j = 0; j < 4; j++)
            { NeighborBlock[j].FromBytes(bytes, i); }
        }

        public TestMessagePacket(Header head, byte[] bytes, int i[]) throws MalformedDataException
        {
        	this();
            int[] packetEnd = new int[] {bytes.length - 1};
            FromBytes(head, bytes, i, packetEnd);
        }

        @Override
        public void FromBytes(Header header, byte[] bytes, int i[], int packetEnd[]) throws MalformedDataException
        {
            this.header = header;
            TestBlock1.FromBytes(bytes, i);
            if(NeighborBlock == null || NeighborBlock.length != 4) {
                NeighborBlock = new NeighborBlockBlock[4];
                for(int j = 0; j < 4; j++)
                { NeighborBlock[j] = new NeighborBlockBlock(); }
            }
            for (int j = 0; j < 4; j++)
            { NeighborBlock[j].FromBytes(bytes, i); }
        }

        @Override
        public byte[] ToBytes()
        {
            int length = 10;
            length += TestBlock1.getLength();
            for (int j = 0; j < 4; j++) { length += NeighborBlock[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[] {0};
            header.ToBytes(bytes, i);
            TestBlock1.ToBytes(bytes, i);
            for (int j = 0; j < 4; j++) { NeighborBlock[j].ToBytes(bytes, i); }
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
        public byte[][] ToBytesMultiple()
        {
            return new byte[][] { ToBytes() };
        }
    }
