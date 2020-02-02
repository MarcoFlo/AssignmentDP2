package it.polito.dp2.BIB.sol1.parser;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import it.polito.dp2.BIB.ItemReader;
import it.polito.dp2.BIB.sol1.jaxb.ItemType;

/**
 * Implementation of the ItemReader interface which returns the bibliographic information
 * retrieved from an ItemType object.
 * This implementation also provides the id assigned to the item.
 *
 */
public class ItemImpl implements ItemReader {
	ItemType item;
	Items items;

	public ItemImpl(ItemType item, Items items) {
		this.item = item;
		this.items = items;
	}

	@Override
	public String[] getAuthors() {
		return item.getAuthor().toArray(new String[item.getAuthor().size()]);
	}

	@Override
	public String getTitle() {
		return item.getTitle();
	}

	@Override
	public String getSubtitle() {
		return item.getSubtitle();
	}

	@Override
	public Set<ItemReader> getCitingItems() {
		Set<ItemReader> retval = new HashSet<ItemReader>();
		for (BigInteger i: item.getCitedBy()) {
			retval.add(items.getItem(i));
		}
		return retval;
	}

	public BigInteger getId() {
		return item.getId();
	}

}
