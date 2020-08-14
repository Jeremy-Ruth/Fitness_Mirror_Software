package fitmirrorteam.fitmirrorapp;

/*
 * Provides a means for a user to pull up stored health information from the mirror side database. The data and image if
 * avilable are shown on the mirror display. Once a date is submitted the phone side will change over to the "Enter Health Info"
 * screen for the date entered here in order to provide a means for the user to make changes to the data if desired.
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.DatePicker;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class manage_data extends SubActivityTemplate {

    private int userDay, userMonth, userYear;
    private Calendar dataCal = Calendar.getInstance();
    EditText dateField;
    DatePickerDialog.OnDateSetListener dayOfData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button contentManageCancel;
        Button contentManageSubmit;

        dateField = (EditText) findViewById(R.id.manageDataChooseDateBox);
        dayOfData = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dataCal.set(Calendar.YEAR, year);
                dataCal.set(Calendar.MONTH, month);
                dataCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                userDay = dayOfMonth;
                userMonth = month+1;
                userYear = year;
                updateDateField();
            }
        };

        dateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(manage_data.this, dayOfData, dataCal.get(Calendar.YEAR),
                        dataCal.get(Calendar.MONTH), dataCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        contentManageSubmit = (Button) findViewById(R.id.manageDataSubmitButton);
        contentManageSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitContentManage();
            }
        });

        contentManageCancel = (Button) findViewById(R.id.manageDataCancelButton);
        contentManageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelContentManage();
            }
        });
    }

    private void cancelContentManage() {
        // clear content here when enabled
        finish();
    }

	// Send the keyword and info for the info the user wants to see to the mirror software
    private void submitContentManage() {
        Bundle passedDate = new Bundle();

         if(userDay+userMonth+userYear == 0) {
            Toast.makeText(manage_data.this, "You must choose a date to continue!", Toast.LENGTH_SHORT).show();
        }else{
             //Toast.makeText(manage_data.this, "The date entered was " + userMonth
             //        + "/" + userDay + "/" + userYear, Toast.LENGTH_SHORT).show();
             passedDate.putInt("day",userDay);
             passedDate.putInt("month", userMonth);
             passedDate.putInt("year",userYear);
             Intent goToDate = new Intent(this, enterHealthInfo.class);
             goToDate.putExtras(passedDate);
             String commandToSend = "sID " + userYear + "-" + userMonth + "-" + userDay;
             new server_interface(manage_data.this).execute(commandToSend);
             startActivity(goToDate);
        }
    }

	// Show the date the user has chosen on the GUI in the specified format. This can be changed if another date
	// format or strucutre is desired using the built in date view options for Android
    private void updateDateField() {
        String dateFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        dateField.setText(sdf.format(dataCal.getTime()));
        long detectFutureTime = System.currentTimeMillis() - dataCal.getTimeInMillis();
        if (detectFutureTime < 0) {
            Toast.makeText(manage_data.this, "You cannot choose a future date!", Toast.LENGTH_SHORT).show();
            dateField.setText(null);
            userDay = 0;
            userMonth = 0;
            userYear = 0;
        }else {
            dateField.setText(sdf.format(dataCal.getTime()));
        }
    }
}
