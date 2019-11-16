package it.polito.dp2.BIB.sol1;

import it.polito.dp2.BIB.ItemReader;

import java.util.Set;

public class BookReaderImpl implements it.polito.dp2.BIB.BookReader {
    @Override
    public String getISBN() {
        return null;
    }

    @Override
    public String getPublisher() {
        return null;
    }

    @Override
    public int getYear() {
        return 0;
    }

    @Override
    public String[] getAuthors() {
        return new String[0];
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSubtitle() {
        return null;
    }

    @Override
    public Set<ItemReader> getCitingItems() {
        return null;
    }
}
