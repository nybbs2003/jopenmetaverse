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

import java.util.ArrayList;
import java.util.List;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;
	public final class ObjectFlagUpdatePacket extends Packet
    {
        /// <exclude/>
        public static final class AgentDataBlock extends PacketBlock
        {
            public UUID AgentID = new UUID();
            public UUID SessionID = new UUID();
            public long ObjectLocalID;
            public boolean UsePhysics;
            public boolean IsTemporary;
            public boolean IsPhantom;
            public boolean CastsShadows;

            @Override
			public int getLength()
            {
                                {
                    return 40;
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
                    ObjectLocalID = Utils.bytesToUIntLit(bytes, i[0]); i[0] += 4;
                    UsePhysics = (bytes[i[0]++] != 0) ? true : false;
                    IsTemporary = (bytes[i[0]++] != 0) ? true : false;
                    IsPhantom = (bytes[i[0]++] != 0) ? true : false;
                    CastsShadows = (bytes[i[0]++] != 0) ? true : false;
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
                Utils.uintToBytesLit(ObjectLocalID, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = (byte)((UsePhysics) ? 1 : 0);
                bytes[i[0]++] = (byte)((IsTemporary) ? 1 : 0);
                bytes[i[0]++] = (byte)((IsPhantom) ? 1 : 0);
                bytes[i[0]++] = (byte)((CastsShadows) ? 1 : 0);
            }

        }

        /// <exclude/>
        public static final class ExtraPhysicsBlock extends PacketBlock
        {
            public byte PhysicsShapeType;
            public float Density;
            public float Friction;
            public float Restitution;
            public float GravityMultiplier;

            @Override
			public int getLength()
            {
                                {
                    return 17;
                }
            }

            public ExtraPhysicsBlock() { }
            public ExtraPhysicsBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    PhysicsShapeType = (byte)bytes[i[0]++];
                    Density = Utils.bytesToFloatLit(bytes, i[0]); i[0] += 4;
                    Friction = Utils.bytesToFloatLit(bytes, i[0]); i[0] += 4;
                    Restitution = Utils.bytesToFloatLit(bytes, i[0]); i[0] += 4;
                    GravityMultiplier = Utils.bytesToFloatLit(bytes, i[0]); i[0] += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException(Utils.getExceptionStackTraceAsString(e));
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = PhysicsShapeType;
                Utils.floatToBytesLit(Density, bytes, i[0]); i[0] += 4;
                Utils.floatToBytesLit(Friction, bytes, i[0]); i[0] += 4;
                Utils.floatToBytesLit(Restitution, bytes, i[0]); i[0] += 4;
                Utils.floatToBytesLit(GravityMultiplier, bytes, i[0]); i[0] += 4;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += AgentData.getLength();
                for (int j = 0; j < ExtraPhysics.length; j++)
                    length += ExtraPhysics[j].getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public ExtraPhysicsBlock[] ExtraPhysics;

        public ObjectFlagUpdatePacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.ObjectFlagUpdate;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 94;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            ExtraPhysics = null;
        }

        public ObjectFlagUpdatePacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(ExtraPhysics == null || ExtraPhysics.length != -1) {
                ExtraPhysics = new ExtraPhysicsBlock[count];
                for(int j = 0; j < count; j++)
                { ExtraPhysics[j] = new ExtraPhysicsBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ExtraPhysics[j].FromBytes(bytes, i); }
        }

        public ObjectFlagUpdatePacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
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
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(ExtraPhysics == null || ExtraPhysics.length != count) {
                ExtraPhysics = new ExtraPhysicsBlock[count];
                for(int j = 0; j < count; j++)
                { ExtraPhysics[j] = new ExtraPhysicsBlock(); }
            }
            for (int j = 0; j < count; j++)
            { ExtraPhysics[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length++;
            for (int j = 0; j < ExtraPhysics.length; j++) { length += ExtraPhysics[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)ExtraPhysics.length;
            for (int j = 0; j < ExtraPhysics.length; j++) { ExtraPhysics[j].ToBytes(bytes, i); }
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            List<byte[]> packets = new ArrayList<byte[]>();
            int[] i = new int[]{0};
            int fixedLength = 10;

            byte[] ackBytes = null;
            int[] acksLength = new int[]{0};
            if (header.AckList != null && header.AckList.length > 0) {
                header.AppendedAcks = true;
                ackBytes = new byte[header.AckList.length * 4 + 1];
                header.AcksToBytes(ackBytes, acksLength);
            }

            fixedLength += AgentData.getLength();
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AgentData.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int ExtraPhysicsStart = 0;
            do
            {
                int variableLength = 0;
                int ExtraPhysicsCount = 0;

              i[0] =ExtraPhysicsStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < ExtraPhysics.length) {
                    int blockLength = ExtraPhysics[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++ExtraPhysicsCount;
                    }
                    else { break; }
                    i[0]++;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)ExtraPhysicsCount;
                for (i[0] = ExtraPhysicsStart; i[0] < ExtraPhysicsStart + ExtraPhysicsCount; i[0]++) { ExtraPhysics[i[0]].ToBytes(packet, length); }
                ExtraPhysicsStart += ExtraPhysicsCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                ExtraPhysicsStart < ExtraPhysics.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
