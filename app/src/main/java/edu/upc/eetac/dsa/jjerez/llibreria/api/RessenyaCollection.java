package edu.upc.eetac.dsa.jjerez.llibreria.api;

/**
 * Created by root on 09/05/15.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RessenyaCollection {
    private List<Ressenya> reviews;
    private int firstReview;
    private int lastReview;
    private Map<String, Link> links = new HashMap<String, Link>();

    public RessenyaCollection() {
        super();
        reviews = new ArrayList<Ressenya>();
    }

    public Map<String, Link> getLinks() {
        return links;
    }

    public void setLinks(Map<String, Link> links) {
        this.links = links;
    }

    public List<Ressenya> getReviews() {
        return reviews;
    }

    public void setReviews(List<Ressenya> reviews) {
        this.reviews = reviews;
    }

    public int getFirstReview() {
        return firstReview;
    }

    public void setFirstReview(int firstReview) {
        this.firstReview = firstReview;
    }

    public int getLastReview() {
        return lastReview;
    }

    public void setLastReview(int lastReview) {
        this.lastReview = lastReview;
    }
}
