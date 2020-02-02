package it.polito.dp2.BIB.sol2;

import java.math.BigInteger;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import it.polito.dp2.BIB.sol2.crossref.jaxb.*;
import it.polito.dp2.xml.biblio.PrintableItem;
import it.polito.pad.dp2.biblio.BiblioItemType;
import it.polito.pad.dp2.biblio.BookType;

/**
 * A Factory that extends the one for the gbooks client with a method
 * to create a PrintableItem from the crossref data structure Items
 *
 */
public class Factory extends it.polito.dp2.rest.gbooks.client.Factory {

	// Create PrintableItem from crossref data structure Items
	public static PrintableItem createPrintableItem(BigInteger id, Items it) {
		BiblioItemType item = new BiblioItemType();
		item.setId(id);
		item.setTitle(it.getTitle());
		item.setSubtitle(it.getSubtitle());
		for(AuthorType at : it.getAuthor()){
			item.getAuthor().add(at.getGiven()+" "+at.getFamily());
		}

		BookType book = new BookType();
		book.setPublisher(it.getPublisher());
		
		try {
        	book.setYear(getXMLGregorianCalendar(Integer.valueOf(it.getPublishedPrint().getDateParts().substring(0, 4))));
    	} catch (DatatypeConfigurationException e) {
    	    throw new Error(e);
    	}
		
		List<ISBNtype> list = it.getIsbnType();
		ISBNtype ii = list.get(0);
		if (ii!=null)
			book.setISBN(ii.getValue());
		item.setBook(book);
		return createPrintableItem(item);
	}
	
	// Create an XMLGregorianCalendar object with only the year set to the specified value
	private static XMLGregorianCalendar getXMLGregorianCalendar(int year) throws DatatypeConfigurationException {
		XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
		cal.setYear(year);
	    return cal;
	}



}
