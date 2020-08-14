package fitmirrorteam.fitmirrorapp;

/**
 * Created by Jeremy Ruth on 11/25/2017.
 */
 
 /*
  * Connections to the mirror side software are handled here. The mobile app only talks to the mirror, which is essentially like
  * the server for all of the devices. In the version I built, I was using a Raspberry PI 3 as the server PC that the mirror was 
  * connected to. Most of the connection information will need to be updated or possibly even rewritten depending on how and what you  
  * plan to connect to as the server. I have left several snippets with comments in the code body to assist with alternative options
  * as well as for debugging and testing purposes.
  *
  * The communication with the server is kept asynchronous to keep this application from blocking other tasks the mobile device 
  * may need to do. The goal of the mobile side software was to keep it as modular and light as possible. Not just for peroformance
  * concerns, but also to avoid storing any personal health information on the mobile device. This will hopefully accomplish 2 goals.
  * One is to have the mobile app be as "secure" as possible when not using the mirror, and also to allow the interfacing between
  * the mobile and mirror side software to be reasonably independent. 
  *
  * Toward the goal of device independence the mobile software primarly only sends keywords to the mirror side software along with a string
  * of expected or necessary settingsor paramter information. This data is not stored on the phone and does not contain any code level
  * values needed by the mirror side software other than user entered variable values in the string. This means that as the product is 
  * developed further, the mirror software can easily be modified and grown. The only edits that should be required on the mobile side
  * software would be to add the user interface/input for new functionality and to use a unique keyword to pass to the mirror software for
  * any new functionality.
  */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.*;
import java.net.*;

public class server_interface extends AsyncTask<String, Void, String> {

    private ProgressDialog currDialog;
    private Context currContext;

    server_interface(Context parentActivity) {
        currDialog = new ProgressDialog(parentActivity);
        currContext = parentActivity.getApplicationContext();
    }

	// Let the user know we are trying to pass their data to the mirror
    @Override
    protected void onPreExecute() {
        currDialog.setMessage("Please wait while transferring...");
        currDialog.show();
    }

    @Override
    protected String doInBackground(String... userData) {

        int portNumber = 4444;
        //String hostName = "192.168.1.5";    // connection for Windows Desktop
        String hostName = "192.168.0.5";      // connection for Pi
        //String userArgs = userData[0];
        String result = "Didn't do anything at all!";

        try (   Socket phoneServer = new Socket(hostName, portNumber);
                PrintWriter fromPhone = new PrintWriter(phoneServer.getOutputStream(), true);
                BufferedReader fromServer = new BufferedReader(new InputStreamReader(phoneServer.getInputStream()));
        ) {
            String phoneResponse, serverResponse;
            while ((serverResponse = fromServer.readLine()) != null) {
                //if(userData[2] != null) {
                //    if(userData[2].equals("DO")) {
                        /////////////////// NEED TO FIX/FINISH THIS CODE ////////////////////
                //    }
                //}
                if (serverResponse.equals("OK")) {
                    //fromPhone.println("LOGOUT");
                    result = "Mirror communication completed";
                    fromServer.close();
                    fromPhone.close();
                    phoneServer.close();
                    break;
                }

                phoneResponse = userData[0];
                if (phoneResponse != null) {
                    //System.out.println("Client: " + fromUser);
                    fromPhone.println(phoneResponse);
                }
            }
        } catch (UnknownHostException e) {
            result = "Couldn't find host " + hostName;
        } catch (IOException e) {
            Log.d("IOException",e.toString(),e);
            result = "Couldn't get I/O for the connection to " + hostName;
        }
        return result;
    }

	// When finished attempting to pass the data close the communication dialog and notify user whether was successful
    @Override
    protected void onPostExecute(String result) {
        if (currDialog.isShowing()) {
            currDialog.dismiss();
            Toast.makeText(currContext,result,Toast.LENGTH_LONG).show();
        }
    }
}
