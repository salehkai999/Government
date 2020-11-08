package com.saleh.government;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class OfficialActivity extends AppCompatActivity {

    private TextView locationText;
    private TextView posText;
    private TextView nameText;
    private ImageView offImage;
    private ImageView partyImage;
    private Officals off;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);
        locationText = findViewById(R.id.locationTextOff);
        posText = findViewById(R.id.posText);
        nameText = findViewById(R.id.nameView);
        offImage = findViewById(R.id.offImage);
        partyImage = findViewById(R.id.partyImage);

        Intent intent = getIntent();
        if(intent.hasExtra("off")) {
            off = (Officals) intent.getSerializableExtra("off");
            if(off != null){
                posText.setText(off.getPosition());
                nameText.setText(off.getName());
            }
        }


    }
}