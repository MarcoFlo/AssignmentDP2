package it.polito.dp2.BIB.sol3.service;

import java.math.BigInteger;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;

import it.polito.dp2.BIB.sol3.db.*;
import it.polito.dp2.BIB.sol3.db.BookshelfEntity;
import it.polito.dp2.BIB.sol3.service.jaxb.*;
import it.polito.dp2.BIB.sol3.service.util.ResourseUtils;

public class BiblioService {
    private DB n4jDb = Neo4jDB.getNeo4jDB();
    private Map<BigInteger, BookshelfEntity> mapBookshelf = BookshelfDB.getMap();
    private int maxBookshelfItems = 20;

    ResourseUtils rutil;


    public BiblioService(UriInfo uriInfo) {
        rutil = new ResourseUtils((uriInfo.getBaseUriBuilder()));
    }

    public Items getItems(SearchScope scope, String keyword, int beforeInclusive, int afterInclusive, BigInteger page) throws Exception {
        ItemPage itemPage = n4jDb.getItems(scope, keyword, beforeInclusive, afterInclusive, page);

        Items items = new Items();
        List<Item> list = items.getItem();

        Set<Entry<BigInteger, Item>> set = itemPage.getMap().entrySet();
        for (Entry<BigInteger, Item> entry : set) {
            Item item = entry.getValue();
            rutil.completeItem(item, entry.getKey());
            list.add(item);
        }
        items.setTotalPages(itemPage.getTotalPages());
        items.setPage(page);
        return items;
    }

    public Item getItem(BigInteger id) throws Exception {
        Item item = n4jDb.getItem(id);
        if (item != null)
            rutil.completeItem(item, id);
        return item;
    }

    public Item updateItem(BigInteger id, Item item) throws Exception {
        Item ret = n4jDb.updateItem(id, item);
        if (ret != null) {
            rutil.completeItem(item, id);
            return item;
        } else
            return null;
    }

    public Item createItem(Item item) throws Exception {
        BigInteger id = n4jDb.createItem(item);
        if (id == null)
            throw new Exception("Null id");
        rutil.completeItem(item, id);
        return item;
    }

    public BigInteger deleteItem(BigInteger id) throws ConflictServiceException, Exception {
        try {
            return n4jDb.deleteItem(id);
        } catch (ConflictInOperationException e) {
            throw new ConflictServiceException();
        }
    }

    public Citation createItemCitation(BigInteger id, BigInteger tid, Citation citation) throws Exception {
        try {
            return n4jDb.createItemCitation(id, tid, citation);
        } catch (BadRequestInOperationException e) {
            throw new BadRequestServiceException();
        }
    }

    public Citation getItemCitation(BigInteger id, BigInteger tid) throws Exception {
        Citation citation = n4jDb.getItemCitation(id, tid);
        if (citation != null)
            rutil.completeCitation(citation, id, tid);
        return citation;
    }

    public boolean deleteItemCitation(BigInteger id, BigInteger tid) throws Exception {
        return n4jDb.deleteItemCitation(id, tid);
    }

    public Items getItemCitations(BigInteger id) throws Exception {
        ItemPage itemPage = n4jDb.getItemCitations(id, BigInteger.ONE);
        if (itemPage == null)
            return null;

        Items items = new Items();
        List<Item> list = items.getItem();

        Set<Entry<BigInteger, Item>> set = itemPage.getMap().entrySet();
        for (Entry<BigInteger, Item> entry : set) {
            Item item = entry.getValue();
            rutil.completeItem(item, entry.getKey());
            list.add(item);
        }
        items.setTotalPages(itemPage.getTotalPages());
        items.setPage(BigInteger.ONE);
        return items;
    }

    public Items getItemCitedBy(BigInteger id) throws Exception {
        ItemPage itemPage = n4jDb.getItemCitedBy(id, BigInteger.ONE);
        if (itemPage == null)
            return null;

        Items items = new Items();
        List<Item> list = items.getItem();

        Set<Entry<BigInteger, Item>> set = itemPage.getMap().entrySet();
        for (Entry<BigInteger, Item> entry : set) {
            Item item = entry.getValue();
            rutil.completeItem(item, entry.getKey());
            list.add(item);
        }
        items.setTotalPages(itemPage.getTotalPages());
        items.setPage(BigInteger.ONE);
        return items;
    }

    public Bookshelves getBookshelves(String name) {
        Bookshelves result = new Bookshelves();
        List<Bookshelf> bookshelfList = result.getBookshelf();
        bookshelfList.addAll(mapBookshelf.values().stream().filter(bookshelfEntity -> bookshelfEntity.getName().contains(name)).map(this::getBookshelfFromBookshelfEntity).collect(Collectors.toList()));
        return result;
    }

    public Bookshelf createBookshelf(BookshelfCreateResource bookshelfCreateResource) {
        if (bookshelfCreateResource.getName().equals(""))
            throw new BadRequestException("The bookshelf must have a name");

        if (bookshelfCreateResource.getItem().size() > maxBookshelfItems)
            throw new BadRequestException("A single bookshelf can contain max " + maxBookshelfItems + " items.");

        //todo sync
        synchronized (mapBookshelf.values()) {
            if (mapBookshelf.values().stream().noneMatch(b -> b.getName().equals(bookshelfCreateResource.getName()))) {
                BookshelfEntity bookshelfEntity = new BookshelfEntity(BigInteger.valueOf(BookshelfDB.getNext()), bookshelfCreateResource);
                mapBookshelf.put(bookshelfEntity.getId(), bookshelfEntity);

                return getBookshelfFromBookshelfEntity(bookshelfEntity);
            } else
                throw new BadRequestException("Duplicate bookshelf name.");
        }
    }

    public Bookshelf getBookshelf(BigInteger bid) {
        BookshelfEntity bookshelfEntity = getBookshelfEntity(bid);
        bookshelfEntity.incrementReadCount();
        return getBookshelfFromBookshelfEntity(bookshelfEntity);
    }

    public void deleteBookshelf(BigInteger bid) {
        if (mapBookshelf.remove(bid) == null)
            throw new NotFoundException("There is no bookshelf with ID " + bid);
    }

    public long getBookshelfCounter(BigInteger bid) {
        return mapBookshelf.get(bid).getReadCount();
    }

    public Items getBookshelfItems(BigInteger bid) {
        Items items = new Items();
        List<Item> list = items.getItem();
        list.addAll(getBookshelf(bid).getItem());
        items.setTotalPages(BigInteger.ONE);
        items.setPage(BigInteger.ONE);
        return items;
    }

    public Item getBookshelfItem(BigInteger bid, BigInteger id) throws Exception {
        BookshelfEntity bookshelfEntity = getBookshelfEntity(bid);
        if (bookshelfEntity.getItem().contains(id)) {
            bookshelfEntity.incrementReadCount();
            return getItem(id);
        } else
            throw new NotFoundException("Bookshelf and item id must exist.");
    }

    public Item addBookshelfItem(BigInteger bid, BigInteger id) throws Exception {
        BookshelfEntity bookshelfEntity = getBookshelfEntity(bid);
        Item item = getItem(id);
        //todo check if it does what I expect
        synchronized (bookshelfEntity.getItem()) {
            CopyOnWriteArraySet<BigInteger> setItem = bookshelfEntity.getItem();
            if (setItem.size() < maxBookshelfItems) {
                setItem.add(id);
                return item;
            } else
                throw new BadRequestException("A single bookshelf can contain max " + maxBookshelfItems + " items.");
        }

    }


    public void deleteBookshelfItem(BigInteger bid, BigInteger id) {
        BookshelfEntity bookshelfEntity = getBookshelfEntity(bid);
        if (!bookshelfEntity.getItem().remove(id))
            throw new NotFoundException("Bookshelf and item id must exist.");
    }


    private Bookshelf getBookshelfFromBookshelfEntity(BookshelfEntity bookshelfEntity) {
        Bookshelf bookshelf = new Bookshelf();
        bookshelf.getItem().addAll(getItemListFromIdSet(bookshelfEntity));

        bookshelf.setReadCount(BigInteger.valueOf(bookshelfEntity.getReadCount()));
        bookshelf.setId(bookshelfEntity.getId());
        bookshelf.setName(bookshelfEntity.getName());

        rutil.completeBookshelf(bookshelf);

        return bookshelf;
    }

    private List<Item> getItemListFromIdSet(BookshelfEntity bookshelfEntity) {
        List<Item> result = new LinkedList<>();
        CopyOnWriteArraySet<BigInteger> idSet = bookshelfEntity.getItem();
        Iterator<BigInteger> iterator = idSet.iterator();

        while (iterator.hasNext()) {
            try {
                BigInteger id = iterator.next();
                Item item = getItem(id);
                if (item != null) {
                    rutil.completeItem(item, id);
                    result.add(item);
                } else
                    iterator.remove();
            } catch (Exception e) {
                //todo
                throw new InternalServerErrorException("getItemfromid");
            }
        }

        return result;
    }

    private BookshelfEntity getBookshelfEntity(BigInteger bid) {
        BookshelfEntity bookshelfEntity = mapBookshelf.get(bid);
        if (bookshelfEntity != null) {
            return bookshelfEntity;
        } else
            throw new NotFoundException("There is no bookshelf with ID " + bid);
    }
}
