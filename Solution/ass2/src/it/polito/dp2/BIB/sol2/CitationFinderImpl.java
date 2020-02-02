package it.polito.dp2.BIB.sol2;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import it.polito.dp2.BIB.BibReader;
import it.polito.dp2.BIB.BibReaderException;
import it.polito.dp2.BIB.BibReaderFactory;
import it.polito.dp2.BIB.BookReader;
import it.polito.dp2.BIB.ItemReader;
import it.polito.dp2.BIB.JournalReader;
import it.polito.dp2.BIB.ass2.CitationFinder;
import it.polito.dp2.BIB.ass2.CitationFinderException;
import it.polito.dp2.BIB.ass2.ServiceException;
import it.polito.dp2.BIB.ass2.UnknownItemException;
import it.polito.dp2.BIB.sol2.exercise1.jaxb.Node;

/**
 * Implementation of the CitationFinder interface
 * This is part of a solution of DP2 Lab 4, exercise 1 (AY 2019/20)
 *
 */
public class CitationFinderImpl implements CitationFinder {
	private BibReader monitor;					// the source of bibliographic information
	private Map<ItemReader,Node> readerToNode;	// a map that maps ItemReader objects to NEO4J nodes
	private Map<URL,ItemReader> urlToReader;	// a map that maps the URLs of NEO4J nodes to ItemReader objects
	private Neo4jClient client;					// a manager that can execute the necessary operations on NEO4J 

	public CitationFinderImpl() throws CitationFinderException {
		try {
			// initialize instance variables
			BibReaderFactory factory = BibReaderFactory.newInstance();
			monitor = factory.newBibReader();
			readerToNode = new HashMap<ItemReader, Node>();
			urlToReader = new HashMap<URL, ItemReader>();
			client = new Neo4jClient();
			
			// create nodes
			Set<ItemReader> items = monitor.getItems(null, 0, 3000);
			for (ItemReader item : items) {
				Node node = client.createNode(item.getTitle());
				readerToNode.put(item, node);
				URL url = new URL(node.getSelf());
				urlToReader.put(url, item);
			}
			
			// create relationships
			for (ItemReader item1 : items) {
				Node node1 = readerToNode.get(item1);
				for (ItemReader item2: item1.getCitingItems()) {
					Node node2 = readerToNode.get(item2);
						client.createRelationship(node1, node2);
				}
			}
		} catch (Neo4jClientException | BibReaderException | MalformedURLException e) {
			throw new CitationFinderException(e);
		}
	}

	@Override
	public Set<ItemReader> findAllCitingItems(ItemReader item, int maxDepth) throws UnknownItemException, ServiceException {
		// get the node object corresponding to the passed item
		Node node = readerToNode.get(item);
		if (node==null)
			throw new UnknownItemException();
		// initialize the set that will be returned
		Set<ItemReader> retval = new HashSet<ItemReader>();
		try {
			// perform the traverse up to the given max depth and obtain the set of URLs of the traversed nodes
			Set<URL> urls = client.performTraverse(new URL(node.getSelf()), maxDepth);
			// get the ItemReader objects that correspond to these URLs and add them to the return set
			for (URL u:urls) {
				ItemReader i = urlToReader.get(u);
				if (i==null)
					throw new ServiceException();
				retval.add(i);
			}
			// return the set
			return retval;
		} catch (Neo4jClientException | MalformedURLException e) {
			throw new ServiceException(e);
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
	
	// test main (not necessary for solution)
	public static void main(String[] args) {
		try {
			CitationFinderImpl cfi = new CitationFinderImpl();
			Set<ItemReader> items = cfi.getItems(null, 0, 3000);
			for (ItemReader item:items) {
				Set<ItemReader> citing = cfi.findAllCitingItems(item,4);
				System.out.println("Item "+item.getTitle());
				System.out.println("Citing items: ");
				for (ItemReader ir:citing) {
					System.out.println(ir.getTitle());
				}
				System.out.println("----");
			}	
		} catch (CitationFinderException | UnknownItemException | ServiceException e) {
			e.printStackTrace();
		}
	}

}
