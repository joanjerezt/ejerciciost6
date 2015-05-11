package edu.upc.eetac.dsa.jjerez.llibreria.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 09/05/15.
 */
public class LlibreCollection {
    private List<Llibre> books;
    private int firstBook;
    private int lastBook;
    private Map<String, Link> links = new HashMap<String, Link>();

    public LlibreCollection() {
        super();
        books = new ArrayList<Llibre>();
    }

    public List<Llibre> getBooks() {
        return books;
    }

    public void setBooks(List<Llibre> books) {
        this.books = books;
    }

    public int getFirstBook() {
        return firstBook;
    }

    public void setFirstBook(int firstBook) {
        this.firstBook = firstBook;
    }

    public int getLastBook() {
        return lastBook;
    }

    public void setLastBook(int lastBook) {
        this.lastBook = lastBook;
    }

    public Map<String, Link> getLinks() {
        return links;
    }

    public void setLinks(Map<String, Link> links) {
        this.links = links;
    }
}
