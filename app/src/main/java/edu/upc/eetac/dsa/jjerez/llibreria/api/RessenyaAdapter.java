package edu.upc.eetac.dsa.jjerez.llibreria.api;

/**
 * Created by root on 09/05/15.
 */
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.upc.eetac.dsa.jjerez.llibreria.LlibreRessenyesActivity;
import edu.upc.eetac.dsa.jjerez.llibreria.R;

public class RessenyaAdapter extends BaseAdapter {
    private ArrayList<Ressenya> data;
    private LayoutInflater inflater;

    public RessenyaAdapter(Context context, ArrayList<Ressenya> data) {
        super();
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    private static class ViewHolder {
        TextView tvContent;
        TextView tvUsername;
        TextView tvBook;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_row_review, null);
            viewHolder = new ViewHolder();
            viewHolder.tvContent = (TextView) convertView
                    .findViewById(R.id.tvContent);
            viewHolder.tvUsername = (TextView) convertView
                    .findViewById(R.id.tvUsername);
            viewHolder.tvBook = (TextView) convertView
                    .findViewById(R.id.tvBook);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String content = data.get(position).getContent();
        String username = data.get(position).getUsername();
        int bookid = data.get(position).getBookid();


        String TAG = LlibreRessenyesActivity.class.getName();
        viewHolder.tvContent.setText(content);
        viewHolder.tvUsername.setText(username);
        viewHolder.tvBook.setText(bookid);
        return convertView;
    }
}
