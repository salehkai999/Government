package com.saleh.government;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.saleh.government.downloader.OfficialDownloader;

import java.util.ArrayList;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int LOC_REQ_CODE = 222;
    private List<Officals> officalsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private OfficialsAdapter officialsAdapter;
    private static final String TAG = "MainActivity";
    private LocationManager locationManager;
    private Criteria criteria;
    private TextView locText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locText = findViewById(R.id.locationText);
        recyclerView = findViewById(R.id.recyclerView);
        officialsAdapter = new OfficialsAdapter(officalsList,this);
        recyclerView.setAdapter(officialsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);

        if(!isConnected())
            noNetworkDialog();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    LOC_REQ_CODE);

        }
        else locattionData();

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOC_REQ_CODE) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PERMISSION_GRANTED) {
                locattionData();
                return;
            }
            else
                noLocationDialog();
        }
    }

    @SuppressLint("MissingPermission")
    private void locattionData() {
        String provider = locationManager.getBestProvider(criteria,true);
        Location currentLocation = null;
        if (provider != null) {
            currentLocation = locationManager.getLastKnownLocation(provider);
        }

        if(currentLocation != null){
            Geocoder geocoder = new Geocoder(this);
            try {
                List<Address> addressList = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 2);
                locText.setText(addressList.get(0).getAddressLine(0));
                if(isConnected())
                    new Thread(new OfficialDownloader(this,addressList.get(0).getAddressLine(0))).start();
                else
                    noNetworkDialog();
            }
            catch (Exception e){
                noLocationDialog();
            }
        }
        else
            noLocationDialog();
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
                if(isConnected()) {
                    new Thread(new OfficialDownloader(MainActivity.this, text)).start();
                    Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
                }
                else
                    noNetworkDialog();
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
        intent.putExtra("loc",locText.getText().toString().trim());
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
    public void setLoc(String location)
    {
        locText.setText(location);
    }

    private void noNetworkDialog() {
        locText.setText("No Data for Location");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Network");
        builder.setMessage("Not Connected to the Internet");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            Toast.makeText(this, "Cannot access ConnectivityManager", Toast.LENGTH_SHORT).show();
            return false;
        }

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    private void noLocationDialog() {

        locText.setText("No Data for Location");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Couldn't Determine Location");
        builder.setMessage("Couldn't Locate");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

}