package it.polito.dp2.BIB.sol2;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;


import it.polito.dp2.rest.gbooks.client.MyErrorHandler;
import it.polito.dp2.xml.biblio.PrintableBook;
import it.polito.dp2.xml.biblio.PrintableItem;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.util.JAXBSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;


/**
 * Solution of DP2 Lab 4, exercise 2 (AY 2019/20)
 *
 */
public class BookClient_e {
	
	JAXBContext jc_gbooks;								// JAXBContext for gbooks
	javax.xml.validation.Validator validator_gbooks;	// validator for gbooks
	JAXBContext jc_crossref;							// JAXBContext for crossref
	javax.xml.validation.Validator validator_crossref;	// validator for crossref
	IdCounter idCounter;								// a counter used to generate item ids
	
	public static void main(String[] args) {
		// check number of arguments
		if (args.length < 2) {
	          System.err.println("Usage: java BookClient_e number keyword1 ...");
	          System.exit(1);
	    }
		try{
			// extract and convert arguments
			int N = Integer.parseInt(args[0]);
			String[] keywords = Arrays.copyOfRange(args, 1, args.length);
			// instantiate this class
			BookClient_e bclient = new BookClient_e();
			// call methods to perform the searches
			List<PrintableBook> crossref = bclient.PerformSearch_crossref(keywords, N);
			List<PrintableBook> gbooks = bclient.PerformSearch_google(keywords, N);
			// leave empty line
			System.out.println();
			// check number of results found for crossref and print them
			if(crossref.size()==N){
				System.out.println("Printing "+N+" crossref results:");
			    for (PrintableBook book:crossref)
			    	book.print();
		    }else{
		    	System.out.println("ERROR: Not found " + N + " books in crossref");
		    }
			// leave empty line
			System.out.println();
			// check number of results found for gbooks and print them
			if(gbooks.size()==N){
				System.out.println("Printing "+N+" gbooks results:");
			    for (PrintableBook book:gbooks)
			    	book.print();
		    }else{
		    	System.out.println("ERROR: Not found " + N + " books in googlebooks");
		    }
		}catch (NumberFormatException e1) {
	          System.err.println("First argument is not a valid number");
	          System.exit(1);			
		} catch(Exception e2 ){
			System.err.println("Error during execution of operation");
			e2.printStackTrace(System.out);
		}
	}
	
	public BookClient_e() throws Exception {        
    	SchemaFactory sf = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
    	
    	Schema schema_google = sf.newSchema(new File("xsd/gbooks/DataTypes.xsd"));
    	validator_gbooks = schema_google.newValidator();
    	validator_gbooks.setErrorHandler(new MyErrorHandler());    	
        jc_gbooks = JAXBContext.newInstance("it.polito.dp2.rest.gbooks.client.jaxb");
        
    	Schema schema_crossref = sf.newSchema(new File("xsd/exercise2/cross.xsd"));
    	validator_crossref = schema_crossref.newValidator();
    	validator_crossref.setErrorHandler(new MyErrorHandler());    	
        jc_crossref = JAXBContext.newInstance("it.polito.dp2.BIB.sol2.crossref.jaxb");
        
        idCounter = new IdCounter();
	}
	
	public List<PrintableBook> PerformSearch_crossref(String[] kw, int N){
		// build the JAX-RS client object 
		Client client = ClientBuilder.newClient();
		int res_cont = 0;	// initialize number of already processed results
		int tot_res = 1;	// initialize total number of results so that it is greater than the number of already processed results
		// build the web target
		WebTarget target = client.target(getBaseURI_crossref()).path("works");
		// create empty list of PrintableBook
		List<PrintableBook> pbooks = new ArrayList<PrintableBook>();
		// prepare query string
		StringBuffer queryString = new StringBuffer(kw[0]);
		for (int i=1; i<kw.length; i++) {
			queryString.append(' ');
			queryString.append(kw[i]);
		}
		int offset;		// declare variable used to store offset
		// main loop on pages
		for(int page=0; pbooks.size()<N && res_cont<tot_res; page++){
			offset = page*20;
			System.out.println("Searching "+queryString+" on Crossref with offset "+offset);
			try {
				// perform a get request using mediaType=APPLICATION_JSON
				// and convert the response into a SearchResult object
				it.polito.dp2.BIB.sol2.crossref.jaxb.SearchResult result = target.queryParam("filter", "type:book")
									   .queryParam("query", queryString)	
									   .queryParam("offset", offset)
									   .request()
									   .accept(MediaType.APPLICATION_JSON)
									   .get(it.polito.dp2.BIB.sol2.crossref.jaxb.SearchResult.class);
				tot_res = result.getMessage().getTotalResults().intValue();
				System.out.println("OK Response received. Total number of Items:"+tot_res);
				System.out.println("Validating items and converting validated items to xml.");
			
				for (it.polito.dp2.BIB.sol2.crossref.jaxb.Items item:result.getMessage().getItems()) {
					try {
						// validate item
				    	JAXBSource source = new JAXBSource(jc_crossref, item);
				    	System.out.println("Validating "+item.getTitle());
				    	validator_crossref.validate(source);
				    	System.out.println("Validation OK");
				    	// add item to list
						System.out.println("Adding item to list");
						PrintableItem pi = Factory.createPrintableItem(BigInteger.valueOf(idCounter.getNextId()),item);				
						if(pi instanceof PrintableBook){
							pbooks.add((PrintableBook)pi);
							if(pbooks.size()==N) break;
						}
					} catch (org.xml.sax.SAXException se) {
					      System.out.println("Validation Failed");
					      // print error messages
					      Throwable t = se;
					      while (t!=null) {
						      String message = t.getMessage();
						      if (message!= null)
						    	  System.out.println(message);
						      t = t.getCause();
					      }
					} catch (IOException e) {
						System.out.println("Unexpected I/O Exception");
					} catch (JAXBException e) {
						System.out.println("Unexpected JAXB Exception");
					}
				}
				res_cont = res_cont + 20;
			} catch(WebApplicationException e) {
				System.err.println("Error in remote operation: "+e.getMessage());
				return null;	
			}
		}
	    System.out.println("Number of validated Bibliography books from crossref: "+pbooks.size());
	    return pbooks;
	}
	
	private static URI getBaseURI_crossref() {
		return UriBuilder.fromUri("https://api.crossref.org/v1").build();
	}
	
	// Perform search on gbooks
	public List<PrintableBook> PerformSearch_google(String[] kw, int N){
		// build the JAX-RS client object 
		Client client = ClientBuilder.newClient();
		int res_cont = 0;	// initialize number of already processed results
		int tot_res = 1;	// initialize total number of results so that it is greater than the number of already processed results
		// build the web target
		WebTarget target = client.target(getBaseURI_google()).path("volumes");
		// create empty list of PrintableBook
		List<PrintableBook> pbooks = new ArrayList<PrintableBook>();
		// prepare query string
		StringBuffer queryString = new StringBuffer(kw[0]);
		for (int i=1; i<kw.length; i++) {
			queryString.append(' ');
			queryString.append(kw[i]);
		}
		int offset;		// declare variable used to store offset
		// main loop on pages
		for(int page=0; pbooks.size()<N && res_cont<tot_res; page++){
			offset = page*20;
			System.out.println("Searching "+queryString+" on Google books with offset "+offset);
			try {
				// perform a get request using mediaType=APPLICATION_JSON
				// and convert the response into a SearchResult object
				it.polito.dp2.rest.gbooks.client.jaxb.SearchResult result = target
									   .queryParam("q", queryString)	
									   .queryParam("printType", "books")
									   .queryParam("maxResults", 20)
									   .queryParam("startIndex", offset)
									   .request()
									   .accept(MediaType.APPLICATION_JSON)
									   .get(it.polito.dp2.rest.gbooks.client.jaxb.SearchResult.class);
				tot_res = result.getTotalItems().intValue();
				System.out.println("OK Response received. Total number of Items:"+tot_res);
				System.out.println("Validating items and converting validated items to xml.");
				for (it.polito.dp2.rest.gbooks.client.jaxb.Items item:result.getItems()) {
					try {
						// validate item
				    	JAXBSource source = new JAXBSource(jc_gbooks, item);
				    	System.out.println("Validating "+item.getSelfLink());
				    	validator_gbooks.validate(source);
				    	System.out.println("Validation OK");
				    	// add item to list
						System.out.println("Adding item to list");
						PrintableItem pi = Factory.createPrintableItem(BigInteger.valueOf(idCounter.getNextId()),item.getVolumeInfo());				
						if(pi instanceof PrintableBook){
							pbooks.add((PrintableBook)pi);
							if(pbooks.size()==N) break;
						}
					} catch (org.xml.sax.SAXException se) {
					      System.out.println("Validation Failed");
					      // print error messages
					      Throwable t = se;
					      while (t!=null) {
						      String message = t.getMessage();
						      if (message!= null)
						    	  System.out.println(message);
						      t = t.getCause();
					      }
					} catch (IOException e) {
						System.out.println("Unexpected I/O Exception");
					} catch (JAXBException e) {
						System.out.println("Unexpected JAXB Exception");
					}
				}
				res_cont = res_cont + 20;
			} catch(WebApplicationException e) {
				System.err.println("Error in remote operation: "+e.getMessage());
				return null;	
			}
		}
	    System.out.println("Number of validated Bibliography books from gbooks: "+pbooks.size());
	    return pbooks;
	}
	
	private static URI getBaseURI_google() {
	    return UriBuilder.fromUri("https://www.googleapis.com/books/v1").build();
	}
}
