package com.asif.linkkin.helper;

import android.net.Uri;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by root on 11/27/14.
 */
public class ConnectionHelper {

    private HttpURLConnection httpURLConnection;
    private DataOutputStream dos;

    public void createConnection(String mUrl,String mRequestType){

        try {
            httpURLConnection = (HttpURLConnection) (new URL(mUrl)).openConnection();
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod(mRequestType);

            httpURLConnection.connect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void addData(Uri.Builder builder){
        try {

            String query = builder.build().getEncodedQuery();

            dos = new DataOutputStream(httpURLConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(dos, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            dos.close();

//            httpURLConnection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getResponse(){
        InputStream is = null;
        StringBuilder buffer = null;
        try {
            is = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            buffer = new StringBuilder();

            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            httpURLConnection.disconnect();
        }

        return null;
    }

    public String getDataFromUrlByGET(String url) {

        InputStream is = null;
        String outPut = null;
        // Making HTTP request
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            outPut = sb.toString();
            return outPut;
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // return  String
        return null;

    }
}
