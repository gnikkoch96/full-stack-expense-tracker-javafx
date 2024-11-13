package org.example.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiHandler {
    // change this url if necessary
    public static final String SPRINGBOOT_API_URL = "http://localhost:8080/";

    public enum RequestMethod{POST, GET, PUT, DELETE}

    public static HttpURLConnection fetchApiResponse(String apiPath, RequestMethod requestMethod){
        try{
            // attempt to create connection
            URL url = new URL(SPRINGBOOT_API_URL + apiPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // set request method to get
            conn.setRequestMethod(requestMethod.toString());

            return conn;
        }catch(IOException e){
            e.printStackTrace();
        }

        // could not make connection
        return null;
    }
}
