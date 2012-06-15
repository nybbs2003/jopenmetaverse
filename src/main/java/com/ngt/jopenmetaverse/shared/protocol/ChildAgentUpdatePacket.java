package com.ngt.jopenmetaverse.shared.protocol;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.ngt.jopenmetaverse.shared.types.Quaternion;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.types.Vector3;
import com.ngt.jopenmetaverse.shared.util.Utils;


    public final class ChildAgentUpdatePacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public BigInteger RegionHandle;
            public long ViewerCircuitCode;
            public UUID AgentID;
            public UUID SessionID;
            public Vector3 AgentPos;
            public Vector3 AgentVel;
            public Vector3 Center;
            public Vector3 Size;
            public Vector3 AtAxis;
            public Vector3 LeftAxis;
            public Vector3 UpAxis;
            public boolean ChangedGrid;
            public float Far;
            public float Aspect;
            public byte[] Throttles;
            public long LocomotionState;
            public Quaternion HeadRotation;
            public Quaternion BodyRotation;
            public long ControlFlags;
            public float EnergyLevel;
            public byte GodLevel;
            public boolean AlwaysRun;
            public UUID PreyAgent;
            public byte AgentAccess;
            public byte[] AgentTextures;
            public UUID ActiveGroupID;

            @Override
			public int getLength()
            {
                                {
                    int length = 211;
                    if (Throttles != null) { length += Throttles.length; }
                    if (AgentTextures != null) { length += AgentTextures.length; }
                    return length;
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
                int length;
                try
                {
                    RegionHandle = Utils.bytesToULong(bytes, i[0]); i[0] += 8;
                    ViewerCircuitCode = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    AgentID.FromBytes(bytes, i[0]); i[0] += 16;
                    SessionID.FromBytes(bytes, i[0]); i[0] += 16;
                    AgentPos.fromBytes(bytes, i[0]); i[0] += 12;
                    AgentVel.fromBytes(bytes, i[0]); i[0] += 12;
                    Center.fromBytes(bytes, i[0]); i[0] += 12;
                    Size.fromBytes(bytes, i[0]); i[0] += 12;
                    AtAxis.fromBytes(bytes, i[0]); i[0] += 12;
                    LeftAxis.fromBytes(bytes, i[0]); i[0] += 12;
                    UpAxis.fromBytes(bytes, i[0]); i[0] += 12;
                    ChangedGrid = (bytes[i[0]++] != 0) ? true : false;
                    Far = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    Aspect = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    length = Utils.ubyteToInt(bytes[i[0]++]);
                    Throttles = new byte[length];
                    Utils.arraycopy(bytes, i[0], Throttles, 0, length); i[0] +=  length;
                    LocomotionState = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    HeadRotation.fromBytes(bytes, i[0], true); i[0] += 12;
                    BodyRotation.fromBytes(bytes, i[0], true); i[0] += 12;
                    ControlFlags = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                    EnergyLevel = Utils.bytesToFloat(bytes, i[0]); i[0] += 4;
                    GodLevel = (byte)bytes[i[0]++];
                    AlwaysRun = (bytes[i[0]++] != 0) ? true : false;
                    PreyAgent.FromBytes(bytes, i[0]); i[0] += 16;
                    AgentAccess = (byte)bytes[i[0]++];
                    length = Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    AgentTextures = new byte[length];
                    Utils.arraycopy(bytes, i[0], AgentTextures, 0, length); i[0] +=  length;
                    ActiveGroupID.FromBytes(bytes, i[0]); i[0] += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.ulongToBytes(RegionHandle, bytes, i[0]); i[0] += 8;
                Utils.uintToBytes(ViewerCircuitCode, bytes, i[0]); i[0] += 4;
                AgentID.ToBytes(bytes, i[0]); i[0] += 16;
                SessionID.ToBytes(bytes, i[0]); i[0] += 16;
                AgentPos.toBytes(bytes, i[0]); i[0] += 12;
                AgentVel.toBytes(bytes, i[0]); i[0] += 12;
                Center.toBytes(bytes, i[0]); i[0] += 12;
                Size.toBytes(bytes, i[0]); i[0] += 12;
                AtAxis.toBytes(bytes, i[0]); i[0] += 12;
                LeftAxis.toBytes(bytes, i[0]); i[0] += 12;
                UpAxis.toBytes(bytes, i[0]); i[0] += 12;
                bytes[i[0]++] = (byte)((ChangedGrid) ? 1 : 0);
                Utils.floatToBytes(Far, bytes, i[0]); i[0] += 4;
                Utils.floatToBytes(Aspect, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = (byte)Throttles.length;
                Utils.arraycopy(Throttles, 0, bytes, i[0], Throttles.length); i[0] +=  Throttles.length;
                Utils.uintToBytes(LocomotionState, bytes, i[0]); i[0] += 4;
                HeadRotation.toBytes(bytes, i[0]); i[0] += 12;
                BodyRotation.toBytes(bytes, i[0]); i[0] += 12;
                Utils.uintToBytes(ControlFlags, bytes, i[0]); i[0] += 4;
                Utils.floatToBytes(EnergyLevel, bytes, i[0]); i[0] += 4;
                bytes[i[0]++] = GodLevel;
                bytes[i[0]++] = (byte)((AlwaysRun) ? 1 : 0);
                PreyAgent.ToBytes(bytes, i[0]); i[0] += 16;
                bytes[i[0]++] = AgentAccess;
                bytes[i[0]++] = (byte)(AgentTextures.length % 256);
                bytes[i[0]++] = (byte)((AgentTextures.length >> 8) % 256);
                Utils.arraycopy(AgentTextures, 0, bytes, i[0], AgentTextures.length); i[0] +=  AgentTextures.length;
                ActiveGroupID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        /// <exclude/>
        public final class GroupDataBlock extends PacketBlock
        {
            public UUID GroupID;
            public BigInteger GroupPowers;
            public boolean AcceptNotices;

            @Override
			public int getLength()
            {
                                {
                    return 25;
                }
            }

            public GroupDataBlock() { }
            public GroupDataBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    GroupID.FromBytes(bytes, i[0]); i[0] += 16;
                    GroupPowers = Utils.bytesToULong(bytes, i[0]); i[0] += 8;
                    AcceptNotices = (bytes[i[0]++] != 0) ? true : false;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                GroupID.ToBytes(bytes, i[0]); i[0] += 16;
                Utils.ulongToBytes(GroupPowers, bytes, i[0]); i[0] += 8;
                bytes[i[0]++] = (byte)((AcceptNotices) ? 1 : 0);
            }

        }

        /// <exclude/>
        public final class AnimationDataBlock extends PacketBlock
        {
            public UUID Animation;
            public UUID ObjectID;

            @Override
			public int getLength()
            {
                                {
                    return 32;
                }
            }

            public AnimationDataBlock() { }
            public AnimationDataBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    Animation.FromBytes(bytes, i[0]); i[0] += 16;
                    ObjectID.FromBytes(bytes, i[0]); i[0] += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Animation.ToBytes(bytes, i[0]); i[0] += 16;
                ObjectID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        /// <exclude/>
        public final class GranterBlockBlock extends PacketBlock
        {
            public UUID GranterID;

            @Override
			public int getLength()
            {
                                {
                    return 16;
                }
            }

            public GranterBlockBlock() { }
            public GranterBlockBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    GranterID.FromBytes(bytes, i[0]); i[0] += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                GranterID.ToBytes(bytes, i[0]); i[0] += 16;
            }

        }

        /// <exclude/>
        public final class NVPairDataBlock extends PacketBlock
        {
            public byte[] NVPairs;

            @Override
			public int getLength()
            {
                                {
                    int length = 2;
                    if (NVPairs != null) { length += NVPairs.length; }
                    return length;
                }
            }

            public NVPairDataBlock() { }
            public NVPairDataBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    length = Utils.bytesToUInt16(bytes, i[0]); i[0] += 2;
                    NVPairs = new byte[length];
                    Utils.arraycopy(bytes, i[0], NVPairs, 0, length); i[0] +=  length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = (byte)(NVPairs.length % 256);
                bytes[i[0]++] = (byte)((NVPairs.length >> 8) % 256);
                Utils.arraycopy(NVPairs, 0, bytes, i[0], NVPairs.length); i[0] +=  NVPairs.length;
            }

        }

        /// <exclude/>
        public final class VisualParamBlock extends PacketBlock
        {
            public byte ParamValue;

            @Override
			public int getLength()
            {
                                {
                    return 1;
                }
            }

            public VisualParamBlock() { }
            public VisualParamBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ParamValue = (byte)bytes[i[0]++];
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = ParamValue;
            }

        }

        /// <exclude/>
        public final class AgentAccessBlock extends PacketBlock
        {
            public byte AgentLegacyAccess;
            public byte AgentMaxAccess;

            @Override
			public int getLength()
            {
                                {
                    return 2;
                }
            }

            public AgentAccessBlock() { }
            public AgentAccessBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    AgentLegacyAccess = (byte)bytes[i[0]++];
                    AgentMaxAccess = (byte)bytes[i[0]++];
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i[0]++] = AgentLegacyAccess;
                bytes[i[0]++] = AgentMaxAccess;
            }

        }

        /// <exclude/>
        public final class AgentInfoBlock extends PacketBlock
        {
            public long Flags;

            @Override
			public int getLength()
            {
                                {
                    return 4;
                }
            }

            public AgentInfoBlock() { }
            public AgentInfoBlock(byte[] bytes, int[] i) throws MalformedDataException
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    Flags = Utils.bytesToUInt(bytes, i[0]); i[0] += 4;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.uintToBytes(Flags, bytes, i[0]); i[0] += 4;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 14;
                length += AgentData.getLength();
                for (int j = 0; j < GroupData.getLength(); j++)
                    length += GroupData[j].getLength();
                for (int j = 0; j < AnimationData.length; j++)
                    length += AnimationData[j].getLength();
                for (int j = 0; j < GranterBlock.length; j++)
                    length += GranterBlock[j].getLength();
                for (int j = 0; j < NVPairData.length; j++)
                    length += NVPairData[j].getLength();
                for (int j = 0; j < VisualParam.length; j++)
                    length += VisualParam[j].getLength();
                for (int j = 0; j < AgentAccess.length; j++)
                    length += AgentAccess[j].getLength();
                for (int j = 0; j < AgentInfo.length; j++)
                    length += AgentInfo[j].getLength();
                return length;
            }
        }
        public AgentDataBlock AgentData;
        public GroupDataBlock[] GroupData;
        public AnimationDataBlock[] AnimationData;
        public GranterBlockBlock[] GranterBlock;
        public NVPairDataBlock[] NVPairData;
        public VisualParamBlock[] VisualParam;
        public AgentAccessBlock[] AgentAccess;
        public AgentInfoBlock[] AgentInfo;

        public ChildAgentUpdatePacket()
        {
            HasVariableBlocks = true;
            Type = PacketType.ChildAgentUpdate;
            this.header =  new Header();
            header.Frequency = PacketFrequency.High;
            header.ID = 25;
            header.Reliable = true;
            header.Zerocoded = true;
            AgentData = new AgentDataBlock();
            GroupData = null;
            AnimationData = null;
            GranterBlock = null;
            NVPairData = null;
            VisualParam = null;
            AgentAccess = null;
            AgentInfo = null;
        }

        public ChildAgentUpdatePacket(byte[] bytes, int[] i) throws MalformedDataException 
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
            if(GroupData == null || GroupData.getLength() != -1) {
                GroupData = new GroupDataBlock[count];
                for(int j = 0; j < count; j++)
                { GroupData[j] = new GroupDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { GroupData[j].FromBytes(bytes, i); }
            count = Utils.ubyteToInt(bytes[i[0]++]);
            if(AnimationData == null || AnimationData.length != -1) {
                AnimationData = new AnimationDataBlock[count];
                for(int j = 0; j < count; j++)
                { AnimationData[j] = new AnimationDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { AnimationData[j].FromBytes(bytes, i); }
            count = Utils.ubyteToInt(bytes[i[0]++]);
            if(GranterBlock == null || GranterBlock.length != -1) {
                GranterBlock = new GranterBlockBlock[count];
                for(int j = 0; j < count; j++)
                { GranterBlock[j] = new GranterBlockBlock(); }
            }
            for (int j = 0; j < count; j++)
            { GranterBlock[j].FromBytes(bytes, i); }
            count = Utils.ubyteToInt(bytes[i[0]++]);
            if(NVPairData == null || NVPairData.length != -1) {
                NVPairData = new NVPairDataBlock[count];
                for(int j = 0; j < count; j++)
                { NVPairData[j] = new NVPairDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { NVPairData[j].FromBytes(bytes, i); }
            count = Utils.ubyteToInt(bytes[i[0]++]);
            if(VisualParam == null || VisualParam.length != -1) {
                VisualParam = new VisualParamBlock[count];
                for(int j = 0; j < count; j++)
                { VisualParam[j] = new VisualParamBlock(); }
            }
            for (int j = 0; j < count; j++)
            { VisualParam[j].FromBytes(bytes, i); }
            count = Utils.ubyteToInt(bytes[i[0]++]);
            if(AgentAccess == null || AgentAccess.length != -1) {
                AgentAccess = new AgentAccessBlock[count];
                for(int j = 0; j < count; j++)
                { AgentAccess[j] = new AgentAccessBlock(); }
            }
            for (int j = 0; j < count; j++)
            { AgentAccess[j].FromBytes(bytes, i); }
            count = Utils.ubyteToInt(bytes[i[0]++]);
            if(AgentInfo == null || AgentInfo.length != -1) {
                AgentInfo = new AgentInfoBlock[count];
                for(int j = 0; j < count; j++)
                { AgentInfo[j] = new AgentInfoBlock(); }
            }
            for (int j = 0; j < count; j++)
            { AgentInfo[j].FromBytes(bytes, i); }
        }

        public ChildAgentUpdatePacket(Header head, byte[] bytes, int[] i) throws MalformedDataException
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
            if(GroupData == null || GroupData.getLength() != count) {
                GroupData = new GroupDataBlock[count];
                for(int j = 0; j < count; j++)
                { GroupData[j] = new GroupDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { GroupData[j].FromBytes(bytes, i); }
            count = Utils.ubyteToInt(bytes[i[0]++]);
            if(AnimationData == null || AnimationData.length != count) {
                AnimationData = new AnimationDataBlock[count];
                for(int j = 0; j < count; j++)
                { AnimationData[j] = new AnimationDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { AnimationData[j].FromBytes(bytes, i); }
            count = Utils.ubyteToInt(bytes[i[0]++]);
            if(GranterBlock == null || GranterBlock.length != count) {
                GranterBlock = new GranterBlockBlock[count];
                for(int j = 0; j < count; j++)
                { GranterBlock[j] = new GranterBlockBlock(); }
            }
            for (int j = 0; j < count; j++)
            { GranterBlock[j].FromBytes(bytes, i); }
            count = Utils.ubyteToInt(bytes[i[0]++]);
            if(NVPairData == null || NVPairData.length != count) {
                NVPairData = new NVPairDataBlock[count];
                for(int j = 0; j < count; j++)
                { NVPairData[j] = new NVPairDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { NVPairData[j].FromBytes(bytes, i); }
            count = Utils.ubyteToInt(bytes[i[0]++]);
            if(VisualParam == null || VisualParam.length != count) {
                VisualParam = new VisualParamBlock[count];
                for(int j = 0; j < count; j++)
                { VisualParam[j] = new VisualParamBlock(); }
            }
            for (int j = 0; j < count; j++)
            { VisualParam[j].FromBytes(bytes, i); }
            count = Utils.ubyteToInt(bytes[i[0]++]);
            if(AgentAccess == null || AgentAccess.length != count) {
                AgentAccess = new AgentAccessBlock[count];
                for(int j = 0; j < count; j++)
                { AgentAccess[j] = new AgentAccessBlock(); }
            }
            for (int j = 0; j < count; j++)
            { AgentAccess[j].FromBytes(bytes, i); }
            count = Utils.ubyteToInt(bytes[i[0]++]);
            if(AgentInfo == null || AgentInfo.length != count) {
                AgentInfo = new AgentInfoBlock[count];
                for(int j = 0; j < count; j++)
                { AgentInfo[j] = new AgentInfoBlock(); }
            }
            for (int j = 0; j < count; j++)
            { AgentInfo[j].FromBytes(bytes, i); }
        }

        @Override
			public byte[] ToBytes()
        {
            int length = 7;
            length += AgentData.getLength();
            length++;
            for (int j = 0; j < GroupData.getLength(); j++) { length += GroupData[j].getLength(); }
            length++;
            for (int j = 0; j < AnimationData.length; j++) { length += AnimationData[j].getLength(); }
            length++;
            for (int j = 0; j < GranterBlock.length; j++) { length += GranterBlock[j].getLength(); }
            length++;
            for (int j = 0; j < NVPairData.length; j++) { length += NVPairData[j].getLength(); }
            length++;
            for (int j = 0; j < VisualParam.length; j++) { length += VisualParam[j].getLength(); }
            length++;
            for (int j = 0; j < AgentAccess.length; j++) { length += AgentAccess[j].getLength(); }
            length++;
            for (int j = 0; j < AgentInfo.length; j++) { length += AgentInfo[j].getLength(); }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int[] i = new int[]{0};
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            bytes[i[0]++] = (byte)GroupData.getLength();
            for (int j = 0; j < GroupData.getLength(); j++) { GroupData[j].ToBytes(bytes, i); }
            bytes[i[0]++] = (byte)AnimationData.length;
            for (int j = 0; j < AnimationData.length; j++) { AnimationData[j].ToBytes(bytes, i); }
            bytes[i[0]++] = (byte)GranterBlock.length;
            for (int j = 0; j < GranterBlock.length; j++) { GranterBlock[j].ToBytes(bytes, i); }
            bytes[i[0]++] = (byte)NVPairData.length;
            for (int j = 0; j < NVPairData.length; j++) { NVPairData[j].ToBytes(bytes, i); }
            bytes[i[0]++] = (byte)VisualParam.length;
            for (int j = 0; j < VisualParam.length; j++) { VisualParam[j].ToBytes(bytes, i); }
            bytes[i[0]++] = (byte)AgentAccess.length;
            for (int j = 0; j < AgentAccess.length; j++) { AgentAccess[j].ToBytes(bytes, i); }
            bytes[i[0]++] = (byte)AgentInfo.length;
            for (int j = 0; j < AgentInfo.length; j++) { AgentInfo[j].ToBytes(bytes, i); }
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            List<byte[]> packets = new ArrayList<byte[]>();
            int[] i = new int[]{0};
            int fixedLength = 7;

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
            fixedLength += 7;

            int GroupDataStart = 0;
            int AnimationDataStart = 0;
            int GranterBlockStart = 0;
            int NVPairDataStart = 0;
            int VisualParamStart = 0;
            int AgentAccessStart = 0;
            int AgentInfoStart = 0;
            do
            {
                int variableLength = 0;
                int GroupDataCount = 0;
                int AnimationDataCount = 0;
                int GranterBlockCount = 0;
                int NVPairDataCount = 0;
                int VisualParamCount = 0;
                int AgentAccessCount = 0;
                int AgentInfoCount = 0;

              i[0] =GroupDataStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < GroupData.getLength()) {
                    int blockLength = GroupData[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++GroupDataCount;
                    }
                    else { break; }
                    i[0]++;
                }

              i[0] =AnimationDataStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < AnimationData.length) {
                    int blockLength = AnimationData[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++AnimationDataCount;
                    }
                    else { break; }
                    i[0]++;
                }

              i[0] =GranterBlockStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < GranterBlock.length) {
                    int blockLength = GranterBlock[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++GranterBlockCount;
                    }
                    else { break; }
                    i[0]++;
                }

              i[0] =NVPairDataStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < NVPairData.length) {
                    int blockLength = NVPairData[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++NVPairDataCount;
                    }
                    else { break; }
                    i[0]++;
                }

              i[0] =VisualParamStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < VisualParam.length) {
                    int blockLength = VisualParam[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++VisualParamCount;
                    }
                    else { break; }
                    i[0]++;
                }

              i[0] =AgentAccessStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < AgentAccess.length) {
                    int blockLength = AgentAccess[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++AgentAccessCount;
                    }
                    else { break; }
                    i[0]++;
                }

              i[0] =AgentInfoStart;
                while (fixedLength + variableLength + acksLength[0] < Packet.MTU && i[0] < AgentInfo.length) {
                    int blockLength = AgentInfo[i[0]].getLength();
                    if (fixedLength + variableLength + blockLength + acksLength[0] <= MTU) {
                        variableLength += blockLength;
                        ++AgentInfoCount;
                    }
                    else { break; }
                    i[0]++;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength[0]];
                int[] length = new int[] {fixedBytes.length};
                Utils.arraycopy(fixedBytes, 0, packet, 0, length[0]);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length[0]++] = (byte)GroupDataCount;
                for (i[0] = GroupDataStart; i[0] < GroupDataStart + GroupDataCount; i[0]++) { GroupData[i[0]].ToBytes(packet, length); }
                GroupDataStart += GroupDataCount;

                packet[length[0]++] = (byte)AnimationDataCount;
                for (i[0] = AnimationDataStart; i[0] < AnimationDataStart + AnimationDataCount; i[0]++) { AnimationData[i[0]].ToBytes(packet, length); }
                AnimationDataStart += AnimationDataCount;

                packet[length[0]++] = (byte)GranterBlockCount;
                for (i[0] = GranterBlockStart; i[0] < GranterBlockStart + GranterBlockCount; i[0]++) { GranterBlock[i[0]].ToBytes(packet, length); }
                GranterBlockStart += GranterBlockCount;

                packet[length[0]++] = (byte)NVPairDataCount;
                for (i[0] = NVPairDataStart; i[0] < NVPairDataStart + NVPairDataCount; i[0]++) { NVPairData[i[0]].ToBytes(packet, length); }
                NVPairDataStart += NVPairDataCount;

                packet[length[0]++] = (byte)VisualParamCount;
                for (i[0] = VisualParamStart; i[0] < VisualParamStart + VisualParamCount; i[0]++) { VisualParam[i[0]].ToBytes(packet, length); }
                VisualParamStart += VisualParamCount;

                packet[length[0]++] = (byte)AgentAccessCount;
                for (i[0] = AgentAccessStart; i[0] < AgentAccessStart + AgentAccessCount; i[0]++) { AgentAccess[i[0]].ToBytes(packet, length); }
                AgentAccessStart += AgentAccessCount;

                packet[length[0]++] = (byte)AgentInfoCount;
                for (i[0] = AgentInfoStart; i[0] < AgentInfoStart + AgentInfoCount; i[0]++) { AgentInfo[i[0]].ToBytes(packet, length); }
                AgentInfoStart += AgentInfoCount;

                if (acksLength[0] > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length[0], acksLength[0]);
                    acksLength[0] = 0;
                }

                packets.add(packet);
            } while (
                GroupDataStart < GroupData.getLength() ||
                AnimationDataStart < AnimationData.length ||
                GranterBlockStart < GranterBlock.length ||
                NVPairDataStart < NVPairData.length ||
                VisualParamStart < VisualParam.length ||
                AgentAccessStart < AgentAccess.length ||
                AgentInfoStart < AgentInfo.length);

            return packets.toArray(new byte[0][0]);
        }
    }

    /// <exclude/>
