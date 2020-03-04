package com.example.uneedvhelp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CustomerRequestActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialogEnd, datePickerDialogStart;
    private SimpleDateFormat dateFormatter;
    //getData from these fields for end and start date
    private TextView endDate, startDate;
    //get Data from these fields for end and start date
    private EditText requestTitle, requestExplanation;
    //submission button
    private Button submit_button;
    private DatabaseHandler db;
    public final String TAG = "Here is your data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        db = new DatabaseHandler(this);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        setContentView(R.layout.activity_customer_request);
        endDate = findViewById(R.id.request_end_date);
        endDate.setInputType(InputType.TYPE_NULL);
        endDate.requestFocus();
        Calendar endCalendar = Calendar.getInstance();
        datePickerDialogEnd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                endDate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH));

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialogEnd.show();
            }
        });
        startDate = findViewById(R.id.request_start_date);
        startDate.setInputType(InputType.TYPE_NULL);
        startDate.requestFocus();
        Calendar startCalendar = Calendar.getInstance();
        datePickerDialogStart = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                startDate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH));

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialogStart.show();
            }
        });
        submit_button = findViewById(R.id.submit_request_btn);
        submit_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean correct_request = false;

                //Your values for validation :)
                requestTitle = findViewById(R.id.request_title);
                String mTitle = requestTitle.getText().toString();


                requestExplanation = findViewById(R.id.request_explanation);
                String mExplanation = requestExplanation.getText().toString();


                String mEndDate = endDate.getText().toString();
                String mStartDate = startDate.getText().toString();


                Intent data = getIntent();
                // customerId retrieved
                int customerId = data.getIntExtra("customerId", -1);
                Log.println(Log.ERROR, TAG, "customerId:" + customerId + "\n mEndDate:" + mEndDate + "\n mExplanation: " + mExplanation);

                // TODO: Verify these and set errors depending on whether or not they are valid

                // TODO: Integrate this value with Mit/Gef

                String temp_Category = "Plumbing";

                Date oStartDate = new Date();
                Date oEndDate = new Date();


                try {
                    oStartDate = new SimpleDateFormat("dd-MM-yyyy").parse(mStartDate);
                } catch (Exception e) {

                }
                try {
                    oEndDate = new SimpleDateFormat("dd-MM-yyyy").parse(mEndDate);
                } catch (Exception e) {

                }
                // TODO:Add Validations here
                boolean titleBoolean = true;
                boolean validation = false;

                if (mTitle.equals("")) {
                    requestTitle.setError("Fill the Field");
                    titleBoolean = false;
                } else if (mTitle.length() < 10) {

                    requestTitle.setError("Title length too short");
                    titleBoolean = false;

                } else if (mTitle.length() > 50) {

                    requestTitle.setError("Title length too long");
                    titleBoolean = false;

                } else {
                    titleBoolean = true;
                }


                //description validation

                boolean explanationBoolean = true;

                if (mExplanation.equals("")) {
                    requestExplanation.setError("Fill the field");
                    explanationBoolean = false;

                } else if (mExplanation.length() > 255) {
                    requestExplanation.setError("Please describe under 255 characters");
                    explanationBoolean = false;
                } else if (mExplanation.length() < 40) {
                    requestExplanation.setError("Please Describe in more than 40 character");
                    explanationBoolean = false;
                } else {
                    explanationBoolean = true;
                }

                Date today = new Date();
                long curtime = today.getTime();
                Date sCur = new Date(curtime);
                long mintime = today.getTime() + 2 * 24 * 60 * 60 * 1000;
                Date sMin = new Date(mintime);
                long maxtime = today.getTime() + 14 * 24 * 60 * 60 * 1000;
                Date sMax = new Date(maxtime);

                boolean dateBoolean = true;
                if (mStartDate.equals("")) {
                    startDate.setError("Fill the field");
                    dateBoolean = false;
                }


                else if (oStartDate.before(sCur) || oStartDate.equals(sCur)) {
                    ((TextView) findViewById(R.id.request_start_date)).requestFocus();
                    ((TextView) findViewById(R.id.request_start_date)).setError("InCorrect Start Date ");
                    dateBoolean = false;
                } else if (oEndDate.before(sMin)) {
                    ((TextView) findViewById(R.id.request_end_date)).requestFocus();
                    ((TextView) findViewById(R.id.request_end_date)).setError("End Date needs to be at least 2 days after Start Date");
                    dateBoolean = false;
                } else if (oEndDate.after(sMax)) {
                    ((TextView) findViewById(R.id.request_end_date)).requestFocus();
                    ((TextView) findViewById(R.id.request_end_date)).setError("End Date needs to be no less than 14 days later ");
                    dateBoolean = false;
                } else {
                    dateBoolean = true;
                }


                if (dateBoolean && explanationBoolean && titleBoolean == true) {
                    // TODO:Add Database Code Here
                    CustomerRequestDataModel cust = new CustomerRequestDataModel();
                    cust.setTitle(mTitle);
                    cust.setDescription(mExplanation);
                    cust.setStartDate(mStartDate);
                    cust.setEndDate(mEndDate);

                    db.insertRequest(cust);


                    Intent i = new Intent(CustomerRequestActivity.this, CustomerSignInActivity.class);

                    startActivity(i);
                    finish();

                } else {
                    Toast.makeText(CustomerRequestActivity.this, "Some of these fields are incorrect, please correct them", Toast.LENGTH_SHORT).show();
                }
            }
        });


        /*String[] arraySpinner = new String[] {
                "1", "2", "3", "4", "5", "6", "7"
        };
        Spinner s = (Spinner) findViewById(R.id.);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);*/
    }


}
