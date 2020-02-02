package it.polito.dp2.BIB.sol1.serializer;

import java.util.Collection;
import java.util.HashMap;

import it.polito.dp2.BIB.ItemReader;
import it.polito.dp2.BIB.sol1.jaxb.ItemType;

/**
 * Class to manage item information
 * It stores item information indexed by ItemReader objects
 */
public class Items {
	private HashMap<ItemReader,ItemType> items;	// map of items with ItemReader as key

	public Items() {
		items = new HashMap<ItemReader,ItemType>();
	}

	// retrieve item info given the corresponding ItemReader
	public ItemType getItemType(ItemReader reader) {
		return items.get(reader);
	}
	
	// store item info with its corresponding ItemReader as key
	public void addItem(ItemReader reader, ItemType itemType) {
		items.put(reader, itemType);
	}

	// retrieve info for all stored items
	public Collection<? extends ItemType> getItems() {
		return items.values();
	}

}
