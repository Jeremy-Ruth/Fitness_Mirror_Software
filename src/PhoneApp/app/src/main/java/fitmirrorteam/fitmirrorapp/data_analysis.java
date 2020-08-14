package fitmirrorteam.fitmirrorapp;

/*
 * The data analysis options provides a means for the user to specify a date range of data to work with and some basic 
 * presentation options. In the current state of the project this functionality is not completely implemented. The phone
 * app can send the expected command keywords, but the mirror side program doesn't have any functionality at this time.
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
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class data_analysis extends SubActivityTemplate implements CompoundButton.OnCheckedChangeListener {

    private ToggleButton prediction;
    private RadioButton useLineGraph;
    private RadioButton useBarGraph;
    private Calendar analysisCal = Calendar.getInstance();
    EditText fromDateField;
    EditText toDateField;
    DatePickerDialog.OnDateSetListener fromDate;
    DatePickerDialog.OnDateSetListener toDate;
    private int graphType;
    private int doPrediction;
    private int fromDay;
    private int fromMonth;
    private int fromYear;
    private int toDay;
    private int toMonth;
    private int toYear;
    private long fromDateChecker;
    private long toDateChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_analysis);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button dataAnlysisCancel;
        Button dataAnalysisSubmit;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Get the FROM date info
        fromDateField = (EditText) findViewById(R.id.dataAnalysisFromEntryBox);
        fromDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                analysisCal.set(Calendar.YEAR, year);
                analysisCal.set(Calendar.MONTH, month);
                analysisCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                fromDay = dayOfMonth;
                fromMonth = month + 1;
                fromYear = year;
                fromDateChecker = analysisCal.getTimeInMillis();
                updateDateField(1);
            }
        };

        // Call the date picker for the TO date
        fromDateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(data_analysis.this, fromDate, analysisCal.get(Calendar.YEAR),
                        analysisCal.get(Calendar.MONTH), analysisCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Get the TO date info
        toDateField = (EditText) findViewById(R.id.dataAnalysisToEntryBox);
        toDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                analysisCal.set(Calendar.YEAR, year);
                analysisCal.set(Calendar.MONTH, month);
                analysisCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                toDay = dayOfMonth;
                toMonth = month + 1;
                toYear = year;
                toDateChecker = analysisCal.getTimeInMillis();
                updateDateField(2);
            }
        };

        // Call the date picker for the FROM date
        toDateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(data_analysis.this, toDate, analysisCal.get(Calendar.YEAR),
                        analysisCal.get(Calendar.MONTH), analysisCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        prediction = (ToggleButton) findViewById(R.id.dataAnalysisPredictionToggle);

        useLineGraph = (RadioButton) findViewById(R.id.dataAnalysisLineRadio);
        useLineGraph.setOnCheckedChangeListener(this);
        useBarGraph = (RadioButton) findViewById(R.id.dataAnalysisBarRadio);
        useBarGraph.setOnCheckedChangeListener(this);

        dataAnalysisSubmit = (Button) findViewById(R.id.dataAnalysisSubmitButton);
        dataAnalysisSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitDataAnalysis();
            }
        });

        dataAnlysisCancel = (Button) findViewById(R.id.dataAnalysisCancelButton);
        dataAnlysisCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelDataAnalysis();
            }
        });
    }

    private void updateDateField(int fieldToUpdate) {
        String dateFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        if (fieldToUpdate == 1) {
            long detectFutureTime = System.currentTimeMillis() - analysisCal.getTimeInMillis();
            if (detectFutureTime < 0) {
                Toast.makeText(data_analysis.this, "You cannot choose a future date for the from field.", Toast.LENGTH_SHORT).show();
                fromDateField.setText(null);
                fromDay = 0;
                fromMonth = 0;
                fromYear = 0;
            } else {
                fromDateField.setText(sdf.format(analysisCal.getTime()));
            }
        } else {
            toDateField.setText(sdf.format(analysisCal.getTime()));
        }
    }

    private void cancelDataAnalysis() {
        // Add clear content section once feature is enabled
        finish();
    }

	// Send user selections to the mirror side for processing and display on mirror
    private void submitDataAnalysis() {
        String temp1;
        String temp2 = "";
        String fromDateStr;
        String toDateStr;

        if (toDateChecker - fromDateChecker < 0) {
            Toast.makeText(data_analysis.this, "\"From\" date must be earlier than the \"To\" date!", Toast.LENGTH_SHORT).show();
        } else if (fromDay + fromMonth + fromYear == 0) {
            Toast.makeText(data_analysis.this, "You must specify a from Date in order to perform analysis", Toast.LENGTH_SHORT).show();
        } else {
            fromDateStr = fromMonth + "/" + fromDay + "/" + fromYear;
            if (toDay + toMonth + toYear == 0) {
                toDateStr = "The current date will be used";
            } else {
                toDateStr = toMonth + "/" + toDay + "/" + toYear;
            }

            if (doPrediction == 1)
                temp1 = "yes";
            else
                temp1 = "no";

            if (graphType == 1) {
                temp2 = "line graph";
            } else if (graphType == 2) {
                temp2 = "bar graph";
            }

            if (graphType == 0) {
                Toast.makeText(data_analysis.this, "You must choose the graph type before you can proceed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(data_analysis.this, "Analytics run with:\nPrediction: " + temp1
                        + "\nGraph type: " + temp2 + "\nFor date range:\n" + "From: " + fromDateStr
                        + "\nTo: " + toDateStr, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (buttonView.getId() == R.id.dataAnalysisLineRadio) {
                useBarGraph.setChecked(false);
                graphType = 1;                      // 1 implies the graph type will be a line graph
            }
            if (buttonView.getId() == R.id.dataAnalysisBarRadio) {
                useLineGraph.setChecked(false);
                graphType = 2;                      // 2 implies the graph type will be a bar graph
            }
        }
    }

	/* This feature indicates that the user wants to extrapolate future health data based off existing values within the 
	 * specified date range. The intent is that estimated values are only trended to fill in missing data within date range
	 * for dates that are beyond the current date. It would not for example fill in past dates that have data entry gaps.
	 */
    public void predictToggle(View v) {
        if (prediction.isChecked())
            doPrediction = 1;
        else
            doPrediction = 0;
    }
}
