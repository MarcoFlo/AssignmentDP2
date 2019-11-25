package it.polito.dp2.BIB.sol2;

import it.polito.dp2.BIB.*;
import it.polito.dp2.BIB.ass2.CitationFinder;
import it.polito.dp2.BIB.ass2.CitationFinderException;
import it.polito.dp2.BIB.ass2.ServiceException;
import it.polito.dp2.BIB.ass2.UnknownItemException;
import it.polito.dp2.BIB.sol2.jaxb.Node;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CitationFinderImplementation implements CitationFinder {
    private BibReader monitor;
    private Map<ItemReader, Node> readerToNode;
    private Map<URL, ItemReader> urlToReader;
    private Neo4jClient client;


    public CitationFinderImplementation() throws CitationFinderException {
        try {
            BibReaderFactory factory = BibReaderFactory.newInstance();
            monitor = factory.newBibReader();
            readerToNode = new HashMap<ItemReader, Node>();
            urlToReader = new HashMap<URL, ItemReader>();
            client = new Neo4jClient();
            // create nodes
            Set<ItemReader> items = monitor.getItems(null, 0, 3000);
            Node from;
            Node to;

            for (ItemReader item : items) {
                from = manageNode(item);

                for (ItemReader citedReader : item.getCitingItems()) {
                    to = manageNode(citedReader);
                    client.createRelationship(from, to);
                }
            }
        } catch (Neo4jClientException | BibReaderException | MalformedURLException e) {
            throw new CitationFinderException(e);
        }
    }

    private Node manageNode(ItemReader item) throws Neo4jClientException, MalformedURLException {
        Node node;

        if (readerToNode.containsKey(item)) {
            node = readerToNode.get(item);
        } else {
            node = client.createNode(item.getTitle());
            readerToNode.put(item, node);
            URL url = new URL(node.getSelf());
            urlToReader.put(url, item);


        }
        return node;

    }

    @Override
    public Set<ItemReader> findAllCitingItems(ItemReader item, int maxDepth) throws UnknownItemException, ServiceException {
        if (!readerToNode.containsKey(item))
            throw new UnknownItemException();

        if (maxDepth <= 0)
            maxDepth = 1;
        try {
            return client.getListTraversed(readerToNode.get(item), maxDepth).stream().map(node -> {
                try {
                    return urlToReader.get(new URL(node.getSelf()));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return null;
                }
            }).collect(Collectors.toSet());
        } catch (Exception e) {
            throw new ServiceException();
        }
    }

    @Override
    public BookReader getBook(String arg0) {
        return monitor.getBook(arg0);
    }

    @Override
    public Set<ItemReader> getItems(String arg0, int arg1, int arg2) {
        return monitor.getItems(arg0, arg1, arg2);
    }

    @Override
    public JournalReader getJournal(String arg0) {
        return monitor.getJournal(arg0);
    }

    @Override
    public Set<JournalReader> getJournals(String arg0) {
        return monitor.getJournals(arg0);
    }

}
