package fitmirrorteam.fitmirrorapp;

/*
 * Sends a request to perform an image timelapse view on the mirror over a selected date range. The current version
 * of the software iterates through all available images in a date range at a rate determined by how long the
 * timelapse is supposed to take.
 */

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class image_time_lapse extends SubActivityTemplate implements CompoundButton.OnCheckedChangeListener {

    private Calendar timeLapseCal = Calendar.getInstance();
    private EditText fromDateField;
    private EditText toDateField;
    private DatePickerDialog.OnDateSetListener fromDate;
    private DatePickerDialog.OnDateSetListener toDate;
    private int fromDay;
    private int fromMonth;
    private int fromYear;
    private int toDay;
    private int toMonth;
    private int toYear;
    private RadioButton tenSecInt;
    private RadioButton twentySecInt;
    private RadioButton sixtySecInt;
    private EditText customInt;
    private int secondsForInt;
    private boolean lapseWasSet = false;
    private boolean customTimeSet = false;
    private boolean customLapseWasFocus = false;
    private long fromDateChecker;
    private long toDateChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_time_lapse);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button timeLapseCancel;
        Button timeLapseSubmit;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Get the FROM date info
        fromDateField = (EditText) findViewById(R.id.fromDateBox);
        fromDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                timeLapseCal.set(Calendar.YEAR, year);
                timeLapseCal.set(Calendar.MONTH, month);
                timeLapseCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                fromDay = dayOfMonth;
                fromMonth = month + 1;
                fromYear = year;
                fromDateChecker = timeLapseCal.getTimeInMillis();
                updateDateField(1);
            }
        };

        // Call the date picker for the TO date
        fromDateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(image_time_lapse.this, fromDate, timeLapseCal.get(Calendar.YEAR),
                        timeLapseCal.get(Calendar.MONTH), timeLapseCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Get the TO date info
        toDateField = (EditText) findViewById(R.id.toDateBox);
        toDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                timeLapseCal.set(Calendar.YEAR, year);
                timeLapseCal.set(Calendar.MONTH, month);
                timeLapseCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                toDay = dayOfMonth;
                toMonth = month + 1;
                toYear = year;
                toDateChecker = timeLapseCal.getTimeInMillis();
                updateDateField(2);
            }
        };

        // Call the date picker for the FROM date
        toDateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(image_time_lapse.this, toDate, timeLapseCal.get(Calendar.YEAR),
                        timeLapseCal.get(Calendar.MONTH), timeLapseCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        tenSecInt = (RadioButton) findViewById(R.id.tenSecondTimeLapseButton);
        tenSecInt.setOnCheckedChangeListener(this);
        twentySecInt = (RadioButton) findViewById(R.id.twentySecondTimeLapseButton);
        twentySecInt.setOnCheckedChangeListener(this);
        sixtySecInt = (RadioButton) findViewById(R.id.sixtySecondTimeLapseButton);
        sixtySecInt.setOnCheckedChangeListener(this);

        ///////////////////////////////////////// CUSTOM TIME FIELD SECTION ////////////////////////////////////////////////
        customInt = (EditText) findViewById(R.id.customTimeLapseBox);
        customInt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager keyIn = (InputMethodManager) image_time_lapse.this.getSystemService(UserLogin.INPUT_METHOD_SERVICE);
                    if (keyIn != null)
                        keyIn.toggleSoftInput(0, 0);
                    else
                        keyErrorFail();
                    secondsForInt = checkForInt(customInt);
                    lapseWasSet = true;
                    //customTimeSet = true;
                    //customLapseWasFocus = true;
                    handled = true;
                }
                return handled;
            }
        });

        customInt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View customTimeView, boolean hasFocus) {
                if (hasFocus) {
                    tenSecInt.setChecked(false);
                    twentySecInt.setChecked(false);
                    sixtySecInt.setChecked(false);
                    customLapseWasFocus = true;
                    customTimeSet = true;
                    //tenSecInt.setEnabled(false);
                    //twentySecInt.setEnabled(false);
                    //sixtySecInt.setEnabled(false);
                }
                if (!hasFocus) {
                    //InputMethodManager keyIn = (InputMethodManager) image_time_lapse.this.getSystemService(UserLogin.INPUT_METHOD_SERVICE);
                    //if (keyIn != null)
                    //    keyIn.toggleSoftInput(0, 0);
                    //else
                    //    keyErrorFail();
                    //tenSecInt.setChecked(false);
                    //twentySecInt.setChecked(false);
                    //sixtySecInt.setChecked(false);
                    secondsForInt = checkForInt((EditText) customTimeView);
                    lapseWasSet = true;
                    //tenSecInt.setEnabled(true);
                    //twentySecInt.setEnabled(true);
                    //sixtySecInt.setEnabled(true);
                }
            }
        });

        ////////////////////////////////////////////// BUTTON SECTION /////////////////////////////////////////////////////
        timeLapseSubmit = (Button) findViewById(R.id.timeLapseSubmitButton);
        timeLapseSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customLapseWasFocus) {
                    customInt.clearFocus();
                    hideSoftKeyboard();
                }
                submitTimeLapse();
            }
        });

        timeLapseCancel = (Button) findViewById(R.id.timeLapseCancelButton);
        timeLapseCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTimeLapse();
            }
        });
    }

    private void updateDateField(int fieldToUpdate) {
        String dateFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        long detectFutureTime = System.currentTimeMillis() - timeLapseCal.getTimeInMillis();
        if (fieldToUpdate == 1) {
            if (detectFutureTime < 0) {
                Toast.makeText(image_time_lapse.this, "You cannot choose future dates for the time lapse.", Toast.LENGTH_SHORT).show();
                fromDateField.setText(null);
                fromDay = 0;
                fromMonth = 0;
                fromYear = 0;
            } else {
                fromDateField.setText(sdf.format(timeLapseCal.getTime()));
            }
        } else {
            if (detectFutureTime < 0) {
                Toast.makeText(image_time_lapse.this, "You cannot choose future dates for the time lapse.", Toast.LENGTH_SHORT).show();
                toDateField.setText(null);
                toDay = 0;
                toMonth = 0;
                toYear = 0;
            } else {
                toDateField.setText(sdf.format(timeLapseCal.getTime()));
            }
        }
    }

	// The current version of the timelapse doesn't have a way to halt or cancel the progress, but would be a nice feature
	// to add in the future.
    private void submitTimeLapse() {
        final String fromDateStr;
        final String toDateStr;

        if (preCheckUserInput()) {
            if (toDateChecker - fromDateChecker < 0) {
                Toast.makeText(image_time_lapse.this, "\"From\" date must be earlier than the \"To\" date!", Toast.LENGTH_SHORT).show();
            } else {
                if (fromDay + fromMonth + fromYear == 0) {
                    Toast.makeText(image_time_lapse.this, "You must specify a \"from\" Date in order to time lapse", Toast.LENGTH_SHORT).show();
                } else if (toDay + toMonth + toYear == 0) {
                    Toast.makeText(image_time_lapse.this, "You must specify a valid \"to\" in order to time lapse", Toast.LENGTH_SHORT).show();
                } else {
                    fromDateStr = fromMonth + "/" + fromDay + "/" + fromYear;
                    toDateStr = toMonth + "/" + toDay + "/" + toYear;
                    if (secondsForInt > 120) {
                        new AlertDialog.Builder(image_time_lapse.this)
                                .setTitle("Warning")
                                .setMessage("You have specified a very long delay (more than 2 minutes). Once the time lapse begins "
                                        + "you will be unable to halt it before it completes. Are you sure "
                                        + "you wish to perform the time lapse with this interval?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(image_time_lapse.this, "Delay confirmed.", Toast.LENGTH_SHORT).show();
                                        sendTimeLapse(fromDateStr, toDateStr);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(image_time_lapse.this, "Delay reset.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .show();
                    } else {
                        sendTimeLapse(fromDateStr, toDateStr);
                    }
                }
            }
        }
    }

    private void cancelTimeLapse() {
        // clear data when ready here
        finish();
    }


	// Sends the keywords and settings for the timelaps command to the mirror side software
    private void sendTimeLapse(String tempFrom, String tempTo) {
        String commandToSend;
        Toast.makeText(image_time_lapse.this, "Time lapse run with:\nTime Interval: " + secondsForInt
                + "\nFor date range:\n" + "From: " + tempFrom
                + "\nTo: " + tempTo, Toast.LENGTH_LONG).show();

        commandToSend = "timeLapse" + " " + fromYear + "-" + fromMonth + "-" + fromDay + " "
                + toYear + "-" + toMonth + "-" + toDay + " " + secondsForInt;
        new server_interface(image_time_lapse.this).execute(commandToSend);
    }

	// A validation check for integers
    private int checkForInt(TextView inputField) {
        try {
            int fieldToCheck;
            fieldToCheck = Integer.parseInt(inputField.getText().toString());
            return fieldToCheck;
        } catch (NumberFormatException notNum) {
            return -1;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            customTimeSet = false;
            lapseWasSet = true;
            customInt.setText(null);
            customInt.clearFocus();
            customLapseWasFocus = false;
            secondsForInt = 0;
            hideSoftKeyboard();
            if (buttonView.getId() == R.id.tenSecondTimeLapseButton) {
                twentySecInt.setChecked(false);
                sixtySecInt.setChecked(false);
                secondsForInt = 10;
            }
            if (buttonView.getId() == R.id.twentySecondTimeLapseButton) {
                tenSecInt.setChecked(false);
                sixtySecInt.setChecked(false);
                secondsForInt = 20;
            }
            if (buttonView.getId() == R.id.sixtySecondTimeLapseButton) {
                tenSecInt.setChecked(false);
                twentySecInt.setChecked(false);
                secondsForInt = 60;
            }
        }
    }

	// If anything strange happenms with the virtual keyboard catch it
    private void keyErrorFail() {
        Toast.makeText(image_time_lapse.this, "There appears to an error with the keyboard."
                + " You may need to restart the app.", Toast.LENGTH_SHORT).show();
    }

	// Some user input validation for time lapse values
    private boolean preCheckUserInput() {
        boolean validInput;

        if (lapseWasSet) {
            if (customTimeSet) {
                secondsForInt = checkForInt(customInt);
            }
            validInput = checkInput();
        } else {
            Toast.makeText(image_time_lapse.this, "You must choose a time delay to time lapse!"
                    , Toast.LENGTH_SHORT).show();
            validInput = false;
        }

        return validInput;
    }

	// Some uer input validation for timelapse length
    private boolean checkInput() {
        boolean valid = false;

        if (lapseWasSet) {
            if (secondsForInt == -1) {
                lapseWasSet = false;
                customInt.setError("An invalid custom time was set. Please try again");
            } else if (secondsForInt < 5) {
                customInt.setError("You must set a time lapse value of at least 5 seconds");
            } else {
                valid = true;
            }
        }
        if (!valid) {
            lapseWasSet = false;
            customInt.setText(null);
            customInt.requestFocus();
        }
        return valid;
    }

	// Don't want to see the virtual keyboard unless it's useful for the current field
    private void hideSoftKeyboard() {
        if (getCurrentFocus() != null && getCurrentFocus() instanceof EditText) {
            InputMethodManager imm = (InputMethodManager) getSystemService(image_time_lapse.INPUT_METHOD_SERVICE);
            if (imm != null)
                imm.hideSoftInputFromWindow(customInt.getWindowToken(), 0);
        }
    }
}
