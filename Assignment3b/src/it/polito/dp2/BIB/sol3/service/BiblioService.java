package it.polito.dp2.BIB.sol3.service;

import java.math.BigInteger;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import it.polito.dp2.BIB.sol3.db.*;
import it.polito.dp2.BIB.sol3.db.BookshelfEntity;
import it.polito.dp2.BIB.sol3.service.jaxb.*;
import it.polito.dp2.BIB.sol3.service.util.ResourseUtils;

public class BiblioService {
    private DB n4jDb = Neo4jDB.getNeo4jDB();
    private Map<BigInteger, BookshelfEntity> mapBookshelf = BookshelfDB.getMap();
    private static final int MAX_BOOKSHELF_ITEMS = 20;
    private ResourseUtils rutil;


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
            throw new BadRequestException("The bookshelf must have a name", Response.status(Response.Status.BAD_REQUEST)
                    .entity("The bookshelf must have a name")
                    .build());

        if (bookshelfCreateResource.getItem().size() > MAX_BOOKSHELF_ITEMS)
            throw new BadRequestException("A single bookshelf can contain max " + MAX_BOOKSHELF_ITEMS + " items.", Response.status(Response.Status.BAD_REQUEST)
                    .entity("A single bookshelf can contain max " + MAX_BOOKSHELF_ITEMS + " items.")
                    .build());

        BookshelfEntity bookshelfEntity = new BookshelfEntity(BigInteger.valueOf(BookshelfDB.getNext()), bookshelfCreateResource);
        mapBookshelf.put(bookshelfEntity.getId(), bookshelfEntity);

        return getBookshelfFromBookshelfEntity(bookshelfEntity);
    }

    public Bookshelf getBookshelf(BigInteger bid) {
        BookshelfEntity bookshelfEntity = getBookshelfEntity(bid);
        bookshelfEntity.incrementReadCount();
        return getBookshelfFromBookshelfEntity(bookshelfEntity);
    }

    public void deleteBookshelf(BigInteger bid) {
        if (mapBookshelf.remove(bid) == null)
            throw new NotFoundException("The bookshelf " + bid + " doesn't exist", Response.status(Response.Status.NOT_FOUND)
                    .entity("The bookshelf " + bid + " doesn't exist")
                    .build());
    }

    public long getBookshelfCounter(BigInteger bid) {
        BookshelfEntity bookshelfEntity = getBookshelfEntity(bid);
        return bookshelfEntity.getReadCount();
    }

    public Items getBookshelfItems(BigInteger bid) throws Exception {
        Items items = new Items();
        List<Item> list = items.getItem();

        BookshelfEntity bookshelfEntity = getBookshelfEntity(bid);
        bookshelfEntity.incrementReadCount();
        CopyOnWriteArraySet<BigInteger> idSet = bookshelfEntity.getItem();

        for (BigInteger id : idSet) {
            Item item = getItem(id);
            if (item != null) {
                rutil.completeItem(item, id);
                list.add(item);
            } else {
                idSet.remove(id);
            }
        }

        items.setTotalPages(BigInteger.ONE);
        items.setPage(BigInteger.ONE);
        return items;
    }

    public Item getBookshelfItem(BigInteger bid, BigInteger id) throws Exception {
        BookshelfEntity bookshelfEntity = getBookshelfEntity(bid);
        Item result = getItem(id);
        if (bookshelfEntity.getItem().contains(id)) {
            if (result != null) {
                bookshelfEntity.incrementReadCount();
                rutil.completeItem(result, id);
                return result;
            } else {
                bookshelfEntity.getItem().remove(id);
                throw new NotFoundException("The item " + id + " doesn't exist", Response.status(Response.Status.NOT_FOUND)
                        .entity("The item " + id + " doesn't exist")
                        .build());
            }
        } else
            throw new NotFoundException("The bookshelf " + bid + " doesn't contain the item " + id, Response.status(Response.Status.NOT_FOUND)
                    .entity("The bookshelf " + bid + " doesn't contain the item " + id)
                    .build());
    }

    /**
     * if the item is deleted after we check is presence but before being added to the bookshelf it will be deleted at the next getItem/s operation
     *
     * @param bid
     * @param id
     * @return
     * @throws Exception
     */
    public synchronized Item addBookshelfItem(BigInteger bid, BigInteger id) throws Exception {
        BookshelfEntity bookshelfEntity = getBookshelfEntity(bid);
        Item item = getItem(id);
        if (item == null)
            throw new NotFoundException("The item " + id + " doesn't exist", Response.status(Response.Status.NOT_FOUND)
                    .entity("The item " + id + " doesn't exist")
                    .build());

        CopyOnWriteArraySet<BigInteger> setItem = bookshelfEntity.getItem();
        if (setItem.size() >= MAX_BOOKSHELF_ITEMS)
            throw new BadRequestException("A single bookshelf can contain max " + MAX_BOOKSHELF_ITEMS + " items.", Response.status(Response.Status.BAD_REQUEST)
                    .entity("A single bookshelf can contain max " + MAX_BOOKSHELF_ITEMS + " items.")
                    .build());

        setItem.add(id);
        return item;
    }


    public void deleteBookshelfItem(BigInteger bid, BigInteger id) {
        BookshelfEntity bookshelfEntity = getBookshelfEntity(bid);
        if (!bookshelfEntity.getItem().remove(id))
            throw new NotFoundException("The bookshelf " + bid + " doesn't contain the item " + id, Response.status(Response.Status.NOT_FOUND)
                    .entity("The bookshelf " + bid + " doesn't contain the item " + id)
                    .build());
    }


    private Bookshelf getBookshelfFromBookshelfEntity(BookshelfEntity bookshelfEntity) {
        Bookshelf bookshelf = new Bookshelf();
        bookshelf.setId(bookshelfEntity.getId());
        bookshelf.setName(bookshelfEntity.getName());
        rutil.completeBookshelf(bookshelf);

        return bookshelf;
    }

    private BookshelfEntity getBookshelfEntity(BigInteger bid) {
        BookshelfEntity bookshelfEntity = mapBookshelf.get(bid);
        if (bookshelfEntity == null)
            throw new NotFoundException("The bookshelf " + bid + " doesn't exist", Response.status(Response.Status.NOT_FOUND)
                    .entity("The bookshelf " + bid + " doesn't exist")
                    .build());

        return bookshelfEntity;
    }
}
