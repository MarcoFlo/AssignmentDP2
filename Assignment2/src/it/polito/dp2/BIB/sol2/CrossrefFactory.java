package it.polito.dp2.BIB.sol2;

import it.polito.dp2.BIB.sol2.jaxb.crossref.Items;
import it.polito.dp2.xml.biblio.PrintableItem;
import it.polito.pad.dp2.biblio.BiblioItemType;
import it.polito.pad.dp2.biblio.BookType;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public class CrossrefFactory extends it.polito.dp2.xml.biblio.Factory {

    public static PrintableItem createPrintableItem(BigInteger id, Items crossrefItem) throws DatatypeConfigurationException {
        BiblioItemType item = new BiblioItemType();
        item.setId(id);
        item.setTitle(crossrefItem.getTitle());
        item.setSubtitle(crossrefItem.getSubtitle());
        item.getAuthor().addAll(crossrefItem.getAuthor().stream().map(authorType -> {
            String result = authorType.getFamily();
            if (authorType.getGiven() != null)
                result += " " + authorType.getGiven();
            return result;
        }).collect(Collectors.toList()));

        BookType book = new BookType();
        XMLGregorianCalendar calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar();
        calendar.setYear(Integer.parseInt(crossrefItem.getPublishedPrint().getDateParts().substring(0, 4)));

        book.setPublisher(crossrefItem.getPublisher());
        book.setYear(calendar);
        List<String> list = crossrefItem.getISBN(); // is a list containing possibily multiple ISBN, the type of each ISBN is explained in an other field "isbn-type" and could be "print" or "electronic"
        book.setISBN(list.get(0));

        item.setBook(book);
        return createPrintableItem(item);
    }
}
