package edu.upc.eetac.dsa.jjerez.llibreria.api;

/**
 * Created by root on 09/05/15.
 */
import java.util.HashMap;
import java.util.Map;

public class LlibreriaRootAPI {

    private Map<String, Link> links;

    public LlibreriaRootAPI() {
        links = new HashMap<String, Link>();
    }

    public Map<String, Link> getLinks() {
        return links;
    }

}
