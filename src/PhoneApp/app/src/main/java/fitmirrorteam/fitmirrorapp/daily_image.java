package fitmirrorteam.fitmirrorapp;

/*
 * The daily image option provides a means for users to provide a settings for working with the camera on the hardware
 * in order to take an image. The image is automatically stored in the current calendar date. If another picture is taken
 * and kept it will overwrite the existing one. 
 */

import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class daily_image extends SubActivityTemplate implements CompoundButton.OnCheckedChangeListener {

    private RadioButton fiveSecDly;
    private RadioButton tenSecDly;
    private RadioButton noDly;
    private EditText customDly;
    private int secondsToDelay;
    private boolean dlyWasSet = false;
    private boolean customTimeSet = false;
    private boolean customDlyWasFocus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button dailyImageCancel;
        Button dailyImageCalibrate;
        final Button dailyImageBegin;
        secondsToDelay = 0;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ////////////////////////////////////////////// RADIO BUTTON SECTION //////////////////////////////////////////////
        fiveSecDly = (RadioButton) findViewById(R.id.fiveSecondButton);
        fiveSecDly.setOnCheckedChangeListener(this);
        tenSecDly = (RadioButton) findViewById(R.id.tenSecondButton);
        tenSecDly.setOnCheckedChangeListener(this);
        noDly = (RadioButton) findViewById(R.id.noDelayButton);
        noDly.setOnCheckedChangeListener(this);

        ////////////////////////////////////////////// CUSTOM DELAY FIELD SECTION ////////////////////////////////////////
        customDly = (EditText) findViewById(R.id.customDelayEntryBox);
        customDly.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager keyIn = (InputMethodManager) daily_image.this.getSystemService(UserLogin.INPUT_METHOD_SERVICE);
                    if (keyIn != null)
                        keyIn.toggleSoftInput(0, 0);
                    else
                        keyErrorFail();
                    secondsToDelay = checkForInt(customDly);
                    fiveSecDly.setEnabled(true);
                    tenSecDly.setEnabled(true);
                    noDly.setEnabled(true);
                    handled = true;
                }
                return handled;
            }
        });

        customDly.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View customTimeView, boolean hasFocus) {
                if(hasFocus) {
                    fiveSecDly.setChecked(false);
                    tenSecDly.setChecked(false);
                    noDly.setChecked(false);
                    fiveSecDly.setEnabled(false);
                    tenSecDly.setEnabled(false);
                    noDly.setEnabled(false);
                    dlyWasSet = true;
                    customTimeSet = true;
                    customDlyWasFocus = true;
                }
                if (!hasFocus) {
                    //InputMethodManager keyIn = (InputMethodManager) daily_image.this.getSystemService(UserLogin.INPUT_METHOD_SERVICE);
                    //if (keyIn != null)
                    //    keyIn.toggleSoftInput(0, 0);
                    //else
                    //    keyErrorFail();
                    secondsToDelay = checkForInt((EditText) customTimeView);
                }
            }
        });

        //////////////////////////////////////////////// BUTTON SECTION ////////////////////////////////////////////////////
        dailyImageBegin = (Button) findViewById(R.id.dailyImageBeginButton);
        dailyImageBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(customDlyWasFocus) {
                    customDly.clearFocus();
                    hideSoftKeyboard();
                }
                beginTakeImage();
            }
        });

        dailyImageCancel = (Button) findViewById(R.id.dailyImageCancelButton);
        dailyImageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelDailyImage();
            }
        });

        dailyImageCalibrate = (Button) findViewById(R.id.dailyImageCalibrateButton);
        dailyImageCalibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calibrateDailyImage();
            }
        });
    }

	/*
	 * With the current camera hardware there was no convenient way to short circuit the timer. The user would
	 * have to use the phone interface to indiciate they want take a picture which would defeat the purpose of 
	 * taking the time to pose. This process could be enhanced in the future if an upgraded camera were used that had it's 
	 * own remote for example.
	 * The 3 minutes was arbitrarely chosen as a "soft max" time. The mirror side programming could be enhanced to display
	 * the countdown of the timer for longer delays.
	 */
    private void beginTakeImage() {
        if (preCheckUserInput()) {
            if (secondsToDelay > 180) {
                new AlertDialog.Builder(daily_image.this)
                        .setTitle("Warning")
                        .setMessage("You have specified a delay longer than 3 minutes. Be aware that once you submit "
                                + "the time delay, you will not be able to take the image earlier. Are you sure you "
                                + "wish to use this delay time?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(daily_image.this, "Delay confirmed.", Toast.LENGTH_SHORT).show();
                                sendTimeDelay();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(daily_image.this, "Delay reset.", Toast.LENGTH_SHORT).show();
                                customDly.setText(null);
                                secondsToDelay = 0;
                                dlyWasSet = false;
                            }
                        })
                        .show();
            } else {
                sendTimeDelay();
            }
        }
    }

	/* Sends a keyword command to the camera control script mirror side to put the camera into video mode to allow a user
	 * to get feedback for physical positioning.
	 */
    private void calibrateDailyImage() {

        new server_interface(daily_image.this).execute("calibrate");

        new AlertDialog.Builder(daily_image.this)
                .setTitle("Calibrating...")
                .setMessage("When you are finished with the calibration press OK to stop the video feed.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        new server_interface(daily_image.this).execute("stopCalibrate");
                    }
                })
                .show();
    }

    private void cancelDailyImage() {
        // Add clear content section as needed        
		finish();
    }

    private void sendTimeDelay() {
        if(secondsToDelay == 0) {
            new server_interface(daily_image.this).execute("takePic 0");
        }else {
            new server_interface(daily_image.this).execute("takePic " + secondsToDelay);

            AlertDialog.Builder delayNotice = new AlertDialog.Builder(daily_image.this);
            delayNotice.setTitle("Waiting to take image...");
            delayNotice.setMessage("If you need to stop early press OK. This will cancel taking the image");
            delayNotice.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(daily_image.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    new server_interface(daily_image.this).execute("stopCalibrate");
                }
            });
            delayNotice.setCancelable(true);

            final AlertDialog delayDialog = delayNotice.create();
            delayDialog.show();

            final Timer timeForDialog = new Timer();
            timeForDialog.schedule(new TimerTask() {
                public void run() {
                    if(delayDialog.isShowing())
                        delayDialog.dismiss(); // when the task active then close the dialog
                    timeForDialog.cancel(); // also just atop the timer thread, otherwise, you may receive a crash report
                }
            }, secondsToDelay*1000); // after .2 second (or 200 milliseconds), the task will be active.
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            customDly.setText(null);
            customTimeSet = false;
            hideSoftKeyboard();
            if(customDlyWasFocus) {
                customDly.clearFocus();
                customDly.setError(null);
            }
            if (buttonView.getId() == R.id.fiveSecondButton) {
                tenSecDly.setChecked(false);
                noDly.setChecked(false);
                secondsToDelay = 5;
                dlyWasSet = true;
            }
            if (buttonView.getId() == R.id.tenSecondButton) {
                fiveSecDly.setChecked(false);
                noDly.setChecked(false);
                secondsToDelay = 10;
                dlyWasSet = true;
            }
            if (buttonView.getId() == R.id.noDelayButton) {
                fiveSecDly.setChecked(false);
                tenSecDly.setChecked(false);
                secondsToDelay = 0;
                dlyWasSet = true;
            }
        }
    }

	// Verify the user has entered a meaningful input for the delay time.
    private int checkForInt(TextView inputField) {
        try {
            int fieldToCheck;
            fieldToCheck = Integer.parseInt(inputField.getText().toString());
            return fieldToCheck;
        } catch (NumberFormatException notNum) {
            return -1;
        }
    }

	/* Verify user input for picture delays. Notify user if there is an issue.
	 * The app MUST have a delay time specified prior to taking a picture. This is because
	 * the delay is currently sent to a python script that controls the camera on the mirror side that requires a value.
	 * Making the user choose is also a way to be sure they know what to anticipate to prepare for the picture.
	 * The custom time will override the choice of a default timer.
	 */
    private boolean preCheckUserInput() {
        boolean validInput;

        if (dlyWasSet) {
            if (customTimeSet) {
                secondsToDelay = checkForInt(customDly);
            }
            validInput = checkInput();
        } else {
            Toast.makeText(daily_image.this, "You must choose a delay in order to take a picture!", Toast.LENGTH_SHORT).show();
            validInput = false;
        }
        return validInput;
    }

    private boolean checkInput() {
        boolean valid = false;

        if (dlyWasSet) {
            if (secondsToDelay == -1) {
                dlyWasSet = false;
                customDly.setError("An invalid custom time was set. Please try again");
            } else {
                valid = true;
            }
        }
        if (!valid) {
            dlyWasSet = false;
            customDly.setText(null);
            customDly.requestFocus();
        }
        return valid;
    }
	
	// Catch all for any unexpected keyboard interface issues that may come up
	private void keyErrorFail() {
        Toast.makeText(daily_image.this, "There appears to an error with the keyboard."
                + " You may need to restart the app.", Toast.LENGTH_SHORT).show();
    }

	// Don't want to see the virtual keyboard unless it's useful for the user
    private void hideSoftKeyboard(){
        if(getCurrentFocus()!=null && getCurrentFocus() instanceof EditText){
            InputMethodManager imm = (InputMethodManager)getSystemService(daily_image.INPUT_METHOD_SERVICE);
            if(imm != null)
                imm.hideSoftInputFromWindow(customDly.getWindowToken(), 0);
        }
    }
}
