package edu.upc.eetac.dsa.jjerez.llibreria;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import edu.upc.eetac.dsa.jjerez.llibreria.api.AppException;
import edu.upc.eetac.dsa.jjerez.llibreria.api.Llibre;
import edu.upc.eetac.dsa.jjerez.llibreria.api.LlibreriaAPI;
import edu.upc.eetac.dsa.jjerez.llibreria.api.Ressenya;

/**
 * Created by root on 09/05/15.
 */
public class WriteRessenyaActivity {
    private final static String TAG = WriteRessenyaActivity.class.getName();
    String urlBook = null;

    private class PostRessenyaTask extends AsyncTask<String, Void, Ressenya> {
        private ProgressDialog pd;
        Llibre llibre = null;

        @Override
        protected Ressenya doInBackground(String... params) {
            Ressenya ressenya = null;
            try {
                llibre = LlibreriaAPI.getInstance(WriteRessenyaActivity.this)
                        .getBook(params[1]);

                String username = "test1";
                String name = "test1";
                int bookid = llibre.getBookid();

                ressenya = LlibreriaAPI.getInstance(WriteRessenyaActivity.this).createRessenya(params[0], username, name, bookid);

            } catch (AppException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return ressenya;
        }

        @Override
        protected void onPostExecute(Ressenya result) {
            showReviews(result);
            if (pd != null) {
                pd.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(WriteRessenyaActivity.this);

            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_review_layout);

        urlBook = (String) getIntent().getExtras().get("url_book");
    }

    public void cancel(View v) { //Como se espera un resultado, al cancelar se devuelve CANCELED
        setResult(RESULT_CANCELED);
        finish();
    }

    public void postReview(View v) {
        EditText etContent = (EditText) findViewById(R.id.etContent);

        String content = etContent.getText().toString();

        (new PostReviewTask()).execute(content, urlBook);
    }

    private void showReviews(Ressenya result) {
        String json = new Gson().toJson(result); //Para que entienda el Gson hay que añadir una dependencia: com.google.code
        Bundle data = new Bundle();
        data.putString("json-review", json);
        Intent intent = new Intent();
        intent.putExtras(data);
        setResult(RESULT_OK, intent);
        finish();
    }

}
