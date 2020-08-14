package fitmirrorteam.fitmirrorapp;

/*
 * Provides a means for a user to create a new account in the mirror side database. The current version does NOT
 * have a robust account handling method. Recovery is a manual process if some key user information is not recalled.
 * This class would need some careful work in the future if a more complete product is developed. A lot of sensitive data
 * can be stored in the program, including potentially nude pictures of an individual.
 * The current version of the software does not implement the accoutn fully on the mirror side and would need to be
 * completed there as well. The last state of the software is more less in an alpha testing stage.
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

public class new_user extends SubActivityTemplate implements CompoundButton.OnCheckedChangeListener {

    private EditText userNameIn;
    private EditText firstNameIn;
    private EditText lastNameIn;
    private EditText passwordIn;
    private EditText confirmPassIn;
    private EditText passHintIn;
    private EditText ageIn;
    private RadioButton maleBttn;
    private RadioButton femaleBttn;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String passwordConfirm;
    private String passwordHint;
    private int age;
    private int sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button newUserSubmit;
        Button newUserCancel;
        username = "";
        password = "";
        firstName = "";
        lastName = "";
        passwordConfirm = "";
        passwordHint = "";
        age = 0;
        sex = 0;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ///////////////////////////////// USER NAME FIELD SECTION //////////////////////////////////////////////////
        userNameIn = (EditText) findViewById(R.id.newUserUsernameEntryField);
        userNameIn.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager keyIn = (InputMethodManager) new_user.this.getSystemService(UserLogin.INPUT_METHOD_SERVICE);
                    if (keyIn != null)
                        keyIn.toggleSoftInput(0, 0);
                    else
                        keyErrorFail();
                    username = textView.getText().toString();
                    handled = true;
                }
                return handled;
            }
        });

        userNameIn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View nameView, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager keyIn = (InputMethodManager) new_user.this.getSystemService(UserLogin.INPUT_METHOD_SERVICE);
                    if (keyIn != null)
                        keyIn.toggleSoftInput(0, 0);
                    else
                        keyErrorFail();
                    username = ((EditText) nameView).getText().toString();
                }
            }
        });

        //////////////////////////////// FIRST NAME FIELD SECTION ///////////////////////////////////////////////////////
        firstNameIn = (EditText) findViewById(R.id.newUserFirstNameEntryField);
        firstNameIn.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager keyIn = (InputMethodManager) new_user.this.getSystemService(UserLogin.INPUT_METHOD_SERVICE);
                    if (keyIn != null)
                        keyIn.toggleSoftInput(0, 0);
                    else
                        keyErrorFail();
                    firstName = textView.getText().toString();
                    handled = true;
                }
                return handled;
            }
        });

        firstNameIn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View firstNameView, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager keyIn = (InputMethodManager) new_user.this.getSystemService(UserLogin.INPUT_METHOD_SERVICE);
                    if (keyIn != null)
                        keyIn.toggleSoftInput(0, 0);
                    else
                        keyErrorFail();
                    firstName = ((EditText) firstNameView).getText().toString();
                }
            }
        });

        //////////////////////////// LAST NAME FIELD SECTION ///////////////////////////////////////////////////////////
        lastNameIn = (EditText) findViewById(R.id.newUserLastNameEntryField);
        lastNameIn.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager keyIn = (InputMethodManager) new_user.this.getSystemService(UserLogin.INPUT_METHOD_SERVICE);
                    if (keyIn != null)
                        keyIn.toggleSoftInput(0, 0);
                    else
                        keyErrorFail();
                    lastName = textView.getText().toString();
                    handled = true;
                }
                return handled;
            }
        });

        lastNameIn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View lastNameView, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager keyIn = (InputMethodManager) new_user.this.getSystemService(UserLogin.INPUT_METHOD_SERVICE);
                    if (keyIn != null)
                        keyIn.toggleSoftInput(0, 0);
                    else
                        keyErrorFail();
                    lastName = ((EditText) lastNameView).getText().toString();
                }
            }
        });

        /////////////////////////////////// PASSWORD FIELD SECTION //////////////////////////////////////////////////////
        passwordIn = (EditText) findViewById(R.id.newUserPasswordEntryField);
        passwordIn.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager keyIn = (InputMethodManager) new_user.this.getSystemService(UserLogin.INPUT_METHOD_SERVICE);
                    if (keyIn != null)
                        keyIn.toggleSoftInput(0, 0);
                    else
                        keyErrorFail();
                    password = textView.getText().toString();
                    handled = true;
                }
                return handled;
            }
        });

        passwordIn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View passwordView, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager keyIn = (InputMethodManager) new_user.this.getSystemService(UserLogin.INPUT_METHOD_SERVICE);
                    if (keyIn != null)
                        keyIn.toggleSoftInput(0, 0);
                    else
                        keyErrorFail();
                    password = ((EditText) passwordView).getText().toString();
                }
            }
        });

        ////////////////////////////////// PASSWORD CONFIRM FIELD SECTION ////////////////////////////////////////////
        confirmPassIn = (EditText) findViewById(R.id.newUserConfirmPasswordEntryField);
        confirmPassIn.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager keyIn = (InputMethodManager) new_user.this.getSystemService(UserLogin.INPUT_METHOD_SERVICE);
                    if (keyIn != null)
                        keyIn.toggleSoftInput(0, 0);
                    else
                        keyErrorFail();
                    passwordConfirm = textView.getText().toString();
                    handled = true;
                }
                return handled;
            }
        });

        confirmPassIn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View confirmPassView, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager keyIn = (InputMethodManager) new_user.this.getSystemService(UserLogin.INPUT_METHOD_SERVICE);
                    if (keyIn != null)
                        keyIn.toggleSoftInput(0, 0);
                    else
                        keyErrorFail();
                    passwordConfirm = ((EditText) confirmPassView).getText().toString();
                }
            }
        });

        /////////////////////////////////////// PASSWORD HINT FIELD SECTION ////////////////////////////////////////////
        passHintIn = (EditText) findViewById(R.id.newUserPasswordHintEntryField);
        passHintIn.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager keyIn = (InputMethodManager) new_user.this.getSystemService(UserLogin.INPUT_METHOD_SERVICE);
                    if (keyIn != null)
                        keyIn.toggleSoftInput(0, 0);
                    else
                        keyErrorFail();
                    passwordHint = textView.getText().toString();
                    handled = true;
                }
                return handled;
            }
        });

        passHintIn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View passHintView, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager keyIn = (InputMethodManager) new_user.this.getSystemService(UserLogin.INPUT_METHOD_SERVICE);
                    if (keyIn != null)
                        keyIn.toggleSoftInput(0, 0);
                    else
                        keyErrorFail();
                    passwordHint = ((EditText) passHintView).getText().toString();
                }
            }
        });


        /////////////////////////////////////// AGE FIELD SECTION ////////////////////////////////////////////////////
        ageIn = (EditText) findViewById(R.id.newUserAgeEntryField);
        ageIn.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {
                    try {
                        InputMethodManager keyIn = (InputMethodManager) new_user.this.getSystemService(UserLogin.INPUT_METHOD_SERVICE);
                        if (keyIn != null)
                            keyIn.toggleSoftInput(0, 0);
                        else
                            keyErrorFail();
                        age = Integer.parseInt(ageIn.getText().toString());
                        handled = true;
                    } catch (NumberFormatException numWasBlank) {
                        age = 0;
                        Toast.makeText(new_user.this, "There was an error with getting the age. "
                                + "Please try again.", Toast.LENGTH_SHORT).show();
                        ageIn.setText(null);
                    }
                }
                return handled;
            }
        });

        ageIn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View ageView, boolean hasFocus) {
                if (!hasFocus) {
                    try {
                        InputMethodManager keyIn = (InputMethodManager) new_user.this.getSystemService(UserLogin.INPUT_METHOD_SERVICE);
                        if (keyIn != null)
                            keyIn.toggleSoftInput(0, 0);
                        else
                            keyErrorFail();
                        age = Integer.parseInt(ageIn.getText().toString());
                    } catch (NumberFormatException numWasBlank) {
                        age = 0;
                        Toast.makeText(new_user.this, "There was an error with getting the age. "
                                + "Please try again.", Toast.LENGTH_SHORT).show();
                        ageIn.setText(null);
                    }
                }
            }
        });

        ////////////////////////////////////////// BUTTON SECTION ///////////////////////////////////////////////////
        maleBttn = (RadioButton) findViewById(R.id.newUserMaleRadioBttn);
        maleBttn.setOnCheckedChangeListener(this);
        femaleBttn = (RadioButton) findViewById(R.id.newUserFemaleRadioBttn);
        femaleBttn.setOnCheckedChangeListener(this);

        newUserSubmit = (Button) findViewById(R.id.newUserSubmitButton);
        newUserSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sex == 0) {
                    Toast.makeText(new_user.this, "You must choose male or female!", Toast.LENGTH_SHORT).show();
                } else {
                    if (preCheckUserInput()) {
                        new AlertDialog.Builder(new_user.this)
                                .setTitle("Important Notice!")
                                .setMessage("In this version of the fitness mirror total account recovery must be done manually. "
                                        + "Because of this, it is VERY important to recall your username and password. "
                                        + "Account retrieval is ONLY possible with at least one of "
                                        + "these two pieces of information.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        submitNewUser();
                                    }
                                }).show();
                    }
                }
            }
        });

        newUserCancel = (Button) findViewById(R.id.newUserCancelButton);
        newUserCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(new_user.this)
                        .setTitle("Final Confirmation")
                        .setMessage("If you cancel you will lose all of your entered data "
                                + "are you sure you wish to return to the previous screen?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(new_user.this, "Exit confirmed", Toast.LENGTH_SHORT).show();
                                cancelNewUser();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(new_user.this, "Cancel reset", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });
    }

	// Submits the user data to the mirror side software along with a verification window prior to sending
    private void submitNewUser() {
        String tempUserSex;

        if (sex == 1) {
            tempUserSex = "male";
        } else
            tempUserSex = "female";

        new AlertDialog.Builder(new_user.this)
                .setTitle("Confirmation")
                .setMessage("The user information that will be submitted:\n" + username + "\n" + firstName + "\n"
                        + lastName + "\n" + password + "\n" + passwordHint + "\n" + age + "\n" + tempUserSex + "\n\n"
                        + "Please carefully verify this information! You will be unable to access your account if "
                        + "you forget your username and password. Do you wish to submit this data and continue?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(new_user.this, "User info submitted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(new_user.this, "Submission cancelled", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }

    private void cancelNewUser() {
        finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (buttonView.getId() == R.id.newUserMaleRadioBttn) {
                femaleBttn.setChecked(false);
                sex = 1;                      // 1 implies the user is male
            }
            if (buttonView.getId() == R.id.newUserFemaleRadioBttn) {
                maleBttn.setChecked(false);
                sex = 2;                      // 2 implies the user is female
            }
        }
    }

    public void onBackPressed() {
        new AlertDialog.Builder(new_user.this)
                .setTitle("Confirmation")
                .setMessage("If you cancel you will lose all of your entered data "
                        + "are you sure you wish to return to the previous screen?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(new_user.this, "Exit confirmed", Toast.LENGTH_SHORT).show();
                        cancelNewUser();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(new_user.this, "Cancel reset.", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }

	// The catchall for any potential issues that arise with the virtual keyboard
    private void keyErrorFail() {
        Toast.makeText(new_user.this, "There appears to an error with the keyboard."
                + " You may need to exit the screen and return or restart the app.", Toast.LENGTH_LONG).show();
    }

	// Validate user input. Not much to do here initially other than make sure desired values are strings except the age
    private boolean preCheckUserInput() {
        boolean validInput;

        username = userNameIn.getText().toString();
        firstName = firstNameIn.getText().toString();
        lastName = lastNameIn.getText().toString();
        password = passwordIn.getText().toString();
        passwordConfirm = confirmPassIn.getText().toString();
        passwordHint = passHintIn.getText().toString();
        try {
            age = Integer.parseInt(ageIn.getText().toString());
        } catch (NumberFormatException numWasBlank) {
            age = 0;
        }
        validInput = checkInput();
        return validInput;
    }

    private void updateFocus(int viewToFocus) {
        userNameIn.clearFocus();
        firstNameIn.clearFocus();
        lastNameIn.clearFocus();
        passwordIn.clearFocus();
        confirmPassIn.clearFocus();
        passHintIn.clearFocus();
        ageIn.clearFocus();

        switch (viewToFocus) {
            case 1:
                userNameIn.requestFocus();
                break;
            case 2:
                firstNameIn.requestFocus();
                break;
            case 3:
                lastNameIn.requestFocus();
                break;
            case 4:
                passwordIn.requestFocus();
                break;
            case 5:
                confirmPassIn.requestFocus();
                break;
            case 6:
                passHintIn.requestFocus();
                break;
            case 7:
                ageIn.requestFocus();
                break;
            default:
                Toast.makeText(new_user.this, "Field focus error...", Toast.LENGTH_SHORT).show();
                break;
        }
    }

	// Second stage of user input validation. Make sure confirmation entries match the companion field such as with passwords etc.
	// There are some restrictions such as password length. A "full" version of the software would likely need more robust
	// account requirements unless it's built completely for personal use.
    private boolean checkInput() {
        boolean valid = false;

        if (password.length() < 4) {
            passwordIn.setError("The password length must be at least 4 characters in length!");
            updateFocus(4);
        } else if (passwordConfirm.length() < 4) {
            confirmPassIn.setError("The password length must be at least 4 characters in length!");
            updateFocus(5);
        } else if (!passwordConfirm.equals(password)) {
            confirmPassIn.setError("The password and confirmation don't match!");
            updateFocus(5);
        } else if (username.length() < 2) {
            userNameIn.setError("The username must be at least 2 characters in length");
            updateFocus(1);
        } else if (firstName.length() < 2) {
            firstNameIn.setError("The first name must be at least 2 characters long");
            updateFocus(2);
        } else if (lastNameIn.length() < 2) {
            lastNameIn.setError("The last name must be at least 2 characters long");
            updateFocus(3);
        } else if (passwordHint.length() < 1) {
            passHintIn.setError("You must provide a password hint!");
            updateFocus(6);
        } else if (age == 0) {
            ageIn.setError("The age field cannot be zero!");
            updateFocus(7);
        } else {
            valid = true;
        }
        return valid;
    }
}
