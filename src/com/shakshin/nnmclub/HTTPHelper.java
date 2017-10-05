package com.shakshin.nnmclub;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Arrays;

public class HTTPHelper {
    private HttpURLConnection getConnection(String targetUrl) throws IOException {
        URL url = new URL(targetUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
        connection.setRequestMethod("GET");
        connection.setUseCaches(false);
        connection.setDoInput(true);
        return connection;
    }

    public String getURLContent(String targetUrl) throws IOException {
        try {

            HttpURLConnection connection = getConnection(targetUrl);

            int rCode = connection.getResponseCode();
            if (rCode == HttpURLConnection.HTTP_MOVED_PERM || rCode == HttpURLConnection.HTTP_MOVED_TEMP) {
                String newTarget = URLDecoder.decode(connection.getHeaderField("Location"), "UTF-8");
                URL base = new URL(targetUrl);
                URL next = new URL(base, newTarget);
                return getURLContent(next.toExternalForm());
            }

            if (rCode != HttpURLConnection.HTTP_OK)
                throw new IOException("Bad HTTP response: " + rCode + " " + connection.getResponseMessage());

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("Windows-1251")));
            StringBuilder bldr = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                bldr.append(line);
                bldr.append("\n");
            }
            reader.close();
            return bldr.toString();
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public void downloadFile(String targetUrl, String path) throws IOException {
        try {
            HttpURLConnection connection = getConnection(targetUrl);

            int rCode = connection.getResponseCode();
            if (rCode != HttpURLConnection.HTTP_OK)
                throw new IOException("Bad HTTP response: " + rCode + " " + connection.getResponseMessage());

            InputStream in = connection.getInputStream();

            File file = new File(path);
            FileOutputStream out = new FileOutputStream(file);

            byte[] buf = new byte[4096];

            while (true) {
                int bc =in.read(buf);
                if (bc < 1) break;
                out.write(Arrays.copyOfRange(buf, 0, bc));
            }
            in.close();
            out.close();

        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
