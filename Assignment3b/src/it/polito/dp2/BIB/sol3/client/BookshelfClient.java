package it.polito.dp2.BIB.sol3.client;

import it.polito.dp2.BIB.ass3.*;
import it.polito.dp2.BIB.ass3.Bookshelf;
import it.polito.dp2.BIB.ass3.ItemReader;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

public class BookshelfClient implements Bookshelf {
    private it.polito.dp2.BIB.sol3.client.Bookshelves.Bookshelf bookshelf;
        private javax.ws.rs.client.Client client;


    public BookshelfClient(it.polito.dp2.BIB.sol3.client.Bookshelves.Bookshelf bookshelf, javax.ws.rs.client.Client client) {
        this.bookshelf = bookshelf;
        this.client = client;
    }

    @Override
    public String getName() throws DestroyedBookshelfException {
        return bookshelf.getName();
    }

    @Override
    public void addItem(it.polito.dp2.BIB.ass3.ItemReader item) throws DestroyedBookshelfException, UnknownItemException, TooManyItemsException, ServiceException {
        WebTarget target = client.target(bookshelf.getItemsUri());
        System.out.print( bookshelf.getItemsUri() + " id item" + (String.valueOf(((ItemReaderImpl) item).getId())));
        Response response = target.queryParam("id",(String.valueOf(((ItemReaderImpl) item).getId())))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(null));
        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode())
                throw new TooManyItemsException();
            if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode())
                throw new UnknownItemException();
            if (response.getStatus() == Response.Status.GONE.getStatusCode())
                throw new DestroyedBookshelfException();
            System.out.println(response.getStatus());
            throw new ServiceException();
        }

    }

    @Override
    public void removeItem(it.polito.dp2.BIB.ass3.ItemReader item) throws DestroyedBookshelfException, UnknownItemException, ServiceException {

    }

    @Override
    public Set<ItemReader> getItems() throws DestroyedBookshelfException, ServiceException {
        return null;
    }


    @Override
    public void destroyBookshelf() throws DestroyedBookshelfException, ServiceException {

    }

    @Override
    public int getNumberOfReads() throws DestroyedBookshelfException {
        return 0;
    }
}
