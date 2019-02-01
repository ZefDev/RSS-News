package com.example.news.service;

import com.example.news.db.entity.RssItem;

import java.util.List;

public interface RssItemService {

        List<RssItem> getAll();

        void delete(RssItem rssItem);

        void deleteAll();

        void update(RssItem rssItem);

        void insertAll(RssItem... rssItems);
}
