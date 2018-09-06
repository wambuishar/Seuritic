package com.example.sha.security.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sha.security.R;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends BaseAdapter {

    // private ArrayList&lt;HashMap&lt;String, String&gt;&gt; data;
    private static List name_arr, phone_arr;
    private static LayoutInflater inflater = null;

    public ContactsAdapter(Activity a, List b, List c) {
        name_arr = b;
        phone_arr = c;

        inflater = (LayoutInflater) a
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return name_arr.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_designer, null);

        final TextView tvName = vi.findViewById(R.id.tv_list_name);
        final String strName = name_arr.get(position).toString();
        tvName.setText(strName);

        final TextView tvPhone = vi.findViewById(R.id.tv_list_phone);
        final String strPhone = "+254 "+phone_arr.get(position).toString();
        tvPhone.setText(strPhone);

        return vi;
    }
}