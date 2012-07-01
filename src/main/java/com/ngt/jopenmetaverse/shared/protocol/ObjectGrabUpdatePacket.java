package com.ngt.jopenmetaverse.shared.protocol;

import java.util.ArrayList;
import java.util.List;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.types.Vector3;
import com.ngt.jopenmetaverse.shared.util.Utils;
	public final class ObjectGrabUpdatePacket extends Packet
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
        public static final class ObjectDataBlock extends PacketBlock
        {
            public UUID ObjectID = new UUID();
            public Vector3 GrabOffsetInitial;
            public Vector3 GrabPosition;
            public long TimeSinceLast;

            @Override
			public int getLength()
            {
                                {
                    return 44;
                }
            }

            public ObjectDataBlock() { }
            public ObjectDataBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ObjectID.FromBytes(bytes, i[0]); i[0] += 16;
                    GrabOffsetInitial.fromBytes(bytes, i[0]); i[0] += 12;
                    GrabPosition.fromBytes(bytes, i[0]); i[0] += 12;
                    TimeSinceLast = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                ObjectID.ToBytes(bytes, i[0]); i[0] += 16;
                GrabOffsetInitial.toBytes(bytes, i[0]); i[0] += 12;
                GrabPosition.toBytes(bytes, i[0]); i[0] += 12;
                Utils.uintToBytes(TimeSinceLast, bytes, i[0]); i[0] += 4;
            }

        }

        /// <exclude/>
        public static final class SurfaceInfoBlock extends PacketBlock
        {
            public Vector3 UVCoord;
            public Vector3 STCoord;
            public int FaceIndex;
            public Vector3 Position;
            public Vector3 Normal;
            public Vector3 Binormal;

            @Override
			public int getLength()
            {
                                {
                    return 64;
                }
            }

            public SurfaceInfoBlock() { }
            public SurfaceInfoBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    UVCoord.fromBytes(bytes, i[0]); i[0] += 12;
                    STCoord.fromBytes(bytes, i[0]); i[0] += 12;
                    FaceIndex = Utils.bytesToInt(bytes, i[0]); i[0]+=4;
                    Position.fromBytes(bytes, i[0]); i[0] += 12;
                    Normal.fromBytes(bytes, i[0]); i[0] += 12;
                    Binormal.fromBytes(bytes, i[0]); i[0] += 12;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                UVCoord.toBytes(bytes, i[0]); i[0] += 12;
                STCoord.toBytes(bytes, i[0]); i[0] += 12;
                Utils.intToBytes(FaceIndex, bytes, i[0]); i[0] += 4;
                Position.toBytes(bytes, i[0]); i[0] += 12;
                Normal.toBytes(bytes, i[0]); i[0] += 12;
                Binormal.toBytes(bytes, i[0]); i[0] += 12;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 11;
                length += AgentData.getLength();
                length += ObjectData.getLength();
                for (int j = 0; j < SurfaceInfo.length; j++)
                    length += SurfaceInfo[j].getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public ObjectDataBlock ObjectData;
        public SurfaceInfoBlock[] SurfaceInfo;

        public ObjectGrabUpdatePacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.ObjectGrabUpdate;
            this.header =  new Header();
            header.Frequency = PacketFrequency.Low;
            header.ID = 118;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            ObjectData = new ObjectDataBlock();
            SurfaceInfo = null;
        }

        public ObjectGrabUpdatePacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            ObjectData.FromBytes(bytes, i);
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(SurfaceInfo == null || SurfaceInfo.length != -1) {
                SurfaceInfo = new SurfaceInfoBlock[count];
                for(int j = 0; j < count; j++)
                { SurfaceInfo[j] = new SurfaceInfoBlock(); }
            }
            for (int j = 0; j < count; j++)
            { SurfaceInfo[j].FromBytes(bytes, i); }
        }

        public ObjectGrabUpdatePacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
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
            ObjectData.FromBytes(bytes, i);
            int count = Utils.ubyteToInt(bytes[i[0]++]);
            if(SurfaceInfo == null || SurfaceInfo.length != count) {
                SurfaceInfo = new SurfaceInfoBlock[count];
                for(int j = 0; j < count; j++)
                { SurfaceInfo[j] = new SurfaceInfoBlock(); }
            }
            for (int j = 0; j < count; j++)
            { SurfaceInfo[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 10;
            length += AgentData.getLength();
            length += ObjectData.getLength();
            length++;
            for (int j = 0; j < SurfaceInfo.length; j++) { length += SurfaceInfo[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            ObjectData.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)SurfaceInfo.length;
            for (int j = 0; j < SurfaceInfo.length; j++) { SurfaceInfo[j].ToBytes(bytes, i); }
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
            fixedLength += ObjectData.getLength();
            byte[] fixedBytes = new byte[fixedLength];
            header.ToBytes(fixedBytes, i);
            AgentData.ToBytes(fixedBytes, i);
            ObjectData.ToBytes(fixedBytes, i);
            fixedLength += 1;

            int SurfaceInfoStart = 0;
            do
            {
                int variableLength = 0;
                int SurfaceInfoCount = 0;

              i[0] =SurfaceInfoStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < SurfaceInfo.length) {
                    int blockLength = SurfaceInfo[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++SurfaceInfoCount;
                    }
                    else { break; }
                    i[0]++;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)SurfaceInfoCount;
                for (i[0] = SurfaceInfoStart; i[0] < SurfaceInfoStart + SurfaceInfoCount; i[0]++) { SurfaceInfo[i[0]].ToBytes(packet, length); }
                SurfaceInfoStart += SurfaceInfoCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                SurfaceInfoStart < SurfaceInfo.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
