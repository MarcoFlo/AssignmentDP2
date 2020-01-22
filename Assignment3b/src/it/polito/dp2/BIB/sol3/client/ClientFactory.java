package it.polito.dp2.BIB.sol3.client;

import java.net.URI;
import java.net.URISyntaxException;

import it.polito.dp2.BIB.ass3.Client;
import it.polito.dp2.BIB.ass3.ClientException;

public class ClientFactory extends it.polito.dp2.BIB.ass3.ClientFactory {
    static String uriDef = "http://localhost:8080/BiblioSystem/rest";
    static String urlProperty = "it.polito.dp2.BIB.ass3.URL";
    static String portProperty = "it.polito.dp2.BIB.ass3.PORT";

    @Override
    public Client newClient() throws ClientException {
        URI uri;
        try {
            String uriProp = System.getProperty(urlProperty);
            String portProp = System.getProperty(portProperty);

            System.out.println("uri property: " + uriProp);
			System.out.println("port property: " + portProp);

			if (uriProp != null) {
                uri = new URI(uriProp);
                if (portProp != null)
                    uri = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), Integer.parseInt(portProp), uri.getPath(), uri.getQuery(), uri.getFragment());
            } else {
                uri = new URI(uriDef);
            }
        } catch (URISyntaxException | NumberFormatException e) {
            throw new ClientException(e, "The client cannot be created");
        }
        return new ClientFactoryImpl(uri);
    }
}
