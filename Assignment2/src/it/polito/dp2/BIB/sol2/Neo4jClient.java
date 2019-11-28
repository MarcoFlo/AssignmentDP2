package it.polito.dp2.BIB.sol2;

import it.polito.dp2.BIB.sol2.jaxb.*;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Neo4jClient {
    Client client;
    String uri = "http://localhost:7474/db";
    String urlProperty = "it.polito.dp2.BIB.ass2.URL";
    String portProperty = "it.polito.dp2.BIB.ass2.PORT";
    private ObjectFactory of = new ObjectFactory();

    public Neo4jClient() {
        client = ClientBuilder.newClient();

        String customUri = System.getProperty(urlProperty);
        String customPort = System.getProperty(portProperty);
        if (customUri != null)
            uri = customUri;
        try {
            URL url = new URL(uri);
//todo
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        client.close();
    }

    public Node createNode(String title) throws Neo4jClientException {
        Data data = of.createData();
        data.setTitle(title);
        try {
            Node node = client.target(uri).path("data").path("node")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .post(Entity.json(data), Node.class);
            return node;
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

    public List<Node> getListTraversed(Node node, int maxDepth) {
        BodyTraversal body = of.createBodyTraversal();
        BodyTraversal.Relationships relationships = new BodyTraversal.Relationships();
        relationships.setDirection("out");
        relationships.setType("CitedBy");
        body.setRelationships(relationships);
        body.setMaxDepth(BigInteger.valueOf(maxDepth));

        return client.target(node.getTraverse()).resolveTemplate("returnType", "node").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(body), new GenericType<List<Node>>() {
                });
    }
}
