package com.ngt.jopenmetaverse.shared.sim;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class AssetManager {
    public AssetManager(GridClient client)
    {
    	//TODO Need to implement
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
    
}
