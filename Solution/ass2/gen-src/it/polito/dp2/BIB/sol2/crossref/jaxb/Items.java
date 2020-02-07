//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.02.02 at 12:30:23 PM CET 
//


package it.polito.dp2.BIB.sol2.crossref.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="publisher" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="isbn-type" type="{}ISBNtype" maxOccurs="unbounded"/>
 *         &lt;element name="published-print" type="{}published_type"/>
 *         &lt;element name="title" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="author" type="{}AuthorType" maxOccurs="unbounded"/>
 *         &lt;element name="subtitle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "publisher",
    "isbnType",
    "publishedPrint",
    "title",
    "author",
    "subtitle"
})
@XmlRootElement(name = "items")
public class Items {

    @XmlElement(required = true)
    protected String publisher;
    @XmlElement(name = "isbn-type", required = true)
    protected List<ISBNtype> isbnType;
    @XmlElement(name = "published-print", required = true)
    protected PublishedType publishedPrint;
    @XmlElement(required = true)
    protected String title;
    @XmlElement(required = true)
    protected List<AuthorType> author;
    protected String subtitle;

    /**
     * Gets the value of the publisher property.
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
     * Sets the value of the publisher property.
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
     * Gets the value of the isbnType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the isbnType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIsbnType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ISBNtype }
     * 
     * 
     */
    public List<ISBNtype> getIsbnType() {
        if (isbnType == null) {
            isbnType = new ArrayList<ISBNtype>();
        }
        return this.isbnType;
    }

    /**
     * Gets the value of the publishedPrint property.
     * 
     * @return
     *     possible object is
     *     {@link PublishedType }
     *     
     */
    public PublishedType getPublishedPrint() {
        return publishedPrint;
    }

    /**
     * Sets the value of the publishedPrint property.
     * 
     * @param value
     *     allowed object is
     *     {@link PublishedType }
     *     
     */
    public void setPublishedPrint(PublishedType value) {
        this.publishedPrint = value;
    }

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the author property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the author property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAuthor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AuthorType }
     * 
     * 
     */
    public List<AuthorType> getAuthor() {
        if (author == null) {
            author = new ArrayList<AuthorType>();
        }
        return this.author;
    }

    /**
     * Gets the value of the subtitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * Sets the value of the subtitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubtitle(String value) {
        this.subtitle = value;
    }

}