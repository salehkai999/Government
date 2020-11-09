package com.saleh.government;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
                        break;
                    case "Republican Party":
                        partyImage.setImageResource(R.drawable.rep_logo);
                        findViewById(R.id.offLayout).setBackgroundColor(getResources().getColor(R.color.red));
                        break;
                    default:
                        partyImage.setVisibility(View.INVISIBLE);
                        findViewById(R.id.offLayout).setBackgroundColor(getResources().getColor(R.color.black));
                }
                if(off.getAddress() != null)
                    addressData.setText(off.getAddress());
                else {
                    addressView.setVisibility(View.INVISIBLE);
                    addressData.setVisibility(View.INVISIBLE);
                }

                if(off.getPhones().get(0) != null){
                    String phone="";
                    for(int i=0;i<off.getPhones().size();i++)
                        phone+=off.getPhones().get(i).toString()+"\n";
                    phoneData.setText(phone);
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
                }
                else {
                    websiteView.setVisibility(View.INVISIBLE);
                    websiteData.setVisibility(View.INVISIBLE);
                }

                downloadImage();
            }
        }


    }


    private void downloadImage(){
        if(!off.getPhotoUrl().equals("none")) {
        Log.d(TAG, "downloadImage: "+off.getPhotoUrl());
        Picasso.get().load(off.getPhotoUrl()).error(R.drawable.brokenimage)
                .placeholder(R.drawable.placeholder).into(offImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                Log.d(TAG, "onError: "+e);
            }
        });
        }
        else {
            offImage.setImageResource(R.drawable.missing);
            Log.d(TAG, "downloadImage: "+"NO IMAGE");
        }
    }
}