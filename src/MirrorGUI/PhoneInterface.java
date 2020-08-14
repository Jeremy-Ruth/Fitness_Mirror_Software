package MirrorGUI;

/*
 * Handles interactions and requests from the mobile app (assumed to be a smartphone). Runs on its own thread
 * since other devices can be talking to the mirror at the same time. This is likely not implemented in an ideal
 * way due to my inexperience with mulithreading.
 *
 * As with most other classes of the package I have left some bug testg code in the methods to assist with developing
 * the software further later on.
 */

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class PhoneInterface implements Runnable {
    private Thread phoneConn = null;
    private volatile boolean exit = false;
    private volatile boolean haveNewArgs = false;
    private volatile boolean shouldVerify = false;
    private volatile boolean userVerified = false;
    private int portNumber = 4444;
    private String phoneCommand = null;

    public PhoneInterface() {
        startPhoneConn();
    }

    public void startPhoneConn() {
        if (phoneConn == null) {
            exit = false;
            System.out.println("Running the phone thread creation");
            phoneConn = new Thread(this, "PhoneThread");
            phoneConn.start();
        }
    }

    public void stopPhoneConn() {
        exit = true;
        while (phoneConn != null) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException threadErr) {
                threadErr.printStackTrace();
            }
        }
    }

    @Override
    public void run(){
        System.out.println("TCP Server thread started...");
        while(!exit) {
            try {
                doPhoneComm();
            } catch (Exception ex) {
                Thread t = Thread.currentThread();
                t.getUncaughtExceptionHandler().uncaughtException(t, ex);
                exit = true;
                stopPhoneConn();
            }
        }
        stopPhoneConn();
        System.out.println("Terminated Server thread");
    }

	/*
	 * The while loop here has a shell where the check for valid user credentials would be, but the functionality has not been fully  
	 * implemented or tested in the rest of the package. The phone is essentially a remote control, so we only need to receive and interpret
	 * commands and parameters here. This was an intentional choice due to privacy concerns of health data with multiple users. It was
	 * beyond my experience to develop a robust security level to ensure data could not be redirected or stolen from the mobile side app.
	 * If you desire the phone app to have more capabilities you will have to implement them.
	 */
    public void doPhoneComm() throws IOException, InterruptedException {

            try {
                ServerSocket phoneServer = new ServerSocket(portNumber);
                phoneServer.setReuseAddress(true);
                String phoneResponse, serverResponse;

                while(!exit) {
                    Socket phoneClient = phoneServer.accept();
                    PrintWriter fromServer = new PrintWriter(phoneClient.getOutputStream(), true);
                    BufferedReader fromClient = new BufferedReader(new InputStreamReader(phoneClient.getInputStream()));

                    serverResponse = "READY";
                    fromServer.println(serverResponse);
                    while ((phoneResponse = fromClient.readLine()) != null) {
                        if(shouldVerify) {
                            if (userVerified) {
                                fromServer.println("VERIFIED");
                                System.out.println("Was able to verify user...");
                            } else {
                                fromServer.println("INVALID");
                                System.out.println("Could not verify user!");
                            }
                        } else {
                            haveNewArgs = true;
                            if (haveNewArgs)
                                System.out.println("GOT NEW ARGS!!");
                            System.out.println("The Phone interface is storing new arguments...");
                            phoneCommand = phoneResponse;
                            fromServer.println("OK");
                        }
                        //System.out.println("TCP Communication finished... Command sent was:\n" + phoneCommand);
                        phoneClient.close();
                        fromServer.close();
                        fromClient.close();
                        break;
                    }
                }
            } catch (IOException phoneCommErr) {
                phoneCommErr.printStackTrace();
                exit = true;
                stopPhoneConn();
            }
        System.out.println("Terminating the connection thread");
    }

        public boolean wasNewArgs() {
            return haveNewArgs;
        }

        public void resetArgState() {
            haveNewArgs = false;
        }

        public String getPhoneCommand() {
            System.out.println("The command stored into the app is: " + phoneCommand);
            return phoneCommand;
        }

        public void setShouldVerify () {
            shouldVerify = true;
        }

        public void setUserVerified(boolean dbResult) {
            userVerified = dbResult;
        }
}

