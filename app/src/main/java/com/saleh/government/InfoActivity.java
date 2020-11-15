package com.saleh.government;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.regex.Pattern;

public class InfoActivity extends AppCompatActivity {

    TextView linkText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        linkText = findViewById(R.id.linkText);
        String data ="Google Civic\nInformation API";
        String link = getString(R.string.google_civic_link);
        linkText.setText(data);
        linkText.setPaintFlags(linkText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        linkText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(i);
            }
        });
    }
}