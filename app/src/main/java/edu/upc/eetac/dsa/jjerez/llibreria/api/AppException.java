package edu.upc.eetac.dsa.jjerez.llibreria.api;

/**
 * Created by root on 09/05/15.
 */
public class AppException extends Exception {
    public AppException() {
        super();
    }

    public AppException(String detailMessage) {
        super(detailMessage);
    }
}
