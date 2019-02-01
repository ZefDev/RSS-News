package com.example.news.service;

import com.example.news.db.entity.Site;

import java.util.List;

public interface SiteService {

    List<Site> getAll();

    void delete(Site site);

    Site findByName(String address);

    void update(Site site);

    void insertAll(Site... sites);
}
