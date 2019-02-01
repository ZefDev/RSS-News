package com.example.news.service.servicelmpl;

import android.content.Context;

import com.example.news.db.AppDatabase;
import com.example.news.db.dao.SiteDAO;
import com.example.news.db.entity.Site;
import com.example.news.service.SiteService;

import java.util.List;

public class SiteServicelmpl implements SiteService {

    private SiteDAO siteDao;

    public SiteServicelmpl(Context context) {
        siteDao = AppDatabase.getInstance(context).siteDao();
    }

    @Override
    public List<Site> getAll() {
        return siteDao.getAll();
    }

    @Override
    public void delete(Site site) {
        siteDao.delete(site);
    }
    @Override
    public Site findByName(String address){
        return siteDao.findByName(address);
    }

    @Override
    public void update(Site site) {
        siteDao.updateSite(site);
    }

    @Override
    public void insertAll(Site... sites) {
        siteDao.insertAll(sites);
    }
}
