package com.shakshin.nnmclub;

import com.shakshin.nnmclub.tracker.Topic;
import com.shakshin.nnmclub.tracker.Tracker;

public class Main {

    private static Config config;
    private static  boolean needWrite = false;

    private static void help() {
        System.out.println(
                "NNMClub automated download tool\n" +
                        "Original code by Sergey V. Shakshin (rigid.mgn@gmail.com)\n\n" +
                        "Commands:\n" +
                        "  help - show this screen\n" +
                        "  passkey - set or show passkey\n" +
                        "  folder - set or show target folder\n" +
                        "  add - add topic to monitoring list\n" +
                        "  del - delete topic from monitoring list\n" +
                        "  list - list monitored topics\n" +
                        "  poll - poll RSS feed and download updated torrents\n" +
                        "  actualize - fetch updated topic titles"
        );
    }

    private static void folder(String[] args){
        switch (args.length) {
            case 1:
                System.out.println(config.folder);
                break;
            case 2:
                config.folder = args[1];
                needWrite = true;
                break;
            default:
                System.err.println("Wrong number of arguments");
                break;
        }
    }

    private static void passkey(String[] args) {
        switch (args.length) {
            case 1:
                System.out.println(config.passkey);
                break;
            case 2:
                config.passkey = args[1];
                needWrite = true;
                break;
            default:
                System.err.println("Wrong number of arguments");
                break;
        }
    }

    private static void add(String[] args) {
        if (args.length != 2) {
            System.err.println("Wrong number of arguments");
            return;
        }
        Integer id;
        try {
            id = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("Topic id must be an integer number");
            return;
        }

        TopicCfg tc = new TopicCfg();
        tc.id = id;

        Tracker trk = new Tracker();
        try {
            Topic t = trk.getTopic(id);
            tc.title = t.title;
        } catch (Exception e) {
            System.err.println("Warning. Can not fetch topic data from tracker. Title was not filled.");
        }

        config.topics.add(tc);
        list();

        needWrite = true;
    }

    private static void del(String[] args) {
        if (args.length != 2) {
            System.err.println("Wrong number of arguments");
            return;
        }
        Integer id;
        try {
            id = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("Topic id must be an integer number");
            return;
        }
        for (TopicCfg tc : config.topics) {
            if (tc.id.equals(id)) {
                config.topics.remove(tc);
                needWrite = true;
                list();
                break;
            }
        }

        if (!needWrite) {
            System.err.println("Topic with id " + args[1] + " is not in list");
        }
    }

    private static void list() {
        if (config.topics.size() == 0) {
            System.out.println("No topics in list");
            return;
        }
        System.out.println("Monitored topics:");
        for (TopicCfg tc : config.topics) {
            System.out.println(tc.toString() + "\n");
        }
    }

    private static void poll() {
        if (config.passkey == null || config.passkey.isEmpty()) {
            System.out.println("No passkey defined");
            return;
        }
        if (config.folder == null || config.folder.isEmpty()) {
            System.out.println("No target folder defined");
            return;
        }
        Tracker trk = new Tracker(config.passkey);
        needWrite = trk.poll(config);
    }

    private static void actualize() {
        Tracker trk = new Tracker();
        needWrite = trk.actualize(config);
        list();
    }

    public static void main(String[] args) {

        if (args.length == 0) {
            help();
            return;
        }

        config = Config.read();

        switch (args[0].toLowerCase()) {
            case "help":
                help();
                break;
            case "passkey":
                passkey(args);
                break;
            case "folder":
                folder(args);
                break;
            case "add":
                add(args);
                break;
            case "del":
                del(args);
                break;
            case "list":
                list();
                break;
            case "poll":
                poll();
                break;
            case "actualize":
                actualize();
                break;
            default:
                System.err.println("No such command: " + args[0]);
        }

        if (needWrite) {
            try {
                config.write();
            } catch (Exception e) {
                System.err.println("Can not write configuration: " + e.getMessage());
            }
        }
        /* Config cfg = Config.read();

        Tracker trk  = new Tracker("vA2jvlzioy");
        try {
            for (Topic t : trk.getRSSTopics())
                System.out.println(t.toString());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        try {
            cfg.write();
        } catch (IOException e) {
            System.err.println("Configuration save error: " + e.getMessage());
        }
        */
    }
}
