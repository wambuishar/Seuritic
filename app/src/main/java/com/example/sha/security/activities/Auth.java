package com.example.sha.security.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;

import com.example.sha.security.R;
import com.example.sha.security.db.DatabaseHandler;
import com.example.sha.security.db.Misc;
import com.example.sha.security.fragments.auth.Login;
import com.example.sha.security.fragments.auth.Register;

public class Auth extends FragmentActivity {

    private FragmentManager fr;
    private DatabaseHandler db;
    private boolean exitApp = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        fr = getSupportFragmentManager();
        db = new DatabaseHandler(this);

        if (savedInstanceState == null) init();
    }

    public void init() {
        int count = db.getMiscCount();
        if (count != 5) {
            if (count > 0) db.deleteAllMisc(); // remove and input data a fresh

            db.addMisc(new Misc("name", ""));
            db.addMisc(new Misc("phone", ""));
            db.addMisc(new Misc("email", ""));
            db.addMisc(new Misc("is_logged_in", "false"));
            db.addMisc(new Misc("is_welcomed", "false"));

            init(); // recall this method to proceed to login

        } else {
            if (isDataDamaged()) {
                db.deleteAllMisc();
                init();
            } else {
                if (db.getMisc("is_logged_in").equals("false")) {
                    db.updateMisc(new Misc("name", ""));
                    db.updateMisc(new Misc("phone", ""));
                    db.updateMisc(new Misc("email", ""));

                    while (fr.getBackStackEntryCount() != 0)
                        fr.popBackStack(); // do some cleaning

                    if (db.getUsersCount() > 0) {
                        fr.beginTransaction()
                                .replace(R.id.login_container, Login.newInstance(), "frag_login")
                                .addToBackStack("frag_login")
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .commit();
                    } else
                        fr.beginTransaction()
                                .replace(R.id.login_container, Register.newInstance(), "frag_reg")
                                .addToBackStack("frag_reg")
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .commit();
                } else {
                    if (isDataMissing()) {
                        db.updateMisc(new Misc("is_logged_in", "false")); // logout
                        init(); // recall this method
                    } else {
                        startActivity(new Intent(this, Main.class));
                    }
                }
            }
        }
    }

    private boolean isDataMissing() {
        return db.getMisc("name").equals("") || db.getMisc("phone").equals("") ||
                db.getMisc("email").equals("");
    } // if true, logout this guy

    private boolean isDataDamaged() {
        return db.getMisc("is_logged_in").equals("") || db.getMisc("is_welcomed").equals("");
    } // these are data unexpected to be empty

    @Override
    public void onBackPressed() {
        int count = fr.getBackStackEntryCount();
        if (count > 1)
            super.onBackPressed();
        else {
            if (exitApp) finish();
            else {
                exitApp = true;
                Toast.makeText(this, "Touch again to exit", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exitApp = false;
                    }
                }, 2000);
            }
        }
    }
}
