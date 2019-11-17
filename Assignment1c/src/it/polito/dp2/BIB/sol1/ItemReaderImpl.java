package it.polito.dp2.BIB.sol1;

import it.polito.dp2.BIB.ItemReader;

import java.util.Set;

public class ItemReaderImpl implements ItemReader {
    private ItemReader itemReader;

    public ItemReaderImpl(ItemReaderImpl itemReader) {
        this.itemReader = itemReader;
    }

    @Override
    public String[] getAuthors() {
        return itemReader.getAuthors();
    }

    @Override
    public String getTitle() {
        return itemReader.getTitle();
    }

    @Override
    public String getSubtitle() {
        return itemReader.getSubtitle();
    }

    @Override
    public Set<ItemReader> getCitingItems() {
        return itemReader.getCitingItems();
    }
}
