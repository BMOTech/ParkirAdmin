package com.tomcat.parkiradmin.DB;
/*
	copyright Albert Alfrianta @TOMCAT INC
	June 2015
	Bogor, Indonesia
*/
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class DBServer{
    Context context;
    public DBServer(Context context){
        this.context = context;
    }

    static InputStream is = null;
    //static String url;
    //static List<NameValuePair> params;
    private String data;
    private JSONArray jA;

    public JSONArray get(){
        return jA;
    }

    public int signal(String url, List<NameValuePair> params){
        int signal=0;       //initialize i=0. 0=unknown error
        if(isConnected()){
            httpRequest(url,params);
            Log.d("DBServer data ", data);
            if(data==null){  //if server error
                signal=4;
                Log.d("DBServer signal ", "4");
            }
            else if(data.equals("0\n")){  //if no error (success)
                signal=0;
            }
            else if(data.equals("1\n")){  //if failed
                signal=1;
                Log.d("DBServer signal ", "1");
            }
            else if(data.equals("2\n")){  //if unknown error
                signal=2;
                Log.d("DBServer signal ", "2");
            }
            else if(data!=null){
                try {
                    jA = new JSONArray(data);
                    Log.d("DBServer jA", jA.toString());
                    signal=0;
                } catch (JSONException e) {
                    signal=1;
                    Log.e("DBServer Parser jA", "Error parsing data " + e.toString());
                }
            }
        }
        else{
            signal=3;
        }
        return signal;
    }

    public void httpRequest(String url, List<NameValuePair> params){
            // Making HTTP request
            try {
                // request method is POST
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));

                HttpResponse httpResponse = httpClient.execute(httpPost);
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
                data = sb.toString();
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}