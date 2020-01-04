package it.polito.dp2.BIB.sol3.service;

import java.math.BigInteger;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;

import it.polito.dp2.BIB.sol3.db.*;
import it.polito.dp2.BIB.sol3.service.jaxb.*;
import it.polito.dp2.BIB.sol3.service.util.ResourseUtils;

public class BiblioService {
    private DB n4jDb = Neo4jDB.getNeo4jDB();
    private Map<BigInteger, Bookshelf> mapBookshelf = BookshelfDB.getMap();
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

    public Bookshelves getBookshelves(String keyword) {
        boolean isNotNull = !keyword.equals("");
        Bookshelves result = new Bookshelves();
        List<Bookshelf> bookshelfList = result.getBookshelf();

        mapBookshelf.values().stream().filter(bookshelf -> bookshelf.getName().contains(keyword) == isNotNull).forEach(bookshelf -> {
            rutil.completeBookshelf(bookshelf);
            bookshelfList.add(bookshelf);
        });
        return result;
    }

    public synchronized Bookshelf createBookshelf(Bookshelf bookshelf) {
        if (bookshelf.getName().equals(""))
            throw new BadRequestException("The bookshelf must have a name");

        if (bookshelf.getItem().size() > maxBookshelfItems)
            throw new BadRequestException("A single bookshelf can contain max " + maxBookshelfItems + " items.");

        if (mapBookshelf.values().stream().noneMatch(b -> b.getName().equals(bookshelf.getName()))) {
            bookshelf.setId(BigInteger.valueOf(BookshelfDB.getNext()));
            bookshelf.setReadCount(BigInteger.ONE);
            return mapBookshelf.put(bookshelf.getId(), bookshelf);
        } else
            throw new BadRequestException("Duplicate bookshelf name.");
    }

    public Bookshelf getBookshelf(BigInteger bid) {
        Bookshelf bookshelf = mapBookshelf.get(bid);

        if (bookshelf != null) {
            BigInteger readCount = bookshelf.getReadCount();
            readCount.add(BigInteger.ONE);

            //if in the meanwhile the bookshelf related to this id is deleted, the replace operation will simply not happen
            mapBookshelf.replace(bid, bookshelf);
            return bookshelf;
        } else
            throw new NotFoundException("There is no bookshelf with ID " + bid);
    }

    public void deleteBookshelf(BigInteger bid) {
        if (mapBookshelf.remove(bid) == null)
            throw new NotFoundException("There is no bookshelf with ID " + bid);
    }

    public int getBookshelfCounter(BigInteger bid) {
        return mapBookshelf.get(bid).getReadCount().intValue();
    }

    public Items getBookshelfItems(BigInteger bid) {
        Items items = new Items();
        List<Item> list = items.getItem();
        list.addAll(mapBookshelf.get(bid).getItem());
        items.setTotalPages(BigInteger.ONE);
        items.setPage(BigInteger.ZERO);
        return items;
    }
}
