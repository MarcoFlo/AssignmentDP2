package it.polito.dp2.BIB.sol1;


import it.polito.dp2.BIB.BookReader;
import it.polito.dp2.BIB.ItemReader;
import it.polito.dp2.BIB.JournalReader;
import it.polito.dp2.BIB.sol1.jaxb.BiblioType;
import it.polito.dp2.BIB.sol1.jaxb.ObjectFactory;
import org.xml.sax.SAXException;


import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Set;

public class BibReader implements it.polito.dp2.BIB.BibReader {
    private String schemaFilename = "xsd/biblio_e.xsd";

    public BibReader() throws Exception {
        String inputFileName = System.getProperty("it.polito.dp2.BIB.sol1.BibInfo.file", "xsd/biblio_e.xml");
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        try {
            unmarshaller.setSchema(sf.newSchema(new File(schemaFilename)));
            BiblioType biblioType = (BiblioType) ((JAXBElement<BiblioType>) unmarshaller.unmarshal(new FileInputStream(inputFileName))).getValue();

            biblioType.getBook().forEach(bookType -> System.out.println(bookType.getTitle()));

        } catch (FileNotFoundException e) {
            System.err.println("Schema file: " + schemaFilename + " not available");
        } catch (SAXException e) {
            System.err.println("Problems with the schema: " + schemaFilename);
        }
    }

    @Override
    public Set<ItemReader> getItems(String s, int i, int i1) {
        return null;
    }

    @Override
    public BookReader getBook(String s) {
        return null;
    }

    @Override
    public Set<JournalReader> getJournals(String s) {
        return null;
    }

    @Override
    public JournalReader getJournal(String s) {
        return null;
    }
}
