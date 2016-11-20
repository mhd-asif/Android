package com.bscheme.linkkin.helper;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonParser {

    private String delimiter = "--";
    private String boundary =  "SwA"+Long.toString(System.currentTimeMillis())+"SwA";
    HttpURLConnection httpURLConnection;
    DataOutputStream dos;

    public void establishConnection(String mUrl,String mRequestType){

        try {
            httpURLConnection = (HttpURLConnection) (new URL(mUrl)).openConnection();

            httpURLConnection.setDoInput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod(mRequestType);
            if (mRequestType.contentEquals("POST")) {
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
                httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            }
            httpURLConnection.connect();

            if (mRequestType.contentEquals("POST")) {
                dos = new DataOutputStream(httpURLConnection.getOutputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long getContentLength(){
        return httpURLConnection.getContentLength();
    }

    /// get respoonse from api for a single string of data
    public void connectionJsonPOST(String mUrl,JSONObject data) {

        try {
            httpURLConnection = (HttpURLConnection) (new URL(mUrl)).openConnection();

            httpURLConnection.setDoInput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setChunkedStreamingMode(1024);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
//            httpURLConnection.connect();

            dos = new DataOutputStream(httpURLConnection.getOutputStream());

            byte[] dataJson = data.toString().getBytes();
            dos.write(dataJson );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeParamData(String paramName, String value) throws Exception {
        dos.write((delimiter + boundary + "\r\n").getBytes());
        dos.write("Content-Type: text/plain\r\n".getBytes());
        dos.write(("Content-Disposition: form-data; name=\"" + paramName + "\"\r\n").getBytes());;
        dos.write(("\r\n" + value + "\r\n").getBytes());
    }

    public void finishConnection() throws Exception {


//        httpURLConnection.disconnect();
    }
    public String getResponse() throws Exception {
        dos.write((delimiter + boundary + delimiter + "\r\n").getBytes());

        dos.flush();
        InputStream is = httpURLConnection.getInputStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder buffer = new StringBuilder();

        String line = null;
        while ((line = br.readLine()) != null){

            buffer.append(line+"\n");
        }

        String response = buffer.toString();
        br.close();
        dos.close();

        return response;
    }
    public void addImageFilePart(String paramName, String fileName, byte[] data) throws Exception {
        dos.write((delimiter + boundary + "\r\n").getBytes());
        dos.write(("Content-Disposition: form-data; name=\"" + paramName + "\"; filename=\"" + fileName +".jpg"+ "\"\r\n").getBytes());
        dos.write(("Content-Type: application/octet-stream\r\n").getBytes());
        dos.write(("Content-Transfer-Encoding: binary\r\n").getBytes());
        dos.write("\r\n".getBytes());

        dos.write(data);

        dos.write("\r\n".getBytes());
    }
    public void addFilePart(String paramName, String fileName, String filePath,String fileExtention) throws IOException {

        dos.write((delimiter + boundary + "\r\n").getBytes());
        dos.write(("Content-Disposition: form-data; name=\"" + paramName + "\"; filename=\"" + paramName + ""+fileExtention + "\"\r\n").getBytes());
        dos.write(("Content-Type: application/octet-stream\r\n").getBytes());
        dos.write(("Content-Transfer-Encoding: binary\r\n").getBytes());
        dos.write("\r\n".getBytes());

        FileInputStream fileInputStream = new FileInputStream(filePath);
        // create a buffer of maximum size
        int bytesAvailable = fileInputStream.available();

        int maxBufferSize = 2*1024;
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[ ] buffer = new byte[bufferSize];

        // read file and write it into form...
        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0)
        {
            dos.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable,maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0,bufferSize);
        }
        dos.write("\r\n".getBytes());

        fileInputStream.close();
    }
}
