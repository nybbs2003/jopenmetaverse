package com.ngt.jopenmetaverse.shared.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class PersistentStorage {
	public static void serializeObject(Object obj, String dstpath) throws FileNotFoundException, IOException
	{
		File f = new File(dstpath);
		ObjectOutput ObjOut = new ObjectOutputStream(new FileOutputStream(f));
		ObjOut.writeObject(obj);
		ObjOut.close();
	}

	public static Object deserializeObject(String srcpath) throws FileNotFoundException, IOException, ClassNotFoundException
	{
		File f = new File(srcpath);
		ObjectInputStream obj = new ObjectInputStream(new FileInputStream(f));
		Object object = obj.readObject();
		obj.close();
		return object;
	}
}
