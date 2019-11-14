//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2019.11.14 alle 09:54:24 PM CET 
//


package it.polito.dp2.BIB.sol1.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.polito.dp2.BIB.sol1.jaxb package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Biblio_QNAME = new QName("http://pad.polito.it/dp2/biblio_e", "biblio");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.polito.dp2.BIB.sol1.jaxb
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JournalType }
     * 
     */
    public JournalType createJournalType() {
        return new JournalType();
    }

    /**
     * Create an instance of {@link BiblioType }
     * 
     */
    public BiblioType createBiblioType() {
        return new BiblioType();
    }

    /**
     * Create an instance of {@link BookType }
     * 
     */
    public BookType createBookType() {
        return new BookType();
    }

    /**
     * Create an instance of {@link BiblioItemType }
     * 
     */
    public BiblioItemType createBiblioItemType() {
        return new BiblioItemType();
    }

    /**
     * Create an instance of {@link ArticleType }
     * 
     */
    public ArticleType createArticleType() {
        return new ArticleType();
    }

    /**
     * Create an instance of {@link JournalType.Issue }
     * 
     */
    public JournalType.Issue createJournalTypeIssue() {
        return new JournalType.Issue();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BiblioType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://pad.polito.it/dp2/biblio_e", name = "biblio")
    public JAXBElement<BiblioType> createBiblio(BiblioType value) {
        return new JAXBElement<BiblioType>(_Biblio_QNAME, BiblioType.class, null, value);
    }

}
