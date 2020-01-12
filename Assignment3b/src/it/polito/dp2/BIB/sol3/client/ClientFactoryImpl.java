package it.polito.dp2.BIB.sol3.client;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import it.polito.dp2.BIB.ass3.*;
import it.polito.dp2.BIB.ass3.Bookshelf;

public class ClientFactoryImpl implements Client {
    javax.ws.rs.client.Client client;
    WebTarget target;
    static String uri = "http://localhost:8080/BiblioSystem/rest";
    static String urlProperty = "it.polito.dp2.BIB.ass3.URL";
    static String portProperty = "it.polito.dp2.BIB.ass3.PORT";

    public ClientFactoryImpl(URI uri) {
        this.uri = uri.toString();

        client = ClientBuilder.newClient();
        target = client.target(uri).path("biblio");
    }


    @Override
    public Bookshelf createBookshelf(String name) throws ServiceException {
        try {
            BookshelfCreateResource bookshelfCreateResource = new BookshelfCreateResource();
            bookshelfCreateResource.setName(name);
            return new BookshelfClient(target.path("/bookshelves")
                    .queryParam("name", name)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .post(Entity.json(bookshelfCreateResource), it.polito.dp2.BIB.sol3.client.Bookshelves.Bookshelf.class), client);

        } catch (WebApplicationException | ProcessingException e) {
            e.printStackTrace();
            throw new ServiceException("Create Bookshelf failed");
        }
    }

    @Override
    public Set<Bookshelf> getBookshelfs(String name) throws ServiceException {
        try {
            Bookshelves bookshelves = target.path("/bookshelves")
                    .queryParam("name", name)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get(Bookshelves.class);

            return bookshelves.getBookshelf().stream().map(bookshelf -> new BookshelfClient(bookshelf, client)).collect(Collectors.toSet());
        } catch (WebApplicationException | ProcessingException e) {
            throw new ServiceException("Get Items failed");
        }
    }

    @Override
    public Set<ItemReader> getItems(String keyword, int since, int to) throws ServiceException {
        try {
            Set<ItemReader> itemSet = new HashSet<>();
            Items items = target.path("/items")
                    .queryParam("keyword", keyword)
                    .queryParam("beforeInclusive", to)
                    .queryParam("afterInclusive", since)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get(Items.class);

            for (it.polito.dp2.BIB.sol3.client.Items.Item i : items.getItem()) {
                itemSet.add(new ItemReaderImpl(i));
            }
            return itemSet;
        } catch (WebApplicationException | ProcessingException e) {
            throw new ServiceException("Get Items failed");
        }
    }


    private static void printItems() throws ServiceException {
        Set<ItemReader> set = mainClient.getItems("", 0, 3000);
        System.out.println("Items returned: " + set.size());

        // For each Item print related data
        for (ItemReader item : set) {
            System.out.println("Title: " + item.getTitle());
            if (item.getSubtitle() != null)
                System.out.println("Subtitle: " + item.getSubtitle());
            System.out.print("Authors: ");
            String[] authors = item.getAuthors();
            System.out.print(authors[0]);
            for (int i = 1; i < authors.length; i++)
                System.out.print(", " + authors[i]);
            System.out.println(";");

            Set<ItemReader> citingItems = item.getCitingItems();
            System.out.println("Cited by " + citingItems.size() + " items:");
            for (ItemReader citing : citingItems) {
                System.out.println("- " + citing.getTitle());
            }
            printLine('-');

        }
        printBlankLine();
    }


    private static void printBlankLine() {
        System.out.println(" ");
    }


    private static void printLine(char c) {
        System.out.println(makeLine(c));
    }

    private static StringBuffer makeLine(char c) {
        StringBuffer line = new StringBuffer(132);

        for (int i = 0; i < 132; ++i) {
            line.append(c);
        }
        return line;
    }


    static ClientFactoryImpl mainClient;

    public static void main(String[] args) {
        System.setProperty("it.polito.dp2.BIB.BibReaderFactory", "it.polito.dp2.BIB.Random.BibReaderFactoryImpl");
        String customUri = System.getProperty(urlProperty);
        String customPort = System.getProperty(portProperty);
        if (customUri != null)
            uri = customUri;

        try {
            mainClient = new ClientFactoryImpl(new URI(uri));
            printItems();
            Set<ItemReader> set = mainClient.getItems("", 0, 3000);

            String libName = (new Date()).toString();
            mainClient.createBookshelf(libName);
            Bookshelf bookshelf = mainClient.getBookshelfs(libName).stream().findFirst().get();
            try {
                bookshelf.addItem(set.stream().findFirst().get());
                System.out.println(bookshelf.getName() + "  has " + bookshelf.getNumberOfReads() + " reads");
            } catch (DestroyedBookshelfException | UnknownItemException | TooManyItemsException e) {
                e.printStackTrace();
            }
        } catch (URISyntaxException | ServiceException e) {
            e.printStackTrace();
        }

    }

}
