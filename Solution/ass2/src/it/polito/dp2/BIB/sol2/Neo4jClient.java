package it.polito.dp2.BIB.sol2;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import it.polito.dp2.BIB.sol2.exercise1.jaxb.Data;
import it.polito.dp2.BIB.sol2.exercise1.jaxb.Node;
import it.polito.dp2.BIB.sol2.exercise1.jaxb.ObjectFactory;
import it.polito.dp2.BIB.sol2.exercise1.jaxb.RelationshipType;
import it.polito.dp2.BIB.sol2.exercise1.jaxb.TraverseRequest;

/**
 * Client for the NEO4J REST API that can perform some operations
 * (create nodes and relationships and perform traversal of CitedBy relationships)
 *
 */
public class Neo4jClient {
	Client client;										// JAX-RS API client
	WebTarget target;									// The WebTarget of the main resource of the API
	String uri = "http://localhost:7474/db";			// The base URI of the service (initialized to the default value)
	String urlProperty = "it.polito.dp2.BIB.ass2.URL";	// The system property that holds the actual base URI of the API
	String portProperty = "it.polito.dp2.BIB.ass2.PORT";// The system property that holds the actual port where the API is available
	private ObjectFactory of = new ObjectFactory();

	public Neo4jClient() {
		client = ClientBuilder.newClient();
		
		String customUri = System.getProperty(urlProperty);
		String customPort = System.getProperty(portProperty);
		if (customUri != null)
			uri = customUri;
		
		target = client.target(uri).path("data");
	}
	
	public void close() {
		client.close();
	}

	/**
	 * Create NEO$J node with given title as property
	 * @param title the title
	 * @return the object that represents the node that has been created
	 * @throws Neo4jClientException if the node cannot be created because of errors in the interaction with the service
	 */
	public Node createNode(String title) throws Neo4jClientException {
		// create the object that represents the body of the POST request
		Data data = of.createData();
		data.setTitle(title);
		
		// perform the POST operation and return the returned node object
		try {
			Node node = target.path("node")
				  .request(MediaType.APPLICATION_JSON_TYPE)
				  .post(Entity.json(data), Node.class);
			return node;
		} catch (WebApplicationException|ProcessingException e) {
			throw new Neo4jClientException(e);
		}
	}

	/**
	 * Create relationship of type "CitedBy" between given nodes 
	 * @param node1 source node
	 * @param node2 target node
	 * @throws Neo4jClientException if the relationship cannot be created because of errors in the interaction with the service
	 */
	public void createRelationship(Node node1, Node node2) throws Neo4jClientException {
		// extract URLs from the nodes
		String url1 = node1.getSelf();
		String url2 = node2.getSelf();
		
		// create object that represents the relationship to be created
		RelationshipType relationship = of.createRelationshipType();
		relationship.setTo(url2);
		relationship.setType("CitedBy");
		
		// perform the POST operation
		try {
			client.target(url1)
			  .path("/relationships")
		 	  .request(MediaType.APPLICATION_JSON_TYPE)
		 	  .post(Entity.json(relationship));
		} catch (WebApplicationException|ProcessingException|IllegalArgumentException e) {
			throw new Neo4jClientException(e);
		}
	}

	/**
	 * Perform a traversal of the graph up to a maximum depth, following CitedBy relationships
	 * and collecting the URLs of the visited nodes
	 * @param url the URL of the node from which the traversal starts
	 * @param maxDepth the maximum depth of the traversal
	 * @return a set containing the URLs of the visited nodes
	 * @throws Neo4jClientException if the traversal could not be completed because of errors in the interaction with the service
	 */
	public Set<URL> performTraverse(URL url, int maxDepth) throws Neo4jClientException {
		// create the object that represents the body of the traversal request POST
		TraverseRequest request = of.createTraverseRequest();
		request.setUniqueness("node_global");
		if (maxDepth>=0)
			request.setMaxDepth(BigInteger.valueOf(maxDepth));
		RelationshipType rel = of.createRelationshipType();
		rel.setDirection("out");
		rel.setType("CitedBy");
		request.getRelationships().add(rel);
		
		// create the initially empty set that will be returned
		Set<URL> retval = new HashSet<URL>();
		
		try {
			// perform the POST operation
			List<Node> nodes =
			client.target(url.toString())
			  .path("/traverse")
			  .path("/node")
		 	  .request(MediaType.APPLICATION_JSON_TYPE)
		 	  .post(Entity.json(request), new GenericType<List<Node>>() {});
			// extract URLs from Node objects and add them to the set to be returned
			for (Node node:nodes) {
				String urlString = node.getSelf();
				if (urlString != null) {
					URL u = new URL(urlString);
					retval.add(u);
				}
			}
			// return the set
			return retval;
		} catch (WebApplicationException | ProcessingException | MalformedURLException e) {
			throw new Neo4jClientException(e);
		}
	}

}
