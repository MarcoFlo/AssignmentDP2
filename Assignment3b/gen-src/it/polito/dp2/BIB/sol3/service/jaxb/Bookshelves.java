//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.01.23 at 11:25:44 AM CET 
//


package it.polito.dp2.BIB.sol3.service.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *     &lt;extension base="{}pagingType">
 *       &lt;sequence>
 *         &lt;element ref="{}bookshelf" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "bookshelf"
})
@XmlRootElement(name = "bookshelves")
public class Bookshelves
    extends PagingType
{

    protected List<Bookshelf> bookshelf;

    /**
     * Gets the value of the bookshelf property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bookshelf property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBookshelf().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Bookshelf }
     * 
     * 
     */
    public List<Bookshelf> getBookshelf() {
        if (bookshelf == null) {
            bookshelf = new ArrayList<Bookshelf>();
        }
        return this.bookshelf;
    }

}
