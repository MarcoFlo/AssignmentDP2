package it.polito.dp2.BIB.sol1;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.*;

import it.polito.dp2.BIB.ArticleReader;
import it.polito.dp2.BIB.BibReader;
import it.polito.dp2.BIB.BibReaderException;
import it.polito.dp2.BIB.BibReaderFactory;
import it.polito.dp2.BIB.BookReader;
import it.polito.dp2.BIB.IssueReader;
import it.polito.dp2.BIB.ItemReader;
import it.polito.dp2.BIB.JournalReader;
import it.polito.dp2.BIB.sol1.jaxb.*;

import javax.xml.bind.*;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;


public class BibInfoSerializer {
    private BibReader monitor;
    private Integer counterBA;
    private Integer counterI;
    private XMLGregorianCalendar calendar;


    Map<String, BigInteger> mapKnownId = new HashMap<>();


    /**
     * Default constructror
     *
     * @throws BibReaderException
     */
    public BibInfoSerializer() throws BibReaderException, DatatypeConfigurationException {
        BibReaderFactory factory = BibReaderFactory.newInstance();
        monitor = factory.newBibReader();
        counterBA = 0;
        counterI = 0;
        calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar();
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
            wf.writeXml(biblioResult);
        } catch (BibReaderException e) {
            System.err.println("Could not instantiate data generator.");
            e.printStackTrace();
            System.exit(1);
        } catch (FileNotFoundException e) {
            System.err.println("Could not find output file.");
            e.printStackTrace();
        } catch (DatatypeConfigurationException e) {
            System.err.println("Could not instantiate GregorianCalendar");
            e.printStackTrace();
        }
    }

    private BiblioType generateBiblioTypeResult() {
        BiblioType biblioResult = new BiblioType();
        handleJournals(biblioResult);
        handleItems(biblioResult);

        biblioResult.getBook().forEach(bookType -> System.out.println(bookType.getTitle()));
        biblioResult.getArticle().forEach(articleType -> System.out.println(articleType.getTitle()));
        biblioResult.getJournal().forEach(journalType -> System.out.println(journalType.getTitle()));
        return biblioResult;
    }


    public void writeXml(BiblioType biblioResult) throws FileNotFoundException {
        String outputFileName = System.getProperty("it.polito.dp2.BIB.Random.output", "xsd/biblio_e.xml");
        System.out.println(outputFileName);
        ObjectFactory objectFactory = new ObjectFactory();
        JAXBElement<BiblioType> biblio = objectFactory.createBiblio(biblioResult);

//        FileOutputStream fileOutputStream = new FileOutputStream(new File(outputFileName));

        try {
            JAXBContext context = JAXBContext.newInstance("it.polito.dp2.BIB.sol1.jaxb");
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.marshal(biblio, new File(outputFileName));
        } catch (JAXBException e) {
            System.err.println("JAXB marshal error.");
            e.printStackTrace();
        }
    }

    private BiblioType handleItems(BiblioType biblioResult) {
        Set<ItemReader> set = monitor.getItems(null, 0, 3000);

        BookType bookType;
        ArticleType articleType;
        for (ItemReader item : set) {
            bookType = new BookType();
            articleType = new ArticleType();


            if (item instanceof ArticleReader) {
                ArticleReader articleSource = (ArticleReader) item;

                //article attribute
                if (mapKnownId.containsKey(articleSource.getTitle()))
                    articleType.setId(mapKnownId.get(articleSource.getTitle()));
                else
                    articleType.setId(BigInteger.valueOf(counterBA++));

                articleType.setJournal(articleSource.getJournal().getISSN());
                articleType.setIssue(BigInteger.valueOf(articleSource.getIssue().getNumber()));

                //article element
                articleType.getAuthor().addAll(Arrays.asList(articleSource.getAuthors()));
                articleType.setTitle(articleSource.getTitle());
                articleType.setSubtitle(articleSource.getSubtitle());

                for (ItemReader citing : articleSource.getCitingItems()) {
                    if (mapKnownId.containsKey(citing.getTitle())) {
                        articleType.getCitedBy().add(mapKnownId.get(citing.getTitle()));
                    } else {
                        mapKnownId.put(citing.getTitle(), BigInteger.valueOf(counterBA++));
                        articleType.getCitedBy().add(BigInteger.valueOf(counterBA));
                    }
                }

                biblioResult.getArticle().add(articleType);
            }
            if (item instanceof BookReader) {
                BookReader bookSource = (BookReader) item;

                //book attribute
                if (mapKnownId.containsKey(bookSource.getTitle()))
                    bookType.setId(mapKnownId.get(bookSource.getTitle()));
                else
                    bookType.setId(BigInteger.valueOf(counterBA++));

                bookType.setISBN(bookSource.getISBN());
                calendar.clear();
                calendar.setYear(bookSource.getYear());
                bookType.setYear(calendar);

                //book element
                bookType.getAuthor().addAll(Arrays.asList(bookSource.getAuthors()));
                bookType.setTitle(bookSource.getTitle());
                bookType.setSubtitle(bookSource.getSubtitle());

                for (ItemReader citing : bookSource.getCitingItems()) {
                    if (mapKnownId.containsKey(citing.getTitle())) {
                        bookType.getCitedBy().add(mapKnownId.get(citing.getTitle()));
                    } else {
                        mapKnownId.put(citing.getTitle(), BigInteger.valueOf(counterBA++));
                        bookType.getCitedBy().add(BigInteger.valueOf(counterBA));
                    }
                }

                bookType.setPublisher(bookSource.getPublisher());

                biblioResult.getBook().add(bookType);
            }
        }

        return biblioResult;
    }


    private BiblioType handleJournals(BiblioType biblioType) {
        Set<JournalReader> set = monitor.getJournals(null);
        JournalType journalType;
        for (JournalReader journalSource : set) {
            journalType = new JournalType();

            //journal attribute
            journalType.setISSN(journalSource.getISSN());

            //journal element
            journalType.setTitle(journalSource.getTitle());
            journalType.setPublisher(journalSource.getPublisher());

            JournalType.Issue issueType;
            for (IssueReader issueSource : journalSource.getIssues(0, 3000)) {
                issueType = new JournalType.Issue();

                calendar.clear();
                calendar.setYear(issueSource.getYear());
                issueType.setYear(calendar);
                System.out.println(calendar + "   " + issueSource.getYear());

                issueType.setNumber(BigInteger.valueOf(issueSource.getNumber()));
                issueType.setId(BigInteger.valueOf(counterI++));

                journalType.getIssue().add(issueType);
            }

            biblioType.getJournal().add(journalType);
        }

        return biblioType;
    }

}
