package it.polito.dp2.BIB.sol1.parser;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

import it.polito.dp2.BIB.BookReader;
import it.polito.dp2.BIB.ItemReader;
import it.polito.dp2.BIB.sol1.jaxb.ArticleType;
import it.polito.dp2.BIB.sol1.jaxb.BiblioType;
import it.polito.dp2.BIB.sol1.jaxb.BookType;
import it.polito.dp2.BIB.sol1.jaxb.ItemType;

/**
 * Class that manages the storage of item information and enables item searching.
 * A constructor is offered to initialize the object with the item information
 * contained in a BiblioType object and with journal information contained in a
 * Journals object.
 *
 */public class Items {

	private HashMap<String,BookImpl> books;				// map of books with ISBN as key
	private TreeMap<Integer, Set<ItemImpl>> itemsByYear;// sorted map of items with publication year as key
	private HashMap<BigInteger,ItemImpl> itemsById;		// map of items with id as key
	
	public Items(BiblioType biblio, Journals journals) {
		books = new HashMap<String,BookImpl>();
		itemsByYear = new TreeMap<Integer,Set<ItemImpl>>();
		itemsById = new HashMap<BigInteger,ItemImpl>();
		for (ItemType item: biblio.getItems().getArticleOrBook()) {
			if (item instanceof ArticleType) {
				ArticleImpl ai = new ArticleImpl((ArticleType)item, this, journals);
				addToItemIndexes(ai, ai.getIssue().getYear());
			} else {
				BookImpl bi = new BookImpl((BookType)item, this);
				addToItemIndexes(bi, bi.getYear());
				books.put(bi.getISBN(), bi);
			}		
		}
	}

	// Add an item to the indexes (maps of items)
	private void addToItemIndexes(ItemImpl item, int year) {
		if (itemsByYear.containsKey(year))
			itemsByYear.get(year).add(item);
		else {
			HashSet<ItemImpl> hs = new HashSet<ItemImpl>();
			hs.add(item);
			itemsByYear.put(year,hs);
		}
		itemsById.put(item.getId(), item);
	}

	/**
	 * Get information about the items that contain a keyword in their title
	 * and that have been published in a given range of years
	 * @param keyword the keyword
	 * @param since the year (inclusive) from which the search is done
	 * @param to the year (inclusive) to which the search is done
	 * @return a set of ItemReader objects that provide the information about the found items
	 */
	public Set<ItemReader> getItems(String keyword, int since, int to) {
		Set<ItemReader> retval = new HashSet<ItemReader>();
		for (Set<ItemImpl> s: itemsByYear.subMap(since, to+1).values()) {
			for (ItemImpl ii:s) {
				if (keyword==null || ii.getTitle().contains(keyword))
					retval.add(ii);
			}
		}
		return retval;
	}

	/**
	 * Get the information about the book with a given ISBN 
	 * @param isbn the ISBN
	 * @return a BookReader object that gives access to the book information
	 */	
	public BookReader getBook(String isbn) {
		return books.get(isbn);
	}

	/**
	 * Get the information about the item with a given id 
	 * @param id the id
	 * @return an ItemReader object that gives access to the item information
	 */	
	public ItemReader getItem(BigInteger id) {
		return itemsById.get(id);
	}

}
