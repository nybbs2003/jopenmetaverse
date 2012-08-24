package com.ngt.jopenmetaverse.shared.sim;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class AssetManager {
    public AssetManager(GridClient client)
    {
    	//TODO Need to implement
    }
    
    //region Enums

    public enum EstateAssetTypeint
    {
        None (-1),
        Covenant (0);
        private int index;
        EstateAssetTypeint(int index)
		{
			this.index = index;
		}     

		public int getIndex()
		{
			return index;
		}

		private static final Map<Integer,EstateAssetTypeint> lookup  
		= new HashMap<Integer,EstateAssetTypeint>();

		static {
			for(EstateAssetTypeint s : EnumSet.allOf(EstateAssetTypeint.class))
				lookup.put(s.getIndex(), s);
		}

		public static EstateAssetTypeint get(Integer index)
		{
			return lookup.get(index);
		}   
    }

    /// <summary>
    /// 
    /// </summary>
    public enum StatusCode
    {
        /// <summary>OK</summary>
        OK(0),
        /// <summary>Transfer completed</summary>
        Done(1),
        /// <summary></summary>
        Skip(2),
        /// <summary></summary>
        Abort(3),
        /// <summary>Unknown error occurred</summary>
        Error(-1),
        /// <summary>Equivalent to a 404 error</summary>
        UnknownSource(-2),
        /// <summary>Client does not have permission for that resource</summary>
        InsufficientPermissions(-3),
        /// <summary>Unknown status</summary>
        Unknown(-4);
        private int index;
        StatusCode(int index)
		{
			this.index = index;
		}     

		public int getIndex()
		{
			return index;
		}

		private static final Map<Integer,StatusCode> lookup  
		= new HashMap<Integer,StatusCode>();

		static {
			for(StatusCode s : EnumSet.allOf(StatusCode.class))
				lookup.put(s.getIndex(), s);
		}

		public static StatusCode get(Integer index)
		{
			return lookup.get(index);
		}   
    }

    /// <summary>
    /// 
    /// </summary>
    public enum ChannelType
    {
        /// <summary></summary>
        Unknown(0),
        /// <summary>Unknown</summary>
        Misc (1),
        /// <summary>Virtually all asset transfers use this channel</summary>
        Asset (2);
        
        private int index;
        ChannelType(int index)
		{
			this.index = index;
		}     

		public int getIndex()
		{
			return index;
		}

		private static final Map<Integer,ChannelType> lookup  
		= new HashMap<Integer,ChannelType>();

		static {
			for(ChannelType s : EnumSet.allOf(ChannelType.class))
				lookup.put(s.getIndex(), s);
		}

		public static ChannelType get(Integer index)
		{
			return lookup.get(index);
		}   
    }

    /// <summary>
    /// 
    /// </summary>
    public enum SourceType
    {
        /// <summary></summary>
        Unknown(0),
        /// <summary>Asset from the asset server</summary>
        Asset (2),
        /// <summary>Inventory item</summary>
        SimInventoryItem (3),
        /// <summary>Estate asset, such as an estate covenant</summary>
        SimEstate (4);
        
        private int index;
        SourceType(int index)
		{
			this.index = index;
		}     

		public int getIndex()
		{
			return index;
		}

		private static final Map<Integer,SourceType> lookup  
		= new HashMap<Integer,SourceType>();

		static {
			for(SourceType s : EnumSet.allOf(SourceType.class))
				lookup.put(s.getIndex(), s);
		}

		public static SourceType get(Integer index)
		{
			return lookup.get(index);
		}   
    }

    /// <summary>
    /// 
    /// </summary>
    public enum TargetType 
    {
        /// <summary></summary>
        Unknown (0),
        /// <summary></summary>
        File (1),
        /// <summary></summary>
        VFile (2);
        
        private int index;
        TargetType(int index)
		{
			this.index = index;
		}     

		public int getIndex()
		{
			return index;
		}

		private static final Map<Integer,TargetType> lookup  
		= new HashMap<Integer,TargetType>();

		static {
			for(TargetType s : EnumSet.allOf(TargetType.class))
				lookup.put(s.getIndex(), s);
		}

		public static TargetType get(Integer index)
		{
			return lookup.get(index);
		}   
    }

    /// <summary>
    /// 
    /// </summary>
    public static enum ImageType 
    {
        /// <summary></summary>
        Normal((byte)0),
        /// <summary></summary>
        Baked ((byte)1);
		private byte index;
		ImageType(byte index)
		{
			this.index = index;
		}     

		public byte getIndex()
		{
			return index;
		}

		private static final Map<Byte,ImageType> lookup  = new HashMap<Byte,ImageType>();

		static {
			for(ImageType s : EnumSet.allOf(ImageType.class))
				lookup.put(s.getIndex(), s);
		}

		public static ImageType get(Byte index)
		{
			return lookup.get(index);
		}
    }

    /// <summary>
    /// Image file format
    /// </summary>
    public enum ImageCodec
    {
        Invalid((byte)0),
        RGB((byte)1),
        J2C((byte)2),
        BMP((byte)3),
        TGA((byte)4),
        JPEG((byte)5),
        DXT ((byte)6),
        PNG ((byte)7);
        
    	private byte index;
    	ImageCodec(byte index)
		{
			this.index = index;
		}     

		public byte getIndex()
		{
			return index;
		}

		private static final Map<Byte,ImageCodec> lookup  = new HashMap<Byte,ImageCodec>();

		static {
			for(ImageCodec s : EnumSet.allOf(ImageCodec.class))
				lookup.put(s.getIndex(), s);
		}

		public static ImageCodec get(Byte index)
		{
			return lookup.get(index);
		}
    }

    public static enum TransferError 
    {
        None(0),
        Failed(-1),
        AssetNotFound(-3),
        AssetNotFoundInDatabase(-4),
        InsufficientPermissions(-5),
        EOF(-39),
        CannotOpenFile(-42),
        FileNotFound(-43),
        FileIsEmpty(-44),
        TCPTimeout(-23016),
        CircuitGone(-23017);
        private int index;
        TransferError(int index)
		{
			this.index = index;
		}     

		public int getIndex()
		{
			return index;
		}

		private static final Map<Integer,TransferError> lookup  
		= new HashMap<Integer,TransferError>();

		static {
			for(TransferError s : EnumSet.allOf(TransferError.class))
				lookup.put(s.getIndex(), s);
		}

		public static TransferError get(Integer index)
		{
			return lookup.get(index);
		}
    }
    
   
    //endregion Enums   
    
}
