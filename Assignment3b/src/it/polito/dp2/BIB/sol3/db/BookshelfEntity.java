package it.polito.dp2.BIB.sol3.db;

import it.polito.dp2.BIB.sol3.service.jaxb.BookshelfCreateResource;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class BookshelfEntity {
    private List<BigInteger> item = new LinkedList<>();
    private BigInteger id;
    private String name;
    private AtomicLong readCount = new AtomicLong(1);
    ;

    public BookshelfEntity(BigInteger id, BookshelfCreateResource bookshelfCreateResource) {
        this.id = id;
        name = bookshelfCreateResource.getName();
        item = bookshelfCreateResource.getItem();
    }

    public List<BigInteger> getItem() {
        return item;
    }

    public void setItem(List<BigInteger> item) {
        this.item = item;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getReadCount() {
        return readCount.get();
    }

    public void incrementReadCount() {
        readCount.incrementAndGet();
    }

}
