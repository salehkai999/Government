package com.saleh.government.downloader;

import android.net.Uri;
import android.util.Log;

import com.saleh.government.MainActivity;
import com.saleh.government.Officals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class OfficialDownloader implements Runnable{
    private static final String URL = "https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyDzRJDU9-2SEAZkigsOO9j2kv8ZuPGxCk8&address=";
    private MainActivity mainActivity;
    private String address;
    private static final String TAG = "OfficialDownloader";
    private static  ArrayList<String> officialsList = new ArrayList<>();
    private static ArrayList<Officals> officals_list;
    private static  HashMap<String,String> officalsData;

    public OfficialDownloader(MainActivity mainActivity, String address) {
        this.mainActivity = mainActivity;
        this.address = address;
        officialsList = new ArrayList<>();
        officalsData = new HashMap<>();
        officals_list = new ArrayList<>();
    }

    @Override
    public void run() {
        Uri.Builder builder = Uri.parse(URL+address).buildUpon();
        String offUrl = builder.toString();
        Log.d(TAG, "run: "+offUrl);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(offUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if(urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK)
            {
                Log.d(TAG, "run: "+urlConnection.getResponseCode());
                return;
            }
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String data;
            while ((data = bufferedReader.readLine()) != null) {
                stringBuilder.append(data).append("\n");
            }
            //Log.d(TAG, "run: "+stringBuilder.toString());
            //Log.d(TAG, "run: "+stringBuilder.toString());
        }
        catch(Exception e){
            e.printStackTrace();
        }
        processData(stringBuilder.toString());
    }

    private void processData(String data)  {

        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject jsonObject1 = jsonObject.getJSONObject("normalizedInput");
            JSONArray jsonArray = jsonObject.getJSONArray("offices");
            JSONArray jsonArray1 = jsonObject.getJSONArray("officials");
            Log.d(TAG, "processData: ADDRESS"+jsonObject1.toString());
            Log.d(TAG, "processData: OFFICES"+jsonArray.toString());
            Log.d(TAG, "processData: OFFICALS"+jsonArray1.toString());
            for(int i=0;i<jsonArray.length();i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                JSONArray jsonArray2 = obj.getJSONArray("officialIndices");
                for(int j=0;j<jsonArray2.length();j++)
                    officialsList.add(obj.getString("name"));

                Log.d(TAG, "processData: "+obj.getString("name"));
            }
            Log.d(TAG, "processData: "+officialsList.size());
            for(int i=0;i<jsonArray1.length();i++) {
                Officals official = new Officals();
                JSONObject obj = jsonArray1.getJSONObject(i);
                official.setName(obj.getString("name"));
                official.setParty(obj.getString("party"));
                official.setPosition(officialsList.get(i));
                try{
                    official.setPhotoUrl(obj.getString("photoUrl"));
                }
                catch (Exception e)
                {
                    official.setPhotoUrl("none");
                }
                officals_list.add(official);
                Log.d(TAG, "processData: "+official.getName()+" "+official.getParty()+" "+official.getPosition()+" "+official.getPhotoUrl());//+" "+obj.getJSONArray("officialIndices").toString());
            }
        }
        catch(Exception e){
            e.printStackTrace();
            Log.d(TAG, "processData: "+e.toString());
        }

        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.addList(officals_list);
            }
        });

    }
}
