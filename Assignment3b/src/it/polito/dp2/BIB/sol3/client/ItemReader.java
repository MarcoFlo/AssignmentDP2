package it.polito.dp2.BIB.sol3.client;

import it.polito.dp2.BIB.sol3.service.jaxb.Item;

import java.util.Set;

public class ItemReader implements it.polito.dp2.BIB.ass3.ItemReader {
    private Item item;
    public ItemReader(Item item)
    {
        this.item = item;
    }
    @Override
    public String[] getAuthors() {
        return item.getAuthor().toArray(new String[0]);
    }

    @Override
    public String getTitle() {
        return item.getTitle();
    }

    @Override
    public String getSubtitle() {
        return item.getSubtitle();
    }

    @Override
    public Set<it.polito.dp2.BIB.ass3.ItemReader> getCitingItems() {
        return item.get;
    }
}
