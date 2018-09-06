package com.example.sha.security.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;

import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sha.security.R;
import com.example.sha.security.db.Contact;
import com.example.sha.security.db.DatabaseHandler;
import com.example.sha.security.tools.ContactsAdapter;

import java.util.List;

public class Contacts extends AppCompatActivity {

    private TextView empty;
    private ListView lvContacts;
    private DatabaseHandler db;
    private List<String> contactsName, contactsPhone;
    private ContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        empty = findViewById(R.id.tv_empty);
        lvContacts = findViewById(R.id.lv_contacts);

        db = new DatabaseHandler(this);

        if (db.getContactsCount()==0)
            empty.setVisibility(View.VISIBLE);
        else {
            empty.setVisibility(View.GONE);
            contactsName = db.getAllContactsName();
            contactsPhone = db.getAllContactsPhone();

            lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    final int pos = position;

                    final AlertDialog.Builder dialog = new AlertDialog.Builder(Contacts.this);
                    dialog.setMessage("Do you want to edit or delete this contact?");
                    dialog.setPositiveButton("EDIT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            Contact contact = db.getAllContacts().get(pos);
                            editContact(contact);
                        }
                    });
                    dialog.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt){
                            db.deleteContact(db.getAllContacts().get(pos).getPhone());

                            contactsName = db.getAllContactsName();
                            contactsPhone = db.getAllContactsPhone();

                            adapter = new ContactsAdapter(Contacts.this, contactsName, contactsPhone);
                            lvContacts.setAdapter(adapter);

                            if (db.getContactsCount()==0)
                                empty.setVisibility(View.VISIBLE);
                            else empty.setVisibility(View.GONE);

                            Toast.makeText(Contacts.this, "Successfully deleted", Toast.LENGTH_SHORT).show();

                        }
                    });
                    dialog.show();
                }
            });


            adapter = new ContactsAdapter(this, contactsName, contactsPhone);
            lvContacts.setAdapter(adapter);
        }

    }

    public void backPressed(View view) { onBackPressed(); }

    @SuppressLint("InflateParams")
    public void addContact(View view) {
        final View customView = getLayoutInflater().inflate(R.layout.dialog_add, null);

        final AlertDialog alertAddContact = new AlertDialog.Builder(this).create();
        alertAddContact.setView(customView);

        alertAddContact.setCanceledOnTouchOutside(true);
        alertAddContact.setCancelable(true);

        final TextView btn_done = customView.findViewById(R.id.btn_done);
        final TextView btn_dismiss = customView.findViewById(R.id.btn_dismiss);

        final EditText name = customView.findViewById(R.id.et_name);
        final EditText email = customView.findViewById(R.id.et_email);
        final EditText phone = customView.findViewById(R.id.et_ph);

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 &&
                        ((String.valueOf(s.charAt(0)).equals("0")) || (!String.valueOf(s.charAt(0)).equals("7")))) {
                    phone.setText("7");
                    phone.setSelection(1);
                }
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strName = name.getText().toString();
                String strPhone = phone.getText().toString();
                String strEmail = email.getText().toString();

                if (strName.equals("")) {
                    phone.setError("name");
                    phone.requestFocus();
                } else if (strPhone.equals("")) {
                    phone.setError("required");
                    phone.requestFocus();
                } else if (strPhone.length() < 9) {
                    phone.setError("invalid");
                    phone.requestFocus();
                } else if (phoneExists(strPhone)) {
                    phone.setError("exists");
                    phone.requestFocus();
                } else if (emailExists(strEmail)) {
                    email.setError("exists");
                    email.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
                    email.setError("invalid");
                    email.requestFocus();
                } else {
                    db.addContact(new Contact(strName,strPhone,strEmail));
                    alertAddContact.dismiss();

                    contactsName = db.getAllContactsName();
                    contactsPhone = db.getAllContactsPhone();

                    adapter = new ContactsAdapter(Contacts.this, contactsName, contactsPhone);
                    lvContacts.setAdapter(adapter);

                    if (db.getContactsCount()==0)
                        empty.setVisibility(View.VISIBLE);
                    else empty.setVisibility(View.GONE);
                }
            }
        });

        btn_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertAddContact.dismiss();
            }
        });

        alertAddContact.show();
    }

    @SuppressLint("InflateParams")
    public void editContact(final Contact contact) {
        final View customView = getLayoutInflater().inflate(R.layout.dialog_add, null);

        final AlertDialog alertAddContact = new AlertDialog.Builder(this).create();
        alertAddContact.setView(customView);

        alertAddContact.setCanceledOnTouchOutside(true);
        alertAddContact.setCancelable(true);

        final TextView tvTitle = customView.findViewById(R.id.tv_add_title);

        final TextView btn_done = customView.findViewById(R.id.btn_done);
        final TextView btn_dismiss = customView.findViewById(R.id.btn_dismiss);

        final EditText name = customView.findViewById(R.id.et_name);
        final EditText email = customView.findViewById(R.id.et_email);
        final EditText phone = customView.findViewById(R.id.et_ph);

        name.setText(contact.getName());
        phone.setText(contact.getPhone());
        email.setText(contact.getEmail());

        tvTitle.setText(getString(R.string.edit_contact));

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strName = name.getText().toString();
                String strPhone = phone.getText().toString();
                String strEmail = email.getText().toString();

                if (strName.equals("")) {
                    phone.setError("name");
                    phone.requestFocus();
                } else if (strPhone.equals("")) {
                    phone.setError("required");
                    phone.requestFocus();
                } else if (strPhone.length() < 9) {
                    phone.setError("invalid");
                    phone.requestFocus();
                } else if (phoneExists(strPhone) && !strPhone.equals(contact.getPhone())) {
                    phone.setError("exists");
                    phone.requestFocus();
                } else if (emailExists(strEmail) && !strEmail.equals(contact.getEmail())) {
                    email.setError("exists");
                    email.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
                    email.setError("invalid");
                    email.requestFocus();
                } else {
                    db.updateContact(contact.getEmail(), new Contact(strName,strPhone,strEmail));

                    contactsName = db.getAllContactsName();
                    contactsPhone = db.getAllContactsPhone();

                    adapter = new ContactsAdapter(Contacts.this, contactsName, contactsPhone);
                    lvContacts.setAdapter(adapter);

                    if (db.getContactsCount()==0)
                        empty.setVisibility(View.VISIBLE);
                    else empty.setVisibility(View.GONE);

                    alertAddContact.dismiss();
                }
            }
        });

        btn_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertAddContact.dismiss();
            }
        });

        alertAddContact.show();
    }

    private boolean phoneExists(String strPhone) {
        for (int i = 0; i < db.getContactsCount(); i++){
            if (db.getAllContacts().get(i).getPhone().equals(strPhone))
                return true;
        }
        return false;
    }

    private boolean emailExists(String strEmail) {
        for (int i = 0; i < db.getContactsCount(); i++){
            if (db.getAllContacts().get(i).getEmail().equals(strEmail))
                return true;
        }
        return false;
    }
}
