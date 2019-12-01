package it.polito.dp2.BIB.sol2;

import it.polito.dp2.BIB.sol2.jaxb.crossref.CrossrefMessage;
import it.polito.dp2.BIB.sol2.jaxb.crossref.CrossrefResult;
import it.polito.dp2.rest.gbooks.client.Factory;
import it.polito.dp2.rest.gbooks.client.MyErrorHandler;
import it.polito.dp2.rest.gbooks.client.jaxb.Items;
import it.polito.dp2.rest.gbooks.client.jaxb.SearchResult;
import it.polito.dp2.xml.biblio.PrintableItem;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.util.JAXBSource;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;


public class BookClient_e {
    private int pageSize = 20; //the first get will always be 20 items also if only 5 is needed, I made this decision due to the large number of invalid items

    private JAXBContext jaxbGoogleContext;
    private JAXBContext jaxbCrossrefContext;

    private Validator validatorGoogle;
    private Validator validatorCrossref;


    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: java BookClient_e N keyword1 keyword2 ...");
//            System.exit(1);
        }

        try {
            BookClient_e bclient = new BookClient_e();
            QueryParameter queryParameter = new QueryParameter(new String[]{"5", "antoness"});
            bclient.PerformGoogleSearch(queryParameter);
            bclient.PerformCrossrefSearch(queryParameter);
        } catch (NumberFormatException ne) {
            System.err.println("First parameter should be an integer");
            System.exit(1);
        } catch (Exception ex) {
            System.err.println("Error during execution of operation");
            ex.printStackTrace(System.out);
        }
    }

    public BookClient_e() throws Exception {
        // create validator that uses the DataTypes schema
        SchemaFactory sf = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
        validatorGoogle = sf.newSchema(new File("xsd/gbooks/DataTypes.xsd")).newValidator();
        validatorGoogle.setErrorHandler(new MyErrorHandler());

        validatorCrossref = sf.newSchema(new File("xsd/CrossrefDataTypes.xsd")).newValidator();
        validatorCrossref.setErrorHandler(new MyErrorHandler());

        // create JAXB context related to the classed generated from the DataTypes schema
        jaxbGoogleContext = JAXBContext.newInstance("it.polito.dp2.rest.gbooks.client.jaxb");

        // create JAXB context related to the classed generated from the CrossrefDataTypes schema
        jaxbCrossrefContext = JAXBContext.newInstance("it.polito.dp2.BIB.sol2.jaxb.crossref");
    }

    public void PerformGoogleSearch(QueryParameter queryParameter) {
        System.out.println("Searching " + queryParameter.getGoogleKeyword() + " on Google Books:");

        // build the JAX-RS client object
        Client client = ClientBuilder.newClient();

        // build the web target
        WebTarget target = client.target(getGoogleBaseURI()).path("volumes");
        List<PrintableItem> printableItems = new ArrayList<>();
        Response response;
        SearchResult result;

        int count = 0;
        int availableItems = 1;
        int id = 0;
        while (printableItems.size() != queryParameter.getItemNumber() && availableItems > count * pageSize) {
            response = target
                    .queryParam("startIndex", count * pageSize)
                    .queryParam("maxResults", pageSize) //max 40
                    .queryParam("q", queryParameter.getGoogleKeyword())
                    .queryParam("printType", "books")
                    .request()
                    .accept(MediaType.APPLICATION_JSON)
                    .get();
            if (response.getStatus() != 200) {
                System.out.println("Error in remote operation: " + response.getStatus() + " " + response.getStatusInfo());
                client.close();
                return;
            }
            response.bufferEntity();
            System.out.println("Response as string: " + response.readEntity(String.class));
            result = response.readEntity(SearchResult.class);
            count++;
            availableItems = result.getTotalItems().intValue();

            System.out.println("OK Response received. Items:" + result.getTotalItems());
            System.out.println("Validating items and converting validated items to xml.");


            for (Items item : result.getItems()) {
                if (printableItems.size() != queryParameter.getItemNumber()) {
                    try {
                        // validate item
                        JAXBSource source = new JAXBSource(jaxbGoogleContext, item);
                        System.out.println("Validating " + item.getSelfLink());
                        validatorGoogle.validate(source);
                        System.out.println("Validation OK");
                        // add item to list
                        System.out.println("Adding item to list");
                        printableItems.add(Factory.createPrintableItem(BigInteger.valueOf(id++), item.getVolumeInfo()));
                    } catch (org.xml.sax.SAXException se) {
                        System.out.println("Validation Failed");
                        // print error messages
                        Throwable t = se;
                        while (t != null) {
                            String message = t.getMessage();
                            if (message != null)
                                System.out.println(message);
                            t = t.getCause();
                        }
                    } catch (IOException e) {
                        System.out.println("Unexpected I/O Exception");
                    } catch (JAXBException e) {
                        System.out.println("Unexpected JAXB Exception");
                    }
                } else
                    break;
            }
        }
        client.close();
        System.out.println("Validated Bibliography items: " + printableItems.size());
        for (PrintableItem item : printableItems)
            item.print();
        System.out.println("End of Validated Bibliography items");
    }

    public void PerformCrossrefSearch(QueryParameter queryParameter) {
        // build the JAX-RS client object
        Client client = ClientBuilder.newClient();

        // build the web target
        WebTarget target = client.target(getCrossrefBooksUri());


        int id = 0;
        String cursor = "*";

        List<PrintableItem> printableItems = new ArrayList<>();
        Response response;
        CrossrefMessage message;
        System.out.println("Searching " + queryParameter.getCrossrefKeyword() + " on Crossref:");

        while (printableItems.size() != queryParameter.getItemNumber()) {
            response = target
                    .queryParam("rows", pageSize)
                    .queryParam("cursor", cursor)
                    .queryParam("select", "publisher,ISBN,published-print,title,subtitle,author") // to avoid large number of unnecessary fields
                    .queryParam("query.bibliographic", queryParameter.getCrossrefKeyword()) //look up into titles, authors, ISSNs and publication years
                    .request()
                    .accept(MediaType.APPLICATION_JSON)
                    .get();
            if (response.getStatus() != 200) {
                System.out.println("Crossref error in remote operation: " + response.getStatus() + " " + response.getStatusInfo());
                client.close();
                return;
            }

            response.bufferEntity();
            System.out.println("Response as string: " + response.readEntity(String.class));
            message = response.readEntity(CrossrefResult.class).getMessage();
            cursor = message.getNextCursor();

            // if no more items
            if (message.getItems().isEmpty())
                break;

            System.out.println("OK Response received. Items:" + message.getTotalResults());

            System.out.println("Validating items and converting validated items to xml.");

            for (it.polito.dp2.BIB.sol2.jaxb.crossref.Items item : message.getItems()) {
                if (printableItems.size() != queryParameter.getItemNumber()) {
                    try {
                        // validate item
                        JAXBSource source = new JAXBSource(jaxbCrossrefContext, item);
                        validatorCrossref.validate(source);
                        System.out.println("Validation OK");
                        // add item to list
                        System.out.println("Adding item to list");
                        printableItems.add(CrossrefFactory.createPrintableItem(BigInteger.valueOf(id++), item));
                    } catch (org.xml.sax.SAXException se) {
                        System.out.println("Validation Failed");
                        // print error messages
                        Throwable t = se;
                        while (t != null) {
                            String msg = t.getMessage();
                            if (msg != null)
                                System.out.println(msg);
                            t = t.getCause();
                        }
                    } catch (IOException e) {
                        System.out.println("Unexpected I/O Exception");
                    } catch (JAXBException e) {
                        System.out.println("Unexpected JAXB Exception");
                    } catch (DatatypeConfigurationException e) {
                        System.out.println("Unexpected calendar exception");
                    }
                } else
                    break;
            }
        }
        client.close();

        System.out.println("Validated Bibliography items: " + printableItems.size());
        for (PrintableItem item : printableItems)
            item.print();
        System.out.println("End of Validated Bibliography items");
    }


    private static URI getGoogleBaseURI() {
        return UriBuilder.fromUri("https://www.googleapis.com/books/v1").build();
    }


    private static URI getCrossrefBooksUri() {
        return UriBuilder.fromUri("http://api.crossref.org/types/book/works").build();
    }


}
