package fitmirrorteam.fitmirrorapp;

/*
 * Handles user verification and access to specific user data as stored in the mirror side database.
 * In the current alpha version of the software there is no actual user verification checks. The database 
 * has a tester account that it automatically connects to when it receives a login request. All of the functionality 
 * should be there to implement specific user accounts within the databse (the start is there anyway).
 *
 * Because of that fact, the only requirement to access the tester account from the mobile device (providing
 * all of the components are properly set up and can communicate) is to enter any user name and password that
 * passes the validation checks in the mobile app (user name of 2 or more characters and password of 4 or
 * more characters). 
 */

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.view.KeyEvent;
import android.widget.Toast;

public class UserLogin extends AppCompatActivity {

    private String userName;
    private String pass;
    private EditText nameIn;
    private EditText passIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button loginSubmit;
        Button forgotPass;
        Button forgotUserName;
        Button newUserSubmit;
        userName = "";
        pass = "";

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        newUserSubmit = (Button) findViewById(R.id.NewUserButton);
        newUserSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchNewUser();
            }
        });

        // Section to handle when submit button is clicked
        loginSubmit = (Button) findViewById(R.id.submitButton_userLogin);
        loginSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCommandMenu();
            }
        });

        //////////////////////////////////////// USERNAME FIELD SECTION ////////////////////////////////////////////
        nameIn = (EditText) findViewById(R.id.userNameEntry);
        nameIn.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                        InputMethodManager keyIn = (InputMethodManager)UserLogin.this.getSystemService(UserLogin.INPUT_METHOD_SERVICE);
                        if(keyIn != null)
                            keyIn.toggleSoftInput(0,0);
                        else
                            keyErrorFail();
                        userName = textView.getText().toString();
                        handled = true;
                }
                return handled;
            }
        });

       nameIn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View usernameView, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager keyIn = (InputMethodManager) UserLogin.this.getSystemService(UserLogin.INPUT_METHOD_SERVICE);
                    if (keyIn != null)
                        keyIn.toggleSoftInput(0, 0);
                    else
                        keyErrorFail();
                    userName = ((EditText) usernameView).getText().toString();
                }
            }
        });

        ///////////////////////////////////////////// PASSWORD FIELD SECTION ////////////////////////////////////////////
        passIn = (EditText) findViewById(R.id.userPassEntry);
        passIn.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                        InputMethodManager keyIn = (InputMethodManager)UserLogin.this.getSystemService(UserLogin.INPUT_METHOD_SERVICE);
                        if(keyIn != null)
                            keyIn.toggleSoftInput(0,0);
                        else
                            keyErrorFail();
                        pass = textView.getText().toString();
                        handled = true;
                }
                return handled;
            }
        });

        passIn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View passwordView, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager keyIn = (InputMethodManager) UserLogin.this.getSystemService(UserLogin.INPUT_METHOD_SERVICE);
                    if (keyIn != null)
                        keyIn.toggleSoftInput(0, 0);
                    else
                        keyErrorFail();
                    pass = ((EditText) passwordView).getText().toString();
                }
            }
        });

        ////////////////////////////////////////////// BUTTON SECTION //////////////////////////////////////////////
        //Section to handle forgotten username
        forgotUserName = (Button) findViewById(R.id.forgotUsername);
        forgotUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserLogin.this, "The forgot username was pressed", Toast.LENGTH_SHORT).show();
            }
        });

        // Section to handle forgotten password
        forgotPass = (Button) findViewById(R.id.forgotPassword);
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UserLogin.this, "The forgot password button was pressed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return true;
    }

    /* Handle action bar item clicks here. The action bar will automatically handle clicks
	 * on the Home/Up button, as long as you specify a parent activity in AndroidManifest.xml.
	 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_settings) {
            Toast.makeText(this, "Settings menu item clicked", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.menu_calibrate) {
            Toast.makeText(this, "calibrate menu item clicked", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.menu_exit) {
            AlertDialog.Builder confirmLogout = new AlertDialog.Builder(this);
            confirmLogout.setMessage("Confirm exit:");
            confirmLogout.setCancelable(false);
            //final Intent rtrnLogin = new Intent(this, UserLogin.class);

            confirmLogout.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    moveTaskToBack(true);
                }
            });
            confirmLogout.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog logoutMssg = confirmLogout.create();
            logoutMssg.show();
        }

        return super.onOptionsItemSelected(item);
    }

	// Show the main app menu. Currently includes a confirmation of username and password that was entered
	// that can be removed as desired (was mainly used for testing purposes).
    private void launchCommandMenu() {
        preCheckUserInput();
        if (preCheckUserInput()) {
            Toast.makeText(UserLogin.this, "The entered username was: " + userName + "\n"
                    + "The entered password was: " + pass + "\n", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, commandMenu.class);
            
			// Clear variables and fields for security before moving to next screen
            nameIn.setText(null);
            passIn.setText(null);
            userName = "";
            pass = "";
            startActivity(intent);
        }
    }

	// Currently side stepping the user verfication check as the only usesr on the mirror side database that
	// implemented at the momenbt is the test account
    private void launchNewUser() {
        //if(verifyUser()) {
            Intent moveToNewUser = new Intent(this, new_user.class);
            nameIn.setText(null);
            passIn.setText(null);
            startActivity(moveToNewUser);
        //} else {
        //    Toast.makeText(UserLogin.this,"The username and password entered was invalid!",Toast.LENGTH_SHORT).show();
        //}
    }

	// Returns whether the user was found and the proper password was given for the account
	// The program will not continue without a successfull verification (once implemented).
    public boolean verifyUser() {
        boolean verified = false;

        String commandToSend = "userLogin " + userName + " " + pass;
        String verifier = "DO";
        new server_interface(UserLogin.this).execute(commandToSend, verifier);

        return verified;
    }

	// Catch any strange virtual keybopard issues
    private void keyErrorFail() {
        Toast.makeText(UserLogin.this, "There appears to an error with the keyboard."
                + " You may need to restart the app.", Toast.LENGTH_SHORT).show();
    }

	// Validate user input values. Since username and password have no type restrictions
	// this really only makes sure that whatever is entered is read as a string.
    private boolean preCheckUserInput() {
        boolean validInput;

        userName = nameIn.getText().toString();
        pass = passIn.getText().toString();

        validInput = checkInput();
        return validInput;
    }

    private void updateFocus(int viewToFocus) {
        nameIn.clearFocus();
        passIn.clearFocus();

        switch (viewToFocus) {
            case 1:
                nameIn.requestFocus();
                break;
            case 2:
                passIn.requestFocus();
                break;
            default:
                Toast.makeText(UserLogin.this, "Field focus error...", Toast.LENGTH_SHORT).show();
                break;
        }
    }

	// Validate user input meets the username and password minimum requirements.
	// This should be much more robust unless the end product will only be for personal use.
    private boolean checkInput() {
        boolean valid = false;

        if (pass.length() < 4) {
            passIn.setError("The password length must be at least 4 characters in length!");
            updateFocus(2);
        } else if (userName.length() < 2) {
            nameIn.setError("The username must be at least 2 characters in length");
            updateFocus(1);
        } else {
            valid = true;
        }
        return valid;
    }
}
