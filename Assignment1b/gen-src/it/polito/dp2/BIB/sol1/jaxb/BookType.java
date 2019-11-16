//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2019.11.16 alle 06:51:42 PM CET 
//


package it.polito.dp2.BIB.sol1.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java per BookType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="BookType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://pad.polito.it/dp2/biblio_e}BiblioItemType">
 *       &lt;sequence>
 *         &lt;element name="publisher" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *       &lt;attribute name="ISBN" use="required" type="{http://pad.polito.it/dp2/biblio_e}ISBNCode" />
 *       &lt;attribute name="year" use="required" type="{http://www.w3.org/2001/XMLSchema}gYear" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BookType", propOrder = {
    "publisher"
})
public class BookType
    extends BiblioItemType
{

    @XmlElement(required = true)
    protected String publisher;
    @XmlAttribute(name = "ISBN", required = true)
    protected String isbn;
    @XmlAttribute(name = "year", required = true)
    @XmlSchemaType(name = "gYear")
    protected XMLGregorianCalendar year;

    /**
     * Recupera il valore della proprietà publisher.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Imposta il valore della proprietà publisher.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPublisher(String value) {
        this.publisher = value;
    }

    /**
     * Recupera il valore della proprietà isbn.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getISBN() {
        return isbn;
    }

    /**
     * Imposta il valore della proprietà isbn.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setISBN(String value) {
        this.isbn = value;
    }

    /**
     * Recupera il valore della proprietà year.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getYear() {
        return year;
    }

    /**
     * Imposta il valore della proprietà year.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setYear(XMLGregorianCalendar value) {
        this.year = value;
    }

}
