package com.shakshin.nnmclub;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Config {
    String passkey;
    String folder;
    ArrayList<TopicCfg> topics;
    public void setPasskey(String value) { passkey = value; }
    public void setFolder(String value) { folder = value; }
    public void setTopics(ArrayList<TopicCfg> value) { topics = value; }
    public String getPasskey() { return passkey; }
    public String getFolder() { return folder; }
    public ArrayList<TopicCfg> getTopics() { return topics; }

    public Config() {
        passkey = "";
        folder = System.getProperty("user.home");
        topics = new ArrayList<TopicCfg>();
    }

    public void write() throws IOException {
        String path = System.getProperty("user.home") + File.separator + ".nnmclub.xml";
        File file = new File(path);
        FileOutputStream out = new FileOutputStream(path);
        XMLEncoder enc = new XMLEncoder(out);
        enc.writeObject(this);
        enc.flush();
        enc.close();
    }

    public static Config read() {
        String path = System.getProperty("user.home") + File.separator + ".nnmclub.xml";
        try {
            FileInputStream in = new FileInputStream(path);
            XMLDecoder dec = new XMLDecoder(in);

            return (Config)dec.readObject();
        } catch (Exception e) {
            return new Config();
        }
    }
}
