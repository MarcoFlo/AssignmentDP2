package it.polito.dp2.BIB.sol1.parser;

import java.io.File;
import java.io.FileInputStream;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import it.polito.dp2.BIB.BibReader;
import it.polito.dp2.BIB.BibReaderException;
import it.polito.dp2.BIB.BookReader;
import it.polito.dp2.BIB.ItemReader;
import it.polito.dp2.BIB.JournalReader;
import it.polito.dp2.BIB.sol1.jaxb.BiblioType;

/**
 * Implementation of the BibReader interface which returns the bibliographic information read
 * from the XML file whose name is in the it.polito.dp2.BIB.sol1.BibInfo.file system property.
 * The input file must be valid against the xsd/biblio_e.xsd schema.
 *
 */
public class BibReaderImpl implements BibReader {
	private Items items;		// an object that manages the storage of items
	private Journals journals;	// an object that manages the storage of journals
	
	public BibReaderImpl() throws BibReaderException {
		// get filename from system property
		String fileName = System.getProperty("it.polito.dp2.BIB.sol1.BibInfo.file");
		if (fileName == null) {
			throw new BibReaderException("The system property it.polito.dp2.BIB.sol1.BibInfo.file is not set");
		}
		try {
			// create JAXBContext and Unmarshaller
			JAXBContext jc = JAXBContext.newInstance("it.polito.dp2.BIB.sol1.jaxb");
			Unmarshaller u = jc.createUnmarshaller();

			// set schema for validation
			SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
			u.setSchema(sf.newSchema(new File("xsd/biblio_e.xsd")));

			// perform unmarshaling
			Object obj = u.unmarshal(new FileInputStream(fileName));
			BiblioType biblio = ((JAXBElement<BiblioType>)obj).getValue();

			// store information
			journals = new Journals(biblio);
			items = new Items(biblio, journals);

		} catch (SAXException e) {
				// schema problem
				throw new BibReaderException("invalid schema for validation: " + e.getMessage());
		} catch (JAXBException | ClassCastException e) {
				// unmarshaling problem
				throw new BibReaderException("invalid input file: " + e.getMessage());
		} catch (Exception e) {
				// last chance for all the runtime exceptions
				throw new BibReaderException(e.getMessage());
		}
	}

	@Override
	public Set<ItemReader> getItems(String keyword, int since, int to) {
		return items.getItems(keyword,since,to);
	}

	@Override
	public BookReader getBook(String isbn) {
		return items.getBook(isbn);
	}

	@Override
	public Set<JournalReader> getJournals(String keyword) {
		return journals.getJournals(keyword);
	}

	@Override
	public JournalReader getJournal(String issn) {
		return journals.getJournal(issn);
	}

}
