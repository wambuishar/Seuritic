package com.example.sha.security.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sha.security.R;
import com.example.sha.security.db.DatabaseHandler;
import com.example.sha.security.db.Misc;

public class Account extends AppCompatActivity {

    private TextView name, email, phone, editProfile;
    private DatabaseHandler db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc);

        name = findViewById(R.id.tv_us_name);
        phone = findViewById(R.id.tv_us_phone);
        email = findViewById(R.id.tv_us_email);
        editProfile = findViewById(R.id.btn_edit_profile);

        db = new DatabaseHandler(this);

        setFields();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("InflateParams")
                final View customView = getLayoutInflater().inflate(R.layout.dialog_add, null);

                final AlertDialog alertEditProfile = new AlertDialog.Builder(Account.this).create();
                alertEditProfile.setView(customView);

                alertEditProfile.setCanceledOnTouchOutside(true);
                alertEditProfile.setCancelable(true);

                final TextView tvTitle = customView.findViewById(R.id.tv_add_title);

                final TextView btn_done = customView.findViewById(R.id.btn_done);
                final TextView btn_dismiss = customView.findViewById(R.id.btn_dismiss);

                final EditText name = customView.findViewById(R.id.et_name);
                final EditText email = customView.findViewById(R.id.et_email);
                final EditText phone = customView.findViewById(R.id.et_ph);

                name.setText(db.getMisc("name"));
                phone.setText(db.getMisc("phone"));
                email.setText(db.getMisc("email"));

                tvTitle.setText(getString(R.string.edit));

                btn_done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strName = name.getText().toString();
                        String strPhone = phone.getText().toString();
                        String strEmail = email.getText().toString();

                        if (strName.equals("")) {
                            phone.setError("required");
                            phone.requestFocus();
                        } else if (strPhone.equals("")) {
                            phone.setError("required");
                            phone.requestFocus();
                        } else if (strPhone.length() < 9) {
                            phone.setError("invalid");
                            phone.requestFocus();
                        } else if (strEmail.equals("")) {
                            email.setError("required");
                            email.requestFocus();
                        } else if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
                            email.setError("invalid");
                            email.requestFocus();
                        } else {
                            db.updateMisc(new Misc("name",strName));
                            db.updateMisc(new Misc("phone",strPhone));
                            db.updateMisc(new Misc("email",strEmail));
                            alertEditProfile.dismiss();
                            setFields();
                            Toast.makeText(Account.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                btn_dismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertEditProfile.dismiss();
                    }
                });

                alertEditProfile.show();
            }
        });
    }

    private void setFields() {
        name.setText(db.getMisc("name"));
        email.setText(db.getMisc("email"));

        String strPhone = "+254 "+db.getMisc("phone");
        phone.setText(strPhone);
    }

    public void backPressed(View view) {
        onBackPressed();
    }
}
