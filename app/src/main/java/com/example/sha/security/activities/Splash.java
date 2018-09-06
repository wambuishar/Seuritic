package com.example.sha.security.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.example.sha.security.db.DatabaseHandler;
import com.example.sha.security.db.Misc;

public class Splash extends FragmentActivity {

    private DatabaseHandler db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check if welcomed
        // check if data is damaged
        // check if logged in
        // check if data is missing

        db = new DatabaseHandler(this);

        init();
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
                if (db.getMisc("is_welcomed").equals("false")) {
                    // welcome
                    startActivity(new Intent(this, Welcome.class));
                    finish();
                } else {
                    if (db.getMisc("is_logged_in").equals("false")) {
                        db.updateMisc(new Misc("name", ""));
                        db.updateMisc(new Misc("phone", ""));
                        db.updateMisc(new Misc("email", ""));

                        startActivity(new Intent(this, Auth.class));
                        finish();
                    } else {
                        if (isDataMissing()) {
                            db.updateMisc(new Misc("is_logged_in", "false")); // logout
                            init(); // recall this method
                        } else {
                            startActivity(new Intent(this, Main.class));
                            finish();
                        }
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
}
