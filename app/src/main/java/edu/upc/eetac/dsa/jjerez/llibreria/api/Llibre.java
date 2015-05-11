package edu.upc.eetac.dsa.jjerez.llibreria.api;

/**
 * Created by root on 09/05/15.
 */
import java.util.HashMap;
import java.util.Map;

public class Llibre {
    private String title;
    private int bookid;
    private String author;
    private String language;
    private String edition;
    private String editionDate;
    private String printingDate;
    private String publisher;
    private String eTag;
    private Map<String, Link> links = new HashMap<String, Link>();

    public Map<String, Link> getLinks() {
        return links;
    }

    public void setLinks(Map<String, Link> links) {
        this.links = links;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getEditionDate() {
        return editionDate;
    }

    public void setEditionDate(String editionDate) {
        this.editionDate = editionDate;
    }

    public String getPrintingDate() {
        return printingDate;
    }

    public void setPrintingDate(String printingDate) {
        this.printingDate = printingDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getETag() {
        return eTag;
    }

    public void setETag(String eTag) {
        this.eTag = eTag;
    }

    public int getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }
}
