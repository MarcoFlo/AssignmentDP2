//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2019.11.16 alle 06:51:42 PM CET 
//


package it.polito.dp2.BIB.sol1.jaxb;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ArticleType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArticleType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://pad.polito.it/dp2/biblio_e}BiblioItemType">
 *       &lt;attribute name="journal" use="required" type="{http://pad.polito.it/dp2/biblio_e}ISSNCode" />
 *       &lt;attribute name="issue" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArticleType")
public class ArticleType
    extends BiblioItemType
{

    @XmlAttribute(name = "journal", required = true)
    protected String journal;
    @XmlAttribute(name = "issue", required = true)
    protected BigInteger issue;

    /**
     * Recupera il valore della proprietà journal.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJournal() {
        return journal;
    }

    /**
     * Imposta il valore della proprietà journal.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJournal(String value) {
        this.journal = value;
    }

    /**
     * Recupera il valore della proprietà issue.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getIssue() {
        return issue;
    }

    /**
     * Imposta il valore della proprietà issue.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setIssue(BigInteger value) {
        this.issue = value;
    }

}
