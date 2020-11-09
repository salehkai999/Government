package com.saleh.government;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class OfficialActivity extends AppCompatActivity {

    private TextView locationText;
    private TextView posText;
    private TextView nameText;
    private TextView partyText;
    private ImageView offImage;
    private ImageView partyImage;
    private Officals off;
    private TextView addressView;
    private TextView addressData;
    private TextView phoneView;
    private TextView phoneData;
    private TextView websiteView;
    private TextView websiteData;
    private ImageView fbImg;
    private ImageView ytImg;
    private ImageView twtImg;
    private static final String TAG = "OfficialActivity";
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);
        locationText = findViewById(R.id.locationTextOff);
        posText = findViewById(R.id.posText);
        nameText = findViewById(R.id.nameView);
        partyText = findViewById(R.id.partyText);
        offImage = findViewById(R.id.offImage);
        partyImage = findViewById(R.id.partyImage);
        addressView = findViewById(R.id.addText);
        addressData = findViewById(R.id.addressData);
        phoneView = findViewById(R.id.phoneText);
        phoneData = findViewById(R.id.phoneData);
        websiteView = findViewById(R.id.websiteText);
        websiteData = findViewById(R.id.websiteData);
        fbImg = findViewById(R.id.fbImg);
        ytImg = findViewById(R.id.ytImg);
        twtImg = findViewById(R.id.twtImg);

        Intent intent = getIntent();
        if(intent.hasExtra("off")) {
            off = (Officals) intent.getSerializableExtra("off");
            if(off != null){
                posText.setText(off.getPosition());
                nameText.setText(off.getName());
                partyText.setText(off.getParty());
                switch (off.getParty()){
                    case "Democratic Party" :
                        partyImage.setImageResource(R.drawable.dem_logo);
                        findViewById(R.id.offLayout).setBackgroundColor(getResources().getColor(R.color.blue));
                        partyImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openDemWebSite();
                            }
                        });
                        break;
                    case "Republican Party":
                        partyImage.setImageResource(R.drawable.rep_logo);
                        findViewById(R.id.offLayout).setBackgroundColor(getResources().getColor(R.color.red));
                        partyImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openRepWebSite();
                            }
                        });
                        break;
                    default:
                        partyImage.setVisibility(View.INVISIBLE);
                        findViewById(R.id.offLayout).setBackgroundColor(getResources().getColor(R.color.black));
                }
                if(off.getAddress() != null) {
                    addressData.setText(off.getAddress());
                    Linkify.addLinks(addressData,Linkify.ALL);
                }

                else {
                    addressView.setVisibility(View.INVISIBLE);
                    addressData.setVisibility(View.INVISIBLE);
                }

                if(off.getPhones().get(0) != null){
                    String phone="";
                    for(int i=0;i<off.getPhones().size();i++)
                        phone+=off.getPhones().get(i).toString()+"\n";
                    phoneData.setText(phone);
                    Linkify.addLinks(phoneData,Linkify.ALL);
                }
                else{
                    phoneData.setVisibility(View.INVISIBLE);
                    phoneView.setVisibility(View.INVISIBLE);
                }

                if(off.getUrls().get(0) != null){
                    String websites="";
                    for(int i=0;i<off.getUrls().size();i++)
                        websites+=off.getUrls().get(i).toString()+"\n";
                    websiteData.setText(websites);
                    Linkify.addLinks(websiteData,Linkify.ALL);
                }
                else {
                    websiteView.setVisibility(View.INVISIBLE);
                    websiteData.setVisibility(View.INVISIBLE);
                }

                downloadImage();
                socialMedia();
            }
        }


    }

    private void openDemWebSite() {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.democrats)));
        startActivity(i);
    }

    private void openRepWebSite() {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.republican)));
        startActivity(i);
    }

    private void socialMedia(){
        Log.d(TAG, "socialMedia: "+off.getChannels().toString());
        if(off.getChannels().get("Facebook") != null){
            Log.d(TAG, "socialMedia: "+off.getChannels().toString());
            fbImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(OfficialActivity.this, "FACEBOOK "+off.getChannels().get("Facebook"), Toast.LENGTH_SHORT).show();
                    openFBPage(off.getChannels().get("Facebook"));
                }
            });
        }
        else {
            fbImg.setVisibility(View.INVISIBLE);
        }

        if(off.getChannels().get("Twitter") != null){
            Log.d(TAG, "socialMedia: "+off.getChannels().toString());
            twtImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(OfficialActivity.this, "TWT "+off.getChannels().get("Twitter"), Toast.LENGTH_SHORT).show();
                    openTwtPage(off.getChannels().get("Twitter"));
                }
            });
        }
        else {
            twtImg.setVisibility(View.INVISIBLE);
        }

        if(off.getChannels().get("YouTube") != null){
            Log.d(TAG, "socialMedia: "+off.getChannels().toString());
            ytImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(OfficialActivity.this, "YT "+off.getChannels().get("YouTube"), Toast.LENGTH_SHORT).show();
                    openYTPage(off.getChannels().get("YouTube"));
                }
            });
        }
        else {
            ytImg.setVisibility(View.INVISIBLE);
        }



    }

    private void openFBPage(String id){
        String FACEBOOK_URL = "https://www.facebook.com/" + id;

        Intent intent;
        String urlToUse;
        try {
            getPackageManager().getPackageInfo("com.facebook.katana", 0);

            int versionCode = getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                urlToUse = "fb://page/" + id;
            }
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlToUse));
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(FACEBOOK_URL));
        }

        startActivity(intent);
    }

    private void openTwtPage(String id){
        String twitterAppUrl = "twitter://user?screen_name=" + id;
        String twitterWebUrl = "https://twitter.com/" + id;

        Intent intent;
        try {
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterAppUrl));
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterWebUrl));
        }
        startActivity(intent);
    }

    private void openYTPage(String id){
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/"+id));
            startActivity(intent);
        }
        catch (Exception e){
            intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://wwww.youtube.com/"+id));
            startActivity(intent);
        }
    }

    private void downloadImage(){
        if(!off.getPhotoUrl().equals("none")) {
        Log.d(TAG, "downloadImage: "+off.getPhotoUrl());
        Picasso.get().load(off.getPhotoUrl().toString()).error(R.drawable.brokenimage)
                .placeholder(R.drawable.placeholder).into(offImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                Log.d(TAG, "onError: "+e);
            }
        });
        offImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPictureActivity();
            }
        });

        }
        else {
            offImage.setImageResource(R.drawable.missing);
            Log.d(TAG, "downloadImage: "+"NO IMAGE");
        }
    }

    private void openPictureActivity() {
        Intent intent = new Intent(this,PictureActivity.class);
        intent.putExtra("off",off);
        startActivity(intent);
    }
}