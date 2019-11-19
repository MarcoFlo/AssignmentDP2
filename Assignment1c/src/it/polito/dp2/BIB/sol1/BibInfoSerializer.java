package it.polito.dp2.BIB.sol1;

import java.io.File;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import it.polito.dp2.BIB.ArticleReader;
import it.polito.dp2.BIB.BibReader;
import it.polito.dp2.BIB.BibReaderException;
import it.polito.dp2.BIB.BibReaderFactory;
import it.polito.dp2.BIB.BookReader;
import it.polito.dp2.BIB.IssueReader;
import it.polito.dp2.BIB.ItemReader;
import it.polito.dp2.BIB.JournalReader;
import it.polito.dp2.BIB.sol1.jaxb.*;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.validation.SchemaFactory;


public class BibInfoSerializer {
    private BibReader monitor;


    /**
     * Default constructror
     *
     * @throws BibReaderException
     */
    public BibInfoSerializer() throws BibReaderException {
        BibReaderFactory factory = BibReaderFactory.newInstance();
        monitor = factory.newBibReader();
    }

    public BibInfoSerializer(BibReader monitor) {
        super();
        this.monitor = monitor;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        BibInfoSerializer wf;
        try {
            wf = new BibInfoSerializer();
            BiblioType biblioResult = wf.generateBiblioTypeResult();
            wf.writeXml(biblioResult, args[0]);
        } catch (BibReaderException e) {
            System.err.println("Could not instantiate data generator.");
            e.printStackTrace();
            System.exit(1);
        } catch (DatatypeConfigurationException e) {
            System.err.println("Could not instantiate XMLGregorianCalendar");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Handles the generation of the BiblioType object, starting from the journal to retrieve the issueId necessary to the article
     *
     * @return
     * @throws DatatypeConfigurationException
     */
    private BiblioType generateBiblioTypeResult() throws DatatypeConfigurationException, BibReaderException {
        BiblioType biblioResult = new BiblioType();
        handleJournals(biblioResult);

        return biblioResult;
    }

    /**
     * Handles the marshaling operation, writing the xml output to the given file
     *
     * @param biblioResult
     * @param outputFileName
     */
    private void writeXml(BiblioType biblioResult, String outputFileName) {
        ObjectFactory objectFactory = new ObjectFactory();
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        JAXBElement<BiblioType> biblio = objectFactory.createBiblio(biblioResult);

        try {
            JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setSchema(schemaFactory.newSchema(new File("xsd/biblio_e.xsd")));
            marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,
                    "http://pad.polito.it/dp2/biblio_e biblio_e.xsd");
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(biblio, new File(outputFileName));
        } catch (JAXBException e) {
            System.err.println("JAXB marshal error.");
            e.printStackTrace();
            System.exit(1);
        } catch (SAXException e) {
            System.err.println("Schema loading error.");
            e.printStackTrace();
            System.exit(1);
        }
    }


    /**
     * Handles the generation of journal
     * Uses counterI to produce an unique id for the issues and mapIssueID to store them for the article
     * Starts the handleItems function
     *
     * @param biblioType
     * @throws DatatypeConfigurationException
     */
    private void handleJournals(BiblioType biblioType) throws DatatypeConfigurationException, BibReaderException {
        Set<JournalReader> set = monitor.getJournals(null);
        int counterI = 0;
        XMLGregorianCalendar calendar;
        JournalType journalType;
        Map<String, Integer> mapIssueID = new HashMap<>(); // key = ISSN + issue year + issue number

        for (JournalReader journalSource : set) {
            journalType = new JournalType();

            //journal attribute
            journalType.setISSN(journalSource.getISSN());

            //journal element
            journalType.setTitle(journalSource.getTitle());
            journalType.setPublisher(journalSource.getPublisher());

            JournalType.Issue issueType;
            List<IssueReader> issueReaderSet = journalSource.getIssues(0, 3000).stream().sorted(Comparator.comparingInt(IssueReader::getYear).thenComparing(IssueReader::getNumber)).collect(Collectors.toList());

            for (IssueReader issueSource : issueReaderSet) {
                issueType = new JournalType.Issue();

                calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar();
                calendar.setYear(issueSource.getYear());
                issueType.setYear(calendar);

                issueType.setNumber(BigInteger.valueOf(issueSource.getNumber()));
                issueType.setId(BigInteger.valueOf(counterI));

                mapIssueID.put(journalType.getISSN() + issueType.getYear() + issueType.getNumber(), counterI++);
                journalType.getIssue().add(issueType);
            }

            biblioType.getJournal().add(journalType);
        }
//        biblioType.getJournal().sort(Comparator.comparing(JournalType::getTitle));
        handleItems(biblioType, mapIssueID);
    }


    /**
     * Handles the generation of book and article starting from an item
     * Uses counterBA to produce an unique id and mapItemId to store the id already produced for an item citedBy
     *
     * @param biblioResult
     * @param mapIssueID
     * @throws DatatypeConfigurationException
     */
    private void handleItems(BiblioType biblioResult, Map<String, Integer> mapIssueID) throws DatatypeConfigurationException, BibReaderException {
        Set<ItemReader> set = monitor.getItems(null, 0, 3000);
        XMLGregorianCalendar calendar;
        int counterBA = 0;
        Map<String, BigInteger> mapItemID = new HashMap<>();
        BookType bookType;
        ArticleType articleType;

        for (ItemReader item : set) {
            bookType = new BookType();
            articleType = new ArticleType();

            if (item instanceof ArticleReader) {
                ArticleReader articleSource = (ArticleReader) item;

                //article attribute
                if (mapItemID.containsKey(articleSource.getTitle())) {
                    articleType.setId(mapItemID.get(articleSource.getTitle()));
                } else {
                    articleType.setId(BigInteger.valueOf(counterBA));
                    mapItemID.put(articleSource.getTitle(), BigInteger.valueOf(counterBA));
                    counterBA++;
                }
                articleType.setJournal(articleSource.getJournal().getISSN());

                String issueKey = articleType.getJournal() + articleSource.getIssue().getYear() + articleSource.getIssue().getNumber();
                if (mapIssueID.containsKey(issueKey))
                    articleType.setIssue(BigInteger.valueOf(mapIssueID.get(issueKey)));
                else {
                    System.err.println("Article issue ref not valid");
                    throw new BibReaderException();
                }
                //article element
                articleType.getAuthor().addAll(Arrays.asList(articleSource.getAuthors()));
                articleType.setTitle(articleSource.getTitle());
                articleType.setSubtitle(articleSource.getSubtitle());


                for (ItemReader citing : articleSource.getCitingItems()) {
                    if (mapItemID.containsKey(citing.getTitle())) {
                        articleType.getCitedBy().add(mapItemID.get(citing.getTitle()));
                    } else {
                        mapItemID.put(citing.getTitle(), BigInteger.valueOf(counterBA));
                        articleType.getCitedBy().add(BigInteger.valueOf(counterBA));
                        counterBA++;

                    }
                }

                biblioResult.getArticle().add(articleType);
            }
            if (item instanceof BookReader) {
                BookReader bookSource = (BookReader) item;

                //book attribute
                if (mapItemID.containsKey(bookSource.getTitle())) {
                    bookType.setId(mapItemID.get(bookSource.getTitle()));
                } else {
                    bookType.setId(BigInteger.valueOf(counterBA));
                    mapItemID.put(bookSource.getTitle(), BigInteger.valueOf(counterBA));
                    counterBA++;
                }
                bookType.setISBN(bookSource.getISBN());

                calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar();
                calendar.setYear(bookSource.getYear());
                bookType.setYear(calendar);

                //book element
                bookType.getAuthor().addAll(Arrays.asList(bookSource.getAuthors()));
                bookType.setTitle(bookSource.getTitle());
                bookType.setSubtitle(bookSource.getSubtitle());

                for (ItemReader citing : bookSource.getCitingItems()) {
                    if (mapItemID.containsKey(citing.getTitle())) {
                        bookType.getCitedBy().add(mapItemID.get(citing.getTitle()));
                    } else {
                        mapItemID.put(citing.getTitle(), BigInteger.valueOf(counterBA));
                        bookType.getCitedBy().add(BigInteger.valueOf(counterBA));
                        counterBA++;
                    }
                }
                bookType.setPublisher(bookSource.getPublisher());

                biblioResult.getBook().add(bookType);
            }
        }
//        biblioResult.getArticle().sort(Comparator.comparing(ArticleType::getTitle));
//        biblioResult.getBook().sort(Comparator.comparing(BookType::getTitle));
    }
}
