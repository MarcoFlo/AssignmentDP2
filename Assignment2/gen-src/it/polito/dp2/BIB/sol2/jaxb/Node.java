//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2019.11.23 alle 11:14:08 AM CET 
//


package it.polito.dp2.BIB.sol2.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="labels" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *         &lt;element name="self" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *         &lt;element ref="{}data"/>
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
    "labels",
    "self",
    "data"
})
@XmlRootElement(name = "node")
public class Node {

    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String labels;
    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String self;
    @XmlElement(required = true)
    protected Data data;

    /**
     * Recupera il valore della proprietà labels.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabels() {
        return labels;
    }

    /**
     * Imposta il valore della proprietà labels.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabels(String value) {
        this.labels = value;
    }

    /**
     * Recupera il valore della proprietà self.
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
     * Imposta il valore della proprietà self.
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
     * Recupera il valore della proprietà data.
     * 
     * @return
     *     possible object is
     *     {@link Data }
     *     
     */
    public Data getData() {
        return data;
    }

    /**
     * Imposta il valore della proprietà data.
     * 
     * @param value
     *     allowed object is
     *     {@link Data }
     *     
     */
    public void setData(Data value) {
        this.data = value;
    }

}
