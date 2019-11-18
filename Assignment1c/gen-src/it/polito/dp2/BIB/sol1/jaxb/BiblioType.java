//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2019.11.18 alle 05:14:18 PM CET 
//


package it.polito.dp2.BIB.sol1.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per BiblioType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="BiblioType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="article" type="{http://pad.polito.it/dp2/biblio_e}ArticleType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="book" type="{http://pad.polito.it/dp2/biblio_e}BookType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="journal" type="{http://pad.polito.it/dp2/biblio_e}JournalType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BiblioType", propOrder = {
    "article",
    "book",
    "journal"
})
public class BiblioType {

    protected List<ArticleType> article;
    protected List<BookType> book;
    protected List<JournalType> journal;

    /**
     * Gets the value of the article property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the article property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getArticle().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArticleType }
     * 
     * 
     */
    public List<ArticleType> getArticle() {
        if (article == null) {
            article = new ArrayList<ArticleType>();
        }
        return this.article;
    }

    /**
     * Gets the value of the book property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the book property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBook().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BookType }
     * 
     * 
     */
    public List<BookType> getBook() {
        if (book == null) {
            book = new ArrayList<BookType>();
        }
        return this.book;
    }

    /**
     * Gets the value of the journal property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the journal property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getJournal().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JournalType }
     * 
     * 
     */
    public List<JournalType> getJournal() {
        if (journal == null) {
            journal = new ArrayList<JournalType>();
        }
        return this.journal;
    }

}
