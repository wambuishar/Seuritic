package com.example.sha.security.fragments.auth;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sha.security.R;
import com.example.sha.security.activities.Main;
import com.example.sha.security.db.DatabaseHandler;
import com.example.sha.security.db.Misc;

public class Login extends Fragment {

    private Context context;
    private DatabaseHandler db;
    private FragmentActivity activity;
    private FragmentManager fr;

    public Login() {}

    public static Login newInstance() { return  new Login(); }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        activity = getActivity();
        context = getContext();

        fr = activity.getSupportFragmentManager();

        db = new DatabaseHandler(context);

        final TextView btnLogin = activity.findViewById(R.id.btn_login);

        final TextView register = rootView.findViewById(R.id.tv_register);
        final EditText email = rootView.findViewById(R.id.et_login_email);
        final EditText pass = rootView.findViewById(R.id.et_login_pass);

        btnLogin.setText(getString(R.string.title_login));
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail = email.getText().toString();
                String strPass = pass.getText().toString();

                if (strEmail.equals("")) {
                    email.setError("required");
                    email.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
                    email.setError("invalid");
                    email.requestFocus();
                } else if (strPass.equals("")) {
                    pass.setError("required");
                    pass.requestFocus();
                } else if (strPass.length() < 6) {
                    pass.setError("too short");
                    pass.requestFocus();
                } else {
                    if (emailExists(strEmail)) {
                        if (db.confirmLogin(strEmail, strPass)) {

                            db.updateMisc(new Misc("name", db.getUserName(strEmail)));
                            db.updateMisc(new Misc("phone", db.getUserPhone(strEmail)));
                            db.updateMisc(new Misc("email", strEmail));
                            db.updateMisc(new Misc("is_logged_in", "true"));

                            startActivity(new Intent(context, Main.class));
                            activity.finish();
                        } else {
                            Toast.makeText(activity, "Wrong password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(activity, "No user exists with that email", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr.beginTransaction()
                        .replace(R.id.login_container, Register.newInstance(), "fragment_reg")
                        .addToBackStack("fragment_reg")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });

        return rootView;
    }

    private boolean emailExists(String strEmail) {
        for (int i = 0; i < db.getUsersCount(); i++){
            if (db.getAllUsers().get(i).getEmail().equals(strEmail))
                return true;
        }
        return false;
    }

}
