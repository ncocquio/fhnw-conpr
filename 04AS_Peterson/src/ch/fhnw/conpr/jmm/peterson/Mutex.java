package ch.fhnw.conpr.jmm.peterson;

public interface Mutex {
	void lock();
	void unlock();
}
