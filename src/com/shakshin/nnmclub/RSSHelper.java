package com.shakshin.nnmclub;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;


public class RSSHelper {
    public class RSSException extends Exception {
        private String _message = "";
        public RSSException(String message) {
            _message = message;
        }

        @Override
        public String getMessage() {
            return _message;
        }
    }

    private ArrayList<HashMap<String, String>> items = null;

    public void parse(String content) throws IOException, SAXException {
        items = new ArrayList<HashMap<String, String>>();
        DOMParser parser = new DOMParser();
        parser.parse(new InputSource(new StringReader(content)));
        Document doc = parser.getDocument();

        Node current = doc.getDocumentElement().getElementsByTagName("channel").item(0).getFirstChild();

        while (true) {
            if (current.getNodeType() == Node.ELEMENT_NODE) {
                String lName = current.getLocalName();
                if (lName != null && lName == "item") {
                    HashMap<String, String> itemData = new HashMap<String, String>();
                    Node sub = current.getFirstChild();
                    while (true) {
                        if (sub.getNodeType() == Node.ELEMENT_NODE) {
                            itemData.put(sub.getLocalName(), sub.getTextContent());
                        }

                        sub = sub.getNextSibling();
                        if (sub == null) break;
                    }
                    items.add(itemData);
                }
            }
            current = current.getNextSibling();
            if (current == null) break;
        }


    }

    public ArrayList<HashMap<String, String>> getItems() {
        return items;
    }
}

