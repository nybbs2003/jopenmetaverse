package com.ngt.jopenmetaverse.shared.protocol;


    public final class ChildAgentUpdatePacket extends Packet
    {
        /// <exclude/>
        public final class AgentDataBlock extends PacketBlock
        {
            public ulong RegionHandle;
            public uint ViewerCircuitCode;
            public UUID AgentID;
            public UUID SessionID;
            public Vector3 AgentPos;
            public Vector3 AgentVel;
            public Vector3 Center;
            public Vector3 Size;
            public Vector3 AtAxis;
            public Vector3 LeftAxis;
            public Vector3 UpAxis;
            public bool ChangedGrid;
            public float Far;
            public float Aspect;
            public byte[] Throttles;
            public uint LocomotionState;
            public Quaternion HeadRotation;
            public Quaternion BodyRotation;
            public uint ControlFlags;
            public float EnergyLevel;
            public byte GodLevel;
            public bool AlwaysRun;
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
                    RegionHandle = (ulong)((ulong)bytes[i++] + ((ulong)bytes[i++] << 8) + ((ulong)bytes[i++] << 16) + ((ulong)bytes[i++] << 24) + ((ulong)bytes[i++] << 32) + ((ulong)bytes[i++] << 40) + ((ulong)bytes[i++] << 48) + ((ulong)bytes[i++] << 56));
                    ViewerCircuitCode = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    AgentID.FromBytes(bytes, i); i += 16;
                    SessionID.FromBytes(bytes, i[0]); i[0] += 16;
                    AgentPos.FromBytes(bytes, i); i += 12;
                    AgentVel.FromBytes(bytes, i); i += 12;
                    Center.FromBytes(bytes, i); i += 12;
                    Size.FromBytes(bytes, i); i += 12;
                    AtAxis.FromBytes(bytes, i); i += 12;
                    LeftAxis.FromBytes(bytes, i); i += 12;
                    UpAxis.FromBytes(bytes, i); i += 12;
                    ChangedGrid = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    Far = Utils.BytesToFloat(bytes, i); i += 4;
                    Aspect = Utils.BytesToFloat(bytes, i); i += 4;
                    length = bytes[i++];
                    Throttles = new byte[length];
                    Utils.arraycopy(bytes, i, Throttles, 0, length); i += length;
                    LocomotionState = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    HeadRotation.FromBytes(bytes, i, true); i += 12;
                    BodyRotation.FromBytes(bytes, i, true); i += 12;
                    ControlFlags = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                    EnergyLevel = Utils.BytesToFloat(bytes, i); i += 4;
                    GodLevel = (byte)bytes[i++];
                    AlwaysRun = (bytes[i++] != 0) ? (bool)true : (bool)false;
                    PreyAgent.FromBytes(bytes, i); i += 16;
                    AgentAccess = (byte)bytes[i++];
                    length = (bytes[i++] + (bytes[i++] << 8));
                    AgentTextures = new byte[length];
                    Utils.arraycopy(bytes, i, AgentTextures, 0, length); i += length;
                    ActiveGroupID.FromBytes(bytes, i); i += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UInt64ToBytes(RegionHandle, bytes, i); i += 8;
                Utils.UIntToBytes(ViewerCircuitCode, bytes, i); i += 4;
                AgentID.ToBytes(bytes, i[0]); i[0] += 16;
                SessionID.ToBytes(bytes, i[0]); i[0] += 16;
                AgentPos.ToBytes(bytes, i); i += 12;
                AgentVel.ToBytes(bytes, i); i += 12;
                Center.ToBytes(bytes, i); i += 12;
                Size.ToBytes(bytes, i); i += 12;
                AtAxis.ToBytes(bytes, i); i += 12;
                LeftAxis.ToBytes(bytes, i); i += 12;
                UpAxis.ToBytes(bytes, i); i += 12;
                bytes[i++] = (byte)((ChangedGrid) ? 1 : 0);
                Utils.FloatToBytes(Far, bytes, i); i += 4;
                Utils.FloatToBytes(Aspect, bytes, i); i += 4;
                bytes[i++] = (byte)Throttles.length;
                Utils.arraycopy(Throttles, 0, bytes, i, Throttles.length); i += Throttles.length;
                Utils.UIntToBytes(LocomotionState, bytes, i); i += 4;
                HeadRotation.ToBytes(bytes, i); i += 12;
                BodyRotation.ToBytes(bytes, i); i += 12;
                Utils.UIntToBytes(ControlFlags, bytes, i); i += 4;
                Utils.FloatToBytes(EnergyLevel, bytes, i); i += 4;
                bytes[i++] = GodLevel;
                bytes[i++] = (byte)((AlwaysRun) ? 1 : 0);
                PreyAgent.ToBytes(bytes, i); i += 16;
                bytes[i++] = AgentAccess;
                bytes[i++] = (byte)(AgentTextures.length % 256);
                bytes[i++] = (byte)((AgentTextures.length >> 8) % 256);
                Utils.arraycopy(AgentTextures, 0, bytes, i, AgentTextures.length); i += AgentTextures.length;
                ActiveGroupID.ToBytes(bytes, i); i += 16;
            }

        }

        /// <exclude/>
        public final class GroupDataBlock extends PacketBlock
        {
            public UUID GroupID;
            public ulong GroupPowers;
            public bool AcceptNotices;

            @Override
			public int getLength()
            {
                                {
                    return 25;
                }
            }

            public GroupDataBlock() { }
            public GroupDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    GroupID.FromBytes(bytes, i); i += 16;
                    GroupPowers = (ulong)((ulong)bytes[i++] + ((ulong)bytes[i++] << 8) + ((ulong)bytes[i++] << 16) + ((ulong)bytes[i++] << 24) + ((ulong)bytes[i++] << 32) + ((ulong)bytes[i++] << 40) + ((ulong)bytes[i++] << 48) + ((ulong)bytes[i++] << 56));
                    AcceptNotices = (bytes[i++] != 0) ? (bool)true : (bool)false;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                GroupID.ToBytes(bytes, i); i += 16;
                Utils.UInt64ToBytes(GroupPowers, bytes, i); i += 8;
                bytes[i++] = (byte)((AcceptNotices) ? 1 : 0);
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
            public AnimationDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    Animation.FromBytes(bytes, i); i += 16;
                    ObjectID.FromBytes(bytes, i); i += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Animation.ToBytes(bytes, i); i += 16;
                ObjectID.ToBytes(bytes, i); i += 16;
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
            public GranterBlockBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    GranterID.FromBytes(bytes, i); i += 16;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                GranterID.ToBytes(bytes, i); i += 16;
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
            public NVPairDataBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                int length;
                try
                {
                    length = (bytes[i++] + (bytes[i++] << 8));
                    NVPairs = new byte[length];
                    Utils.arraycopy(bytes, i, NVPairs, 0, length); i += length;
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i++] = (byte)(NVPairs.length % 256);
                bytes[i++] = (byte)((NVPairs.length >> 8) % 256);
                Utils.arraycopy(NVPairs, 0, bytes, i, NVPairs.length); i += NVPairs.length;
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
            public VisualParamBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    ParamValue = (byte)bytes[i++];
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i++] = ParamValue;
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
            public AgentAccessBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    AgentLegacyAccess = (byte)bytes[i++];
                    AgentMaxAccess = (byte)bytes[i++];
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                bytes[i++] = AgentLegacyAccess;
                bytes[i++] = AgentMaxAccess;
            }

        }

        /// <exclude/>
        public final class AgentInfoBlock extends PacketBlock
        {
            public uint Flags;

            @Override
			public int getLength()
            {
                                {
                    return 4;
                }
            }

            public AgentInfoBlock() { }
            public AgentInfoBlock(byte[] bytes, int[] i)
            {
                FromBytes(bytes, i);
            }

            @Override
			public void FromBytes(byte[] bytes, int[] i) throws MalformedDataException
            {
                try
                {
                    Flags = (uint)(bytes[i++] + (bytes[i++] << 8) + (bytes[i++] << 16) + (bytes[i++] << 24));
                }
                catch (Exception e)
                {
                    throw new MalformedDataException();
                }
            }

            @Override
			public void ToBytes(byte[] bytes, int[] i)
            {
                Utils.UIntToBytes(Flags, bytes, i); i += 4;
            }

        }

        @Override
			public int getLength()
        {
                        {
                int length = 14;
                length += AgentData.getLength();
                for (int j = 0; j < GroupData.length; j++)
                    length += GroupData[j].length;
                for (int j = 0; j < AnimationData.length; j++)
                    length += AnimationData[j].length;
                for (int j = 0; j < GranterBlock.length; j++)
                    length += GranterBlock[j].length;
                for (int j = 0; j < NVPairData.length; j++)
                    length += NVPairData[j].length;
                for (int j = 0; j < VisualParam.length; j++)
                    length += VisualParam[j].length;
                for (int j = 0; j < AgentAccess.length; j++)
                    length += AgentAccess[j].length;
                for (int j = 0; j < AgentInfo.length; j++)
                    length += AgentInfo[j].length;
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

        public ChildAgentUpdatePacket(byte[] bytes, int[] i) 
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
            AgentData.FromBytes(bytes, i);
            int count = (int)bytes[i++];
            if(GroupData == null || GroupData.length != -1) {
                GroupData = new GroupDataBlock[count];
                for(int j = 0; j < count; j++)
                { GroupData[j] = new GroupDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { GroupData[j].FromBytes(bytes, i); }
            count = (int)bytes[i++];
            if(AnimationData == null || AnimationData.length != -1) {
                AnimationData = new AnimationDataBlock[count];
                for(int j = 0; j < count; j++)
                { AnimationData[j] = new AnimationDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { AnimationData[j].FromBytes(bytes, i); }
            count = (int)bytes[i++];
            if(GranterBlock == null || GranterBlock.length != -1) {
                GranterBlock = new GranterBlockBlock[count];
                for(int j = 0; j < count; j++)
                { GranterBlock[j] = new GranterBlockBlock(); }
            }
            for (int j = 0; j < count; j++)
            { GranterBlock[j].FromBytes(bytes, i); }
            count = (int)bytes[i++];
            if(NVPairData == null || NVPairData.length != -1) {
                NVPairData = new NVPairDataBlock[count];
                for(int j = 0; j < count; j++)
                { NVPairData[j] = new NVPairDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { NVPairData[j].FromBytes(bytes, i); }
            count = (int)bytes[i++];
            if(VisualParam == null || VisualParam.length != -1) {
                VisualParam = new VisualParamBlock[count];
                for(int j = 0; j < count; j++)
                { VisualParam[j] = new VisualParamBlock(); }
            }
            for (int j = 0; j < count; j++)
            { VisualParam[j].FromBytes(bytes, i); }
            count = (int)bytes[i++];
            if(AgentAccess == null || AgentAccess.length != -1) {
                AgentAccess = new AgentAccessBlock[count];
                for(int j = 0; j < count; j++)
                { AgentAccess[j] = new AgentAccessBlock(); }
            }
            for (int j = 0; j < count; j++)
            { AgentAccess[j].FromBytes(bytes, i); }
            count = (int)bytes[i++];
            if(AgentInfo == null || AgentInfo.length != -1) {
                AgentInfo = new AgentInfoBlock[count];
                for(int j = 0; j < count; j++)
                { AgentInfo[j] = new AgentInfoBlock(); }
            }
            for (int j = 0; j < count; j++)
            { AgentInfo[j].FromBytes(bytes, i); }
        }

        public ChildAgentUpdatePacket(Header head, byte[] bytes, int[] i)
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
            int count = (int)bytes[i++];
            if(GroupData == null || GroupData.length != count) {
                GroupData = new GroupDataBlock[count];
                for(int j = 0; j < count; j++)
                { GroupData[j] = new GroupDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { GroupData[j].FromBytes(bytes, i); }
            count = (int)bytes[i++];
            if(AnimationData == null || AnimationData.length != count) {
                AnimationData = new AnimationDataBlock[count];
                for(int j = 0; j < count; j++)
                { AnimationData[j] = new AnimationDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { AnimationData[j].FromBytes(bytes, i); }
            count = (int)bytes[i++];
            if(GranterBlock == null || GranterBlock.length != count) {
                GranterBlock = new GranterBlockBlock[count];
                for(int j = 0; j < count; j++)
                { GranterBlock[j] = new GranterBlockBlock(); }
            }
            for (int j = 0; j < count; j++)
            { GranterBlock[j].FromBytes(bytes, i); }
            count = (int)bytes[i++];
            if(NVPairData == null || NVPairData.length != count) {
                NVPairData = new NVPairDataBlock[count];
                for(int j = 0; j < count; j++)
                { NVPairData[j] = new NVPairDataBlock(); }
            }
            for (int j = 0; j < count; j++)
            { NVPairData[j].FromBytes(bytes, i); }
            count = (int)bytes[i++];
            if(VisualParam == null || VisualParam.length != count) {
                VisualParam = new VisualParamBlock[count];
                for(int j = 0; j < count; j++)
                { VisualParam[j] = new VisualParamBlock(); }
            }
            for (int j = 0; j < count; j++)
            { VisualParam[j].FromBytes(bytes, i); }
            count = (int)bytes[i++];
            if(AgentAccess == null || AgentAccess.length != count) {
                AgentAccess = new AgentAccessBlock[count];
                for(int j = 0; j < count; j++)
                { AgentAccess[j] = new AgentAccessBlock(); }
            }
            for (int j = 0; j < count; j++)
            { AgentAccess[j].FromBytes(bytes, i); }
            count = (int)bytes[i++];
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
            for (int j = 0; j < GroupData.length; j++) { length += GroupData[j].length; }
            length++;
            for (int j = 0; j < AnimationData.length; j++) { length += AnimationData[j].length; }
            length++;
            for (int j = 0; j < GranterBlock.length; j++) { length += GranterBlock[j].length; }
            length++;
            for (int j = 0; j < NVPairData.length; j++) { length += NVPairData[j].length; }
            length++;
            for (int j = 0; j < VisualParam.length; j++) { length += VisualParam[j].length; }
            length++;
            for (int j = 0; j < AgentAccess.length; j++) { length += AgentAccess[j].length; }
            length++;
            for (int j = 0; j < AgentInfo.length; j++) { length += AgentInfo[j].length; }
            if (header.AckList != null && header.AckList.length > 0) { length += header.AckList.length * 4 + 1; }
            byte[] bytes = new byte[length];
            int i = 0;
            header.ToBytes(bytes, i);
            AgentData.ToBytes(bytes, i);
            bytes[i++] = (byte)GroupData.length;
            for (int j = 0; j < GroupData.length; j++) { GroupData[j].ToBytes(bytes, i); }
            bytes[i++] = (byte)AnimationData.length;
            for (int j = 0; j < AnimationData.length; j++) { AnimationData[j].ToBytes(bytes, i); }
            bytes[i++] = (byte)GranterBlock.length;
            for (int j = 0; j < GranterBlock.length; j++) { GranterBlock[j].ToBytes(bytes, i); }
            bytes[i++] = (byte)NVPairData.length;
            for (int j = 0; j < NVPairData.length; j++) { NVPairData[j].ToBytes(bytes, i); }
            bytes[i++] = (byte)VisualParam.length;
            for (int j = 0; j < VisualParam.length; j++) { VisualParam[j].ToBytes(bytes, i); }
            bytes[i++] = (byte)AgentAccess.length;
            for (int j = 0; j < AgentAccess.length; j++) { AgentAccess[j].ToBytes(bytes, i); }
            bytes[i++] = (byte)AgentInfo.length;
            for (int j = 0; j < AgentInfo.length; j++) { AgentInfo[j].ToBytes(bytes, i); }
            if (header.AckList != null && header.AckList.length > 0) { header.AcksToBytes(bytes, i); }
            return bytes;
        }

        @Override
			public byte[][] ToBytesMultiple()
        {
            List<byte[]> packets = new ArrayList<byte[]>();
            int i = 0;
            int fixedLength = 7;

            byte[] ackBytes = null;
            int acksLength = 0;
            if (header.AckList != null && header.AckList.length > 0) {
                header.AppendedAcks = true;
                ackBytes = new byte[header.AckList.length * 4 + 1];
                header.AcksToBytes(ackBytes, ref acksLength);
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

                i = GroupDataStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < GroupData.length) {
                    int blockLength = GroupData[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++GroupDataCount;
                    }
                    else { break; }
                    ++i;
                }

                i = AnimationDataStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < AnimationData.length) {
                    int blockLength = AnimationData[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++AnimationDataCount;
                    }
                    else { break; }
                    ++i;
                }

                i = GranterBlockStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < GranterBlock.length) {
                    int blockLength = GranterBlock[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++GranterBlockCount;
                    }
                    else { break; }
                    ++i;
                }

                i = NVPairDataStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < NVPairData.length) {
                    int blockLength = NVPairData[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++NVPairDataCount;
                    }
                    else { break; }
                    ++i;
                }

                i = VisualParamStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < VisualParam.length) {
                    int blockLength = VisualParam[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++VisualParamCount;
                    }
                    else { break; }
                    ++i;
                }

                i = AgentAccessStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < AgentAccess.length) {
                    int blockLength = AgentAccess[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++AgentAccessCount;
                    }
                    else { break; }
                    ++i;
                }

                i = AgentInfoStart;
                while (fixedLength + variableLength + acksLength < Packet.MTU && i < AgentInfo.length) {
                    int blockLength = AgentInfo[i].length;
                    if (fixedLength + variableLength + blockLength + acksLength <= MTU) {
                        variableLength += blockLength;
                        ++AgentInfoCount;
                    }
                    else { break; }
                    ++i;
                }

                byte[] packet = new byte[fixedLength + variableLength + acksLength];
                int length = fixedBytes.length;
                Utils.arraycopy(fixedBytes, 0, packet, 0, length);
                if (packets.size() > 0) { packet[0] = (byte)(packet[0] & ~0x10); }

                packet[length++] = (byte)GroupDataCount;
                for (i = GroupDataStart; i < GroupDataStart + GroupDataCount; i++) { GroupData[i].ToBytes(packet, ref length); }
                GroupDataStart += GroupDataCount;

                packet[length++] = (byte)AnimationDataCount;
                for (i = AnimationDataStart; i < AnimationDataStart + AnimationDataCount; i++) { AnimationData[i].ToBytes(packet, ref length); }
                AnimationDataStart += AnimationDataCount;

                packet[length++] = (byte)GranterBlockCount;
                for (i = GranterBlockStart; i < GranterBlockStart + GranterBlockCount; i++) { GranterBlock[i].ToBytes(packet, ref length); }
                GranterBlockStart += GranterBlockCount;

                packet[length++] = (byte)NVPairDataCount;
                for (i = NVPairDataStart; i < NVPairDataStart + NVPairDataCount; i++) { NVPairData[i].ToBytes(packet, ref length); }
                NVPairDataStart += NVPairDataCount;

                packet[length++] = (byte)VisualParamCount;
                for (i = VisualParamStart; i < VisualParamStart + VisualParamCount; i++) { VisualParam[i].ToBytes(packet, ref length); }
                VisualParamStart += VisualParamCount;

                packet[length++] = (byte)AgentAccessCount;
                for (i = AgentAccessStart; i < AgentAccessStart + AgentAccessCount; i++) { AgentAccess[i].ToBytes(packet, ref length); }
                AgentAccessStart += AgentAccessCount;

                packet[length++] = (byte)AgentInfoCount;
                for (i = AgentInfoStart; i < AgentInfoStart + AgentInfoCount; i++) { AgentInfo[i].ToBytes(packet, ref length); }
                AgentInfoStart += AgentInfoCount;

                if (acksLength > 0) {
                    Utils.arraycopy(ackBytes, 0, packet, length, acksLength);
                    acksLength = 0;
                }

                packets.add(packet);
            } while (
                GroupDataStart < GroupData.length ||
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
