package com.example.news;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity { // AppCompat

    ArrayList<RssItem> list = new ArrayList<RssItem>();
    ListView listView;
    ProgressBar progressBar;
    TextView noNewsTextView;
    private ActionBar actionBar;
    BottomNavigationView bottomNavigation;
    LinearLayout rssView;
    LinearLayout settingsView;
    Spinner listSites;

    public String defaultRss = "http://online.anidub.com/rss.xml";
    //https://news.tut.by/rss/index.rss // https://news.yandex.ru/society.rss //https://lenta.ru/rss/news //http://www.animacity.ru/rss/animes/news.rss
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        task t = new task();
        t.execute();
        settingsView = (LinearLayout) findViewById(R.id.settingsView);
        rssView = (LinearLayout) findViewById(R.id.rssView);
        listView = (ListView)findViewById(R.id.listView);
        progressBar = (ProgressBar)findViewById(R.id.progressBar3);
        noNewsTextView= (TextView)findViewById(R.id.noNewsTextView);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        listSites = (Spinner) findViewById(R.id.listSites);

        actionBar = getActionBar();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                Toast.makeText(getApplicationContext(), ((TextView) itemClicked).getText(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(MainActivity.this,list.get(position).getDescription(),Toast.LENGTH_LONG).show();
                Intent i = new Intent(MainActivity.this, WebPage.class);
                i.putExtra("link", list.get(position).getLink());
                startActivity(i);
                //NewsAdapter adapter=(NewsAdapter) listView.getAdapter();
                //String  selectedFromList = (String) adapter.getItem(position);
            }
        });
        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_map:
                                rssView.setVisibility(View.VISIBLE);
                                settingsView.setVisibility(View.GONE);
                                progressBar.setVisibility(View.VISIBLE);
                                listView.setVisibility(View.GONE);
                                task t = new task();
                                t.execute();
                                break;
                            case R.id.action_dial:
                                rssView.setVisibility(View.GONE);
                                settingsView.setVisibility(View.VISIBLE);
                                break;
                            case R.id.action_close:
                                finish();
                                break;
                        }
                        return true;
                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                progressBar.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                task t = new task();
                t.execute();

                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private class task extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this,"Im loading",Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            NewsAdapter listAdapter = new
                    NewsAdapter(MainActivity.this, list);

            listView.setAdapter(listAdapter);
            if(list.size()!=0) {
                progressBar.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }
            else {
                progressBar.setVisibility(View.GONE);
                noNewsTextView.setVisibility(View.VISIBLE);
            }

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Parser w1 = new Parser();
            list = w1.getList(listSites.getSelectedItem().toString());

            return null;
        }
    }
}
