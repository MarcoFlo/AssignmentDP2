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
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;


public class BookClient_e {

    JAXBContext jaxbGoogleContext;
    JAXBContext jaxbCrossrefContext;

    javax.xml.validation.Validator validatorGoogle;
    javax.xml.validation.Validator validatorCrossref;


    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: java BookClient_e N keyword1 keyword2 ...");
//            System.exit(1);
        } else
            args = new String[]{"5", "ab"};
        try {
            BookClient_e bclient = new BookClient_e();
            QueryParameter queryParameter = new QueryParameter(new String[]{"5", "ab"});
//            bclient.PerformGoogleSearch(args);
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
        Schema schema = sf.newSchema(new File("xsd/gbooks/DataTypes.xsd"));
        validatorGoogle = schema.newValidator();
        validatorGoogle.setErrorHandler(new MyErrorHandler());

        Schema schemaCrossref = sf.newSchema(new File("xsd/CrossrefDataTypes.xsd"));
        validatorCrossref = schemaCrossref.newValidator();
        validatorCrossref.setErrorHandler(new MyErrorHandler());

        // create JAXB context related to the classed generated from the DataTypes schema
        jaxbGoogleContext = JAXBContext.newInstance("it.polito.dp2.rest.gbooks.client.jaxb");
        jaxbCrossrefContext = JAXBContext.newInstance("it.polito.dp2.BIB.sol2.jaxb.crossref");
    }

    public void PerformGoogleSearch(String[] kw) {
        // build the JAX-RS client object
        Client client = ClientBuilder.newClient();

        // build the web target
        WebTarget target = client.target(getGoogleBaseURI()).path("volumes");

        // perform a get request using mediaType=APPLICATION_JSON
        // and convert the response into a SearchResult object
        StringBuffer queryString = new StringBuffer(kw[0]);
        for (int i = 1; i < kw.length; i++) {
            queryString.append(' ');
            queryString.append(kw[i]);
        }
        System.out.println("Searching " + queryString + " on Google Books:");
        Response response = target
                .queryParam("q", queryString)
                .queryParam("printType", "books")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get();
        if (response.getStatus() != 200) {
            System.out.println("Error in remote operation: " + response.getStatus() + " " + response.getStatusInfo());
            return;
        }
        response.bufferEntity();
        System.out.println("Response as string: " + response.readEntity(String.class));
        SearchResult result = response.readEntity(SearchResult.class);

        System.out.println("OK Response received. Items:" + result.getTotalItems());

        System.out.println("Validating items and converting validated items to xml.");
        // create empty list
        List<PrintableItem> pitems = new ArrayList<PrintableItem>();
        int i = 0;
        for (Items item : result.getItems()) {
            try {
                // validate item
                JAXBSource source = new JAXBSource(jaxbGoogleContext, item);
                System.out.println("Validating " + item.getSelfLink());
                validatorGoogle.validate(source);
                System.out.println("Validation OK");
                // add item to list
                System.out.println("Adding item to list");
                pitems.add(Factory.createPrintableItem(BigInteger.valueOf(i++), item.getVolumeInfo()));
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
        }
        System.out.println("Validated Bibliography items: " + pitems.size());
        for (PrintableItem item : pitems)
            item.print();
        System.out.println("End of Validated Bibliography items");
    }

    public void PerformCrossrefSearch(QueryParameter queryParameter) {
        // build the JAX-RS client object
        Client client = ClientBuilder.newClient();

        // build the web target
        WebTarget target = client.target(getCrossrefBooksUri());

        int availableItem = 1;
        int requestedItem = 0;
        int id = 0;

        List<PrintableItem> printableItems = new ArrayList<>();
        Response response;
        CrossrefMessage message;
        System.out.println("Searching " + queryParameter.getCrossrefKeyword() + " on Crossref:");

        while (printableItems.size() != queryParameter.getItemNumber() && availableItem > requestedItem) {
            System.out.println(printableItems.size() + "   " + requestedItem);
            response = target
                    .queryParam("rows", queryParameter.getItemNumber())
                    .queryParam("offset", requestedItem)
                    .queryParam("select", "publisher,ISBN,published-print,title,subtitle,author") // to avoid large number of unnecessary fields
                    .queryParam("query.bibliographic", queryParameter.getCrossrefKeyword())
                    .request()
                    .accept(MediaType.APPLICATION_JSON)
                    .get();
            if (response.getStatus() != 200) {
                System.out.println("Crossref error in remote operation: " + response.getStatus() + " " + response.getStatusInfo());
                return;
            }

            requestedItem += queryParameter.getItemNumber();
            response.bufferEntity();
            System.out.println("Response as string: " + response.readEntity(String.class));
            message = response.readEntity(CrossrefResult.class).getMessage();


            System.out.println("OK Response received. Items:" + message.getTotalResults());

            availableItem = message.getTotalResults().intValue();
            System.out.println("Validating items and converting validated items to xml.");
            // create empty list
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
                }
            }

        }


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
