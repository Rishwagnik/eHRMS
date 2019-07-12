package com.rishwagnik.ehrms;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import static androidx.core.content.ContextCompat.getSystemService;


public class HttpHandler {

    private static final String TAG = HttpHandler.class.getSimpleName();

    public HttpHandler(){}

    //Takes url as input and returns received data as a string
    public String makeServiceCall(String reqURL){

        if(!isInternetAvailable()){
            return "No Internet";
        }

        String response = null;
        try {

            URL url = new URL(reqURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(15000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            response = convertStreamToString(in);

        } catch (Exception e) {
            Log.e(TAG,e.getClass().getName() + ": " + e.getMessage());
        }

        return response;

    }

    //Converts received data stream into a string
    public String convertStreamToString(InputStream is){

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;

        try{

            while((line = reader.readLine()) != null){
                sb.append(line).append("\n");
            }

        } catch (Exception e) {
            Log.e(TAG,e.getClass().getName() + ": " + e.getMessage());
        } finally {

            try {
                reader.close();
                is.close();
            } catch (Exception e) {
                Log.e(TAG,e.getClass().getName() + ": " + e.getMessage());
            }

        }

        return sb.toString();
    }

    //Takes JSON Object and url as input and hits the url with the JSON
    public String uploadData(JSONObject jsonObject, String serverURL){

        String result = "";

        if(!isInternetAvailable()){
            return "No Internet";
        }

        try{
            URL url = new URL(serverURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setReadTimeout(15000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();
            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            writer.write(getPostDataString(jsonObject));
            writer.flush();
            writer.close();
            os.close();

            //Get the response from the site
            int responseCode = urlConnection.getResponseCode();
            if(responseCode == 200){//Response code for success

                InputStream in = urlConnection.getInputStream();
                result = convertStreamToString(in);
                return result;

            }
            else {
                return new String("false: " + result);
            }

        } catch (Exception e){
            Log.i(TAG, e.getClass().getName() + ": " + e.getMessage());
            return new String("Exception: " + result);
        }

    }

    //Takes JSON Object, converts it to string and encodes it
    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){
            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();

    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

}
