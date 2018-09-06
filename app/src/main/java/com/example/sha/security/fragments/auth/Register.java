package com.example.sha.security.fragments.auth;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sha.security.R;
import com.example.sha.security.db.DatabaseHandler;
import com.example.sha.security.db.User;

public class Register extends Fragment {

    private FragmentActivity activity;
    private DatabaseHandler db;

    public Register() {}

    public static Register newInstance() { return  new Register(); }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_register, container, false);

        activity = getActivity();
        Context context = getContext();

        db = new DatabaseHandler(context);

        final TextView btnReg = activity.findViewById(R.id.btn_login);

        final EditText userName = rootView.findViewById(R.id.et_reg_name);
        final EditText userPhone = rootView.findViewById(R.id.et_reg_ph);
        final EditText userEmail = rootView.findViewById(R.id.et_reg_email);

        final EditText pass1 = rootView.findViewById(R.id.et_reg_pass_1);
        final EditText pass2 = rootView.findViewById(R.id.et_reg_pass_2);

        btnReg.setText(getString(R.string.title_register));
        userPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 &&
                        ((String.valueOf(s.charAt(0)).equals("0")) || (!String.valueOf(s.charAt(0)).equals("7")))) {
                    userPhone.setText("7");
                    userPhone.setSelection(1);
                }
            }
        });
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strName = userName.getText().toString();
                String strPhone = userPhone.getText().toString();
                String strEmail = userEmail.getText().toString();
                String strPass1 = pass1.getText().toString();
                String strPass2 = pass2.getText().toString();

                if (strName.equals("")) {
                    userName.setError("required");
                    userName.requestFocus();
                } else if (strPhone.equals("")) {
                    userPhone.setError("required");
                    userPhone.requestFocus();
                } else if (strPhone.length() < 9) {
                    userPhone.setError("invalid");
                    userPhone.requestFocus();
                } else if (contactExists(strPhone)) {
                    userPhone.setError("this number exists");
                    userPhone.requestFocus();
                } else if (strEmail.equals("")) {
                    userEmail.setError("required");
                    userEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
                    userEmail.setError("invalid");
                    userEmail.requestFocus();
                } else if (emailExists(strEmail)) {
                    userEmail.setError("this email already exists");
                    userEmail.requestFocus();
                } else if (strPass1.equals("")) {
                    pass1.setError("required");
                    pass1.requestFocus();
                } else if (strPass1.length() < 6) {
                    pass1.setError("too short");
                    pass1.requestFocus();
                } else if (!strPass2.equals(strPass1)) {
                    pass2.setError("passwords do not much");
                    pass2.requestFocus();
                } else {
                    db.addUser(new User(strName,strPhone,strEmail,strPass1));
                    if (db.getUsersCount() == 1) { // nothing behind this fragment.. open login
                        activity.getSupportFragmentManager().popBackStack(); // pop this, open login

                        activity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.login_container, Login.newInstance(), "fragment_login")
                                .addToBackStack("fragment_reg")
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .commit();

                        Toast.makeText(activity, "Registered successfully\n" +
                                "You can now login with your details", Toast.LENGTH_LONG).show();

                    } else
                        activity.getSupportFragmentManager().popBackStack();

                }
            }
        });

        return rootView;
    }

    private boolean contactExists(String strPhone) {
        for (int i = 0; i < db.getUsersCount(); i++){
            if (db.getAllUsers().get(i).getPhone().equals(strPhone))
                return true;
        }
        return false;
    }

    private boolean emailExists(String strEmail) {
        for (int i = 0; i < db.getUsersCount(); i++){
            if (db.getAllUsers().get(i).getEmail().equals(strEmail))
                return true;
        }
        return false;
    }

}
