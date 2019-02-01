package com.example.news.service.servicelmpl;

import android.content.Context;

import com.example.news.db.AppDatabase;
import com.example.news.db.dao.RssItemDAO;
import com.example.news.db.entity.RssItem;
import com.example.news.service.RssItemService;

import java.util.List;

public class RssItemServicelmpl implements RssItemService {

    private RssItemDAO rssItemDAO;

    public RssItemServicelmpl(Context context) {
        rssItemDAO = AppDatabase.getInstance(context).rssItemDao();
    }

    @Override
    public List<RssItem> getAll() {
        return rssItemDAO.getAll();
    }

    @Override
    public void delete(RssItem rssItem) {
        rssItemDAO.delete(rssItem);
    }

    @Override
    public void deleteAll() {
        rssItemDAO.deleteAll();
    }


    @Override
    public void update(RssItem rssItem) {
        rssItemDAO.updateRssItem(rssItem);
    }

    @Override
    public void insertAll(RssItem... rssItems) {
        rssItemDAO.insertAll(rssItems);
    }
}
