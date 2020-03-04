package com.example.uneedvhelp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.util.Log;

public class VendorRegistrationForm1Activity extends AppCompatActivity {
    //regex for email
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    EditText fname, lname, email, phone, password, confirm_password, address, dob,zip,state,city;
    DatePickerDialog dt_dob;
    private SimpleDateFormat dateFormatter;
    Button btnNext;
    RadioGroup radiogrp;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_registration_form1);


        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        // get the listview

       /* expListView = findViewById(R.id.expandable);*/

        fname= findViewById(R.id.edt_firstName);
        lname= findViewById(R.id.edt_lastName);
        email = findViewById(R.id.edt_email);
        password = findViewById(R.id.edt_password);
        confirm_password= findViewById(R.id.edt_confirm_password);
        phone= findViewById(R.id.edt_phone_number);
        zip=(EditText) findViewById(R.id.edt_zipcode);
        state=(EditText) findViewById(R.id.edt_state);
        city=(EditText) findViewById(R.id.edt_city);
        address=(EditText) findViewById(R.id.edt_address);
        radiogrp= findViewById(R.id.radio_grp);

        /*gender= findViewById(R.id.edt_gender);
        gender.setInputType(InputType.TYPE_NULL);
        gender.requestFocus();*/

        /*expListView = new ExpandableListView(this, new ExpandableListView.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
       /* gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
*/

        /*
         * Date picker code
         * */

        //date of birth edittext and date picker
        dob = findViewById(R.id.edt_dob);
        dob.setInputType(InputType.TYPE_NULL);
        dob.requestFocus();
        Calendar newCalendar = Calendar.getInstance();
        dt_dob = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dob.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dt_dob.show();
            }
        });



        // preparing list data
        prepareListData();

        listAdapter = new com.example.uneedvhelp.ExpandableListAdapter(this, listDataHeader, listDataChild);


        // setting list adapter
        //expListView.setAdapter(listAdapter);

        //next button will redirect to other part of the registration form
        btnNext = (Button) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean allValid=false;
                boolean fBool = false;
                //validations
                String fValue =getStringValue(fname);

                if(fValue.equals("")){
                    fname.setError("Fill this field");
                }
                else{
                    fBool=checkName(fValue);
                    if(fBool==false){
                        fname.setError("First Name needs to be under 40 characters");
                    }
                }
                String lValue =getStringValue(lname);
                boolean lBool = false;

                if(lValue.equals("")){
                    lname.setError("Fill this field");
                }
                else{
                    lBool=checkName(lValue);
                    if(lBool==false){
                        lname.setError("Last Name needs to be under 40 characters");
                    }
                }
                String eValue = getStringValue(email);
                boolean eBool = false;
                if(eValue.equals("")){
                    email.setError("Fill this field");
                }
                else{
                    eBool=checkEmail(eValue);
                    if(eBool==false){
                        email.setError("Invalid Email");
                    }
                }

                String pValue=getStringValue(password);
                String pcValue=getStringValue(confirm_password);
                boolean pBool = false;
                if(pValue.equals("")){
                    password.setError("Fill this field");
                }
                else{
                    pBool=checkPassword(pValue);
                    if(pBool==false){
                        password.setError("Password must be at least 8 characters");
                    }
                    if(pBool==true){
                        if(!pcValue.equals(pValue)){
                            pBool=false;
                            confirm_password.setError("Please make the passwords match");
                        }

                    }
                }

                String mobileValue =getStringValue(phone);
                boolean mBool = false;
                if(mobileValue.equals("")){
                    phone.setError("Fill this field");

                }
                else{
                    mBool=checkMobile(mobileValue);
                    //Log.w(":Mobile",mobileValue);
                    if(mBool==false){
                        phone.setError("Please type a 10 digit phone number");
                    }
                }

                String addrValue =getStringValue(address);
                boolean aBool = true;
                if(addrValue.equals("")){
                    aBool=false;
                    address.setError("Fill this field");
                }
                String zipValue =getStringValue(zip);
                boolean zBool = false;
                if(zipValue.equals("")){

                    zip.setError("Fill this field");
                }
                else{
                    zBool=checkZip(zipValue);
                    if(zBool==false){
                        zip.setError("Please type a 5 digit zipcode");
                    }
                }
                String stateValue =getStringValue(state);
                boolean sBool = true;
                if(stateValue.equals("")){
                    sBool = false;
                    state.setError("Fill this field");
                }
                boolean cBool = true;
                String cityValue =getStringValue(city);
                if(cityValue.equals("")){
                    cBool=false;
                    city.setError("Fill this field");
                }

                /*String firstName = fname.getText().toString();
                String lastName = lname.getText().toString();
                String email_id = email.getText().toString();
                String pwd = password.getText().toString();
                String conf_pwd = confirm_password.getText().toString();
                String dt_birth = dob.getText().toString();
                String mobile = phone.getText().toString();*/



                if(fBool&&lBool&&eBool&&pBool&&mBool&&aBool&&zBool&&sBool&&cBool) {
                    Intent intent = new Intent(VendorRegistrationForm1Activity.this, VendorRegistrationForm2Activity.class);

                    int radioID = radiogrp.getCheckedRadioButtonId();
                    RadioButton radio_btn= findViewById(radioID);
                    String str_radio = radio_btn.getText().toString();

                    intent.putExtra("fname", fValue);
                    intent.putExtra("lname", lValue);
                    intent.putExtra("email", eValue);
                    intent.putExtra("password", pValue);
                    intent.putExtra("phone", mobileValue);
                    intent.putExtra("address",addrValue);
                    intent.putExtra("zip",zipValue);
                    intent.putExtra("state",stateValue);
                    intent.putExtra("city",cityValue);
                    intent.putExtra("dob",getStringValue(dob));
                    intent.putExtra("gender",str_radio);

                    startActivity(intent);
                }
                else{
                    Toast.makeText(VendorRegistrationForm1Activity.this, "Some fields are incorrect, please tap the tooltip icon to check the issue", Toast.LENGTH_LONG).show();
                }





                //intent to redirect to form 2
                
            }
        });
    }

    /*
     * Preparing the list data
     */

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Expandable GenderList Header
        listDataHeader.add("Gender");


        // Adding types of gender
        List<String> gender = new ArrayList<String>();
        gender.add("Male");
        gender.add("Female");
        gender.add("Trans");


        listDataChild.put(listDataHeader.get(0), gender); // Header, Child data

    }
    //validation functions
        private boolean checkName(String name){
        boolean isValid = false;
        if(name.length()>40){
            //error
            isValid=false;

        }
        else if(name.length()<=40){
            //good
            isValid=true;
        }
        return isValid;
    }

    private boolean checkMobile(String mobile){
        boolean isValid = false;
        isValid=Pattern.matches("^\\d{10}$",mobile)? true : false;
        return isValid;
    }

    private boolean checkEmail(String email){
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        boolean isValid = false;
        isValid=matcher.find()? true : false;
        return isValid;
    }
    private boolean checkPassword(String password){
        boolean isValid = false;
        if(password.length()<8){
            //error
            isValid=false;

        }
        else if(password.length()>=8){
            //good
            isValid=true;
        }
        return isValid;
    }
    private boolean checkAddress(String address){
        boolean isValid = false;
        if(address!=null && address!=" "){
            isValid=true;
        }
        return isValid;
    }
    private boolean checkZip(String zip){
        boolean isValid = false;
        isValid=Pattern.matches("^\\d{5}$",zip)? true : false;
        return isValid;
    }
    private boolean checkState(String state){
        boolean isValid = false;
        if(state!=null && state!=" "){
            isValid=true;
        }

        return isValid;
    }
    private boolean checkCity(String city){
        boolean isValid = false;
        if(city!=null && city!= " "){
            isValid=true;
        }
        return isValid;
    }
    private boolean checkDob(String dob){
        boolean isValid = false;
        if(dob!=null){
            isValid=true;
        }
        return isValid;
    }
    private String getStringValue(EditText itemValue ){
        String myValue=itemValue.getText().toString();
        return myValue;
    }


}

