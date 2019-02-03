package com.example.news;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.news.db.entity.RssItem;
import com.example.news.db.entity.Site;
import com.example.news.service.servicelmpl.RssItemServicelmpl;
import com.example.news.service.servicelmpl.SiteServicelmpl;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity { // AppCompat

    ArrayList<RssItem> list = new ArrayList<>();
    ListView listView;
    ProgressBar progressBar;
    TextView noNewsTextView;
    private ActionBar actionBar;
    BottomNavigationView bottomNavigation;
    LinearLayout rssView;
    LinearLayout settingsView;
    Spinner listSites;
    private static final String LOG_TAG = "MyActivity";
    Boolean isCanRefresh = false;
    task t;
    private SiteTask mAddSiteTask = null;
    private SiteServicelmpl siteService;
    private List<Site> sites = new ArrayList<>();
    private RssItemServicelmpl rssItemsService;
    Button btnAddSite,btnDeleteSite,btnChangeSite;
    TextView name,adress;

    public String defaultRss = "http://online.anidub.com/rss.xml";
    //https://news.tut.by/rss/index.rss // https://news.yandex.ru/society.rss //https://lenta.ru/rss/news //http://www.animacity.ru/rss/animes/news.rss


    @Override
    protected void onStart() {
        super.onStart();
        getSitesFromDB();
        startApp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        siteService = new SiteServicelmpl(MainActivity.this);
        rssItemsService = new RssItemServicelmpl(MainActivity.this);
        //t.execute();
        settingsView = (LinearLayout) findViewById(R.id.settingsView);
        rssView = (LinearLayout) findViewById(R.id.rssView);
        listView = (ListView)findViewById(R.id.listView);
        progressBar = (ProgressBar)findViewById(R.id.progressBar3);
        noNewsTextView= (TextView)findViewById(R.id.noNewsTextView);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        listSites = (Spinner) findViewById(R.id.listSites);
        btnAddSite = (Button) findViewById(R.id.btnAddSite);
        btnDeleteSite = (Button) findViewById(R.id.btnDeleteSite);
        btnChangeSite = (Button) findViewById(R.id.btnChangeSite);
        name = (TextView) findViewById(R.id.editText);
        adress = (TextView) findViewById(R.id.editText2);


        actionBar = getSupportActionBar();
        /* update rss-site*/
        btnChangeSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listSites.getSelectedItem()!=null) {
                    Site changingSite = (Site)listSites.getSelectedItem();
                    changingSite.setName(name.getText().toString());
                    changingSite.setAddress(adress.getText().toString());
                    changeSite((Site)listSites.getSelectedItem());
                }
            }
        });
        /* deleted rss-site*/
        btnDeleteSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listSites.getSelectedItem()!=null) {
                    deleteSite((Site)listSites.getSelectedItem());
                }
            }
        });
        /* addition rss-site*/
        btnAddSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().length()!=0 & adress.getText().toString().length()!=0 ) {
                    new SiteTask(name.getText().toString(), adress.getText().toString()).execute();
                }
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(listView.getFirstVisiblePosition()>0){
                    isCanRefresh = true;
                }
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (listView.getLastVisiblePosition() - listView.getHeaderViewsCount() -
                        listView.getFooterViewsCount()) >= (listView.getCount() - 1)) {
                    Toast.makeText(getApplicationContext(),"Типо подгрузил контент из базы",Toast.LENGTH_LONG).show();
                    // Now your listview has hit the bottom
                }
                else if((scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) & (listView.getFirstVisiblePosition()==0)){
                    /* Updating the list when reaching the beginning list
                    if(isCanRefresh) {
                        progressBar.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                        new task().execute();
                        isCanRefresh = false;
                    }*/
                }
                //Log.d(LOG_TAG, "scrollState = " + scrollState);
            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                /*Log.d(LOG_TAG, "scroll: firstVisibleItem = " + firstVisibleItem
                        + ", visibleItemCount" + visibleItemCount
                        + ", totalItemCount" + totalItemCount);*/
            }
        });

        /* open web-version rss-news*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, WebPage.class);
                i.putExtra("link", list.get(position).getLink());
                startActivity(i);
            }
        });



        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_map:
                                startApp();
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

    public void startApp(){
        rssView.setVisibility(View.VISIBLE);
        settingsView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        noNewsTextView.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        task t = new task();
        t.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                progressBar.setVisibility(View.VISIBLE);
                noNewsTextView.setVisibility(View.GONE);
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
    /* loading rss.xml in bd and display in list */
    private class task extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this,"Im loading",Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            listView.setAdapter(null);
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
            if(listSites.getSelectedItem()!=null) {
                actionBar.setTitle(((Site) listSites.getSelectedItem()).getName());
            }
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Site selectedSite = null;
            if(isNetworkConnected()){
                Parser w1 = new Parser();

                if(listSites.getAdapter().getCount()!=0) {
                    selectedSite = (Site) listSites.getSelectedItem();
                    noNewsTextView.setVisibility(View.GONE);
                    list.clear();
                    list = w1.getList(selectedSite.getAddress());

                    rssItemsService.deleteAllBySiteId(selectedSite.getId());
                    for (int i=0;i<list.size();i++)
                    {
                        list.get(i).setSiteId(selectedSite.getId());
                        rssItemsService.insertAll(list.get(i));
                    }

                }
            }
            else {
                if(listSites.getAdapter().getCount()!=0) {
                    noNewsTextView.setVisibility(View.GONE);

                    /* Велосипед для румаа так как эта падла види только List*/
                    list.clear();
                    try {
                        selectedSite = (Site) listSites.getSelectedItem();
                    }
                    catch (Exception e){
                        return null; //ещё один лясик
                    }
                    List<RssItem> l = rssItemsService.findRssItemBySiteId(selectedSite.getId());
                    for (int i=0;i<l.size();i++){
                        list.add(l.get(i));
                    }
                }
            }

            return null;
        }

        private boolean isNetworkConnected() {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            return cm.getActiveNetworkInfo() != null;
        }


    }



    /* addition new rss-site*/
    public class SiteTask extends AsyncTask<Void, Void, Boolean> {

        private final String mName;
        private final String mAdress;

        SiteTask(String name, String adress) {
            mName = name;
            mAdress = adress;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

                Site site = new Site();
                site.setName(mName);
                site.setAddress(mAdress);
                siteService.insertAll(site);
                sites = siteService.getAll();

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAddSiteTask = null;
            if (success) {
                Toast.makeText(MainActivity.this, "Site is added ", Toast.LENGTH_LONG).show();
                listSites.setAdapter(new myCursorAdapter(MainActivity.this,sites));
            }
        }
        @Override
        protected void onCancelled() {
            mAddSiteTask = null;
        }
    }

    /* loading rss-site in spinner from db*/
    private void getSitesFromDB() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                sites = siteService.getAll();
                return null;
            }

            @Override
            protected void onPostExecute(Void agentsCount) {

                listSites.setAdapter(new myCursorAdapter(MainActivity.this,sites));

            }
        }.execute();

    }

    /* update rss-site from db */
    private void changeSite(Site site){
        new AsyncTask<Site, Void, Void>() {
            @Override
            protected Void doInBackground(Site... params) {
                if(params[0]!=null) {
                    siteService.update(params[0]);
                    sites = siteService.getAll();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void agentsCount) {
                listSites.setAdapter(new myCursorAdapter(MainActivity.this,sites));

            }
        }.execute(site);
    }

    /* delete rss-site from db*/
    private void deleteSite(Site site) {
        new AsyncTask<Site, Void, Void>() {
            @Override
            protected Void doInBackground(Site... params) {
                if(params[0]!=null) {
                    siteService.delete(params[0]);
                    sites = siteService.getAll();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void agentsCount) {
                listSites.setAdapter(new myCursorAdapter(MainActivity.this,sites));

            }
        }.execute(site);
    }
}
