package com.example.news;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Parser {
    static final String PUB_DATE = "pubDate";
    static final String DESCRIPTION = "description";
    static final String CHANNEL = "channel";
    static final String LINK = "link";
    static final String TITLE = "title";
    static final String ITEM = "item";
    static final String ENCLOSER = "enclosure";
    static final String CATEGORY = "category";

    public ArrayList<RssItem> getList(String RssFeead) {
        ArrayList<RssItem> list = new ArrayList<RssItem>();
        XmlPullParser parser = Xml.newPullParser();
        InputStream stream = null;
        try {
            // auto-detect the encoding from the stream
            stream = new URL(RssFeead).openConnection().getInputStream();
            parser.setInput(stream, null);
            int eventType = parser.getEventType();
            boolean done = false;
            RssItem item = null;
            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                String name = null;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(ITEM)) {
                            Log.i("new item", "Create new item");
                            item = new RssItem();
                        } else if (item != null) {
                            if (name.equalsIgnoreCase(LINK)) {
                                Log.i("Attribute", "setLink");
                                item.setLink(parser.nextText());
                            } else if (name.equalsIgnoreCase(DESCRIPTION)) {
                                Log.i("Attribute", "description");
                                String html = parser.nextText().trim();
                                Document doc = Jsoup.parse(html);
                                String imgLink = "";
                                imgLink = doc.getElementsByTag("img").attr("src").toString();
                                if (imgLink.length()>0){
                                    imgLink = imgLink.replace("\\","");
                                    imgLink = imgLink.replace("\"","");
                                    item.setImageUrl(imgLink);
                                }
                                item.setDescription(android.text.Html.fromHtml(html).toString());
                            } else if (name.equalsIgnoreCase(PUB_DATE)) {
                                String date = parser.nextText().trim();
                                date = date.substring(0,date.length()-5);
                                item.setPubDate(date);//parser.nextText().trim()
                            } else if (name.equalsIgnoreCase(TITLE)) {
                                Log.i("Attribute", "title");
                                item.setTitle(parser.nextText().trim());
                            }
                            else if (name.equalsIgnoreCase(CATEGORY)) {
                                Log.i("Attribute", "category");
                                item.setLabel(parser.nextText().trim());
                            }
                            else if (name.equalsIgnoreCase(ENCLOSER)) {
                                Log.i("Attribute", "enclosure");
                                if (parser.getAttributeValue(0).trim().length()>0) {
                                    item.setImageUrl(parser.getAttributeValue(0).trim());
                                }// Получаем ссылку на картинку
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        Log.i("End tag", name);
                       /* if (name.equalsIgnoreCase(ENCLOSER)) {
                            Log.i("Attribute", "enclosure");

                            item.setImageUrl(parser.getAttributeName(0).trim());
                        }*/
                        if (name.equalsIgnoreCase(ITEM) && item != null) {
                            Log.i("Added", item.toString());
                            list.add(item);
                        } else if (name.equalsIgnoreCase(CHANNEL)) {
                            done = true;
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {

        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }


}


