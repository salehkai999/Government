package com.saleh.government;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.saleh.government.downloader.OfficialDownloader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private List<Officals> officalsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private OfficialsAdapter officialsAdapter;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        officialsAdapter = new OfficialsAdapter(officalsList,this);
        recyclerView.setAdapter(officialsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*for(int i=0;i<20;i++)
        {
            officalsList.add(new Officals(i+"","LOL"+i));
        }

        officialsAdapter.notifyDataSetChanged();*/


    }

    @Override
    public void onClick(View v) {
        int index = recyclerView.getChildLayoutPosition(v);
        Toast.makeText(this, "Clicked on "+officalsList.get(index).getName(), Toast.LENGTH_SHORT).show();
        openOfficialView(officalsList.get(index));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menus,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.search :
                //Toast.makeText(this, "SEARCH!!!", Toast.LENGTH_SHORT).show();
                openSearchDialog();
                return true;
            case R.id.info :
                Toast.makeText(this, "INFO!!!", Toast.LENGTH_SHORT).show();
                openInfo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openSearchDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText addressText = new EditText(this);
        addressText.setInputType(InputType.TYPE_CLASS_TEXT);
        addressText.setGravity(Gravity.CENTER_HORIZONTAL);
        builder.setView(addressText);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = addressText.getText().toString();
                new Thread(new OfficialDownloader(MainActivity.this,text)).start();
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setMessage("Enter Address Zip Code or Name");
        builder.setTitle("Select Address");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void openOfficialView(Officals off){
        Intent intent = new Intent(this,OfficialActivity.class);
        intent.putExtra("off",off);
        startActivity(intent);
    }

    private void openInfo() {
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }

    public void addList(ArrayList<Officals> officals_list) {
        officalsList = new ArrayList<>();
        officialsAdapter = new OfficialsAdapter(officalsList,this);
        recyclerView.setAdapter(officialsAdapter);
        for(Officals off : officals_list) {

            officalsList.add(off);

        }
        officialsAdapter.notifyDataSetChanged();
    }
}