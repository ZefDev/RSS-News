package com.example.news;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.news.db.entity.Site;

import java.util.List;

public class myCursorAdapter extends BaseAdapter implements SpinnerAdapter{
    private Activity activity;
    private List<Site> list_sites;

    public myCursorAdapter(Activity activity, List<Site> list_sites){
        this.activity = activity;
        this.list_sites = list_sites;
    }

    public int getCount() {
        return list_sites.size();
    }

    public Object getItem(int position) {
        return list_sites.get(position);
    }

    public long getItemId(int position) {
        return list_sites.get(position).getId();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View spinView;
        if( convertView == null ){
            LayoutInflater inflater = activity.getLayoutInflater();
            spinView = inflater.inflate(R.layout.spin_layout, null);
        } else {
            spinView = convertView;
        }
        TextView t1 = (TextView) spinView.findViewById(R.id.field1);
        TextView t2 = (TextView) spinView.findViewById(R.id.field2);
        t1.setText(String.valueOf(list_sites.get(position).getName()));
        t2.setText(list_sites.get(position).getAddress());
        return spinView;
    }
}
