package com.example.uneedvhelp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class VendorHomeActivity extends AppCompatActivity implements View.OnClickListener {

    Button login,register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_home);

        login = findViewById(R.id.btn_login);
        login.setOnClickListener(this);

        register = findViewById(R.id.btn_register);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:
                {
                Intent i = new Intent(VendorHomeActivity.this,VendorRegistrationForm1Activity.class);
                startActivity(i);
                break;
                }
            case R.id.btn_login:
                {
                Intent i  = new Intent(VendorHomeActivity.this,VendorLoginActivity.class);
                startActivity(i);
                break;
                }
        }
    }
}
