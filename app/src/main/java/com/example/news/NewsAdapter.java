package com.example.news;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.news.db.entity.RssItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdapter extends BaseAdapter {

    private Context context;
    ArrayList<RssItem> news;

    public NewsAdapter(Context context, ArrayList<RssItem> news) {
        this.context = context;
        this.news = news;
    }

    @Override
    public int getCount() {
        return news.size();
    }

    @Override
    public Object getItem(int position) {
        return news.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = LayoutInflater.from(context).inflate(R.layout.list_item_news, parent, false);
        // Get title element
        TextView titleTextView = (TextView) rowView.findViewById(R.id.recipe_list_title);
        TextView dateTextView = (TextView) rowView.findViewById(R.id.list_date);
// Get subtitle element
        final TextView subtitleTextView = (TextView)  rowView.findViewById(R.id.recipe_list_subtitle);
// Get detail element
        //TextView detailTextView = (TextView)  rowView.findViewById(R.id.recipe_list_detail);
// Get thumbnail element
        ImageView thumbnailImageView = (ImageView)  rowView.findViewById(R.id.recipe_list_thumbnail);
        // 1
        final RssItem news = (RssItem) getItem(position);
        Button share = (Button) rowView.findViewById(R.id.share);
        Button more = (Button) rowView.findViewById(R.id.more);
        final Button readMore = (Button) rowView.findViewById(R.id.read_more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, v);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int i = item.getItemId();
                        if (i == R.id.openInApp) {
                            Intent intentOpenInApp = new Intent(context, WebPage.class);
                            intentOpenInApp.putExtra("link", ((RssItem) getItem(position)).getLink());
                            context.startActivity(intentOpenInApp);
                            return true;
                        }
                        else if (i == R.id.openInBrowser){
                            String url = ((RssItem) getItem(position)).getLink();
                            Intent intentOpenInBrowser = new Intent(Intent.ACTION_VIEW);
                            intentOpenInBrowser.setData(Uri.parse(url));
                            context.startActivity(intentOpenInBrowser);
                            return true;
                        }
                        else {
                            return onMenuItemClick(item);
                        }
                    }
                });

                popup.show();

            }
        });

        readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    if(subtitleTextView.getMaxLines() != Integer.MAX_VALUE){
                        subtitleTextView.setMaxLines(Integer.MAX_VALUE);
                        readMore.setBackgroundResource(R.drawable.expand_less);
                    }
                    else {
                        subtitleTextView.setMaxLines(3);
                        readMore.setBackgroundResource(R.drawable.expand_more);
                    }
                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Ссылка на новость "+news.getLink();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                context.startActivity(sharingIntent);
            }
        });


// 2
        titleTextView.setText(news.getTitle());
        subtitleTextView.setText(news.getDescription());
        //detailTextView.setText(news.getLabel());
        dateTextView.setText(news.getPubDate());

// 3
        if(news.getImageUrl()!=null) {
            Picasso.get().load(news.getImageUrl()).into(thumbnailImageView);
        }

        return rowView;
    }



}
