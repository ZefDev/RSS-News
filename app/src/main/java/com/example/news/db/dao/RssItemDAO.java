package com.example.news.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.news.db.entity.RssItem;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface RssItemDAO {

    @Query("SELECT * FROM RssItem")
    List<RssItem> getAll();

    @Query("DELETE FROM RssItem")
    void deleteAll();

    @Update
    public void updateRssItem(RssItem... rssItems);

    @Query("SELECT * FROM RssItem WHERE id IN (:userIds)")
    List<RssItem> loadAllByIds(int[] userIds);

    @Insert
    void insertAll(RssItem... rssItems);

    @Delete
    void delete(RssItem rssItem);
}

