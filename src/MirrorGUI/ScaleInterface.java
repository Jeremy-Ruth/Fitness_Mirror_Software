package MirrorGUI;

/*
 * Data transfers for a users weight coming from the bathroom scale are handled in this class. The scale is
 * meant to operate as normally and transparently as a regular bathroom scale. To accomdate this, whenever the
 * scale is used it attempts to connect to the mirror and sends the weight data it captures immediately. When
 * weight data is received by the mirror application it is stored on the current date for the current user.
 *
 * This class is running on it's own thread since it is unknown when a user might want to use the scale to send
 * weight data. Multithreading is likely not implemented very well due to my lack of experience and could use a 
 * second look for those with expertise in that area. Most of the debugging code is left intact to asssit with
 * troubleshooting.
 *
 * The bathroom scale that was used in the protoype was a low cost digital scale that was reverse engineered in
 * order to integrate a custom built circuit for capturing weight and sending it to the mirror wirelessly. The wireless
 * chip that was used in the scale circuit was an ESP8266 if you want to build a similar circuit. If you use
 * this chip, or likely even another version of the ESP line, this code will mostly likely work with little to no 
 * changes. The further you deviate from this approach the greater the coding changes are likely to be required. An
 * appealing alternative that may be worthwile would be to reverse engineer the software on a commercial smart scale
 * to capture the data in this program as well. Several other points of health data could then be captured for "free" such as
 * a BMI estimate.
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.Scanner;

public class ScaleInterface implements Runnable {

    private Thread scaleConn = null;
    private volatile boolean exit = false;
    private volatile boolean haveNewWeight = false;
    private int portNumber = 1888;
    private String scaleMessage = null;

    public ScaleInterface() {
        startPhoneConn();
    }

    public void startPhoneConn() {
        if (scaleConn == null) {
            System.out.println("Running the scale thread creation");
            scaleConn = new Thread(this, "ScaleThread");
            scaleConn.start();
        }
    }

    public void stopScaleConn() {
        exit = true;
        while (scaleConn != null) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException threadErr) {
                threadErr.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        System.out.println("AT Server thread started...");
        try {
            doScaleComm();
        } catch (Exception ex) {
            Thread tS = Thread.currentThread();
            tS.getUncaughtExceptionHandler().uncaughtException(tS, ex);
            exit = true;
            stopScaleConn();
        }
        System.out.println("Terminated scale server thread");
    }

	/*
	 * We are essentially just waiting for the scale to send something. If we get a value we
	 * have to tell the ESP chip on the scale we got it and are done communicating. The error checking
     * for whether a reliable weight value was sent is assumed to be done on scale circuit side here. If
	 * your approach is different and you are not able to program the scale side to do checking, input will
	 * need to be validated here instead.
	 */
    public void doScaleComm() throws IOException, InterruptedException {

        try {
            ServerSocket scaleServer = new ServerSocket(portNumber);
            scaleServer.setReuseAddress(true);
            String scaleResponse, serverResponse;

            while (!exit) {
                System.out.println("Waiting for the scale...");
                Socket scaleClient = scaleServer.accept();
                Scanner fromClient = new Scanner(scaleClient.getInputStream());

                scaleResponse = "";
                while (!scaleResponse.equals("quit")) {
                    scaleResponse = fromClient.nextLine();
                    if(!scaleResponse.equals("quit"))
                        scaleMessage = scaleResponse;
                    System.out.println("Message from scale was: " + scaleResponse);
                }
                haveNewWeight = true;
                if (haveNewWeight)
                    System.out.println("GOT NEW WEIGHT!!");
                System.out.println("AT Communication finished... weight sent was:\n" + scaleMessage);
                scaleClient.close();
                fromClient.close();
                //break;
            }
        } catch (IOException scaleCommErr) {
            scaleCommErr.printStackTrace();
            exit = true;
            stopScaleConn();
        }
        Thread.sleep(500);
        System.out.println("Terminating the connection thread");
    }

    public boolean wasNewWeight() {
        return haveNewWeight;
    }

    public void resetWeightState() {
        haveNewWeight = false;
    }

    public int getScaleValue() {
        int convertMessage=0;
        try {
            convertMessage = Integer.parseInt(scaleMessage);
        }catch (NumberFormatException notNum) {
            notNum.printStackTrace();
        }
        System.out.println("The weight stored into the app is: " + scaleMessage);
        return convertMessage;
    }
}

