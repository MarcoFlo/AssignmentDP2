package it.polito.dp2.BIB.sol2;

/**
 * A class that manages a counter in order to generate
 * unique integer ids.
 *
 */
public class IdCounter {
	private long nextId = 0;
	
	long getNextId() {
		return nextId++;
	}
}
