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
    private static String location = "";

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
            JSONObject jsonObjectAdd = jsonObject.getJSONObject("normalizedInput");
            location = jsonObjectAdd.getString("city")+", "+jsonObjectAdd.getString("state")+" "+jsonObjectAdd.getString("zip");
            JSONArray jsonArrayOffices = jsonObject.getJSONArray("offices");
            JSONArray jsonArrayOfficials = jsonObject.getJSONArray("officials");
            Log.d(TAG, "processData: ADDRESS"+jsonObjectAdd.toString());
            Log.d(TAG, "processData: OFFICES"+jsonArrayOffices.toString());
            Log.d(TAG, "processData: OFFICALS"+jsonArrayOfficials.toString());
            for(int i=0;i<jsonArrayOffices.length();i++) {
                JSONObject obj = jsonArrayOffices.getJSONObject(i);
                JSONArray jsonArray2 = obj.getJSONArray("officialIndices");
                for(int j=0;j<jsonArray2.length();j++)
                    officialsList.add(obj.getString("name"));

                Log.d(TAG, "processData: "+obj.getString("name"));
            }
            Log.d(TAG, "processData: "+officialsList.size());
            for(int i=0;i<jsonArrayOfficials.length();i++) {
                Officals official = new Officals();
                JSONObject obj = jsonArrayOfficials.getJSONObject(i);
                official.setName(obj.getString("name"));
                official.setParty(obj.getString("party"));
                official.setPosition(officialsList.get(i));
                try{
                    official.setPhotoUrl(obj.getString("photoUrl"));
                }
                catch (Exception e)
                {
                    Log.d(TAG, "processData: "+e);
                    official.setPhotoUrl("none");
                }

                try {
                    JSONArray officialAdd = obj.getJSONArray("address");
                        official.setAddress(officialAdd.getJSONObject(0).getString("line1") + "\n"
                                + officialAdd.getJSONObject(0).getString("city") + "\n"
                                + officialAdd.getJSONObject(0).getString("state") + "\n" + officialAdd.getJSONObject(0).getString("zip"));

                }
                catch (Exception e){
                  // e.printStackTrace();
                    Log.d(TAG, "processData: "+e);
                    official.setAddress(null);
                }

                try{
                    //obj.getString("urls")
                    JSONArray urlsData = obj.getJSONArray("urls");
                    for(int j=0;j<1;j++)
                        official.getUrls().add(urlsData.get(j).toString());

                    Log.d(TAG, "processData: "+official.getUrls().toString());
                }
                catch (Exception e){
                    Log.d(TAG, "processData: "+e);
                }

                try{
                    JSONArray channelsData = obj.getJSONArray("channels");
                    Log.d(TAG, "processData: "+channelsData.get(0).toString());
                    for(int x=0;x<channelsData.length();x++)
                    {
                        JSONObject channelObj = channelsData.getJSONObject(x);
                        official.getChannels().put(channelObj.getString("type"),channelObj.getString("id"));
                        //Log.d(TAG, "processData: "+channelObj.toString());
                    }
                }
                catch (Exception e) {
                    Log.d(TAG, "processData: " + e);
                }

                try{
                    JSONArray emailsData = obj.getJSONArray("emails");
                    Log.d(TAG, "processData: "+emailsData.get(0));
                    official.setEmail(emailsData.get(0).toString());
                    Log.d(TAG, "processData: email: "+official.getEmail());
                }
                catch(Exception e){
                    Log.d(TAG, "processData: "+e);
                    official.setEmail(null);
                }

                try{
                    JSONArray phones = obj.getJSONArray("phones");
                    for(int z=0;z<1;z++)
                        official.getPhones().add(phones.get(z).toString());
                   // Log.d(TAG, "processData: "+official.getPhones().toString());
                }
                catch (Exception e){
                    Log.d(TAG, "processData: " + e);
                }

                officals_list.add(official);
                Log.d(TAG, "processData: "+official.getName()+" "+official.getParty()+" "+official.getPosition()+" "+official.getPhotoUrl()+", ADD "+official.getAddress()
                        +" URL: "+official.getUrls()+" \nSocial:"+official.getChannels().toString()+" Phone: "+official.getPhones().toString());//+" "+obj.getJSONArray("officialIndices").toString());
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
                mainActivity.setLoc(location);
            }
        });

    }
}
