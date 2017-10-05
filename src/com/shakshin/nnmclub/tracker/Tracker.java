package com.shakshin.nnmclub.tracker;

import com.shakshin.nnmclub.Config;
import com.shakshin.nnmclub.HTTPHelper;
import com.shakshin.nnmclub.RSSHelper;
import com.shakshin.nnmclub.TopicCfg;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Tracker {
    private final String baseURL ="https://nnm-club.name";
    private String passKey;

    public Tracker(String passKey) {
        this.passKey = passKey;
    }

    public Tracker() {}

    public Topic getTopic(Integer id) throws IOException, SAXException {
        HTTPHelper http = new HTTPHelper();
        String content = http.getURLContent(baseURL + "/forum/viewtopic.php?t=" + id.toString());
        String title = content.split("<title>")[1].split("</title>")[0];
        return new Topic(id, title);
    }

    public List<Topic> getRSSTopics() {
        ArrayList<Topic> list = new ArrayList<Topic>();
        RSSHelper rss = new RSSHelper();
        try {
            rss.parse(new HTTPHelper().getURLContent(baseURL + "/forum/rss2.php?h=1&uk=" + passKey));
        } catch (Exception e) {}

        if (rss.getItems() == null) {
            System.err.println("No items fetched from RSS");
            return list;
        }

        for (HashMap<String, String> item : rss.getItems()) {
            String id = item.get("description").split("t=")[1].split("\"")[0];
            Topic t = new Topic(Integer.parseInt(id), item.get("title").replaceAll("\n", ""));
            t.url = item.get("link").replaceAll("\n", "");
            list.add(t);
        }

        return list;
    }

    public boolean poll(Config config) {
        boolean needWrite = false;
        ArrayList<Integer> ids = new ArrayList<Integer>();
        ArrayList<Integer> upd = new ArrayList<Integer>();
        for (TopicCfg tc : config.getTopics())
            ids.add(tc.getId());

        HTTPHelper http = new HTTPHelper();

        for (Topic t : getRSSTopics()) {
            if (ids.contains(t.id)) {
                if (t.url != null && !t.url.isEmpty()) {
                    try {
                        http.downloadFile(t.url, config.getFolder() + File.separator + "nnmclub-" + t.id.toString() + ".torrent");
                        System.out.println("Downloaded update for topic " + t.id.toString() + " (" + t.title + ")");
                        upd.add(t.id);
                    } catch(Exception e){
                        System.err.println("Can not download torrent file: " + e.getMessage());
                    }
                }
            }
        }

        SimpleDateFormat fmt = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        if (upd.size() > 0) {
            for (Integer id : upd) {
                for (TopicCfg tc : config.getTopics())
                    tc.setLastUpdated(fmt.format(Calendar.getInstance().getTime()));
            }
            needWrite = true;
        }

        return needWrite;
    }

    public boolean actualize(Config config) {
        boolean needWrite = false;
        for (TopicCfg tc : config.getTopics()) {
            try {
                Topic t = getTopic(tc.getId());
                if (t != null && !t.title.equals(tc.getTitle())) {
                    tc.setTitle(t.title);
                    needWrite = true;
                }

            } catch (Exception e) {} // just leave topic alone
        }
        return needWrite;
    }
}
