package it.polito.dp2.BIB.sol1;

import it.polito.dp2.BIB.ItemReader;
import it.polito.dp2.BIB.BookReader;
import it.polito.dp2.BIB.sol1.jaxb.BookType;

import java.util.HashSet;
import java.util.Set;

public class BookReaderImpl implements BookReader {
    private BookType bookType;
    private Set<ItemReader> citingItems;

    public BookReaderImpl(BookType bookType) {
        this.bookType = bookType;
        citingItems = new HashSet<>();

    }

    @Override
    public String getISBN() {
        return bookType.getISBN();
    }

    @Override
    public String getPublisher() {
        return bookType.getPublisher();
    }

    @Override
    public int getYear() {
        return bookType.getYear().getYear();
    }

    @Override
    public String[] getAuthors() {
        return bookType.getAuthor().toArray(new String[0]);
    }

    @Override
    public String getTitle() {
        return bookType.getTitle();
    }

    @Override
    public String getSubtitle() {
        return bookType.getSubtitle();
    }

    @Override
    public Set<ItemReader> getCitingItems() {
        return citingItems;
    }

    public void addCitingItem(ItemReader itemReader)
    {
        citingItems.add(itemReader);
    }
}
