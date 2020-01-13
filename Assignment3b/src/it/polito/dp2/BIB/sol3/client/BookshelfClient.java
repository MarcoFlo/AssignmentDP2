package it.polito.dp2.BIB.sol3.client;

import it.polito.dp2.BIB.ass3.*;
import it.polito.dp2.BIB.ass3.Bookshelf;
import it.polito.dp2.BIB.ass3.ItemReader;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;
import java.util.stream.Collectors;

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
        Response response = target.queryParam("id", (String.valueOf(((ItemReaderImpl) item).getId())))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(null));
        if (response.getStatus() != Response.Status.NO_CONTENT.getStatusCode()) {
            if (response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode())
                throw new TooManyItemsException();
            if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode())
                throw new UnknownItemException();
            if (response.getStatus() == Response.Status.GONE.getStatusCode())
                throw new DestroyedBookshelfException();
            throw new ServiceException();
        }

    }

    @Override
    public void removeItem(it.polito.dp2.BIB.ass3.ItemReader item) throws DestroyedBookshelfException, UnknownItemException, ServiceException {
        WebTarget target = client.target(bookshelf.getItemsUri());
        Response response = target.path((String.valueOf(((ItemReaderImpl) item).getId())))
                .request()
                .delete();
        if (response.getStatus() != Response.Status.NO_CONTENT.getStatusCode()) {
            if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode())
                throw new UnknownItemException();
            if (response.getStatus() == Response.Status.GONE.getStatusCode())
                throw new DestroyedBookshelfException();
            System.out.println(response.getStatus());
            throw new ServiceException();
        }
    }

    @Override
    public Set<ItemReader> getItems() throws DestroyedBookshelfException, ServiceException {
        WebTarget target = client.target(bookshelf.getItemsUri());
        Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode())
                throw new DestroyedBookshelfException();
            throw new ServiceException();
        }
        response.bufferEntity();
        return response.readEntity(Items.class).getItem().stream().map(ItemReaderImpl::new).collect(Collectors.toSet());
    }


    @Override
    public void destroyBookshelf() throws DestroyedBookshelfException, ServiceException {

    }

    @Override
    public int getNumberOfReads() throws DestroyedBookshelfException {
        WebTarget target = client.target(bookshelf.getReadCountUri());
        Response response = target.request(MediaType.TEXT_PLAIN).get();
        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode())
                throw new DestroyedBookshelfException();
        }
        response.bufferEntity();
        return response.readEntity(Integer.class);
    }
}
