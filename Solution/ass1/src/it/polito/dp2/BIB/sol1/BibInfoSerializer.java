package it.polito.dp2.BIB.sol1;

import java.io.File;
import java.math.BigInteger;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import it.polito.dp2.BIB.ArticleReader;
import it.polito.dp2.BIB.BibReader;
import it.polito.dp2.BIB.BibReaderException;
import it.polito.dp2.BIB.BibReaderFactory;
import it.polito.dp2.BIB.BookReader;
import it.polito.dp2.BIB.IssueReader;
import it.polito.dp2.BIB.ItemReader;
import it.polito.dp2.BIB.JournalReader;
import it.polito.dp2.BIB.sol1.jaxb.ArticleType;
import it.polito.dp2.BIB.sol1.jaxb.BiblioItemsType;
import it.polito.dp2.BIB.sol1.jaxb.BiblioJournalsType;
import it.polito.dp2.BIB.sol1.jaxb.BiblioType;
import it.polito.dp2.BIB.sol1.jaxb.ItemType;
import it.polito.dp2.BIB.sol1.jaxb.BookType;
import it.polito.dp2.BIB.sol1.jaxb.JournalType;
import it.polito.dp2.BIB.sol1.jaxb.ObjectFactory;
import it.polito.dp2.BIB.sol1.serializer.Items;
import it.polito.dp2.BIB.sol1.serializer.Journals;

// Serializer that reads bibliographic information from a data source
// and stores it into an XML document
public class BibInfoSerializer {
	private BibReader monitor;			// the main interface for reading the data to be serialized
	private IdManager issueIdManager;	// id manager used for issues
	private IdManager itemIdManager;	// id manager used for items
	private Journals journals;			// an object used to manage journal information
	private Items items;				// an object used to manage item information
	
	/**
	 * Default constructor: builds an instance of the serializer that uses a data source built within the constructor itself
	 * @throws BibReaderException if the data source cannot be instantiated
	 */
	public BibInfoSerializer() throws BibReaderException {
		BibReaderFactory factory = BibReaderFactory.newInstance();
		monitor = factory.newBibReader();
		issueIdManager = new IdManager();	
		itemIdManager = new IdManager();	
		journals = new Journals();
		items = new Items();
	}
	
	/**
	 * Constructor that builds an instance of the serializer that uses a given data source
	 * @param monitor the data source
	 */
	public BibInfoSerializer(BibReader monitor) {
		super();
		this.monitor = monitor;
	}

	/**
	 * Tha main that starts the serializer application
	 * @param args the command line arguments (the only argument is the output file name)
	 */
	public static void main(String[] args) {
		if (args.length!=1) {
			System.out.println("Wrong number of arguments.");
			System.out.println("Usage: java BibInfoSerializer filename");
			System.exit(1);
		}
		BibInfoSerializer wf;
		try {
			wf = new BibInfoSerializer();
			wf.serialize(args[0]);
		} catch (BibReaderException e) {
			System.err.println("Could not instantiate data generator.");
			e.printStackTrace();
			System.exit(1);
		} catch (JAXBException e) {
			System.err.println("Could not serialize data.");
			e.printStackTrace();
			System.exit(1);
		} catch (DatatypeConfigurationException e) {
			System.err.println("The system is not properly configured.");
			e.printStackTrace();
			System.exit(1);
		}
	}


	/**
	 * Serialize the data read from the data source into the specified file
	 * @param filename the name of the output file
	 * @throws JAXBException if an error occurs during serialization
	 * @throws DatatypeConfigurationException if the system is not properly configured
	 */
	public void serialize(String filename) throws JAXBException, DatatypeConfigurationException {
		createJournals();
		createItems();
		ObjectFactory of = new ObjectFactory();
		BiblioType biblio = new BiblioType();
		BiblioItemsType itemsType = new BiblioItemsType();
		BiblioJournalsType journalsType = new BiblioJournalsType();
		biblio.setItems(itemsType);
		biblio.setJournals(journalsType);
		itemsType.getArticleOrBook().addAll(items.getItems());
		journalsType.getJournal().addAll(journals.getJournals());
        // create a JAXBContext
        JAXBContext jc = JAXBContext.newInstance( "it.polito.dp2.BIB.sol1.jaxb" );
        Marshaller m = jc.createMarshaller();
        m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
        m.marshal( of.createBiblio(biblio), new File(filename) );
	}
	
	// Private class that manages integer ids by means of a counter
	private class IdManager {
		private long counter=0;
		
		// returns the next integer id to use
		public long getNextId() {
			return counter++;
		}	
	}
	
	// Gather and store journal information
	private void createJournals() throws DatatypeConfigurationException {
		// Get info for all journals from data source
		Set<JournalReader> set = monitor.getJournals(null);
		
		// For each journal, create and fill JournalType object and store it
		for (JournalReader journal:set) {
			JournalType jtype = new JournalType();
			jtype.setISSN(journal.getISSN());
			jtype.setTitle(journal.getTitle());
			jtype.setPublisher(journal.getPublisher());
			List<JournalType.Issue> list = jtype.getIssue();
			for (IssueReader issue: journal.getIssues(0, 3000)) {
				JournalType.Issue itype = new JournalType.Issue();
				itype.setYear(getXMLGregorianCalendar(issue.getYear()));
				itype.setNumber(BigInteger.valueOf(issue.getNumber()));
				itype.setId(BigInteger.valueOf(issueIdManager.getNextId()));
				list.add(itype);
			}
			journals.putJournal(jtype);
		}	
	}

	// Create an XMLGregorianCalendar object with only the year set to the specified value
	private XMLGregorianCalendar getXMLGregorianCalendar(int year) throws DatatypeConfigurationException {
		XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
		cal.setYear(year);
	    return cal;
	}

	// Gather and store item information
	private void createItems() throws DatatypeConfigurationException {
		// Get info for all items from data source
		Set<ItemReader> set = monitor.getItems(null, 0, 3000);
		
		// For each item, create and fill ItemType object and store it
		for (ItemReader item: set) {
			ItemType itype=null;
			if (item instanceof ArticleReader) {
				ArticleReader article = (ArticleReader) item;
				ArticleType atype = new ArticleType();
				String issn = article.getJournal().getISSN();
				atype.setJournal(issn);
				atype.setIssue(journals.getIssueId(issn, article.getIssue().getNumber()));
				itype = atype;
			} else if (item instanceof BookReader) {
				BookReader book = (BookReader) item;
				BookType btype = new BookType();
				btype.setISBN(book.getISBN());
				btype.setYear(getXMLGregorianCalendar(book.getYear()));
				btype.setPublisher(book.getPublisher());
				itype = btype;
			}
			itype.setTitle(item.getTitle());
			itype.setSubtitle(item.getSubtitle());
			List<String> authorList = itype.getAuthor();
			String[] authors = item.getAuthors();
			for (int i=0; i<authors.length; i++)
				authorList.add(authors[i]);
			BigInteger id = BigInteger.valueOf(itemIdManager.getNextId());
			itype.setId(id);
			items.addItem(item, itype);	
		}
		
		// For each item, add citation information
		for (ItemReader item: set) {
			ItemType btype = items.getItemType(item);
			List<BigInteger> list = btype.getCitedBy();
			for (ItemReader citing: item.getCitingItems()) {
				list.add(items.getItemType(citing).getId());
			}
		}
	
	}

}
