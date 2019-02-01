package com.example.news.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.news.db.dao.RssItemDAO;
import com.example.news.db.dao.SiteDAO;
import com.example.news.db.entity.RssItem;
import com.example.news.db.entity.Site;

@Database(entities = {Site.class, RssItem.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase appDatabase = null;

    /**
     * from developers android, made my own singleton
     *
     * @param context
     * @return
     */
    public static AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "database-name").build();
        }
        return appDatabase;
    }

    public abstract SiteDAO siteDao();
    public abstract RssItemDAO rssItemDao();
}