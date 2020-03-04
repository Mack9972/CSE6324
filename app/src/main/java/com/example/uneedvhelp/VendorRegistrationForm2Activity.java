package com.example.uneedvhelp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;


public class VendorRegistrationForm2Activity extends AppCompatActivity {

    //back button
    Button btn_back, btn_register;
    CheckBox iAgree;
    private DatabaseHandler db;
    EditText ssn,rate;
    TextView txt_login;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_registration_form2);

        //SQL database
        db = new DatabaseHandler(this);

        //redirecting to login page using the link
        txt_login = (TextView)findViewById(R.id.txt_login_here);
        txt_login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(VendorRegistrationForm2Activity.this, VendorLoginActivity.class);
                startActivity(intent);
            }
        });

        //Firebase
        // Write a message to the database
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("message");

        ssn = findViewById(R.id.edt_ssn);
        rate = findViewById(R.id.edt_hourly_rate);


        //20% commission condition
        iAgree = findViewById(R.id.check_i_agree);
        //back button event listener
        btn_back = findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent to go back to form 1
                Intent intent = new Intent(VendorRegistrationForm2Activity.this, VendorRegistrationForm1Activity.class);
                startActivity(intent);

            }
        });
        //register event listener
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fetching data from Registration Form1
                Intent data = getIntent();
                System.out.println("************************"+data);
                String fName = data.getStringExtra("fname");
                String lName = data.getStringExtra("lname");
                String emailID = data.getStringExtra("email");
                String passWord = data.getStringExtra("password");
                String DOB = data.getStringExtra("dob");
                String gender = data.getStringExtra("gender");
                String mobile = data.getStringExtra("phone");
                String address =data.getStringExtra("address");
                String city=data.getStringExtra("city");
                String state=data.getStringExtra("state");
                String zip=data.getStringExtra("zip");
                String social = ssn.getText().toString();
                String hrly_rate = rate.getText().toString();

                String strKey = myRef.push().getKey();

                VendorRegistrationDataModel dataModel = new VendorRegistrationDataModel();
                dataModel.setFirstName(fName);
                dataModel.setLastName(lName);
                dataModel.setEmail(emailID);
                dataModel.setPassword(passWord);
                dataModel.setMobile(mobile);
                dataModel.setDob(DOB);
                dataModel.setGender(gender);
                dataModel.setAddress(address);
                dataModel.setCity(city);
                dataModel.setState(state);
                dataModel.setZipcode(zip);
                dataModel.setSsn(social);
                dataModel.setHourly_rate(hrly_rate);
                db.insertRecord(dataModel);
                dataModel.setStrKey(strKey);


                myRef.child(strKey).setValue(dataModel);

                //Validation for Agree checkbox condition
                boolean ssnBool = false;
                String sValue =getStringValue(ssn);
                //validates fields on page
                if(sValue.equals("")){
                    ssn.setError("Fill this field");
                }
                else{
                    ssnBool=checkSSN(sValue);
                    if(ssnBool==false){
                        ssn.setError("SSN needs to be 9 characters");
                    }
                }
                boolean rateBool = false;
                String rateValue =getStringValue(rate);
                int rateI;
                if(!rateValue.equals("")) {
                    try {
                        rateI=Integer.parseInt(rateValue);
                    } catch (Exception any) {
                        rate.setError("Not an integer");
                    }
                }


                if (iAgree.isChecked() && ssnBool) {
                    Intent i = new Intent(VendorRegistrationForm2Activity.this, VendorLoginActivity.class);
                    startActivity(i);

                } else {
                    Toast.makeText(VendorRegistrationForm2Activity.this, "Please check I agree to the terms if you want to Register", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private boolean checkSSN(String ssn){
        boolean isValid = false;
        isValid= Pattern.matches("^\\d{9}$",ssn)? true : false;
        return isValid;
    }

    private boolean checkRate(String rate){
        boolean isValid = false;
        isValid= Pattern.matches("^\\d{1,5}$",rate)? true : false;
        return isValid;
    }
    private boolean checkItems(String rate) {
        boolean isValid = false;

        return isValid;
    }
    private String getStringValue(EditText itemValue ){
        String myValue=itemValue.getText().toString();
        return myValue;
    }


}
