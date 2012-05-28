package com.ngt.jopenmetaverse.shared.types;

import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;

    /// <summary>
    /// Same as Queue except Dequeue function blocks until there is an object to return.
    /// Note: This class does not need to be synchronized
    /// </summary>
    public class JOMBlockingQueue<T> extends ArrayBlockingQueue<T>
    {

		public JOMBlockingQueue(int capacity, boolean fair,
				Collection<? extends T> c) {
			super(capacity, fair, c);
		}

		public JOMBlockingQueue(int capacity, boolean fair) {
			super(capacity, fair);
		}

		public JOMBlockingQueue(int capacity) {
			super(capacity);
		}
    }
