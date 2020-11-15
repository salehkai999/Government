package com.saleh.government;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class PictureActivity extends AppCompatActivity {

    private TextView currentLoc;
    private TextView posText;
    private TextView nameText;
    private ImageView offImg;
    private ImageView partyImg;
    private Officals off;
    private static final String TAG = "PictureActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        currentLoc = findViewById(R.id.picLoc);
        posText = findViewById(R.id.picPosText);
        nameText = findViewById(R.id.picNameText);
        offImg = findViewById(R.id.picOffImg);
        partyImg = findViewById(R.id.picParty);

        Intent intent = getIntent();
        if(intent.hasExtra("loc")){
            currentLoc.setText(intent.getStringExtra("loc"));
        }

        if(intent.hasExtra("off")){
            off = (Officals) intent.getSerializableExtra("off");
            posText.setText(off.getPosition());
            nameText.setText(off.getName());
            setPartyLogo();
            downloadImage();
        }
    }

    private void setPartyLogo() {
        switch (off.getParty()){
            case "Democratic Party" :
                partyImg.setImageResource(R.drawable.dem_logo);
                findViewById(R.id.picLayout).setBackgroundColor(getResources().getColor(R.color.blue));
                partyImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openDemWebSite();
                    }
                });
                break;
            case "Republican Party":
                partyImg.setImageResource(R.drawable.rep_logo);
                findViewById(R.id.picLayout).setBackgroundColor(getResources().getColor(R.color.red));
                partyImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openRepWebSite();
                    }
                });
                break;
            default:
                partyImg.setVisibility(View.INVISIBLE);
                findViewById(R.id.picLayout).setBackgroundColor(getResources().getColor(R.color.black));
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

    private void downloadImage(){
        if(!off.getPhotoUrl().equals("none")) {
            Log.d(TAG, "downloadImage: "+off.getPhotoUrl());
            Picasso.get().load(off.getPhotoUrl().toString()).error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder).into(offImg, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Log.d(TAG, "onError: "+e);
                }
            });

        }
    }
}