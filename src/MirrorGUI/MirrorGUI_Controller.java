package MirrorGUI;

/*
 * The controller "bridge" between the JavaFX GUI and the backend.
 * Left some debugging code in to use as needed.
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class MirrorGUI_Controller {

    @FXML
    private javafx.scene.control.Button closeButton;

    double nativeHeight;
    double nativeWidth;
    private SimpleDateFormat usLocal = new SimpleDateFormat("MM/dd/yyyy");

    private String[] singleViewUserData = new String[9];
    @FXML private SplitPane singleViewDataPane;
    @FXML private ImageView sviewUserImage;
    @FXML private HBox sviewUserImageBox;
    @FXML Label sviewUserName;
    @FXML Label sviewCurrDate;
    @FXML Label sviewWeight;
    @FXML Label sviewBPSys;
    @FXML Label sviewBPDia;
    @FXML Label sviewTemp;
    @FXML Label sviewPulse;
    @FXML Label sviewBMI;

    private String[] wholeWindowUserData;
    @FXML private Pane wholeWindowImagePane;
    @FXML private HBox wholeWindowImageBox;
    @FXML private ImageView wholeWindowImage;

    @FXML
    public void closeButtonAction(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    ////////////////////////////////// METHODS FOR SINGLE DATA AND IMAGE VIEWS /////////////////////////////////////////
    public void visOfSingleData (boolean currVisibility) {
        singleViewDataPane.setVisible(currVisibility);
    }

    public boolean viewSingleUserData(String dateToUse, String currUser) throws SQLException, ClassNotFoundException, IOException {
        //boolean validResult = true;
        sviewUserImage.fitHeightProperty().unbind();
        sviewUserImage.fitWidthProperty().unbind();

        nativeHeight = 0;
        nativeWidth=0;
        singleViewUserData = MirrorInterfaces.getSingleViewDB(dateToUse, currUser);
        //System.out.println("The path that will be used is: " + singleViewUserData[7]);

        // Use the next 2 lines for testing in IntelliJ
        File currImg = null;
        Image imageToLoad = null;
        if (singleViewUserData[0] != null) {
            //currImg = new File(singleViewUserData[7]);
            //imageToLoad = new Image(currImg.toURI().toString());

            // Use the next 4 lines on the Pi
            //String currJarLoc = getClass().getProtectionDomain().getCodeSource().getLocation().toString();
            //System.out.println("The path from the system is: " + currJarLoc);
            //currJarLoc = currJarLoc.substring(6,21);
            currImg = new File(singleViewUserData[7]);
            imageToLoad = new Image(currImg.toURI().toString());
        }else {
            //currImg = new File("UserImages/DefaultUserPic.png");
            //imageToLoad = new Image(currImg.toURI().toString());

            // Use the next 4 lines on the Pi
            //String currJarLoc = getClass().getProtectionDomain().getCodeSource().getLocation().toString();
            //currJarLoc = currJarLoc.substring(6,21);
            currImg = new File("UserImages/DefaultUserPic.png");
            imageToLoad = new Image(currImg.toURI().toString());
        }

        System.out.println("The path that is being looked at: " + currImg.toURI().toString());
            sviewUserImage.setImage(imageToLoad);
            nativeHeight = imageToLoad.getHeight();
            if (nativeHeight > sviewUserImageBox.getHeight())
                sviewUserImage.fitHeightProperty().bind(sviewUserImageBox.heightProperty());
            if (nativeWidth > sviewUserImageBox.getWidth())
                sviewUserImage.fitWidthProperty().bind(sviewUserImageBox.widthProperty());
            System.out.println("The current height of view is: " + sviewUserImageBox.getHeight());
            System.out.println("The current width of view is: " + sviewUserImageBox.getWidth());

        if (singleViewUserData[0] != null) {
            sviewUserName.setText(singleViewUserData[0]);
            sviewCurrDate.setText(singleViewUserData[8]);
            sviewWeight.setText(singleViewUserData[1]);
            sviewBPSys.setText(singleViewUserData[2]);
            sviewBPDia.setText(singleViewUserData[3]);
            sviewPulse.setText(singleViewUserData[4]);
            sviewTemp.setText(singleViewUserData[5]);
            sviewBMI.setText(singleViewUserData[6]);
        } else {
            System.out.println("There was no data for the specified date!");
        }
        //}
        return true;
    }


    ////////////////////////////////// METHODS FOR TIME LAPSE AND IMAGE ONLY VIEW /////////////////////////////////////////
    public void visOfFullImg (boolean currVisibility) {
        wholeWindowImagePane.setVisible(currVisibility);
    }

    public boolean viewFullImg(String[] viewArgs, String currUser) throws SQLException, ClassNotFoundException, IOException, InterruptedException {
        nativeHeight = 0;
        nativeWidth = 0;
        int dayCount = 1;
        int timeLapseInterval = 0;
        wholeWindowImage.fitWidthProperty().unbind();
        wholeWindowImage.fitHeightProperty().unbind();

        if(viewArgs[0].equals("timeLapse")) {
            timeLapseInterval = Integer.parseInt(viewArgs[3]);
            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
            String startDate = viewArgs[1];
            String endsDate = viewArgs[2];
            try {
                Date date1 = myFormat.parse(startDate);
                Date date2 = myFormat.parse(endsDate);
                long diff = date2.getTime() - date1.getTime();
                dayCount = (int) (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                System.out.println("Days calculated: " + dayCount);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            wholeWindowUserData = new String[dayCount];
            wholeWindowUserData = MirrorInterfaces.getImagesDB(viewArgs, currUser, true, dayCount);
        }else {
            wholeWindowUserData = new String[1];
            wholeWindowUserData = MirrorInterfaces.getImagesDB(viewArgs, currUser, false, dayCount);
        }

        if(viewArgs[0].equals("timeLapse")) {
            TimeLapseView currTimeLapse = new TimeLapseView(wholeWindowUserData,timeLapseInterval,dayCount, wholeWindowImagePane,wholeWindowImageBox,wholeWindowImage);
        }else {
            //wholeWindowImage.setVisible(true);
            // Use the next 2 lines for testing in IntelliJ
            //File currImg = new File(wholeWindowUserData[0]);
            //Image imageToLoad = new Image(currImg.toURI().toString());

            // Use the next 4 lines on the Pi
            //String currJarLoc = getClass().getProtectionDomain().getCodeSource().getLocation().toString();
            //currJarLoc = currJarLoc.substring(6,49);
            File currImg = new File(wholeWindowUserData[0]);
            Image imageToLoad = new Image(currImg.toURI().toString());

            System.out.println("The path that is being looked at: " + currImg.toURI().toString());

            if (wholeWindowUserData[0] != null) {
                wholeWindowImage.setImage(imageToLoad);
                nativeHeight = imageToLoad.getHeight();
                nativeWidth = imageToLoad.getWidth();
                if (nativeHeight > wholeWindowImageBox.getHeight())
                    wholeWindowImage.fitHeightProperty().bind(wholeWindowImageBox.heightProperty());
                if (nativeWidth > wholeWindowImageBox.getWidth())
                    wholeWindowImage.fitWidthProperty().bind(wholeWindowImageBox.widthProperty());

                System.out.println("The current height of image is: " + nativeHeight);
                System.out.println("The current width of image is: " + nativeWidth);
                System.out.println("The current height of view is: " + wholeWindowImageBox.getHeight());
                System.out.println("The current width of view is: " + wholeWindowImageBox.getWidth());
            } else {
                System.out.println("There was no image for the specified date!");
            }
        }
        //}
        return true;
    }
}
