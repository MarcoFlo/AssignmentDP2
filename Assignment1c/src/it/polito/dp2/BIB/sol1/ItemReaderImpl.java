package it.polito.dp2.BIB.sol1;

import java.util.Set;

public class ItemReaderImpl implements it.polito.dp2.BIB.ItemReader {
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
    public Set<it.polito.dp2.BIB.ItemReader> getCitingItems() {
        return null;
    }
}
