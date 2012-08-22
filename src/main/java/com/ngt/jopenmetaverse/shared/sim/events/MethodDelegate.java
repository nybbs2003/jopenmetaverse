package com.ngt.jopenmetaverse.shared.sim.events;

public interface MethodDelegate<T, E> {
	public T execute(E e);
}
