//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.12.04 at 07:54:11 PM CET 
//


package it.polito.pad.dp2.biblio;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.polito.pad.dp2.biblio package. 
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


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.polito.pad.dp2.biblio
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
     * Create an instance of {@link Biblio }
     * 
     */
    public Biblio createBiblio() {
        return new Biblio();
    }

    /**
     * Create an instance of {@link BiblioItemType }
     * 
     */
    public BiblioItemType createBiblioItemType() {
        return new BiblioItemType();
    }

    /**
     * Create an instance of {@link BookType }
     * 
     */
    public BookType createBookType() {
        return new BookType();
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

}
