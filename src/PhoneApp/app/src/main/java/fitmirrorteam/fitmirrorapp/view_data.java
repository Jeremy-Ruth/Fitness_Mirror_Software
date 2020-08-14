package fitmirrorteam.fitmirrorapp;

/*
 * Allows the user to access health information in some preset views for the date(s) requested.
 * Single view takes up the whole screen for one date's data. Choosing image only also gives users
 * a way to view a full screenshot of a given daily photo. 
 *
 * Multi view gives a user the option to compare the images and data from two dates. This was especially 
 * intended to provide a before and after option to see progress in meeting health goals. Image only 
 * can also be selected in multi viewed to use as much screen space as possible for both images. It is 
 * also possible to see only data in both view types.
 */

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.Button;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class view_data extends SubActivityTemplate implements CompoundButton.OnCheckedChangeListener{

    /* "multi" in this activity implies side-by-side. Used for naming convenience
	 * "viewType" string definitions: 
	 * single image and data = sID
	 * single image = sI,
	 * single data = sD
	 * multi image and data = mID
	 * multi image = mI
	 * multi data = mD
	 */
    private Calendar viewDataCal = Calendar.getInstance();
    private EditText firstDateField;
    private EditText secondDateField;
    private DatePickerDialog.OnDateSetListener firstDate;
    private DatePickerDialog.OnDateSetListener secondDate;
    private int firstDay;
    private int firstMonth;
    private int firstYear;
    private int secondDay;
    private int secondMonth;
    private int secondYear;
    private RadioButton singleImgDat;
    private RadioButton singleImg;
    private RadioButton singleDat;
    private RadioButton multiImgDat;
    private RadioButton multiImg;
    private RadioButton multiDat;
    private boolean viewWasSet = false;
    private boolean wasDoubleView = false;
    private String viewType;                

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button viewDataCancel;
        Button viewDataSubmit;
        viewType = "";

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //////////////////////////////////// DATE PICKER FIRST DATE SECTION //////////////////////////////////////
        // Get the first date info
        firstDateField = (EditText) findViewById(R.id.viewDataFirstDateEntryBox);
        firstDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                viewDataCal.set(Calendar.YEAR, year);
                viewDataCal.set(Calendar.MONTH, month);
                viewDataCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                firstDay = dayOfMonth;
                firstMonth = month+1;
                firstYear = year;
                updateDateField(1);
            }
        };

        // Call the date picker for the first date
        firstDateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(view_data.this, firstDate, viewDataCal.get(Calendar.YEAR),
                        viewDataCal.get(Calendar.MONTH), viewDataCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //////////////////////////////// DATE PICKER SECOND DATE SECTION ////////////////////////////////////////////
        // Get the second date info
        secondDateField = (EditText) findViewById(R.id.viewDataSecondDataEntryBox);
        secondDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                viewDataCal.set(Calendar.YEAR, year);
                viewDataCal.set(Calendar.MONTH, month);
                viewDataCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                secondDay = dayOfMonth;
                secondMonth = month+1;
                secondYear = year;
                updateDateField(2);
            }
        };

        // Call the date picker for second date
        secondDateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(view_data.this, secondDate, viewDataCal.get(Calendar.YEAR),
                        viewDataCal.get(Calendar.MONTH), viewDataCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ///////////////////////////////////// BUTTON SECTION ///////////////////////////////////////////////////////
        singleImgDat = (RadioButton) findViewById(R.id.viewDataSingleImageDataRadio);
        singleImgDat.setOnCheckedChangeListener(this);
        singleDat = (RadioButton) findViewById(R.id.viewDataSingleDataRadio);
        singleDat.setOnCheckedChangeListener(this);
        singleImg = (RadioButton) findViewById(R.id.viewDataSingleImageRadio);
        singleImg.setOnCheckedChangeListener(this);
        multiImgDat = (RadioButton) findViewById(R.id.viewDataDoubleImageDataRadio);
        multiImgDat.setOnCheckedChangeListener(this);
        multiDat = (RadioButton) findViewById(R.id.viewDataDoubleDataRadio);
        multiDat.setOnCheckedChangeListener(this);
        multiImg = (RadioButton) findViewById(R.id.viewDataDoubleImageRadio);
        multiImg.setOnCheckedChangeListener(this);

        viewDataSubmit = (Button) findViewById(R.id.viewDataSubmitButton);
        viewDataSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                try {
                    submitViewData();
                }catch (IOException e) {
                    Toast.makeText(view_data.this,"There was an IO error",Toast.LENGTH_LONG).show();
                }
            }
        });

        viewDataCancel = (Button) findViewById(R.id.viewDataCancelButton);
        viewDataCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelViewData();
            }
        });
    }

	// Date field updates including some basic validation. Makes use of built in Android date formatting options so can be 
	// easily changed if the current date format is not desired.
    private void updateDateField(int fieldToUpdate) {
        String dateFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        long detectFutureTime = System.currentTimeMillis() - viewDataCal.getTimeInMillis();
        if(fieldToUpdate == 1) {
            if (detectFutureTime < 0) {
                Toast.makeText(view_data.this, "You cannot choose future dates, no data!", Toast.LENGTH_SHORT).show();
                firstDateField.setText(null);
                firstDay = 0;
                firstMonth = 0;
                firstYear = 0;
            }else {
                firstDateField.setText(sdf.format(viewDataCal.getTime()));
            }
        }else {
            if (detectFutureTime < 0) {
                Toast.makeText(view_data.this, "You cannot choose future dates, no data!", Toast.LENGTH_SHORT).show();
                secondDateField.setText(null);
                secondDay = 0;
                secondMonth = 0;
                secondYear = 0;
            }else {
                secondDateField.setText(sdf.format(viewDataCal.getTime()));
            }
        }
    }

	// Send the requested view parameters to the mirror side and database
	// Includes some validation and verification for user input
    private void submitViewData() throws IOException{
        String firsDateStr;
        String secondDateStr;
        String currViewType;
        String viewDateSpecs;
        String response = "";
        //server_interface currConnection;

        switch (viewType) {
            case "sID":
                currViewType = "Single: Image and Data";
                break;
            case "sD":
                currViewType = "Single: Data only";
                break;
            case "sI":
                currViewType = "Single: Image only";
                break;
            case "mID":
                currViewType = "Double: Image and Data";
                break;
            case "mD":
                currViewType = "Double: Data only";
                break;
            case "mI":
                currViewType = "Double: Image only";
                break;
            default:
                currViewType = "Something went wrong with assignment";
                break;
        }

        if(viewWasSet) {
            if(firstDay+firstMonth+firstYear == 0) {
                Toast.makeText(view_data.this, "At the least you must specify a single view date ",Toast.LENGTH_SHORT).show();
            }else if(secondDay+secondMonth+secondYear == 0 && wasDoubleView) {
                Toast.makeText(view_data.this, "You have selected a side-by-side view " +
                        "but did not specify the date of the second view.",Toast.LENGTH_SHORT).show();
            }else if(secondDay+secondMonth+secondYear != 0 && !wasDoubleView) {
                Toast.makeText(view_data.this, "You have selected a single view " +
                        "and so you should not specify a second date.",Toast.LENGTH_SHORT).show();
                secondDateField.setText(null);
                secondDay = 0;
                secondMonth = 0;
                secondYear = 0;
                wasDoubleView = false;
            }else {
                    String commandToSend = "";
                    firsDateStr = firstMonth + "/" + firstDay + "/" + firstYear;
                    secondDateStr = secondMonth + "/" + secondDay + "/" + secondYear;

                    if(wasDoubleView) {
                        viewDateSpecs = firsDateStr + "\n" + secondDateStr;
                    }else {
                        viewDateSpecs = firsDateStr;
                        commandToSend = viewType + " " + firstYear + "-" + firstMonth + "-" + firstDay;
                        new server_interface(view_data.this).execute(commandToSend);
                    }

                    Toast.makeText(view_data.this, "View Data run with:\nView Type: " + currViewType
                            + "\nFor date(s):\n" + viewDateSpecs, Toast.LENGTH_LONG).show();
                    Toast.makeText(view_data.this, response,Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(view_data.this, "You must choose a view type and the date(s)"
                    + " you would like to view.", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelViewData() {
        // clear data here when read
        finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (buttonView.getId() == R.id.viewDataSingleImageDataRadio) {
                singleDat.setChecked(false);
                singleImg.setChecked(false);
                multiImgDat.setChecked(false);
                multiDat.setChecked(false);
                multiImg.setChecked(false);
                viewType = "sID";
                viewWasSet = true;
                wasDoubleView = false;
            }
            if (buttonView.getId() == R.id.viewDataSingleDataRadio) {
                singleImgDat.setChecked(false);
                singleImg.setChecked(false);
                multiImgDat.setChecked(false);
                multiDat.setChecked(false);
                multiImg.setChecked(false);
                viewType = "sD";
                viewWasSet = true;
                wasDoubleView = false;
            }
            if(buttonView.getId() == R.id.viewDataSingleImageRadio) {
                singleImgDat.setChecked(false);
                singleDat.setChecked(false);
                multiImgDat.setChecked(false);
                multiDat.setChecked(false);
                multiImg.setChecked(false);
                viewType = "sI";
                viewWasSet = true;
                wasDoubleView = false;
            }
            if (buttonView.getId() == R.id.viewDataDoubleImageDataRadio) {
                singleImgDat.setChecked(false);
                singleDat.setChecked(false);
                singleImg.setChecked(false);
                multiDat.setChecked(false);
                multiImg.setChecked(false);
                viewType = "mID";
                viewWasSet = true;
                wasDoubleView = true;
            }
            if (buttonView.getId() == R.id.viewDataDoubleDataRadio) {
                singleImgDat.setChecked(false);
                singleDat.setChecked(false);
                singleImg.setChecked(false);
                multiImgDat.setChecked(false);
                multiImg.setChecked(false);
                viewType = "mD";
                viewWasSet = true;
                wasDoubleView = true;
            }
            if(buttonView.getId() == R.id.viewDataDoubleImageRadio) {
                singleImgDat.setChecked(false);
                singleDat.setChecked(false);
                singleImg.setChecked(false);
                multiImgDat.setChecked(false);
                multiDat.setChecked(false);
                viewType = "mI";
                viewWasSet = true;
                wasDoubleView = true;
            }
        }
    }
}
