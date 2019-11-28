package it.polito.dp2.BIB.sol2;

import it.polito.dp2.BIB.sol2.jaxb.*;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Neo4jClient {
    private Client client;
    private String uri = "http://localhost:7474/db";
    private String urlProperty = "it.polito.dp2.BIB.ass2.URL";
    private String portProperty = "it.polito.dp2.BIB.ass2.PORT";
    private ObjectFactory of = new ObjectFactory();

    public Neo4jClient() throws Neo4jClientException {
        client = ClientBuilder.newClient();

        String customUri = System.getProperty(urlProperty);
        String customPort = System.getProperty(portProperty);
        if (customUri != null)
            uri = manageCustomInput(customUri, customPort).toString();
    }

    private URL manageCustomInput(String customUri, String customPort) throws Neo4jClientException {
        try {
            URL customUrl = new URL(customUri);
            if (customUrl.getPort() == -1)
                return new URL(customUrl.getProtocol(), customUrl.getHost(), Integer.parseInt(customPort), customUrl.getFile());
            else
                return customUrl;
        } catch (MalformedURLException | NumberFormatException e) {
            throw new Neo4jClientException("Input parameter not valid", e);
        }

    }

    public void close() {
        client.close();
    }

    public Node createNode(String title) throws Neo4jClientException {
        Data data = of.createData();
        data.setTitle(title);
        try {
            return client.target(uri).path("data").path("node")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .post(Entity.json(data), Node.class);
        } catch (WebApplicationException | ProcessingException e) {
            throw new Neo4jClientException("Node creation exception", e);
        }
    }


    public Relationship createRelationship(Node from, Node to) throws Neo4jClientException {
        BodyRelationship body = of.createBodyRelationship();
        body.setTo(to.getSelf());
        body.setType("CitedBy");
        try {
            return client.target(from.getCreateRelationship())
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .post(Entity.json(body), Relationship.class);
        } catch (WebApplicationException | ProcessingException e) {
            throw new Neo4jClientException("Relationship creation exception", e);
        }
    }

    public List<Node> getListTraversed(Node node, int maxDepth) throws Neo4jClientException {
        BodyTraversal body = of.createBodyTraversal();
        BodyTraversal.Relationships relationships = new BodyTraversal.Relationships();
        relationships.setDirection("out");
        relationships.setType("CitedBy");
        body.setRelationships(relationships);
        body.setMaxDepth(BigInteger.valueOf(maxDepth));

        try {
            return client.target(node.getTraverse()).resolveTemplate("returnType", "node").request(MediaType.APPLICATION_JSON_TYPE)
                    .post(Entity.json(body), new GenericType<List<Node>>() {
                    });
        } catch (WebApplicationException | ProcessingException e) {
            throw new Neo4jClientException("Get Traversed exception", e);
        }
    }
}
