//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.12.04 at 07:54:12 PM CET 
//


package it.polito.dp2.BIB.sol2.jaxb.crossref;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CrossrefMessage complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CrossrefMessage">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="next-cursor" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="total-results" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *         &lt;element ref="{}items" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CrossrefMessage", propOrder = {
    "nextCursor",
    "totalResults",
    "items"
})
public class CrossrefMessage {

    @XmlElement(name = "next-cursor", required = true)
    protected String nextCursor;
    @XmlElement(name = "total-results", required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger totalResults;
    protected List<Items> items;

    /**
     * Gets the value of the nextCursor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNextCursor() {
        return nextCursor;
    }

    /**
     * Sets the value of the nextCursor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNextCursor(String value) {
        this.nextCursor = value;
    }

    /**
     * Gets the value of the totalResults property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTotalResults() {
        return totalResults;
    }

    /**
     * Sets the value of the totalResults property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTotalResults(BigInteger value) {
        this.totalResults = value;
    }

    /**
     * Gets the value of the items property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the items property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getItems().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Items }
     * 
     * 
     */
    public List<Items> getItems() {
        if (items == null) {
            items = new ArrayList<Items>();
        }
        return this.items;
    }

}
