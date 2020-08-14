package fitmirrorteam.fitmirrorapp;

/*
 * The "hamburger" main menu options for the app. 
 * The current menu bar only has some very basic functionality that was required for the prototype model
 * of the smart mirror. "Calibrate" refers to the physical camera that is attached to the mirror, not the 
 * camera on the mobile device. 
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class SubActivityTemplate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_template);
        setTitle("Fitness Mirror");							// Name of the app here if you want to change it...
    }

	// Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return true;
    }

    /* Handle action bar item clicks here. The action bar will automatically handle clicks on the 
     * Home/Up button, as long as you specify a parent activity in AndroidManifest.xml.
	 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_settings) {
            Toast.makeText(this, "Settings menu item clicked", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.menu_calibrate) {
            Toast.makeText(this, "calibrate menu item clicked", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.menu_exit) {
            AlertDialog.Builder confirmLogout = new AlertDialog.Builder(this);
            confirmLogout.setMessage("Confirm Logout:");
            confirmLogout.setCancelable(false);
            final Intent rtrnLogin = new Intent(this,UserLogin.class);

            confirmLogout.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "User was logged out", Toast.LENGTH_SHORT).show();
                    rtrnLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(rtrnLogin);
                }
            });
            confirmLogout.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            AlertDialog logoutMssg = confirmLogout.create();
            logoutMssg.show();
        }

        return super.onOptionsItemSelected(item);
    }
}
