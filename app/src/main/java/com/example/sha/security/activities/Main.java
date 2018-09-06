package com.example.sha.security.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sha.security.R;
import com.example.sha.security.db.DatabaseHandler;
import com.example.sha.security.db.Misc;
import com.example.sha.security.tools.MyReceiver;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Main extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double latitude, longitude;
    private LinearLayout left_drawer;
    private DrawerLayout dl;
    private DatabaseHandler db;
    private TextView usName;
    private MarkerOptions marker = new MarkerOptions();
    private Marker m;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(this);

        usName = findViewById(R.id.userName);

        usName.setText(db.getMisc("name"));

        left_drawer = findViewById(R.id.left_drawer);
        dl = findViewById(R.id.dl);
        final LinearLayout mainView = findViewById(R.id.mainView);

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        MyReceiver mReceiver = new MyReceiver ();
        registerReceiver(mReceiver, filter);

        // todo create help page
        // todo maps

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, dl, null, R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                Main.this.invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                Main.this.invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                mainView.setTranslationX(slideOffset * drawerView.getWidth());
                dl.bringChildToFront(drawerView);
                dl.requestLayout();
            }
        };
        dl.addDrawerListener(mDrawerToggle);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "happened 1", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 642);
        } else {

            final LocationListener ml = new LocationListener( ) {
                @Override
                public void onLocationChanged(Location location) {
                    latitude = location.getLatitude( );
                    longitude = location.getLongitude( );

                    updateMarker();
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };

            mMap.setMyLocationEnabled(true);

            LocationManager locMan = (LocationManager) getSystemService(LOCATION_SERVICE);

            assert locMan != null;
            locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, ml);

            boolean gps_enabled = false,network_enabled = false;

            try {
                gps_enabled = locMan.isProviderEnabled(LocationManager.GPS_PROVIDER);
            }catch (Exception ex){
                ex.printStackTrace();
            }
            try{
                network_enabled = locMan.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            }catch (Exception ex){
                ex.printStackTrace();
            }
            if(!gps_enabled && !network_enabled){
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage(getResources().getString(R.string.gps_network_not_enabled));
                dialog.setPositiveButton(getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        Main.this.startActivity(myIntent);
                    }
                });
                dialog.setNegativeButton(getString(R.string.dismiss), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt){
                        Main.this.finish();
                    }
                });
                dialog.show();
            }

            mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener( ) {
                @Override
                public boolean onMyLocationButtonClick() {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(latitude, longitude), 12));
                    return true;
                }
            });

            marker.position(new LatLng(latitude, longitude));
            m = mMap.addMarker(marker);

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 12));
        }
    }

    private void updateMarker() {
        m.setPosition(new LatLng(latitude, longitude));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 12));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        onMapReady(mMap);
    }

    @Override
    public void onBackPressed() {
        if (dl.isDrawerOpen(left_drawer)) dl.closeDrawer(left_drawer);
        else super.onBackPressed();
    }

    public void openDrawer(View view) {
        if (!dl.isDrawerOpen(left_drawer)) dl.openDrawer(left_drawer);
    }

    public void ambulance(View view) {
        confirmPassword("ambulance");
    }

    public void fire(View view) {
        confirmPassword("fire");
    }

    public void emContacts(View view) {
        dl.closeDrawer(left_drawer);
        Intent myIntent = new Intent(this, Contacts.class);
        startActivity(myIntent);
    }

    public void help(View view) {
        dl.closeDrawer(left_drawer);
        Intent myIntent = new Intent(this, Help.class);
        startActivity(myIntent);
    }

    public void account(View view) {
        dl.closeDrawer(left_drawer);
        Intent myIntent = new Intent(this, Account.class);
        Main.this.startActivity(myIntent);
    }

    public void logOut(View view) {
        dl.closeDrawer(left_drawer);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(getResources().getString(R.string.logout_msg));
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                db.updateMisc(new Misc("is_logged_in", "false"));
                startActivity(new Intent(Main.this, Auth.class));
                finish();
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt){}
        });
        dialog.show();

    }

    @SuppressLint("InflateParams")
    public void confirmPassword (final String emergency) {
        final View customView = getLayoutInflater().inflate(R.layout.dialog_pass, null);

        final AlertDialog alertUpdate = new AlertDialog.Builder(this).create();
        alertUpdate.setView(customView);

        alertUpdate.setCanceledOnTouchOutside(true);
        alertUpdate.setCancelable(true);

        final TextView btn_done = customView.findViewById(R.id.btn_done);
        final TextView btn_dismiss = customView.findViewById(R.id.btn_dismiss);
        final EditText et_pass = customView.findViewById(R.id.et_confirm_pass);
        final CheckBox cb_em = customView.findViewById(R.id.cb_alert);

        cb_em.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (db.getContactsCount() == 0) {
                        cb_em.setChecked(false);
                        Toast.makeText(Main.this, "No emergency contacts added", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strPass = et_pass.getText().toString();

                if (strPass.equals("")) {
                    et_pass.setError("required");
                    et_pass.requestFocus();
                } else if (strPass.length() < 6) {
                    et_pass.setError("too short");
                    et_pass.requestFocus();
                } else {
                    String strEmail = db.getMisc("email");

                    if (db.confirmLogin(strEmail, strPass)) {
                        sendMessage(cb_em.isChecked(), emergency);
                    } else {
                        Toast.makeText(Main.this, "Wrong password", Toast.LENGTH_SHORT).show();
                    }
                    alertUpdate.dismiss();
                }
            }
        });

        btn_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertUpdate.dismiss();
            }
        });

        alertUpdate.show();
    }

    private void sendMessage(boolean checked, String emergency) {

        String message = "Location link is: => http://maps.google.com/maps?saddr=" + latitude + "," + longitude +
                "\n\n Emergency: "+emergency.toUpperCase();

        String strNum;

        int count = db.getContactsCount();

        StringBuilder sb = new StringBuilder();

        if (checked) {
            if (count > 0)
            strNum = sb.append("+254711582200;").toString();
            else
                strNum = sb.append("+254711582200").toString();
            for (int i = 0; i < count; i++) {
                if (count - i == 1)
                    strNum = sb.append("+254").append(db.getAllContactsPhone().get(i)).toString();
                else
                    strNum = sb.append("+254").append(db.getAllContactsPhone().get(i)).append(";").toString();

            }
        } else {
            strNum = sb.append("+254711582200").toString();
        }


        Uri smsToUri = Uri.parse("smsto:" + strNum);

        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", message);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (db==null) db = new DatabaseHandler(this);

        usName.setText(db.getMisc("name"));
    }
}
