package it.polito.dp2.BIB.sol1;


import it.polito.dp2.BIB.*;
import it.polito.dp2.BIB.sol1.jaxb.*;
import org.xml.sax.SAXException;


import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class BibReaderImpl implements it.polito.dp2.BIB.BibReader {
    private String schemaFilename = "xsd/biblio_e.xsd";
    private TreeMap<Integer, List<ItemReader>> mapItemByYear;
    private TreeMap<String, BookReaderImpl> mapBookByIsbn;
    private HashMap<String, JournalReaderImpl> journalByIssn;

    public BibReaderImpl() throws Exception {
        mapItemByYear = new TreeMap<>();
        mapBookByIsbn = new TreeMap<>();
        String inputFileName = System.getProperty("it.polito.dp2.BIB.sol1.BibInfo.file", "xsd/biblio_e.xml");
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        try {
            unmarshaller.setSchema(sf.newSchema(new File(schemaFilename)));
            BiblioType biblioType = ((JAXBElement<BiblioType>) unmarshaller.unmarshal(new FileInputStream(inputFileName))).getValue();
            fillDataStructure(biblioType);

        } catch (FileNotFoundException e) {
            System.err.println("Schema file: " + schemaFilename + " not available");
        } catch (SAXException e) {
            System.err.println("Problems with the schema: " + schemaFilename);
        }
    }

    private void fillDataStructure(BiblioType biblioType) {
        //journal
        journalByIssn = biblioType.getJournal().stream().map(JournalReaderImpl::new).collect(Collectors.toMap(JournalReaderImpl::getISSN, journalReader -> journalReader, (prev, next) -> next, HashMap::new));

        //article
        HashMap<BigInteger, ItemReader> mapItemById = biblioType.getArticle().stream().collect(Collectors.toMap(ArticleType::getId, articleType -> {
            JournalReaderImpl journalReader = journalByIssn.get(articleType.getJournal());
            IssueReaderImpl issueReader = journalReader.getIssueReaderById(articleType.getIssue());
            ArticleReaderImpl articleReader = new ArticleReaderImpl(articleType, journalReader, issueReader);
            issueReader.addArticle(articleReader);
            return articleReader;
        }, (prev, post) -> post, HashMap::new));

        //book
        mapItemById.putAll(biblioType.getBook().stream().collect(Collectors.toMap(BookType::getId, BookReaderImpl::new, (prev, post) -> post, HashMap::new)));

        //citing
        biblioType.getArticle().forEach(articleType -> {
            articleType.getCitedBy().forEach(citedId ->
                    ((ArticleReaderImpl) mapItemById.get(articleType.getId())).addCitingItem(mapItemById.get(citedId)));
        });

        biblioType.getBook().forEach(bookType -> {
            bookType.getCitedBy().forEach(citedId ->
                    ((BookReaderImpl) mapItemById.get(bookType.getId())).addCitingItem(mapItemById.get(citedId)));
        });


        //map construction
        List<ItemReader> list;
        for (ItemReader itemReader : mapItemById.values()) {
            if (itemReader instanceof ArticleReaderImpl) {
                ArticleReaderImpl articleReader = (ArticleReaderImpl) itemReader;
                list = mapItemByYear.getOrDefault(articleReader.getIssue().getYear(), new LinkedList<>());
                list.add(itemReader);
                mapItemByYear.put(articleReader.getIssue().getYear(), list);
            }
            if (itemReader instanceof BookReaderImpl) {
                BookReaderImpl bookReader = (BookReaderImpl) itemReader;
                list = mapItemByYear.getOrDefault(bookReader.getYear(), new LinkedList<>());
                list.add(itemReader);
                mapItemByYear.put(bookReader.getYear(), list);
                mapBookByIsbn.put(bookReader.getISBN(), bookReader);
            }
        }
    }

    /**
     * Gets readers for all the items available in the BIB system whose title contains the specified keyword and whose publication year is between the specified values (between the given "since" and "to" years inclusive; if "to" is lower than "since", an empty set is returned).
     *
     * @param keyword the title keyword for selecting items or null to get all items.
     * @param since   the publication year since which (inclusive) items have to be selected, or null to get items published since any year.
     * @param to      the publication date to which (inclusive) items have to be selected, or null to get items published to any year.
     * @return a set of interfaces for reading the selected items.
     */
    @Override
    public Set<ItemReader> getItems(String keyword, int since, int to) {
        boolean isNotNull = keyword != null;
        if (to < since)
            return Collections.emptySet();

        return mapItemByYear.subMap(since, to + 1).values().stream().flatMap(Collection::parallelStream).filter(itemReader -> itemReader.getTitle().equals(keyword) == isNotNull).collect(Collectors.toSet());
    }

    /**
     * Gets a reader for a single book, available in the BIB system, given its ISBN.
     *
     * @param isbn the ISBN of the book to get.
     * @return an interface for reading the book with the given ISBN or null if a book with the given ISBN is not available in the system.
     */
    @Override
    public BookReader getBook(String isbn) {
        return mapBookByIsbn.get(isbn);
    }

    /**
     * Gets readers for all the journals available in the BIB system whose title or publisher contains the specified keyword
     *
     * @param keyword the title/publisher keyword or null to get readers for all journals
     * @return a set of interfaces for reading the selected journals
     */
    @Override
    public Set<JournalReader> getJournals(String keyword) {
        if (keyword == null)
            return new HashSet<>(journalByIssn.values());

        return journalByIssn.values().stream().filter(journalReader -> journalReader.getTitle().contains(keyword) || journalReader.getPublisher().contains(keyword)).collect(Collectors.toSet());
    }

    /**
     * Gets a reader for a single journal, available in the BIB system, given its ISSN.
     *
     * @param issn the title/publisher keyword or null to get readers for all journals
     * @return an interface for reading the journal with the given ISSN or null if a journal with the given ISSN is not available in the system.
     */
    @Override
    public JournalReader getJournal(String issn) {
        return journalByIssn.get(issn);
    }
}
