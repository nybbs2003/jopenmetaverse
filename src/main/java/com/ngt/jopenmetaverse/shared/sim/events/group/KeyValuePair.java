package com.ngt.jopenmetaverse.shared.sim.events.group;

public class KeyValuePair<T1, T2> {

	private T1 key;
	private T2 Value;
	
	public KeyValuePair(T1 key, T2 value) {
		super();
		this.key = key;
		Value = value;
	}
	public T1 getKey() {
		return key;
	}
	public void setKey(T1 key) {
		this.key = key;
	}
	public T2 getValue() {
		return Value;
	}
	public void setValue(T2 value) {
		Value = value;
	}
}
