package com.example.sha.security.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sha.security.R;

public class Help extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        LinearLayout btnHelp1 = findViewById(R.id.btn_help_1);
        LinearLayout btnHelp2 = findViewById(R.id.btn_help_2);
        LinearLayout btnHelp3 = findViewById(R.id.btn_help_3);

        final LinearLayout view1 = findViewById(R.id.help_1_view);
        final LinearLayout view2 = findViewById(R.id.help_2_view);
        final LinearLayout view3 = findViewById(R.id.help_3_view);

        final TextView tvMsg1 = findViewById(R.id.tv_help_1_msg);
        final TextView tvMsg2 = findViewById(R.id.tv_help_2_msg);
        final TextView tvMsg3 = findViewById(R.id.tv_help_3_msg);

        btnHelp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMsg1.setText(Html.fromHtml("Securitik work by picking your pin location and your emergency type that is fire or Ambulance and connecting you to a Security firm that offers those services.<br>"+
                        "You are also able to notify your friends and families in case of an emergency."));
                if (visCheck(view2)) visGo(view2);
                if (visCheck(view3)) visGo(view3);
                if (visCheck(view1)) visGo(view1); else view1.setVisibility(View.VISIBLE);
            }
        });
        btnHelp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMsg2.setText(Html.fromHtml("In today’s world, one of the biggest concerns for most of the people is personal safety!<br>" +
                        "Securitik is a Personal Safety App packed with various features  used to enhance personal safety and send single action alert to  security personnel with maximum possible information in case of fire or ambulance  emergency.<br>"+
                        "Securitik  also helps you alert your family & friends about a particular event happening around you and you are safe."));
                if (visCheck(view1)) visGo(view1);
                if (visCheck(view3)) visGo(view3);
                if (visCheck(view2)) visGo(view2); else view2.setVisibility(View.VISIBLE);
            }
        });
        btnHelp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMsg3.setText(Html.fromHtml("We require one to enter a password to ensure that the user is legit."));
                if (visCheck(view2)) visGo(view2);
                if (visCheck(view1)) visGo(view1);
                if (visCheck(view3)) visGo(view3); else view3.setVisibility(View.VISIBLE);
            }
        });
    }

    public void backPressed(View view) {
        onBackPressed();
    }

    //code shortening
    private boolean visCheck(LinearLayout view) { return view.getVisibility() == View.VISIBLE; }
    private void visGo(LinearLayout view) { view.setVisibility(View.GONE); }

}
