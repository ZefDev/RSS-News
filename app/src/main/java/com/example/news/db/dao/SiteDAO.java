package com.example.news.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.news.db.entity.Site;

import java.util.List;

@Dao
public interface SiteDAO {
    @Query("SELECT * FROM site")
    List<Site> getAll();

    @Query("SELECT * FROM site WHERE id IN (:userIds)")
    List<Site> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM site WHERE address LIKE :address LIMIT 1")
    Site findByName(String address);

    @Update
    public void updateSite(Site... sites);

    @Insert
    void insertAll(Site... sites);

    @Delete
    void delete(Site site);
}