package fitmirrorteam.fitmirrorapp;

/*
 * The main GUI menu. The sections should be self-explanatory by their variable and method names
 * The other package files that handle menu controls and app behavior contain detail comments as well
 * snippets of debug or testing code that was left in, but commented out. They may be useful for further
 * development later on.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

public class commandMenu extends SubActivityTemplate {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button enterHealth;
        Button dailyImage;
        Button timeLapse;
        Button manageData;
        Button viewInfo;
        Button dataAnalytics;

        enterHealth = (Button) findViewById(R.id.enterHealthButton);
        enterHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchEnterHealth();
            }
        });

        dailyImage = (Button) findViewById(R.id.dailyImageButton);
        dailyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchDailyImage();
            }
        });

        timeLapse = (Button) findViewById(R.id.imageLoopButton);
        timeLapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchTimeLapse();
            }
        });

        manageData = (Button) findViewById(R.id.manageDailyButton);
        manageData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchManageData();
            }
        });

        viewInfo = (Button) findViewById(R.id.viewPersonalButton);
        viewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchViewInfo();
            }
        });

        dataAnalytics = (Button) findViewById(R.id.dataAnalyticsButton);
        dataAnalytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchDataAnalytics();
            }
        });
    }

    private void launchEnterHealth() {
        Intent intent = new Intent(this, enterHealthInfo.class);
        startActivity(intent);
    }

    private void launchDailyImage() {
        Intent intent = new Intent(this, daily_image.class);
        startActivity(intent);
    }

    private void launchTimeLapse() {
        Intent intent = new Intent(this, image_time_lapse.class);
        startActivity(intent);
    }

    private void launchManageData() {
        Intent intent = new Intent(this, manage_data.class);
        startActivity(intent);
    }

    private void launchViewInfo() {
        Intent intent = new Intent(this, view_data.class);
        startActivity(intent);
    }

    private void launchDataAnalytics() {
        Intent intent = new Intent(this, data_analysis.class);
        startActivity(intent);
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("If you continue you will be logged out. Do you want to log out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(commandMenu.this, "You were logged out.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }
}
