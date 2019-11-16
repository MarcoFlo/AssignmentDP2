package it.polito.dp2.BIB.sol1;

import it.polito.dp2.BIB.BibReaderException;

public class BibReaderFactory extends it.polito.dp2.BIB.BibReaderFactory {

    @Override
    public BibReaderImpl newBibReader() throws BibReaderException {
        try {
            return new BibReaderImpl();
        } catch (Exception e) {
            throw new BibReaderException(e);
        }
    }
}