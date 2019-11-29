package it.polito.dp2.BIB.sol2;

import it.polito.dp2.BIB.sol2.jaxb.crossref.CrossrefItem;
import it.polito.dp2.xml.biblio.PrintableItem;
import it.polito.pad.dp2.biblio.BiblioItemType;
import it.polito.pad.dp2.biblio.BookType;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public class CrossrefFactory extends it.polito.dp2.xml.biblio.Factory {

    public static PrintableItem createPrintableItem(BigInteger id, CrossrefItem crossrefItem) {
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
        book.setPublisher(crossrefItem.getPublisher());
        book.setYear(crossrefItem.getPublishedPrint());
        List<String> list = crossrefItem.getISBN(); // is a list containing possibily multiple ISBN, the type of each ISBN is explained in an other field "isbn-type" and could be "print" or "electronic"
        book.setISBN(list.get(0));

        item.setBook(book);
        return createPrintableItem(item);
    }
}
