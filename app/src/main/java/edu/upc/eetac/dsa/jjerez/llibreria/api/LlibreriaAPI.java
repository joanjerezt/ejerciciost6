package edu.upc.eetac.dsa.jjerez.llibreria.api;

/**
 * Created by root on 09/05/15.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import edu.upc.eetac.dsa.jjerez.llibreria.MediaType;

public class LlibreriaAPI {
    private final static String TAG = LlibreriaAPI.class.getName();
    private static LlibreriaAPI instance = null;
    private URL url;

    private LlibreriaRootAPI rootAPI = null;

    private LlibreriaAPI(Context context) throws IOException, AppException {
        super();

        AssetManager assetManager = context.getAssets();
        Properties config = new Properties();
        config.load(assetManager.open("config.properties"));
        String urlHome = config.getProperty("beeter.home");
        url = new URL(urlHome);

        Log.d("LINKS", url.toString());
        getRootAPI();
    }

    public final static LlibreriaAPI getInstance(Context context) throws AppException {
        if (instance == null)
            try {
                instance = new LlibreriaAPI(context);
            } catch (IOException e) {
                throw new AppException(
                        "Can't load configuration file");
            }
        return instance;
    }

    private void getRootAPI() throws AppException {
        Log.d(TAG, "getRootAPI()");
        rootAPI = new LlibreriaRootAPI();
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
        } catch (IOException e) {
            throw new AppException(
                    "Can't connect to Beeter API Web Service");
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONArray jsonLinks = jsonObject.getJSONArray("links");
            parseLinks(jsonLinks, rootAPI.getLinks());
        } catch (IOException e) {
            throw new AppException(
                    "Can't get response from Llibreria API Web Service");
        } catch (JSONException e) {
            throw new AppException("Error parsing Llibreria Root API");
        }

    }
    private Map<String, Llibre> bookscache = new HashMap<String, Llibre>();

    public Llibre getBook(String urlBook) throws AppException {
        Llibre book = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urlBook);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);

            book = bookscache.get(urlBook);
            String eTag = (book == null) ? null : book.getETag();
            if (eTag != null)
                urlConnection.setRequestProperty("If-None-Match", eTag);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_NOT_MODIFIED) {
                Log.d(TAG, "CACHE");
                return bookscache.get(urlBook);
            }
            Log.d(TAG, "NOT IN CACHE");
            book = new Llibre();
            eTag = urlConnection.getHeaderField("ETag");
            book.setETag(eTag);
            bookscache.put(urlBook, book);

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JSONObject jsonBook = new JSONObject(sb.toString());
            book.setAuthor(jsonBook.getString("author"));
            book.setEdition(jsonBook.getString("edition"));
            book.setEditionDate(jsonBook.getString("editionDate"));
            book.setLanguage(jsonBook.getString("language"));
            book.setPrintingDate(jsonBook.getString("printingDate"));
            book.setPublisher(jsonBook.getString("publisher"));
            book.setTitle(jsonBook.getString("title"));
            book.setBookid(jsonBook.getInt("bookid"));
            JSONArray jsonLinks = jsonBook.getJSONArray("links");
            parseLinks(jsonLinks, book.getLinks());
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new AppException("URL incorrecta del llibre");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new AppException("Excepció a l'obtenir el llibre");
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new AppException("Excepció a l'analitzar la resposta");
        }

        return book;
    }

    public LlibreCollection getBooks() throws AppException {
        Log.d(TAG, "getBooks()");
        LlibreCollection books = new LlibreCollection();

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(rootAPI.getLinks()
                    .get("books").getTarget()).openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
        } catch (IOException e) {
            throw new AppException(
                    "No puc connectar-me a Llibreria API Web Service");
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONArray jsonLinks = jsonObject.getJSONArray("links");
            parseLinks(jsonLinks, books.getLinks());

            books.setNewestTimestamp(jsonObject.getLong("newestTimestamp"));
            books.setOldestTimestamp(jsonObject.getLong("oldestTimestamp"));
            JSONArray jsonLlibres = jsonObject.getJSONArray("books");
            for (int i = 0; i < jsonLlibres.length(); i++) {
                Llibre llibre = new Llibre();
                JSONObject jsonLlibre = jsonLlibres.getJSONObject(i);
                llibre.setAuthor(jsonLlibre.getString("author"));
                llibre.setBookid(jsonLlibre.getInt("bookid"));
                llibre.setLastModified(jsonLlibre.getLong("lastModified"));
                llibre.setCreationTimestamp(jsonLlibre.getLong("creationTimestamp"));
                llibre.setSubject(jsonLlibre.getString("subject"));
                llibre.setUsername(jsonLlibre.getString("username"));
                jsonLinks = jsonLlibre.getJSONArray("links");
                parseLinks(jsonLinks, llibre.getLinks());
                books.getBooks().add(llibre);
            }
        } catch (IOException e) {
            throw new AppException(
                    "No es pot obtenir resposta Beeter API Web Service");
        } catch (JSONException e) {
            throw new AppException("Error parsing Beeter Root API");
        }

        return books;
    }
    public Llibre createBook(String subject, String content) throws AppException {
        Llibre llibre = new Llibre();
        llibre.setSubject(subject);
        llibre.setContent(content);
        HttpURLConnection urlConnection = null;
        try {
            JSONObject jsonBook = createJsonBook(llibre);
            URL urlPostBooks = new URL(rootAPI.getLinks().get("create-llibres")
                    .getTarget());
            urlConnection = (HttpURLConnection) urlPostBooks.openConnection();
            urlConnection.setRequestProperty("Accept",
                    MediaType.LLIBRERIA_API_BOOK);
            urlConnection.setRequestProperty("Content-Type",
                    MediaType.LLIBRERIA_API_BOOK);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            PrintWriter writer = new PrintWriter(
                    urlConnection.getOutputStream());
            writer.println(jsonBook.toString());
            writer.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            jsonBook = new JSONObject(sb.toString());

            llibre.setAuthor(jsonBook.getString("author"));
            llibre.setBookid(jsonBook.getInt("llibreid"));
            llibre.setLastModified(jsonBook.getLong("lastModified"));
            llibre.setSubject(jsonBook.getString("subject"));
            llibre.setContent(jsonBook.getString("content"));
            llibre.setUsername(jsonBook.getString("username"));
            JSONArray jsonLinks = jsonBook.getJSONArray("links");
            parseLinks(jsonLinks, llibre.getLinks());
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new AppException("Error parsing response");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new AppException("Error getting response");
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return llibre;
    }

    private JSONObject createJsonBook(Llibre llibre) throws JSONException {
        JSONObject jsonBook = new JSONObject();
        jsonBook.put("subject", llibre.getSubject());
        jsonBook.put("content", llibre.getContent());

        return jsonBook;
    }

    private void parseLinks(JSONArray jsonLinks, Map<String, Link> map)
            throws AppException, JSONException {
        for (int i = 0; i < jsonLinks.length(); i++) {
            Link link = null;
            try {
                link = SimpleLinkHeaderParser
                        .parseLink(jsonLinks.getString(i));
            } catch (Exception e) {
                throw new AppException(e.getMessage());
            }
            String rel = link.getParameters().get("rel");
            String rels[] = rel.split("\\s");
            for (String s : rels)
                map.put(s, link);
        }
    }
}
