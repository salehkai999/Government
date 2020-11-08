package com.saleh.government;

import androidx.appcompat.app.AppCompatActivity;
import android.text.util.Linkify;
import android.os.Bundle;
import android.widget.TextView;

import java.util.regex.Pattern;

public class InfoActivity extends AppCompatActivity {

    TextView linkText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        linkText = findViewById(R.id.linkText);
        String data ="Google Civic Information API";
        String link = getString(R.string.google_civic_link);
        linkText.setText(data);
        Pattern pattern = Pattern.compile(data);
        Linkify.addLinks(linkText,pattern,link);
    }
}