package edu.upc.eetac.dsa.jjerez.llibreria;

/**
 * Created by root on 09/05/15.
 */
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.gson.Gson;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;

import edu.upc.eetac.dsa.jjerez.llibreria.api.AppException;
import edu.upc.eetac.dsa.jjerez.llibreria.api.Llibre;
import edu.upc.eetac.dsa.jjerez.llibreria.api.LlibreriaAPI;
import edu.upc.eetac.dsa.jjerez.llibreria.api.Ressenya;
import edu.upc.eetac.dsa.jjerez.llibreria.api.RessenyaAdapter;
import edu.upc.eetac.dsa.jjerez.llibreria.api.RessenyaCollection;


public class LlibreRessenyesActivity extends ListActivity {
    private final static String TAG = LlibreRessenyesActivity.class.getName();
    private ArrayList<Ressenya> ressenyesList;
    private RessenyaAdapter adapter;
    String urlBook = null;
    Llibre llibre = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_reviews_layout);

        ressenyesList = new ArrayList<Ressenya>();
        adapter = new RessenyaAdapter(this, ressenyesList);
        setListAdapter(adapter);

        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("test", "test"
                        .toCharArray());
            }
        });

        urlBook = (String) getIntent().getExtras().get("url_book");
        String urlRessenyes = (String) getIntent().getExtras().get("url");
        (new FetchRessenyesTask()).execute(urlRessenyes);
    }

//    private void loadBook(Book book) {
//        TextView tvDetailTitle = (TextView) findViewById(R.id.tvDetailTitle);
//        TextView tvDetailAuthor = (TextView) findViewById(R.id.tvDetailAuthor);
//        TextView tvDetailPublisher = (TextView) findViewById(R.id.tvDetailPublisher);
//        TextView tvDetailDate = (TextView) findViewById(R.id.tvDetailDate);
//
//        tvDetailTitle.setText(book.getTitle());
//        tvDetailAuthor.setText(book.getAuthor());
//        tvDetailPublisher.setText(book.getPublisher());
//        tvDetailDate.setText(book.getPrintingDate());
//    }

    private class FetchRessenyesTask extends
            AsyncTask<String, Void, RessenyaCollection> {
        private ProgressDialog pd;

        @Override
        protected RessenyaCollection doInBackground(String... params) {
            RessenyaCollection reviews = null;
            try {
                reviews = LlibreriaAPI.getInstance(LlibreRessenyesActivity.this)
                        .getReviews(params[0]);
            } catch (AppException e) {
                e.printStackTrace();
            }
            return reviews;
        }

        @Override
        protected void onPostExecute(RessenyaCollection result) {
            addRessenyes(result);
            if (pd != null) {
                pd.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(LlibreRessenyesActivity.this);
            pd.setTitle("Searching...");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }
    }

    private void addRessenyes(RessenyaCollection reviews){
        //Log.d(TAG, reviews.getRessenyes().get(0).getContent().toString()); //ESTA FUNCIÓN FUNCIONA ENTERA
        ressenyesList.addAll(reviews.getReviews());
        //ressenyesList.add(reviews.getRessenyes().get(0));
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_write_review, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.write_reviewMenuItem:
                Intent intent = new Intent(this, WriteRessenyaActivity.class);
                //intent.putExtra("url", book.getLinks().get("reviews").getTarget());
                intent.putExtra("url_book", urlBook);
                //startActivity(intent);
                startActivityForResult(intent, WRITE_ACTIVITY); //Para que aparezca la nueva ressenya después de postearla
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Método para que se visualice la nueva ressenya
    private final static int WRITE_ACTIVITY = 0; //Porque solo hay una actividad, si se lanzan varias se asignan números sucesivos
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case WRITE_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    Bundle res = data.getExtras();
                    String jsonReview = res.getString("json-review");
                    Ressenya review = new Gson().fromJson(jsonReview, Ressenya.class);
                    ressenyesList.add(0, review);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }
}
