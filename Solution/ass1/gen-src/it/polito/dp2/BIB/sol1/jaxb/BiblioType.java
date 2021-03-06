//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.02.02 at 11:59:01 AM CET 
//


package it.polito.dp2.BIB.sol1.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BiblioType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BiblioType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="items" type="{http://pad.polito.it/dp2/biblio}BiblioItemsType"/>
 *         &lt;element name="journals" type="{http://pad.polito.it/dp2/biblio}BiblioJournalsType"/>
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
    "items",
    "journals"
})
public class BiblioType {

    @XmlElement(required = true)
    protected BiblioItemsType items;
    @XmlElement(required = true)
    protected BiblioJournalsType journals;

    /**
     * Gets the value of the items property.
     * 
     * @return
     *     possible object is
     *     {@link BiblioItemsType }
     *     
     */
    public BiblioItemsType getItems() {
        return items;
    }

    /**
     * Sets the value of the items property.
     * 
     * @param value
     *     allowed object is
     *     {@link BiblioItemsType }
     *     
     */
    public void setItems(BiblioItemsType value) {
        this.items = value;
    }

    /**
     * Gets the value of the journals property.
     * 
     * @return
     *     possible object is
     *     {@link BiblioJournalsType }
     *     
     */
    public BiblioJournalsType getJournals() {
        return journals;
    }

    /**
     * Sets the value of the journals property.
     * 
     * @param value
     *     allowed object is
     *     {@link BiblioJournalsType }
     *     
     */
    public void setJournals(BiblioJournalsType value) {
        this.journals = value;
    }

}
