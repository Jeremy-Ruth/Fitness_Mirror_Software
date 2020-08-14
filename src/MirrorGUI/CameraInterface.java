package MirrorGUI;

/*
 * Handles camera processes within the mirror side application. Because multiple devices could be talking to
 * the mirror at the same time this functionality is implemented on its own thread. As I am new to multithreading
 * this is likely not handled in the most ideal way. 
 *
 * The code needed is relatively simple, but is communicating with hardware, which in the current version is a 
 * Raspberry Pi Camera. The commands and methods used to perform these tasks will have to be modified to match the
 * hardware you choose. MariaDB is the current database being used and if that is changed further code will need to
 * be edited in order to facilitate the change.
 */

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.time.Instant;

public class CameraInterface implements Runnable {

	private Thread cameraThread = null;
	private volatile boolean shouldStop = true;
	private volatile boolean doPicture = false;
	private volatile boolean haveNewPic = false;
	private volatile boolean calibProcIsRunning = false;
	private volatile boolean makePicProcRunning = false;
	private Process currDoCalib;
	private Process makePic;
	volatile String currUser = "";
	volatile String currDate = "";
	volatile int delayBeforePic = 0;
	long startTime;
	long currTime;
	String calibrationCommand;
	String dbFilePath = "";
	double nativeHeight;
	double nativeWidth;

	@FXML private Pane wholeWindowImagePane;
	@FXML private HBox wholeWindowImageBox;
	@FXML private ImageView wholeWindowImage;

	public CameraInterface() {
		startCameraThread();
	}

	public void startCameraThread() {
		if (cameraThread == null) {
			System.out.println("Running the camera thread...");
			cameraThread = new Thread(this, "cameraThread");
			cameraThread.start();
		}
	}

	@Override
	public void run(){
		try {
			doCameraAction();
		} catch (Exception ex) {
			Thread tC = Thread.currentThread();
			tC.getUncaughtExceptionHandler().uncaughtException(tC, ex);
		}
		System.out.println("Terminated camera thread");
	}

	// All the camera actions are handled in this loop. I have left most of the testing feedback code in
	// for troubleshooting purposes to hopefully save time with sorting it all out again if needed later.
	public void doCameraAction() throws IOException, InterruptedException {

		while(true) {
			if (!shouldStop) {
				if(!doPicture) {
					if(!calibProcIsRunning) {
						System.out.println("In the regular camera calibration routine");
						calibrationCommand = "raspistill -p 0,0,1080,1920 -t 0 -vf";
						currDoCalib = Runtime.getRuntime().exec(calibrationCommand);
						calibProcIsRunning = true;
					}
				}else {
					if (!calibProcIsRunning) {
						if (delayBeforePic == 0) {
							delayBeforePic = 1;
						}
						delayBeforePic = delayBeforePic * 1000;
						System.out.println("In the delay before taking a picture");
						calibProcIsRunning = true;
						calibrationCommand = "raspistill -p 0,0,1080,1920 -t " + delayBeforePic + " -vf";
						currDoCalib = Runtime.getRuntime().exec(calibrationCommand);
						//currDoCalib.waitFor();
						startTime = Instant.now().toEpochMilli();
						currTime = Instant.now().toEpochMilli();
						while ((currTime - startTime) < delayBeforePic && !shouldStop) {
							currTime = Instant.now().toEpochMilli();
						}
						calibProcIsRunning = false;
						Thread.sleep(200);
						currDoCalib.destroy();
					}if(doPicture) {
						System.out.println("Was able to get to the take a new picture stage after the wait");
						String finalFileAndPath = "/home/pi/FitMirrorApp/UserImages/" + currUser + "/" + currUser + currDate + ".jpg";
						dbFilePath = "UserImages/" + currUser + "/" + currUser + currDate + ".jpg";

						String takePictureCommand = "sudo raspistill -n -q 75 -t 1500 -o " + finalFileAndPath + " -w 1256 -h 2464 -vf -p 0,0,1080,1920";
						System.out.println("The completed camera command line that will be used is:\n" + takePictureCommand);

						try {
							// Backup of command arguments to set the picture width and height -w 890 -h 1760
							makePic = Runtime.getRuntime().exec(takePictureCommand);
							makePic.waitFor();
							makePicProcRunning = true;
						} catch (Exception e) {
							System.exit(hashCode());
						}

						System.out.println("The picture process was completed");
						haveNewPic = true;
						shouldStop = true;
						Thread.sleep(150);
					}
				}
			}else {
				if(calibProcIsRunning) {
					while (currDoCalib.isAlive()) {
						currDoCalib.destroy();
						Thread.sleep(100);
						calibProcIsRunning = false;
					}
				}
				if(makePicProcRunning) {
					while (makePic.isAlive()) {
						makePic.destroy();
						Thread.sleep(100);
						makePicProcRunning = false;
					}
				}
				//Thread.sleep(200);
			}
			//Thread.sleep(100);
		}
	}

	public void takePicSetter(String theUser, String todayDate, String picDelay) {
		currUser = theUser;
		currDate = todayDate;
		try {
			delayBeforePic = Integer.parseInt(picDelay);
		} catch (NumberFormatException numErr) {
			shouldStop = true;
			numErr.printStackTrace();
		}
	}

	/*
	public void takePicReset() {
		currUser = null;
		currDate = null;
		delayBeforePic = 0;
	}*/

    public void setCalibrateStop(boolean stopOrNot) {
        shouldStop = stopOrNot;
    }

    public void setTakepic(boolean takePicOrNot) {
        doPicture = takePicOrNot;
    }

    public boolean wasNewPic() {
        return haveNewPic;
    }

    public void resetHaveNewPic() {
        haveNewPic = false;
    }

    public String getPic() {
        return dbFilePath;
    }

}
