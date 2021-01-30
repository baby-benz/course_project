package com.example.workerinterface;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HTTPRequest {

    private URL url;
    private HttpURLConnection con;
    private final String IP;
    private final String USER_AGENT = "Mozilla/5.0";

    public HTTPRequest() {
        this.IP = "192.168.1.130:8080";
    }

    public void patchRequest(@NotNull String type, String urlParameters, int id) {
        try {
            if (type.equals("order")) {
                url = new URL(new URL("http", IP, "/api/order/" + id + "/status?" + urlParameters).toString().replace("[", "").replace("]", ""));
            } else if (type.equals("product")) {
                url = new URL(new URL("http", IP, "/api/product/" + id + "/available?" + urlParameters).toString().replace("[", "").replace("]", ""));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            con = (HttpURLConnection)url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            con.setRequestMethod("PATCH");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        con.setRequestProperty("User-Agent", this.USER_AGENT);

        // For PATCH only - START
        con.setDoOutput(true);
        try {
            OutputStream os = con.getOutputStream();
            os.write(urlParameters.getBytes());
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // For PATCH only - END

        try {
            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            } else {
                System.out.println("PATCH request not worked");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            con.disconnect();
        }

    }

    public String getRequest(@NotNull String type, @NotNull String urlParameters, int id) {
        if (urlParameters.equals("")) {
            urlParameters = "/"+id;
        }
        try {
            if (type.equals("order") || type.equals("product")) {
                this.url = new URL(new URL("http", IP, "/api/" + type + urlParameters).toString().replace("[", "").replace("]", ""));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        try {
            this.con = (HttpURLConnection) this.url.openConnection();
            this.con.setRequestMethod("GET");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        this.con.setRequestProperty("User-Agent", USER_AGENT);
        StringBuilder total = new StringBuilder();
        try {
            if (this.con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(con.getInputStream());
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                for (String line; (line = r.readLine()) != null; ) {
                    total.append(line).append('\n');
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            con.disconnect();
        }
        return total.toString();
    }

}