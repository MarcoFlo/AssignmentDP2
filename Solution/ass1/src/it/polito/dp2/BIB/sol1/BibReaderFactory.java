package it.polito.dp2.BIB.sol1;

import it.polito.dp2.BIB.BibReader;
import it.polito.dp2.BIB.BibReaderException;
import it.polito.dp2.BIB.sol1.parser.BibReaderImpl;

/**
 * Concrete BibReaderFactory that instantiates the solution in this package
 *
 */
public class BibReaderFactory extends it.polito.dp2.BIB.BibReaderFactory {

	@Override
	public BibReader newBibReader() throws BibReaderException {	
		try {
			return new BibReaderImpl();
		} catch (BibReaderException e) {
			throw e;
		} catch (Exception e) {
			// last chance for all the runtime exceptions
			throw new BibReaderException(e.getMessage());
		}
	}
}


