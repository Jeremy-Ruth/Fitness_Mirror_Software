package MirrorGUI;

/*
 * The primary purpose of this class is currently to interact with the database. Other software backend interactions
 * could also be added here to support new functionality. As with most other classes in the package, some commenting and
 * debug code has been left in to support troublshooting later on.
 *
 * The current version of the sotware uses MariaDB, which is an open source database using the MySQL type query structure for
 * the most part. The code will need to be modified in order to support other database types. There is currently no framework in
 * place to support easily switching out to a different database type. Additionally, there are no methods in the program to facilitate
 * creating a new database from scratch. That will have to be done manually outside of this software.
 */

import java.sql.*;

public class MirrorInterfaces {
    public static Connection mirrorDBConnection;
    public static ResultSet mirrorDBResult = null;
    public static Statement mirrorDBStatement = null;
    private static String usernameDB = "FitTeam";
    private static String password = "FitMirror";
    private static String Url = "jdbc:MySql://localhost:3306/fit_mirror_app";


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////  Method to retrieve data for a single date form the database  ////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String[] getSingleViewDB(String dateToUse, String currUser) throws SQLException, ClassNotFoundException {
        System.out.println("Connecting to database...");
        String[] currData = new String[9];
        //String validationQuery = "SELECT hasData FROM daily_vitals WHERE info_date='"+dateToUse+",";
        String query = "SELECT username, weight, bp_top, bp_bottom, pulse, temperature, bmi" +
                ", photo_path, info_date FROM daily_vitals WHERE username='"+currUser+"' AND info_date='"+dateToUse+"' AND has_data=1";

        try {
            mirrorDBConnection = DriverManager.getConnection(Url,usernameDB,password);
            System.out.println("Database Connected! Intent to retrieve data!");
            try {
                mirrorDBStatement = mirrorDBConnection.createStatement();
                mirrorDBResult = mirrorDBStatement.executeQuery(query);
                System.out.println("The user data was:");
                String username;
                while (mirrorDBResult.next()) {
                    if ((username = mirrorDBResult.getString("username")).equals(null)) {
                        System.out.println("The user doesn't exist!");
                        break;
                    }else {
                        int weight = mirrorDBResult.getInt("weight");
                        int bpTop = mirrorDBResult.getInt("bp_top");
                        int bpBottom = mirrorDBResult.getInt("bp_bottom");
                        int pulse = mirrorDBResult.getInt("pulse");
                        int temperature = mirrorDBResult.getInt("temperature");
                        int bmi = mirrorDBResult.getInt("bmi");
                        String photoPath = mirrorDBResult.getString("photo_path");
                        String infoDate = mirrorDBResult.getString("info_date");
                        currData[0] = username;
                        currData[1] = Integer.toString(weight);
                        currData[2] = Integer.toString(bpTop);
                        currData[3] = Integer.toString(bpBottom);
                        currData[4] = Integer.toString(pulse);
                        currData[5] = Integer.toString(temperature);
                        currData[6] = Integer.toString(bmi);
                        currData[7] = photoPath;
                        currData[8] = infoDate;
                        System.out.println(username + "\t" + weight +
                                "\t" + bpTop + "\t" + bpBottom + "\t" + pulse + "\t" + temperature +
                                "\t" + bmi + "\n" + photoPath + "\t" + infoDate);
                    }
                }
            } catch (SQLException datErr ) {
                System.out.println("There was a problem getting some of the data");
                datErr.printStackTrace();
            } finally {
                if (mirrorDBStatement != null) { mirrorDBStatement.close(); }
            }
        }catch(SQLException ex){
            System.out.println("Connection Error");
            ex.printStackTrace();
        }
        return currData;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////  Method to store new user data into the database for the specified date  ///////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static boolean setHealthData(String[] dataToUse, String currUser) throws SQLException, ClassNotFoundException {
        boolean wasSet = true;
        System.out.println("Connecting to database...");
        //String validationQuery = "SELECT EXISTS(SELECT 1 FROM user_accounts WHERE username = '"+currUser+"')";
        String query = "INSERT INTO daily_vitals (username, weight, bp_top, bp_bottom, pulse, temperature, bmi, info_date, has_data) "
                + "VALUES('"+currUser+"',"+dataToUse[1]+","+dataToUse[2]+","+dataToUse[3]+","+dataToUse[4]+","+dataToUse[5]
                + ","+dataToUse[6]+",'"+dataToUse[7]+"',1) "
                + "ON DUPLICATE KEY UPDATE  username='"+currUser+"',"
                + "weight=IF("+dataToUse[1]+" != 0,"+dataToUse[1]+",weight),"
                + "bp_top=IF("+dataToUse[2]+" != 0,"+dataToUse[2]+",bp_top),"
                + "bp_bottom=IF("+dataToUse[3]+" != 0,"+dataToUse[3]+",bp_bottom),"
                + "pulse=IF("+dataToUse[4]+" != 0,"+dataToUse[4]+",pulse),"
                + "temperature=IF("+dataToUse[5]+" != 0,"+dataToUse[5]+",temperature),"
                + "bmi=IF("+dataToUse[6]+"!= 0,"+dataToUse[6]+",bmi)";

        try {
            boolean mirrorDBSetter;
            mirrorDBConnection = DriverManager.getConnection(Url, usernameDB, password);
            System.out.println("Database Connected! Intent to set data!");
            try {
                mirrorDBStatement = mirrorDBConnection.createStatement();
                //mirrorDBResult = mirrorDBStatement.executeQuery(validationQuery);
                //if(!mirrorDBResult.next()) {
                //}
                mirrorDBSetter = mirrorDBStatement.execute(query);
                System.out.println("The DB response was:");
                if (!mirrorDBSetter) {
                    System.out.println("No errors inserting data: got " + mirrorDBResult);
                }else {
                    System.out.println("Something went wrong inserting data");
                }
            } catch (SQLException datErr) {
                System.out.println("There was a problem setting some of the data");
                wasSet = false;
                datErr.printStackTrace();
            } finally {
                if (mirrorDBStatement != null) {
                    mirrorDBStatement.close();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Connection Error");
            wasSet = false;
            ex.printStackTrace();
        }
        return wasSet;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  Method to verify valid user  ////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static boolean verifyUser(String[] dataToUse) throws SQLException, ClassNotFoundException {
        boolean wasFound = false;
        System.out.println("Connecting to database...");
        System.out.println("At user verification step, checking for user: " + dataToUse[1] + "with password: " + dataToUse[2]);
        String query = "SELECT 1 FROM user_accounts WHERE username = '"+dataToUse[1]+"' AND password = '"+dataToUse[2]+"' LIMIT 1";

        try {
            mirrorDBConnection = DriverManager.getConnection(Url, usernameDB, password);
            System.out.println("Database Connected! Intent to set data!");
            try {
                mirrorDBStatement = mirrorDBConnection.createStatement();
                mirrorDBResult = mirrorDBStatement.executeQuery(query);
                if(mirrorDBResult.next()) {
                    System.out.println("User found in the DB!");
                    wasFound = true;
                }else {
                    System.out.println("User was not found!");
                }
            } catch (SQLException datErr) {
                System.out.println("There was a problem checking for the user");
                datErr.printStackTrace();
            } finally {
                if (mirrorDBStatement != null) {
                    mirrorDBStatement.close();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Connection Error");
            ex.printStackTrace();
        }
        return wasFound;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////  Method to retrieve a set of images only form the database  /////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String[] getImagesDB(String[] dataToUse, String currUser, boolean wasMulti, int numDays) throws SQLException, ClassNotFoundException {
        System.out.println("Connecting to database...");
        String[] imgArry = new String[numDays+1];
        String query;
        if(wasMulti) {
            query = "SELECT photo_path FROM daily_vitals WHERE username='"+currUser+"' AND info_date between '"
                    +dataToUse[1]+"' AND '"+dataToUse[2]+"' AND photo_path != 'UserImages/DefaultUserPic.png' AND has_data=1 ORDER BY info_date";
        }else {
            query = "SELECT photo_path FROM daily_vitals WHERE username='"+currUser+"' AND info_date='"+dataToUse[1]+"' AND has_data=1";
        }

        try {
            mirrorDBConnection = DriverManager.getConnection(Url, usernameDB, password);
            System.out.println("Database Connected! Intent to retrieve user images!");
            try {
                mirrorDBStatement = mirrorDBConnection.createStatement();
                mirrorDBResult = mirrorDBStatement.executeQuery(query);
                System.out.println("The images pulled were:");
                int counter = 0;
                while (mirrorDBResult.next()) {
                    String photoPath = mirrorDBResult.getString("photo_path");
                    imgArry[counter] = photoPath;
                    counter++;
                    System.out.println(photoPath + "\t");
                }
            } catch (SQLException datErr) {
                System.out.println("There was a problem getting some of the data");
                datErr.printStackTrace();
            } finally {
                if (mirrorDBStatement != null) {
                    mirrorDBStatement.close();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Connection Error");
            ex.printStackTrace();
        }
        return imgArry;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////  Method to store new weight from scale into the current date  ///////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static boolean setDailyWeight(int weightToSet, String currUser, String todayDate) throws SQLException, ClassNotFoundException {
        boolean wasSet = true;
        System.out.println("Connecting to database...");
        System.out.println("The data to use: " + weightToSet + " " + currUser + " " +todayDate);
        String query = "INSERT INTO daily_vitals (username, weight, info_date, has_data) "
                + "VALUES('"+currUser+"',"+weightToSet+",'"+todayDate+"',1)"
                + "ON DUPLICATE KEY UPDATE weight="+weightToSet;

        try {
            boolean mirrorDBSetter;
            mirrorDBConnection = DriverManager.getConnection(Url, usernameDB, password);
            System.out.println("Database Connected! Intent to set data!");
            try {
                mirrorDBStatement = mirrorDBConnection.createStatement();
                //mirrorDBResult = mirrorDBStatement.executeQuery(validationQuery);
                //if(!mirrorDBResult.next()) {
                //}
                mirrorDBSetter = mirrorDBStatement.execute(query);
                System.out.println("The DB response was:");
                if (!mirrorDBSetter) {
                    System.out.println("No errors inserting data: got " + mirrorDBResult);
                }else {
                    System.out.println("Something went wrong inserting data");
                }
            } catch (SQLException datErr) {
                System.out.println("There was a problem setting some of the data");
                wasSet = false;
                datErr.printStackTrace();
            } finally {
                if (mirrorDBStatement != null) {
                    mirrorDBStatement.close();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Connection Error");
            wasSet = false;
            ex.printStackTrace();

        }
        return wasSet;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////  Method to store new camera image into the current date  ///////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static boolean setDailyImage(String pathToImage, String currUser, String todayDate) throws SQLException, ClassNotFoundException {
        boolean wasSet = true;
        System.out.println("Connecting to database...");
        System.out.println("The data to use: " + pathToImage + " " + currUser + " " +todayDate);
        String query = "INSERT INTO daily_vitals (username, photo_path, info_date, has_data) "
                + "VALUES('"+currUser+"','"+pathToImage+"','"+todayDate+"',1)"
                + "ON DUPLICATE KEY UPDATE photo_path='"+pathToImage+"'";

        try {
            boolean mirrorDBSetter;
            mirrorDBConnection = DriverManager.getConnection(Url, usernameDB, password);
            System.out.println("Database Connected! Intent to set data!");
            try {
                mirrorDBStatement = mirrorDBConnection.createStatement();
                //mirrorDBResult = mirrorDBStatement.executeQuery(validationQuery);
                //if(!mirrorDBResult.next()) {
                //}
                mirrorDBSetter = mirrorDBStatement.execute(query);
                System.out.println("The DB response was:");
                if (!mirrorDBSetter) {
                    System.out.println("No errors inserting data: got " + mirrorDBResult);
                }else {
                    System.out.println("Something went wrong inserting data");
                }
            } catch (SQLException datErr) {
                System.out.println("There was a problem setting some of the data");
                wasSet = false;
                datErr.printStackTrace();
            } finally {
                if (mirrorDBStatement != null) {
                    mirrorDBStatement.close();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Connection Error");
            wasSet = false;
            ex.printStackTrace();

        }
        return wasSet;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////  Method to break user argument list into separated components for processing////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String[] getUserArgs(String currArgs){
        System.out.println("Was in the arguments splitting routine");
        String[] argsArray;
        argsArray = currArgs.split("\\s+");
        System.out.println("The arguments that were split: ");
        for(int i = 0; i < argsArray.length;i++){
            System.out.print(argsArray[i] + " ");
        }
        System.out.println();
        return argsArray;
    }
}
