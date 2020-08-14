package MirrorGUI;

/*
 * Created by: Jeremy Ruth - 2017
 * 
 * The main entry point for the "mirror side" application. The mirror is the brain of the smart mirror and it's devices.
 * In this version the database used is MariaDB and the main application is running a JavaFX GUI. The original hardware
 * that this was implemented on was a Raspberry Pi 3. In the prototype testing it was found that the Pi 3 wasn't quite
 * powerful enough for some processing tasks such as the image slideshow with the way JavaFX ,and most certainly I, implement them.
 * If you are trying to recreate this smart mirror build and are wondering what hardware to use, I would recommend trying at least a 
 * Pi 4 or whatever the most current version is (or a competitor model). I would also recommend looking for 3rd party code resource for 
 * image manipulation and processing. The current method used is more of a brute force, simple, and straighfoward approach and doesn't 
 * seem very memory efficient. It isn't a problem for a desktop PC, but the small mountable processors you would likely want to use to
 * make this a complete and useable system need a very lean approach to be effective and responsive.
 *
 * The available peripherals coded in this version are:
 * A mobile device (assumed to be a smartphone) which is used for user data entry and to control the program
 * A camera (Pi camera) that uses Python scripts to work with the hardware. This is part of the smart mirror hardware itself
 * Bathroom scale modified to send weight data to the program. The scale used was reverse engineered with a custom built circuit. There
 *   are other approaches including modifying this software to accept outputs from a store bought smart scale. Regardless of the
 *   approach there will most certainly need to be some modifications in this software to work with your hardware options.
 *
 * The software was meant to be modular and to expand to accept several more sensor or data inputs. If you are using this to build
 * a health monitoring smart mirror, some other common sensors to look at next are a blood pressure cuff, and oxygen sensor. A camera 
 * that has infrared capabilities can also be used to get BMI estimates (the XBox Kinect sensor bars have this capability). 
 *
 **** IMPORTANT NOTE: I have added a small, barely visitble (depending on your monitor) exit button in the mid to lower left of the main
 **** GUI entry screen. This is a failsafe in case the phone app fails to connect or communication gets droppped. It will allow you to
 **** terminate the mirror side app without an external signal. This can be removed at your discretion. The exit action was originally
 **** intended to be programmed into the PI from a hardware pin once the prototype was more complete so a physical switch could be installed
 **** on the mirror. I mention it in case you want to implement a more "household appliance" based approach to your mirror.
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Main extends Application {
    private boolean isRunning = true;
    private String[] userCommandSet = new String[30];
    private String initialUserArgs = "";

    public static void main(String[] args) {
        launch(args);
    }

	/*
	 * This GUI layout assumes the monitor for the mirror is arranged vertically like a tall mirror would be.
	 * Since this is not normal for a PC monitor (usually horizontally oriented) you will need to modify this
	 * section and probably additioanl code in other sections to adjust for this fundamental change if you do not want
	 * a vertical monitor setup.
	 * This version does not have a dynmic GUI and does not automatically adjust to changes in pixel dimensions either.
	 */
    @Override
    public void start(Stage primaryStage) throws Exception{
        List<Screen> allScreens = Screen.getScreens();

        FXMLLoader guiLoader = new FXMLLoader(getClass().getResource("MirrorGUI.fxml"));
        Parent root = guiLoader.load();
        MirrorGUI_Controller currController = guiLoader.getController();

        primaryStage.setTitle("Fitness Mirror");
        primaryStage.setScene(new Scene(root, 1080, 1920));

        if(allScreens.size() > 1) {
            Screen secondaryScreen = allScreens.get(1);
            javafx.geometry.Rectangle2D bounds = secondaryScreen.getVisualBounds();
            primaryStage.setX(bounds.getMinX());
            primaryStage.setY(bounds.getMinY());
        }else {
            Screen primaryScreen = allScreens.get(0);
            javafx.geometry.Rectangle2D bounds = primaryScreen.getVisualBounds();
            primaryStage.setX(bounds.getMinX());
            primaryStage.setY(bounds.getMinY());
        }

        primaryStage.show();
        currController.visOfSingleData(false);
        currController.visOfFullImg(false);
        startTask(currController);
    }

    public void startTask(MirrorGUI_Controller currGUICntrl) {
        // Create a Runnable
        Runnable getUserIn = () -> {
            try {
                runTask(currGUICntrl);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        // Run the task in a background thread
        Thread backgroundThread = new Thread(getUserIn);
        // Terminate the running thread if the application exits
        backgroundThread.setDaemon(true);
        // Start the thread
        backgroundThread.start();
    }

    public void runTask(MirrorGUI_Controller currGUI) throws InterruptedException {
        PhoneInterface currPhoneSession = new PhoneInterface();
        ScaleInterface currScaleSession = new ScaleInterface();
        CameraInterface currCameraInterface = new CameraInterface();
		
		// As is will always connect to the tester account in the database. Will need code added for full user support
        String currUser = "tester";

		// We want to know today's date for the scale and daily image functions which are somewhat automated
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currDate = sdf.format(today);
        String todayDate = currDate.toString();

		/*
		 * Look for changes on the threads for the other devices and process as they come in.
		 * I have left some console debugging messages in to assist with troubleshooting. They can be
		 * very helpful to figure out which thread and command is taking action at a given time. Keep or delete as desired.
		 * Commands are passed in using keywords to try to remove some of the confusion when reading through the code.
		 */
        try {
            while (isRunning) {
                if (currPhoneSession.wasNewArgs() || currScaleSession.wasNewWeight() || currCameraInterface.wasNewPic()) {
                    if (currPhoneSession.wasNewArgs()) {
                        currPhoneSession.resetArgState();
                        System.out.println("Got new args and about to split them");
                        Thread.sleep(200);
                        initialUserArgs = currPhoneSession.getPhoneCommand();
                        System.out.println("The return of the command stored into the app confirms they are: " + initialUserArgs);
                        userCommandSet = MirrorInterfaces.getUserArgs(initialUserArgs);

                        if (userCommandSet[0].equals("userLogin")) {
                            Platform.runLater(() -> {
                                try {
                                    boolean foundUser;
                                    if (foundUser = MirrorInterfaces.verifyUser(userCommandSet)) {
                                        currPhoneSession.setShouldVerify();
                                        currPhoneSession.setUserVerified(foundUser);
                                        currPhoneSession.getPhoneCommand();
                                        if (currGUI.viewSingleUserData(userCommandSet[1], currUser))
                                            currGUI.visOfSingleData(true);
                                    }
                                } catch (ClassNotFoundException | SQLException | IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                        if (userCommandSet[0].equals("sID") || userCommandSet[0].equals("sD")) {
                            System.out.println("Was in single image/data view caller with a date of: " + userCommandSet[1]);
                            Platform.runLater(() -> {
                                try {
                                    if (currGUI.viewSingleUserData(userCommandSet[1], currUser)) {
                                        currGUI.visOfSingleData(true);
                                        currGUI.visOfFullImg(false);
                                    }
                                } catch (ClassNotFoundException | SQLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                        if (userCommandSet[0].equals("sI")) {
                            System.out.println("Was in the full image caller");
                            Platform.runLater(() -> {
                                try {
                                    if (currGUI.viewFullImg(userCommandSet, currUser)) {
                                        currGUI.visOfFullImg(true);
                                        currGUI.visOfSingleData(false);
                                    }
                                } catch (ClassNotFoundException | SQLException | InterruptedException | IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                        if (userCommandSet[0].equals("timeLapse")) {
                            System.out.println("Was in the time lapse caller");
                            Platform.runLater(() -> {
                                try {
                                    if (currGUI.viewFullImg(userCommandSet, currUser)) {
                                        currGUI.visOfFullImg(true);
                                        currGUI.visOfSingleData(false);
                                    }
                                } catch (ClassNotFoundException | SQLException | InterruptedException | IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                        if (userCommandSet[0].equals("calibrate") || userCommandSet[0].equals("stopCalibrate")) {
                            System.out.println("Was in the camera calibration caller");
                            Platform.runLater(() -> {
                                if (userCommandSet[0].equals("calibrate")) {
                                    System.out.println("Got the message to begin calibrating");
                                    currCameraInterface.setCalibrateStop(false);
                                } else {
                                    System.out.println("Got the message to stop calibrating");
                                    currCameraInterface.setCalibrateStop(true);
                                    currCameraInterface.setTakepic(false);
                                }
                            });
                        }
                        if (userCommandSet[0].equals("takePic")) {
                            System.out.println("Was in the take picture caller");
                            Platform.runLater(() -> {
                                    System.out.println("Got the message to a picture");
                                    currCameraInterface.takePicSetter(currUser,todayDate,userCommandSet[1]);
                                    currCameraInterface.setTakepic(true);
                                    currCameraInterface.setCalibrateStop(false);

                            });
                        }
                        if (userCommandSet[0].equals("newHealth")) {
                            System.out.println("Was in the new health information caller");
                            Platform.runLater(() -> {
                                try {
                                    if (MirrorInterfaces.setHealthData(userCommandSet, currUser)) {
                                        System.out.println("The user data being passed is: " + currUser + userCommandSet[0] + userCommandSet[1] + userCommandSet[2]);
                                        currGUI.viewSingleUserData(userCommandSet[7], currUser);
                                        currGUI.visOfSingleData(true);
                                        currGUI.visOfFullImg(false);
                                    }
                                } catch (ClassNotFoundException | SQLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    } else if (currCameraInterface.wasNewPic()) {
                        currPhoneSession.resetArgState();
                        System.out.println("Was in the new picture available caller");
                        currCameraInterface.resetHaveNewPic();
                        System.out.println("Got into found new pic routine");
                        String[] tempCommandSet = new String[3];
                        tempCommandSet[0] = "sI";
                        tempCommandSet[1] = todayDate;
                        String photoPath = currCameraInterface.getPic();
                        if(photoPath != null){
                            Platform.runLater(() -> {
                                try {
                                    if (MirrorInterfaces.setDailyImage(photoPath, currUser, tempCommandSet[1])) {
                                        currGUI.viewFullImg(tempCommandSet, currUser);
                                        currGUI.visOfFullImg(true);
                                        currGUI.visOfSingleData(false);
                                        //currCameraInterface.takePicReset();
                                        currCameraInterface.setTakepic(false);
                                        System.out.println("Done resetting everything after the new pic process");
                                    }
                                } catch (ClassNotFoundException | SQLException | InterruptedException | IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    } else if (currScaleSession.wasNewWeight()) {
                        currPhoneSession.resetArgState();
                        currScaleSession.resetWeightState();
                        System.out.println("Got into the weight update routine caller");
                        int weightToSend;
                        String lambdaTodayDate = todayDate;
                        weightToSend = currScaleSession.getScaleValue();
                        System.out.println("The weightToSend val was: "+weightToSend+" the currUser: "+currUser+" and lambdaTodayDate: "+lambdaTodayDate);
                        if (weightToSend != 0) {
                            System.out.println("Got into the weight submission stage");
                            Platform.runLater(() -> {
                                try {
                                    System.out.println("About to try setting the weight in the database");
                                    if (MirrorInterfaces.setDailyWeight(weightToSend, currUser, lambdaTodayDate)) {
                                        System.out.println("Got a positive result back from the database");
                                        currGUI.viewSingleUserData(lambdaTodayDate, currUser);
                                        currGUI.visOfSingleData(true);
                                        currGUI.visOfFullImg(false);
                                    }
                                    else{
                                        System.out.println("The database returned a failure to submit the weight");
                                    }
                                } catch (ClassNotFoundException | SQLException e) {
                                    e.printStackTrace();
                                    currScaleSession.resetWeightState();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    currScaleSession.resetWeightState();
                                }
                            });
                        }else{
                            System.out.println("Didn't get any weight!");
                        }
                    }
                    System.out.println("Checked all command and found no match!");
                    Thread.sleep(100);
                }
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
