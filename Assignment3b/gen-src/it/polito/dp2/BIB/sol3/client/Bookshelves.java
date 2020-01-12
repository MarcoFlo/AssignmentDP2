
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
 *     &lt;extension base="{}pagingType">
 *       &lt;sequence>
 *         &lt;element name="bookshelf" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="self" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *                   &lt;element name="readCountUri" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *                   &lt;element name="itemsUri" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *                   &lt;element name="item" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="self" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *                             &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *                             &lt;element name="author" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
 *                             &lt;element name="title" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="subtitle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                             &lt;element name="article" type="{}ArticleType" minOccurs="0"/>
 *                             &lt;element name="book" type="{}BookType" minOccurs="0"/>
 *                             &lt;element name="citedBy" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *                             &lt;element name="citations" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *                             &lt;element name="targets" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *                 &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="readCount" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
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

    @XmlElement(nillable = true)
    protected List<Bookshelves.Bookshelf> bookshelf;

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
     * {@link Bookshelves.Bookshelf }
     * 
     * 
     */
    public List<Bookshelves.Bookshelf> getBookshelf() {
        if (bookshelf == null) {
            bookshelf = new ArrayList<Bookshelves.Bookshelf>();
        }
        return this.bookshelf;
    }


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
     *         &lt;element name="item" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="self" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
     *                   &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
     *                   &lt;element name="author" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
     *                   &lt;element name="title" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="subtitle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                   &lt;element name="article" type="{}ArticleType" minOccurs="0"/>
     *                   &lt;element name="book" type="{}BookType" minOccurs="0"/>
     *                   &lt;element name="citedBy" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
     *                   &lt;element name="citations" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
     *                   &lt;element name="targets" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
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
    public static class Bookshelf {

        @XmlSchemaType(name = "anyURI")
        protected String self;
        @XmlSchemaType(name = "anyURI")
        protected String readCountUri;
        @XmlSchemaType(name = "anyURI")
        protected String itemsUri;
        @XmlElement(nillable = true)
        protected List<Bookshelves.Bookshelf.Item> item;
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
         * {@link Bookshelves.Bookshelf.Item }
         * 
         * 
         */
        public List<Bookshelves.Bookshelf.Item> getItem() {
            if (item == null) {
                item = new ArrayList<Bookshelves.Bookshelf.Item>();
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
         *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
         *         &lt;element name="author" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
         *         &lt;element name="title" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="subtitle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *         &lt;element name="article" type="{}ArticleType" minOccurs="0"/>
         *         &lt;element name="book" type="{}BookType" minOccurs="0"/>
         *         &lt;element name="citedBy" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
         *         &lt;element name="citations" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
         *         &lt;element name="targets" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
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
            "self",
            "id",
            "author",
            "title",
            "subtitle",
            "article",
            "book",
            "citedBy",
            "citations",
            "targets"
        })
        public static class Item {

            @XmlSchemaType(name = "anyURI")
            protected String self;
            protected BigInteger id;
            @XmlElement(required = true)
            protected List<String> author;
            @XmlElement(required = true)
            protected String title;
            protected String subtitle;
            protected ArticleType article;
            protected BookType book;
            @XmlSchemaType(name = "anyURI")
            protected String citedBy;
            @XmlSchemaType(name = "anyURI")
            protected String citations;
            @XmlSchemaType(name = "anyURI")
            protected String targets;

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
             * {@link String }
             * 
             * 
             */
            public List<String> getAuthor() {
                if (author == null) {
                    author = new ArrayList<String>();
                }
                return this.author;
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

            /**
             * Gets the value of the article property.
             * 
             * @return
             *     possible object is
             *     {@link ArticleType }
             *     
             */
            public ArticleType getArticle() {
                return article;
            }

            /**
             * Sets the value of the article property.
             * 
             * @param value
             *     allowed object is
             *     {@link ArticleType }
             *     
             */
            public void setArticle(ArticleType value) {
                this.article = value;
            }

            /**
             * Gets the value of the book property.
             * 
             * @return
             *     possible object is
             *     {@link BookType }
             *     
             */
            public BookType getBook() {
                return book;
            }

            /**
             * Sets the value of the book property.
             * 
             * @param value
             *     allowed object is
             *     {@link BookType }
             *     
             */
            public void setBook(BookType value) {
                this.book = value;
            }

            /**
             * Gets the value of the citedBy property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCitedBy() {
                return citedBy;
            }

            /**
             * Sets the value of the citedBy property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCitedBy(String value) {
                this.citedBy = value;
            }

            /**
             * Gets the value of the citations property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCitations() {
                return citations;
            }

            /**
             * Sets the value of the citations property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCitations(String value) {
                this.citations = value;
            }

            /**
             * Gets the value of the targets property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getTargets() {
                return targets;
            }

            /**
             * Sets the value of the targets property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setTargets(String value) {
                this.targets = value;
            }

        }

    }

}
