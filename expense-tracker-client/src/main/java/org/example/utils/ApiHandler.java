package org.example.utils;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ApiHandler {
    // change this url if necessary
    private static final String SPRINGBOOT_URL = "http://localhost:8080";
    public enum RequestMethod{POST, GET, PUT, DELETE}

    public static HttpURLConnection fetchApiResponse(String apiPath, RequestMethod requestMethod, JsonObject jsonData){
        try{
            // attempt to create connection
            URL url = new URL(SPRINGBOOT_URL + apiPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // set request method to get
            conn.setRequestMethod(requestMethod.toString());

            if(jsonData != null && requestMethod != RequestMethod.GET){
                // lets the api know that we will be sending in json data
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                // expects the response body to be of json type
                conn.setRequestProperty("Accept", "application/json");

                // allows us to send data to the connected api
                conn.setDoOutput(true);

                // send JSON data to the server by writing it to the output stream (closes the stream automatically)
                try (OutputStream os = conn.getOutputStream()) {
                    // convert the JSON data to a byte array
                    byte[] input = jsonData.toString().getBytes(StandardCharsets.UTF_8);

                    // write the byte array to the output stream
                    os.write(input, 0, input.length);
                }
            }

            return conn;
        }catch(IOException e){
            e.printStackTrace();
        }

        // could not make connection
        return null;
    }

    public static String readApiResponse(HttpURLConnection apiConnection) {
        try {
            // create a StringBuilder to store the resulting JSON data
            StringBuilder resultJson = new StringBuilder();

            // create a Scanner to read from the InputStream of the HttpURLConnection
            Scanner scanner = new Scanner(apiConnection.getInputStream());

            // loop through each line in the response and append it to the StringBuilder
            while (scanner.hasNext()) {
                // read and append the current line to the StringBuilder
                resultJson.append(scanner.nextLine());
            }

            // close the Scanner to release resources associated with it
            scanner.close();

            // return the JSON data as a String
            return resultJson.toString();

        } catch (IOException e) {
            // print the exception details in case of an IOException
            e.printStackTrace();
        }

        // return null if there was an issue reading the response
        return null;
    }
}
