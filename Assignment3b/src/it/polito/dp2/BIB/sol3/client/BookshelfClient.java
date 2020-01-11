package it.polito.dp2.BIB.sol3.client;

import it.polito.dp2.BIB.ass3.*;
import it.polito.dp2.BIB.ass3.Bookshelf;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class BookshelfClient implements Bookshelf {
    private it.polito.dp2.BIB.sol3.client.Bookshelves.Bookshelf bookshelf;

    public BookshelfClient(it.polito.dp2.BIB.sol3.client.Bookshelves.Bookshelf bookshelf) {
        this.bookshelf = bookshelf;

    }

    @Override
    public String getName() throws DestroyedBookshelfException {
        return bookshelf.getName();
    }

    @Override
    public void addItem(it.polito.dp2.BIB.ass3.ItemReader item) throws DestroyedBookshelfException, UnknownItemException, TooManyItemsException, ServiceException {

    }

    @Override
    public void removeItem(it.polito.dp2.BIB.ass3.ItemReader item) throws DestroyedBookshelfException, UnknownItemException, ServiceException {

    }

    @Override
    public Set<ItemReader> getItems() throws DestroyedBookshelfException, ServiceException {
        return bookshelf.getItem().mastream().collect(Collectors.toSet());
    }

    @Override
    public void destroyBookshelf() throws DestroyedBookshelfException, ServiceException {

    }

    @Override
    public int getNumberOfReads() throws DestroyedBookshelfException {
        return 0;
    }
}
