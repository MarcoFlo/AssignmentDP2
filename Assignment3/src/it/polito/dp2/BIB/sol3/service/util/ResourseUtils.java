package it.polito.dp2.BIB.sol3.service.util;

import java.math.BigInteger;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import it.polito.dp2.BIB.sol3.service.jaxb.*;

public class ResourseUtils {
    UriBuilder base;
    UriBuilder items;
    UriBuilder journals;
    UriBuilder bookshelves;

    public ResourseUtils(UriBuilder base) {
        this.base = base;
        this.items = base.clone().path("biblio/items");
        this.journals = base.clone().path("biblio/journals");
        this.bookshelves = base.clone().path("biblio/bookshelves");

    }

    public void completeItem(Item item, BigInteger id) {
        UriBuilder selfBuilder = items.clone().path(id.toString());
        item.setId(id);
        URI self = selfBuilder.build();
        item.setSelf(self.toString());
        URI citations = selfBuilder.clone().path("citations").build();
        item.setCitations(citations.toString());
        URI citedBy = selfBuilder.clone().path("citedBy").build();
        item.setCitedBy(citedBy.toString());
        URI targets = selfBuilder.clone().path("citations/targets").build();
        item.setTargets(targets.toString());
    }

    public void completeCitation(Citation citation, BigInteger id, BigInteger tid) {
        UriBuilder fromBuilder = items.clone().path(id.toString());
        citation.setFrom(fromBuilder.build().toString());
        citation.setTo(items.clone().path(tid.toString()).build().toString());
        citation.setSelf(fromBuilder.clone().path("citations").path(tid.toString()).build().toString());
    }

    public void completeJournals(Journals res, String keyword, int page) {
        res.setNext(journals.clone().queryParam("page", page).queryParam("keyword", keyword).toString());
    }

    public void completeJournal(Journal res) {
        res.setSelf(journals.clone().path(res.getISSN()).toString());
    }

    public void completeBookshelf(Bookshelf bookshelf) {
        UriBuilder selfBuilder = bookshelves.clone().path(bookshelf.getId().toString());
        bookshelf.setSelf(selfBuilder.build().toString());
        bookshelf.setReadCountUri(selfBuilder.clone().path("counter").build().toString());
        bookshelf.setItemsUri(selfBuilder.clone().path("items").build().toString());
    }
}
