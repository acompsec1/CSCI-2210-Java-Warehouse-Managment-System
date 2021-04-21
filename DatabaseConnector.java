import javafx.scene.control.Alert;
import javafx.stage.Window;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DatabaseConnector {

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/wmdb";
    private static final String DATABASE_USERNAME = "root";
    private static final String DATABASE_PASSWORD = "ki8&fRda";
    private static final String SELECT_QUERY = "SELECT * FROM users WHERE username = ? and password_hash = ?";
    private static final String ROLE_QUERY = "SELECT role_id FROM users WHERE username = ? and password_hash = ?";
    private static final String USER_QUERY = "SELECT username FROM users WHERE username = ?";
    private static final String CREATEUSER_QUERY = "Insert INTO users (username, password_hash, role_id) VALUES (?,?,?)";
    private static final String DELETE_USER = "DELETE FROM users WHERE ID = ? and username = ?";
    private static final String FIND_USER = "Select username from users where ID = ?";
    private static final String UPDATE_USER_PASSWORD = "UPDATE users SET password_hash = ? where ID = ?";
    private static final String UPDATE_USER_ROLE = "UPDATE users set role_id = ? where ID = ?";
    private static final String UPDATE_USER_BOTH = "Update users set role_id = ?, password_hash = ? where ID = ?";
    private static final String CREATE_BORROW_REQUEST = "INSERT INTO borrowed_items (item_id, amount, user_id, borrow_date, return_date) VALUES (?, 1, ?, STR_TO_DATE(?, \"%H:%i:%s %d/%m/%Y\"), STR_TO_DATE(?,\"%H:%i:%s %d/%m/%Y\"))";
    private static final String ITEM_QUERY = "Select ITEMID from items where ITEMID = ?";
    private static final String USERID_QUERY = "Select ID from users where username = ?";


    public static void infoBox(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

    public static Connection getConnection() throws SQLException{
        try{

            Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
//            System.out.println("Connected to database");
            return conn;
        } catch(SQLException e){
            System.out.println(e);
        }

        return null;
    }

    public boolean validate(String usernameId, String password) throws SQLException{

        // Step 1: Establishing a Connection and
        // try-with-resource statement will auto close the connection.
        Connection con = getConnection();
        try (

             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = con.prepareStatement(SELECT_QUERY)) {
            preparedStatement.setString(1, usernameId);
            preparedStatement.setString(2, password);

//            System.out.println(preparedStatement);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }


        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
        return false;
    }

    public boolean userExists(String usernameId) throws Exception{

        // Step 1: Establishing a Connection and
        // try-with-resource statement will auto close the connection.
        Connection con = getConnection();
        try (

                // Step 2:Create a statement using connection object
                PreparedStatement preparedStatement = con.prepareStatement(USER_QUERY)) {
            preparedStatement.setString(1, usernameId);

//            System.out.println(preparedStatement);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String value = resultSet.getString(1);
//                System.out.print(value);
                if (value.equals(usernameId)){
                    return true;
                }
                else{
                    return false;
                }
            }


        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
        return false;
    }

    public boolean getUsername(Integer user_ID, String username) throws Exception{
        Connection con = getConnection();
        try (
                // Step 2:Create a statement using connection object
                PreparedStatement preparedStatement = con.prepareStatement(FIND_USER)) {
            preparedStatement.setInt(1, user_ID);

//            System.out.println(preparedStatement);

            //EXECUTE THE QUERY
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String value = resultSet.getString(1);

                if (value.equals(username)){
                    return true;
                }
                else{
                    return false;
                }
//
            }

        } catch (SQLException e) {
            // print SQL exception information
            System.out.print("FAILED TO EXECUTE USERNAME SEARCH PROPERLY");
            printSQLException(e);
        }
        return false;
    }

    public int getUserID(String username) throws SQLException{
        Connection con = getConnection();
        Integer value = 0;
        try (
                // Step 2:Create a statement using connection object
                PreparedStatement preparedStatement = con.prepareStatement(USERID_QUERY)) {
            preparedStatement.setString(1, username);

//            System.out.println(preparedStatement);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                value = Integer.parseInt(resultSet.getString(1));
            }

        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
        return value;
    }

    public boolean getRole(String usernameId, String password) throws SQLException{

        // Step 1: Establishing a Connection and
        // try-with-resource statement will auto close the connection.
        Connection con = getConnection();
        try (

                // Step 2:Create a statement using connection object
                PreparedStatement preparedStatement = con.prepareStatement(ROLE_QUERY)) {
            preparedStatement.setString(1, usernameId);
            preparedStatement.setString(2, password);

//            System.out.println(preparedStatement);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String value = resultSet.getString(1);
//                System.out.print(value);
                if (value.equals("1")){
                    return true;
                }
                else{
                    return false;
                }
            }


        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
        return false;
    }

    public boolean create(String usernameId, String password, Integer role) throws Exception{

        // Step 1: Establishing a Connection and
        // try-with-resource statement will auto close the connection.
        Connection con = getConnection();
        try (
                // Step 2:Create a statement using connection object
                PreparedStatement preparedStatement = con.prepareStatement(CREATEUSER_QUERY)) {
            preparedStatement.setString(1, usernameId);
            preparedStatement.setString(2, password);
            preparedStatement.setInt(3, role);

//            System.out.println(preparedStatement);

            //EXECUTE THE QUERY
            preparedStatement.executeUpdate();
            infoBox("Username created!", null, "Success!");

        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
        return false;
    }

    public boolean deleteUser(Integer user_Id, String username) throws Exception{

        // Step 1: Establishing a Connection and
        // try-with-resource statement will auto close the connection.
        Connection con = getConnection();
        try (
                // Step 2:Create a statement using connection object
                PreparedStatement preparedStatement = con.prepareStatement(DELETE_USER)) {
            preparedStatement.setInt(1, user_Id);
            preparedStatement.setString(2,username);
//            System.out.println(preparedStatement);

            //EXECUTE THE QUERY
            preparedStatement.executeUpdate();
            infoBox("User deleted!", null, "Success!");

        } catch (SQLException e) {
            // print SQL exception information
            System.out.print("FAILED TO EXECUTE DELETE PROPERLY");
            printSQLException(e);
        }
        return false;
    }

    public boolean updateUser(Integer user_Id, String username, String password, Integer role, String option) throws Exception{
        // Step 1: Establishing a Connection and
        // try-with-resource statement will auto close the connection.
        Connection con = getConnection();
        if (option.equals("password")) {
            try (
                    // Step 2:Create a statement using connection object
                    PreparedStatement preparedStatement = con.prepareStatement(UPDATE_USER_PASSWORD)) {
                preparedStatement.setString(1, password);
                preparedStatement.setInt(2,user_Id);
//            System.out.println(preparedStatement);

                //EXECUTE THE QUERY
                preparedStatement.executeUpdate();
                infoBox("User password updated!", null, "Success!");

            } catch (SQLException e) {
                // print SQL exception information
                System.out.print("FAILED TO EXECUTE UPDATE PROPERLY");
                printSQLException(e);
            }
            return false;
        }

        if (option.equals("role")){
            try (
                    // Step 2:Create a statement using connection object
                    PreparedStatement preparedStatement = con.prepareStatement(UPDATE_USER_ROLE)) {
                preparedStatement.setInt(1, role);
                preparedStatement.setInt(2, user_Id);
//            System.out.println(preparedStatement);

                //EXECUTE THE QUERY
                preparedStatement.executeUpdate();
                infoBox("User role updated!", null, "Success!");

            } catch (SQLException e) {
                // print SQL exception information
                System.out.print("FAILED TO EXECUTE UPDATE PROPERLY");
                printSQLException(e);
            }
            return false;
        }
        if (option.equals("both")){
            try (
                    // Step 2:Create a statement using connection object
                    PreparedStatement preparedStatement = con.prepareStatement(UPDATE_USER_BOTH)) {
                preparedStatement.setInt(1, role);
                preparedStatement.setString(2,password);
                preparedStatement.setInt(3, user_Id);
//            System.out.println(preparedStatement);

                //EXECUTE THE QUERY
                preparedStatement.executeUpdate();
                infoBox("User password and role updated!", null, "Success!");

            } catch (SQLException e) {
                // print SQL exception information
                System.out.print("FAILED TO EXECUTE DELETE PROPERLY");
                printSQLException(e);
            }
            return false;
        }
        return false;
    }

    public boolean getItem(Integer id_item) throws SQLException {
        Connection con = getConnection();
        try (

                // Step 2:Create a statement using connection object
                PreparedStatement preparedStatement = con.prepareStatement(ITEM_QUERY)) {
            preparedStatement.setInt(1, id_item);

//            System.out.println(preparedStatement);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Integer value = Integer.parseInt(resultSet.getString(1));
//                System.out.print(value);
                if (value.equals(id_item)){
                    return true;
                }
                else{
                    return false;
                }
            }


        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
        return false;
    }

    public boolean createRequest(Integer user_id, Integer item_id, String time_in, String time_out) throws SQLException {
        Connection con = getConnection();


//        String in_time = time_in.split(" ", 3)[0];
//        String in_date = time_in.split(" ", 3)[1];
//
//        String hours_in = in_time.split(":", 3)[0];
//        String minutes_in = in_time.split(":", 3)[1];
//        String seconds_in = in_time.split(":", 3)[2];
//
//        String month_in = in_date.split("/",3)[0];
//        String day_in = in_date.split("/", 3)[1];
//        String year_in = in_date.split("/", 3)[2];
//
//        String out_time = time_out.split(" ", 3)[0];
//        String out_date = time_out.split(" ", 3)[1];
//
//        String hours_out = out_time.split(":", 3)[0];
//        String minutes_out = out_time.split(":", 3)[1];
//        String seconds_out = out_time.split(":", 3)[2];
//
//        String month_out = out_date.split("/",3)[0];
//        String day_out = out_date.split("/", 3)[1];
//        String year_out = out_date.split("/", 3)[2];
//
//        System.out.println("ORIGINAL TIME IN " + time_in);
//        System.out.println("ORIGINAL TIME OUT " + time_out);
//        System.out.println("Hours In: " + hours_in);
//        System.out.println("Minutes In: " + minutes_in);
//        System.out.println("Seconds In: " + seconds_in);
//        System.out.println("Month In: " + month_in);
//        System.out.println("Day In: "+ day_in);
//        System.out.println("Year In: " + year_in);
//        System.out.println("Hours Out: " + hours_out);
//        System.out.println("Minutes Out: "+ minutes_out);
//        System.out.println("Seconds Out: " + seconds_out);
//        System.out.println("Month Out: "+ month_out);
//        System.out.println("Day Out: " + day_out);
//        System.out.println("Year Out: " + year_out);

        try (
                // Step 2:Create a statement using connection object
                PreparedStatement preparedStatement = con.prepareStatement(CREATE_BORROW_REQUEST)) {
            preparedStatement.setInt(1, item_id);
            preparedStatement.setInt(2, user_id);
            preparedStatement.setString(3, time_in);
            preparedStatement.setString(4, time_out);

            //EXECUTE THE QUERY
            preparedStatement.executeUpdate();
            infoBox("Borrow Request Created!", null, "Success!");

        } catch (SQLException e) {
            // print SQL exception information
            System.out.print("FAILED TO EXECUTE CREATE REQUEST PROPERLY");
            printSQLException(e);
        }
        return false;
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

    public static String getMD5(String password)
    {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");

            byte[] messageDigest = md.digest(password.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while(hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        // Source: https://www.geeksforgeeks.org/md5-hash-in-java/
    }

    public static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

}