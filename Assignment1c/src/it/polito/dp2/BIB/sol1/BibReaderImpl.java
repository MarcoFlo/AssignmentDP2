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
    private BiblioType biblioType;
    private TreeMap<Integer, ItemReaderImpl> mapItemByYear;
    private TreeMap<String, BookReaderImpl> mapBookByIsbn;
    private HashMap<String, JournalReaderImpl> journalByIssn;

    public BibReaderImpl() throws Exception {
        System.out.println("My BibreaderImpl");
        String inputFileName = System.getProperty("it.polito.dp2.BIB.sol1.BibInfo.file", "xsd/biblio_e.xml");
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        try {
            unmarshaller.setSchema(sf.newSchema(new File(schemaFilename)));
            biblioType = ((JAXBElement<BiblioType>) unmarshaller.unmarshal(new FileInputStream(inputFileName))).getValue();
            fillDataStructure(biblioType);
            biblioType.getBook().forEach(bookType -> System.out.println(bookType.getTitle()));
        } catch (FileNotFoundException e) {
            System.err.println("Schema file: " + schemaFilename + " not available");
        } catch (SAXException e) {
            System.err.println("Problems with the schema: " + schemaFilename);
        }
    }

    private void fillDataStructure(BiblioType biblioType) {
        //journal
        journalByIssn = biblioType.getJournal().stream().map(JournalReaderImpl::new).collect(Collectors.toMap(JournalReaderImpl::getISSN, journalReader -> journalReader, (prev, next) -> next, HashMap::new));
        journalByIssn.values().forEach(journalReader -> System.out.println(journalReader.getTitle()));

        //article
        HashMap<BigInteger, ItemReader> mapItemById = biblioType.getArticle().stream().collect(Collectors.toMap(ArticleType::getId, articleType -> {
            JournalReaderImpl journalReader = journalByIssn.get(articleType.getJournal());
            IssueReaderImpl issueReader = journalReader.getIssueReaderById(articleType.getIssue());
            ArticleReaderImpl result = new ArticleReaderImpl(articleType, journalReader, issueReader);
            issueReader.addArticle(result);
            return result;
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







    }

    /**
     * Gets readers for all the items available in the BIB system whose title contains the specified keyword and whose publication year is between the specified values (between the given "since" and "to" years inclusive; if "to" is lower than "since", an empty set is returned).
     *
     * @param keyword
     * @param since
     * @param to
     * @return
     */
    @Override
    public Set<ItemReader> getItems(String keyword, int since, int to) {
        return null;
    }

    /**
     * Gets a reader for a single book, available in the BIB system, given its ISBN.
     *
     * @param isbn
     * @return
     */
    @Override
    public BookReader getBook(String isbn) {
        return null;
    }

    /**
     * Gets readers for all the journals available in the BIB system whose title or publisher contains the specified keyword
     *
     * @param keyword
     * @return
     */
    @Override
    public Set<JournalReader> getJournals(String keyword) {
        return journalByIssn.values().stream().filter(journalReader -> journalReader.getTitle().contains(keyword) || journalReader.getPublisher().contains(keyword)).collect(Collectors.toSet());
    }

    /**
     * Gets a reader for a single journal, available in the BIB system, given its ISSN.
     *
     * @param issn
     * @return
     */
    @Override
    public JournalReader getJournal(String issn) {
        return journalByIssn.get(issn);
    }
}
