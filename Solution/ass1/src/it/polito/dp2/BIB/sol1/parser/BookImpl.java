package it.polito.dp2.BIB.sol1.parser;


import it.polito.dp2.BIB.BookReader;
import it.polito.dp2.BIB.sol1.jaxb.BookType;

/**
 * Implementation of the BookReader interface which returns the bibliographic information
 * retrieved from a BookType object.
 *
 */
public class BookImpl extends ItemImpl implements BookReader {

	public BookImpl(BookType item, Items items) {
		super(item, items);
	}

	@Override
	public String getISBN() {
		return ((BookType) item).getISBN();
	}

	@Override
	public String getPublisher() {
		return ((BookType) item).getPublisher();
	}

	@Override
	public int getYear() {
		return ((BookType) item).getYear().getYear();
	}

}
