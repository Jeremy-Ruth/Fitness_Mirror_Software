package MirrorGUI;

/*
 * The time lapse was broken out into its own class because it ended up being a fairly involved process. The intent
 * is to provide a user with the ability to view their transition over time as they meet their health goals and begin
 * to look more fit. This means that we may be processing quite a few daily images over a large period of dates. All images
 * are stored in the database (technically only a file path reference is stored).
 *
 * The decision to put this task on it's own thread was made in the hopes of keeping the potentially large amount of image
 * processing from blocking other phone activites. It may also be contributing the to the poor performance of the time lapse on 
 * the Pi 3. Added to that is the rather brute force method of generating a slide show here. There are surely more
 * efficent means to accomplish this and some deeper research or using a 3rd party library like OpenCV would likely make a big
 * difference. The slideshow seemed to work smoothly and well on a full desktop, but something about the approach here does not
 * work well on the Pi 3 and it usually locked up before displaying more than a couple images.
 */

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


public class TimeLapseView implements Runnable {
    private Thread lapseThread = null;
    private String[] wholeWindowUserData;
    private int timeLapseInterval;
    private int dayCount;
    double nativeHeight;
    double nativeWidth;

    @FXML private Pane wholeWindowImagePane;
    @FXML private HBox wholeWindowImageBox;
    @FXML private ImageView wholeWindowImage;

    public TimeLapseView(String[] dataToUse, int intervalToUse, int numOfDays, Pane currWholeWindowImagePane, HBox curWholeWindowImageBox, ImageView currWholeWindowImage) {
        startTimeLapse();
        wholeWindowUserData = dataToUse;
        timeLapseInterval = intervalToUse;
        dayCount = numOfDays;
        wholeWindowImagePane = currWholeWindowImagePane;
        wholeWindowImageBox= curWholeWindowImageBox;
        wholeWindowImage = currWholeWindowImage;
    }

    public void startTimeLapse() {
        if (lapseThread == null) {
            System.out.println("Running the time lapse thread...");
            lapseThread = new Thread(this, "timeLapseThread");
            lapseThread.start();
        }
    }

    @Override
    public void run(){
            try {
                doTimeLapse();
            } catch (Exception ex) {
                Thread tL = Thread.currentThread();
                tL.getUncaughtExceptionHandler().uncaughtException(tL, ex);
            }
        System.out.println("Terminated time lapse thread");
    }

	/* 
	 * We're bulding an array of images here to cycle though all of them in an equal time distribution
	 * over the total time specified by the user. In JavaFX we are loading an Image object in and then painting
	 * it into an ImageView (for the Pi) so we can resize it to fit the screen in a way we desire. The approach
	 * may have to change a bit if using other types of mini PCs
	 */
    public void doTimeLapse() throws IOException, InterruptedException, ExecutionException {

            System.out.println("Time lapse starting with interval of "+timeLapseInterval+"...");
            long timeForEachPic = Math.round((timeLapseInterval/dayCount)*1000);
            System.out.println("The interval between each pic will be: " + timeForEachPic + " milliseconds");

            ImageView[] picSet = new ImageView[dayCount];
            for(int i = 0;i<dayCount;i++) {
                // Use the next 2 lines for testing in IntelliJ
                //File currImg = new File(wholeWindowUserData[i]);
                //Image imageToLoad = new Image(currImg.toURI().toString());

                // Use the next 2 lines on the Pi
                File currImg = new File(wholeWindowUserData[0]);
                Image imageToLoad = new Image(currImg.toURI().toString());
                picSet[i] = new ImageView(imageToLoad);
            }
            for (int i = 0; i < dayCount; i++) {
                wholeWindowImage.setImage(null);
                wholeWindowImage.fitWidthProperty().unbind();
                wholeWindowImage.fitHeightProperty().unbind();
                // Use the next 2 lines for testing in IntelliJ
                //File currImg = new File(wholeWindowUserData[i]);
                //Image imageToLoad = new Image(currImg.toURI().toString());

                // Use the next 2 lines on the Pi
                File currImg = new File(wholeWindowUserData[i]);
                Image imageToLoad = new Image(currImg.toURI().toString());

                System.out.println("The path that is being looked at: " + currImg.toURI().toString());
                updateTimeLapse(imageToLoad, timeForEachPic);

            }
    }

	// We need to cycle to the next image in the view pane at the speed set by the time lapse. Get the new image
	// and then resize it to take up the entire image area (whole screen in the current version)
    private void updateTimeLapse(Image currImg, long delayTime) throws InterruptedException, ExecutionException {
        FutureTask updateTimeLapseTask = new FutureTask(() -> {
            wholeWindowImage.setImage(currImg);
            if (wholeWindowUserData[0] != null) {
                nativeHeight = currImg.getHeight();
                nativeWidth = currImg.getWidth();
                if (nativeHeight > wholeWindowImageBox.getHeight())
                    wholeWindowImage.fitHeightProperty().bind(wholeWindowImageBox.heightProperty());
                if (nativeWidth > wholeWindowImageBox.getWidth())
                    wholeWindowImage.fitWidthProperty().bind(wholeWindowImageBox.widthProperty());
            } else {
                System.out.println("Something went wrong while updating the time lapse!");
            }
            try {
                Thread.sleep(delayTime);
            } catch (InterruptedException err) {
                err.printStackTrace();
            }

        }, /* return value from task: */ null);

        Platform.runLater(updateTimeLapseTask);
        updateTimeLapseTask.get();
    }
}
