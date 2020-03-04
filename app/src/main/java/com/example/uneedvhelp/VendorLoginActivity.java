package com.example.uneedvhelp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VendorLoginActivity extends AppCompatActivity {

    Button login;
    EditText email,password;
    VendorRegistrationDataModel dataModel;
    TextView register_text;
    private DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_login);

        //Firebase
        // Write a message to the database
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("message");
        db = new DatabaseHandler(this);
        login = findViewById(R.id.btn_login);
        email = findViewById(R.id.edt_email);
        password = findViewById(R.id.edt_password);
        register_text = (TextView)findViewById(R.id.start_register);
        register_text.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(VendorLoginActivity.this, VendorRegistrationForm1Activity.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email_id = email.getText().toString();
                if(email_id.equals("")){
                    email.setError("Please type email");
                }
                final String pwd = password.getText().toString();
                if(pwd.equals("")){
                    password.setError("Please type password");

                }

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                       /* String email = dataSnapshot.child("email").getValue().toString();
                        String pass = dataSnapshot.child("password").getValue().toString();*/

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                             dataModel = snapshot.getValue(VendorRegistrationDataModel.class);

                        }
                        db.retiriveData(dataModel);

                        String email = dataModel.getEmail();
                        String pass = dataModel.getPassword();



                        if(email_id.equals(email)&& pwd.equals(pass)) {
                            Intent intent = new Intent(VendorLoginActivity.this, VendorSignedInActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(VendorLoginActivity.this, "Username or Password incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

    }

}
