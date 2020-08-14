package fitmirrorteam.fitmirrorapp;

/*
 * The health data entry screen is the main method a user has to manually enter data into the mirror database.
 * Devices connected to the mirror can enter data for the current date as the measurement is taken, but any
 * past dates or info that does not have a means to gather through devices needs to be entered manually.
 * Currently the only device that can auto collect data for the user is a bathroom scale to get daily weight. This
 * data collection happens independently of this phone application.
 */

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class enterHealthInfo extends SubActivityTemplate {

    private EditText weightIn;
    private EditText sysIn;
    private EditText diaIn;
    private EditText tempIn;
    private EditText pulseIn;
    private EditText bmiIn;
    private Calendar enterHealthCal = Calendar.getInstance();
    EditText dateField;
    DatePickerDialog.OnDateSetListener dayOfData;
    private int weight;
    private int systolic;
    private int diastolic;
    private int temperature;
    private int pulse;
    private int bmi;
    private int enterHealthDay;
    private int enterHealthMonth;
    private int enterHealthYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_health_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button enterHealthCancel;
        Button enterHealthSubmit;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ///////////////////////////////////////////////// WEIGHT FIELD SECTION ////////////////////////////////////////////////
        // Get user entered weight
        weightIn = (EditText) findViewById(R.id.weightEntryBox);
        weightIn.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager keyIn = (InputMethodManager) enterHealthInfo.this.getSystemService(UserLogin.INPUT_METHOD_SERVICE);
                    if (keyIn != null)
                        keyIn.toggleSoftInput(0, 0);
                    else
                        keyErrorFail();
                    weight = checkForInt(weightIn);
                    handled = true;
                }
                return handled;
            }
        });

        weightIn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View weightView, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager keyIn = (InputMethodManager) enterHealthInfo.this.getSystemService(UserLogin.INPUT_METHOD_SERVICE);
                    if (keyIn != null)
                        keyIn.toggleSoftInput(0, 0);
                    else
                        keyErrorFail();
                    weight = checkForInt((EditText) weightView);
                }
            }
        });

        ////////////////////////////////////////////////// SYSTOLIC FIELD SECTION ///////////////////////////////////////////
        // Get user entered systolic value of BP
        sysIn = (EditText) findViewById(R.id.systolicEntryBox);
        sysIn.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                        InputMethodManager keyIn = (InputMethodManager) enterHealthInfo.this.getSystemService(UserLogin.INPUT_METHOD_SERVICE);
                        if (keyIn != null)
                            keyIn.toggleSoftInput(0, 0);
                        else
                            keyErrorFail();
                        systolic = checkForInt(sysIn);
                        handled = true;
                }
                return handled;
            }
        });

        sysIn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View sysView, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager keyIn = (InputMethodManager) enterHealthInfo.this.getSystemService(UserLogin.INPUT_METHOD_SERVICE);
                    if (keyIn != null)
                        keyIn.toggleSoftInput(0, 0);
                    else
                        keyErrorFail();
                    systolic = checkForInt((EditText) sysView);
                }
            }
        });


        //////////////////////////////////////////////// DIASTOLIC FIELD SECTION ///////////////////////////////////////////
        // Get user entered diastolic value of BP
        diaIn = (EditText) findViewById(R.id.diastolicEntryBox);
        diaIn.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                        InputMethodManager keyIn = (InputMethodManager) enterHealthInfo.this.getSystemService(UserLogin.INPUT_METHOD_SERVICE);
                        if (keyIn != null)
                            keyIn.toggleSoftInput(0, 0);
                        else
                            keyErrorFail();
                        diastolic = checkForInt(diaIn);
                        handled = true;
                }
                return handled;
            }
        });

        diaIn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View diaView, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager keyIn = (InputMethodManager) enterHealthInfo.this.getSystemService(UserLogin.INPUT_METHOD_SERVICE);
                    if (keyIn != null)
                        keyIn.toggleSoftInput(0, 0);
                    else
                        keyErrorFail();
                    diastolic = checkForInt((EditText) diaView);
                }
            }
        });

        /////////////////////////////////////////// TEMPERATURE FIELD SECTION /////////////////////////////////////////////
        // Get user entered temperature
        tempIn = (EditText) findViewById(R.id.temperatureEntryBox);
        tempIn.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                        InputMethodManager keyIn = (InputMethodManager) enterHealthInfo.this.getSystemService(UserLogin.INPUT_METHOD_SERVICE);
                        if (keyIn != null)
                            keyIn.toggleSoftInput(0, 0);
                        else
                            keyErrorFail();
                        temperature = checkForInt(tempIn);
                        handled = true;
                }
                return handled;
            }
        });

        tempIn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View tempView, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager keyIn = (InputMethodManager) enterHealthInfo.this.getSystemService(UserLogin.INPUT_METHOD_SERVICE);
                    if (keyIn != null)
                        keyIn.toggleSoftInput(0, 0);
                    else
                        keyErrorFail();
                    temperature = checkForInt((EditText) tempView);
                }
            }
        });

        //////////////////////////////////////////// PULSE FIELD SECTION /////////////////////////////////////////////
        // Get user entered pulse info
        pulseIn = (EditText) findViewById(R.id.heartRateEntryBox);
        pulseIn.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                        InputMethodManager keyIn = (InputMethodManager) enterHealthInfo.this.getSystemService(UserLogin.INPUT_METHOD_SERVICE);
                        if (keyIn != null)
                            keyIn.toggleSoftInput(0, 0);
                        else
                            keyErrorFail();
                        pulse = checkForInt(pulseIn);
                        handled = true;
                }
                return handled;
            }
        });

        pulseIn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View pulseView, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager keyIn = (InputMethodManager) enterHealthInfo.this.getSystemService(UserLogin.INPUT_METHOD_SERVICE);
                    if (keyIn != null)
                        keyIn.toggleSoftInput(0, 0);
                    else
                        keyErrorFail();
                    pulse = checkForInt((EditText) pulseView);
                }
            }
        });

        ///////////////////////////////////////////////// BMI FIELD SECTION ////////////////////////////////////////////////
        // Get user entered BMI info
        bmiIn = (EditText) findViewById(R.id.bmiHeaderBox);
        bmiIn.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                        InputMethodManager keyIn = (InputMethodManager) enterHealthInfo.this.getSystemService(UserLogin.INPUT_METHOD_SERVICE);
                        if (keyIn != null)
                            keyIn.toggleSoftInput(0, 0);
                        else
                            keyErrorFail();
                        bmi = checkForInt(bmiIn);
                        handled = true;
                }
                return handled;
            }
        });

        bmiIn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View bmiView, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager keyIn = (InputMethodManager) enterHealthInfo.this.getSystemService(UserLogin.INPUT_METHOD_SERVICE);
                    if (keyIn != null)
                        keyIn.toggleSoftInput(0, 0);
                    else
                        keyErrorFail();
                    bmi = checkForInt((EditText) bmiView);
                }
            }
        });

        ///////////////////////////////////////////// DATE FIELD SECTION ////////////////////////////////////////////////
        // Get the user date information
        dateField = (EditText) findViewById(R.id.dateEntryBox);
        dayOfData = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                enterHealthCal.set(Calendar.YEAR, year);
                enterHealthCal.set(Calendar.MONTH, month);
                enterHealthCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                enterHealthDay = dayOfMonth;
                enterHealthMonth = month + 1;
                enterHealthYear = year;
                updateDateField();
            }
        };

        // Call the date picker
        dateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(enterHealthInfo.this, dayOfData, enterHealthCal.get(Calendar.YEAR),
                        enterHealthCal.get(Calendar.MONTH), enterHealthCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ///////////////////////////////////////////////////// BUTTON SECTION /////////////////////////////////////////////
        enterHealthSubmit = (Button) findViewById(R.id.submitButton_enterHealth);
        enterHealthSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitEnteredHealthInfo();
            }
        });

        enterHealthCancel = (Button) findViewById(R.id.cancelButton_enterHealth);
        enterHealthCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelEnterHealth();
            }
        });

        /////////////////////////// POPULATE DATE FIELD IF CALLED FROM MANAGE DATA ACTIVITY //////////////////
        if(getIntent().hasExtra("day")) {
            Bundle retrievedDate = getIntent().getExtras();
            enterHealthDay = retrievedDate.getInt("day");
            enterHealthMonth =  retrievedDate.getInt("month");
            enterHealthYear = retrievedDate.getInt("year");
            String dateSet = enterHealthMonth + "-" + enterHealthDay + "-" + enterHealthYear;
            Date date;
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
                SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                date = inputFormat.parse(dateSet);
                String dateOut = outputFormat.format(date);
                dateField.setText(dateOut);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

	// Pass the health information to the mirror
    private void submitEnteredHealthInfo() {
        if(preCheckUserInput()) {
            if (weight + systolic + diastolic + temperature + pulse + bmi == 0) {
                Toast.makeText(enterHealthInfo.this, "Cannot submit, at least one field must have data!", Toast.LENGTH_LONG).show();
            } else if (enterHealthYear + enterHealthDay + enterHealthMonth == 0) {
                Toast.makeText(enterHealthInfo.this, "You must choose a date in which to place your health info!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(enterHealthInfo.this, "The info entered was:\nweight: " + weight
                        + "\nBP-systolic: " + systolic + "\nBP-diastolic:" + diastolic
                        + "\ntemperature: " + temperature + "\nheart rate: " + pulse
                        + "\nBMI: " + bmi + "\nfor the date: " + enterHealthMonth + "/"
                        + enterHealthDay + "/" + enterHealthYear, Toast.LENGTH_SHORT).show();
                String commandToSend = "newHealth " + weight + " " + systolic + " " + diastolic + " "
                        + pulse + " " + temperature + " " + bmi + " "
                        + enterHealthYear + "-" + enterHealthMonth + "-" + enterHealthDay;
                new server_interface(enterHealthInfo.this).execute(commandToSend);
            }
        }
    }

    private void updateDateField() {
        String dateFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        long detectFutureTime = System.currentTimeMillis() - enterHealthCal.getTimeInMillis();
        if (detectFutureTime < 0) {
            Toast.makeText(enterHealthInfo.this, "You cannot choose a future date!", Toast.LENGTH_SHORT).show();
            dateField.setText(null);
            enterHealthDay = 0;
            enterHealthMonth = 0;
            enterHealthYear = 0;
        } else {
            dateField.setText(sdf.format(enterHealthCal.getTime()));
        }
    }

    private void cancelEnterHealth() {
        // clear contents here when ready
        finish();
    }

    private int checkForInt(TextView inputField) {
        try {
            int fieldToCheck;
            fieldToCheck = Integer.parseInt(inputField.getText().toString());
            return fieldToCheck;
        } catch (NumberFormatException notNum) {
            return 0;
        }
    }

	// Catch any strangeness that happens with the virtual keyboard
    private void keyErrorFail() {
        Toast.makeText(enterHealthInfo.this, "There appears to an error with the keyboard."
                + " You may need to restart the app.", Toast.LENGTH_SHORT).show();
    }

	// Validate user input for proper value type for the given field. Keep a log for
	// debugging purposes
    private boolean preCheckUserInput() {
        boolean validInput;
        String temp;
        try {
            temp = weightIn.getText().toString();
            Log.i("weightCheck", "About to check weight: temp is " + temp);
            if (!(temp.matches("^-?\\d+$")) || weightIn.getText() == null) {
                weight = 0;
            } else {
                weight = Integer.parseInt(weightIn.getText().toString());
            }
            temp = sysIn.getText().toString();
            Log.i("sysCheck", "About to check systolic: temp is " + temp);
            if (!(temp.matches("^-?\\d+$")) || sysIn.getText() == null) {
                systolic = 0;
            } else {
                systolic = Integer.parseInt(sysIn.getText().toString());
            }
            temp = diaIn.getText().toString();
            Log.i("diaCheck", "About to check diastolic: temp is " + temp);
            if (!(temp.matches("^-?\\d+$")) || diaIn.getText() == null) {
                diastolic = 0;
            } else {
                diastolic = Integer.parseInt(diaIn.getText().toString());
            }
            temp = tempIn.getText().toString();
            Log.i("tempCheck", "About to check temperature: temp is " + temp);
            if (!(temp.matches("^-?\\d+$")) || tempIn.getText() == null) {
                temperature = 0;
            } else {
                temperature = Integer.parseInt(tempIn.getText().toString());
            }
            temp = pulseIn.getText().toString();
            Log.i("pulseCheck", "About to check pulse: temp is " + temp);
            if (!(temp.matches("^-?\\d+$")) || pulseIn.getText() == null) {
                pulse = 0;
            } else {
                pulse = Integer.parseInt(pulseIn.getText().toString());
            }
            temp = bmiIn.getText().toString();
            Log.i("bmiCheck", "About to check BMI: temp is " + temp);
            if (!(temp.matches("^-?\\d+$")) || bmiIn.getText() == null) {
                bmi = 0;
            } else {
                bmi = Integer.parseInt(bmiIn.getText().toString());
            }
            Log.i("Summary", "The values were " + weight + systolic + diastolic + temperature + pulse + bmi);
        } catch (NumberFormatException wasNotNum) {
            Toast.makeText(enterHealthInfo.this, "It appears one or more numeric fields are not valid. "
                    + "Please check all fields and try again", Toast.LENGTH_LONG).show();
            return false;
        }

        validInput = checkInput();
        return validInput;
    }

    private void updateFocus(int viewToFocus) {
        weightIn.clearFocus();
        sysIn.clearFocus();
        diaIn.clearFocus();
        tempIn.clearFocus();
        pulseIn.clearFocus();
        bmiIn.clearFocus();

        switch (viewToFocus) {
            case 1:
                weightIn.requestFocus();
                break;
            case 2:
                sysIn.requestFocus();
                break;
            case 3:
                diaIn.requestFocus();
                break;
            case 4:
                tempIn.requestFocus();
                break;
            case 5:
                pulseIn.requestFocus();
                break;
            case 6:
                bmiIn.requestFocus();
                break;
            default:
                Toast.makeText(enterHealthInfo.this, "Field focus error...", Toast.LENGTH_SHORT).show();
                break;
        }
    }

	// Validate user input for reasonable health values. These ranges were chosen as standard healthy medical ranges
	// for an adolescent up to an adult.
    private boolean checkInput() {
        boolean valid = false;

        if ((weight < 30 && weight != 0)) {
            weightIn.setError("The weight entered was too low. "
                    + "A valid value should be greater than 30lbs and assumes that the user is no "
                    + "younger than 6 years old.");
            updateFocus(1);
        } else if ((systolic < 90 && systolic != 0) || systolic > 180) {
            sysIn.setError("The systolic (upper #) BP value entered was invalid. "
                    + "A valid value is 90 to 180. If your actual value is outside this "
                    + "range you should seek medical assistance immediately");
            updateFocus(2);
        } else if ((diastolic < 60 && diastolic != 0) || diastolic > 110) {
            diaIn.setError("The diastolic (lower #) BP value entered was invalid. "
                    + "A valid value is 60 to 110. If your actual value is outside this "
                    + "range you should seek medical assistance immediately");
            updateFocus(3);
        } else if ((temperature < 95 && temperature != 0) || temperature > 104) {
            tempIn.setError("The temperature entered was invalid. A valid temperature is 95 to 104 Fahrenheit");
            updateFocus(4);
        } else if ((pulse < 20 && pulse != 0) || pulse > 200) {
            pulseIn.setError("The heart rate entered was invalid. A valid heart rate is 20 to 200");
            updateFocus(5);
        } else if ((bmi < 10 && bmi != 0) || bmi > 200) {
            bmiIn.setError("The BMI entered was invalid. BMI must be from 10 to 200");
            updateFocus(6);
        } else {
            valid = true;
        }
        return valid;
    }
}
