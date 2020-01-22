
package it.polito.dp2.BIB.sol3.client;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
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
 *         &lt;element name="self" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="readCountUri" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="itemsUri" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="item" type="{http://www.w3.org/2001/XMLSchema}integer" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="readCount" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "self",
    "readCountUri",
    "itemsUri",
    "item"
})
@XmlRootElement(name = "bookshelf")
public class Bookshelf {

    @XmlSchemaType(name = "anyURI")
    protected String self;
    @XmlSchemaType(name = "anyURI")
    protected String readCountUri;
    @XmlSchemaType(name = "anyURI")
    protected String itemsUri;
    @XmlElement(nillable = true)
    protected List<BigInteger> item;
    @XmlAttribute(name = "id", required = true)
    protected BigInteger id;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "readCount")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger readCount;

    /**
     * Gets the value of the self property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSelf() {
        return self;
    }

    /**
     * Sets the value of the self property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSelf(String value) {
        this.self = value;
    }

    /**
     * Gets the value of the readCountUri property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReadCountUri() {
        return readCountUri;
    }

    /**
     * Sets the value of the readCountUri property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReadCountUri(String value) {
        this.readCountUri = value;
    }

    /**
     * Gets the value of the itemsUri property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemsUri() {
        return itemsUri;
    }

    /**
     * Sets the value of the itemsUri property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemsUri(String value) {
        this.itemsUri = value;
    }

    /**
     * Gets the value of the item property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the item property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BigInteger }
     * 
     * 
     */
    public List<BigInteger> getItem() {
        if (item == null) {
            item = new ArrayList<BigInteger>();
        }
        return this.item;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setId(BigInteger value) {
        this.id = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the readCount property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getReadCount() {
        return readCount;
    }

    /**
     * Sets the value of the readCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setReadCount(BigInteger value) {
        this.readCount = value;
    }

}
