package edu.upc.eetac.dsa.jjerez.llibreria;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import edu.upc.eetac.dsa.jjerez.llibreria.api.AppException;
import edu.upc.eetac.dsa.jjerez.llibreria.api.Llibre;
import edu.upc.eetac.dsa.jjerez.llibreria.api.LlibreriaAPI;

/**
 * Created by root on 09/05/15.
 */
public class LlibreDetailActivity {
    private final static String TAG = LlibreDetailActivity.class.getName();
    Llibre llibre = null;
    String urlReviews = null;
    String urlBook = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_detail_layout);
        urlBook = (String) getIntent().getExtras().get("url");
        urlReviews = (String) getIntent().getExtras().get("url_reviews");
        (new FetchBookTask()).execute(urlBook);
    }

    private void loadBook(Llibre llibre) {
        TextView tvDetailTitle = (TextView) findViewById(R.id.tvDetailTitle);
        TextView tvDetailAuthor = (TextView) findViewById(R.id.tvDetailAuthor);
        TextView tvDetailPublisher = (TextView) findViewById(R.id.tvDetailPublisher);
        TextView tvDetailDate = (TextView) findViewById(R.id.tvDetailDate);
        TextView tvDetailEdition = (TextView) findViewById(R.id.tvDetailEdition);
        TextView tvDetailLanguage = (TextView) findViewById(R.id.tvDetailLanguage);

        tvDetailTitle.setText(llibre.getTitle());
        tvDetailAuthor.setText(llibre.getAuthor());
        tvDetailPublisher.setText(llibre.getPublisher());
        tvDetailDate.setText(llibre.getPrintingDate());
        tvDetailEdition.setText("Edition " + llibre.getEdition());
        tvDetailLanguage.setText(llibre.getLanguage());
    }

    private class FetchBookTask extends AsyncTask<String, Void, Llibre> {
        private ProgressDialog pd;

        @Override
        protected Llibre doInBackground(String... params) {
            Llibre book = null;
            try {
                book = LlibreriaAPI.getInstance(LlibreDetailActivity.this)
                        .getBook(params[0]);
            } catch (AppException e) {
                Log.d(TAG, e.getMessage(), e);
            }
            return book;
        }

        @Override
        protected void onPostExecute(Llibre result) {
            loadBook(result);
            if (pd != null) {
                pd.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(LlibreDetailActivity.this);
            pd.setTitle("Loading...");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_llibreria_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reviewsMenuItem:
                Intent intent = new Intent(this, LlibreRessenyesActivity.class);
                //intent.putExtra("url", book.getLinks().get("reviews").getTarget());
                intent.putExtra("url", urlReviews);
                intent.putExtra("url_book", urlBook);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
}
