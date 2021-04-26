import javafx.scene.control.Alert;
import javafx.stage.Window;

import java.math.BigDecimal;
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
    private static final String CREATE_BORROW_REQUEST = "INSERT INTO borrowed_items (item_id, amount, user_id, borrow_date, return_date, borrow_status) VALUES (?, 1, ?, STR_TO_DATE(?, \"%H:%i:%s %m/%d/%Y\"), STR_TO_DATE(?,\"%H:%i:%s %m/%d/%Y\"), \"PENDING\")";
    private static final String ITEM_QUERY = "Select ITEMID from items where ITEMID = ?";
    private static final String ITEM_EXISTS = "SELECT name FROM items WHERE name = ?";
    private static final String USERID_QUERY = "Select ID from users where username = ?";
    private static final String REQUEST_QUERY = "Select BORROW_REQUEST from borrowed_items where BORROW_REQUEST = ?";
    private static final String FIND_ITEM = "Select name from items where ITEMID = ?";
    private static final String DELETE_ITEM = "DELETE FROM items where ITEMID = ?";
    private static final String ACCEPT_REQUEST = "UPDATE borrowed_items SET borrow_status = \"ACCEPTED\" WHERE BORROW_REQUEST = ?";
    private static final String REJECT_REQUEST = "DELETE FROM borrowed_items WHERE BORROW_REQUEST = ?";
//    private static final String GET_USER_BORROWS = "SELECT * FROM borrowed_items WHERE user_id = ?";
    private static final String ADD_FAVORITE = "INSERT INTO favorites (item_id, item_name, user_id) VALUES (?, ?, ?)";
    private static final String FAVORITE_QUERY = "SELECT FAVORITEID from favorites where FAVORITEID = ?";
    private static final String DELETE_FAVORITE = "DELETE FROM favorites where USER_ID = ? and FAVORITEID = ?";
    private static final String FAVORITE_EXISTS = "SELECT item_id, user_id FROM favorites WHERE ITEM_ID = ? and USER_ID = ?";
    private static final String COUNT_BORROW_REQUESTS = "SELECT count(BORROW_REQUEST) from borrowed_items where user_id = ? and borrow_status = 'PENDING'";

    private static final String SEARCH_ITEMS = "SELECT ITEMID FROM items WHERE name = ?";

    private static final String EDIT_ITEM_BOTH = "UPDATE items SET quantity = ?, price = ? WHERE ITEMID = ? AND name = ?";
    private static final String EDIT_ITEM_PRICE = "Update items SET price = ? where ITEMID = ?";
    private static final String EDIT_ITEM_QUANTITY = "UPDATE items SET quantity = ? where ITEMID = ?";


    public static int session;


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
            session = getUserID(usernameId);
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

    public boolean itemExists(String item_name) throws Exception{

        // Step 1: Establishing a Connection and
        // try-with-resource statement will auto close the connection.
        Connection con = getConnection();
        try (

                // Step 2:Create a statement using connection object
                PreparedStatement preparedStatement = con.prepareStatement(ITEM_EXISTS)) {
            preparedStatement.setString(1, item_name);

//            System.out.println(preparedStatement);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String value = resultSet.getString(1);
//                System.out.print(value);
                if (value.equals(item_name)){
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

    public int getItemId(String name) throws Exception
    {
        Connection con = getConnection();
        try (

                // Step 2:Create a statement using connection object
                PreparedStatement preparedStatement = con.prepareStatement(SEARCH_ITEMS)) {
            preparedStatement.setString(1, name);

//            System.out.println(preparedStatement);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int value = resultSet.getInt(1);
//                System.out.print(value);
                return value;
            }


        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
        return -1;
    }

    public boolean favoriteItemExists(int item_id, int user_id) throws Exception{

        // Step 1: Establishing a Connection and
        // try-with-resource statement will auto close the connection.
        Connection con = getConnection();
        try (

                // Step 2:Create a statement using connection object
                PreparedStatement preparedStatement = con.prepareStatement(FAVORITE_EXISTS)) {
            preparedStatement.setInt(1, item_id);
            preparedStatement.setInt(2, user_id);

//            System.out.println(preparedStatement);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int item = resultSet.getInt(1);
                int user = resultSet.getInt(2);
//                System.out.print(value);
                if (item == item_id && user == user_id){
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

    public boolean requestExists(int requestId) throws Exception{

        // Step 1: Establishing a Connection and
        // try-with-resource statement will auto close the connection.
        Connection con = getConnection();
        try (

                // Step 2:Create a statement using connection object
                PreparedStatement preparedStatement = con.prepareStatement(REQUEST_QUERY)) {
            preparedStatement.setInt(1, requestId);

//            System.out.println(preparedStatement);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int value = resultSet.getInt(1);
//                System.out.print(value);
                if (value == requestId){
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

    public static void acceptRequest(Integer requestId) throws Exception
    {
        // Step 1: Establishing a Connection and
        // try-with-resource statement will auto close the connection.
        Connection con = getConnection();
        try (
                // Step 2:Create a statement using connection object
                PreparedStatement preparedStatement = con.prepareStatement(ACCEPT_REQUEST)) {
            preparedStatement.setInt(1, requestId);

//            System.out.println(preparedStatement);

            //EXECUTE THE QUERY
            preparedStatement.executeUpdate();
            infoBox("Borrow request accepted!", null, "Success!");

        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
    }

    public static void rejectRequest(Integer requestId) throws Exception
    {
        // Step 1: Establishing a Connection and
        // try-with-resource statement will auto close the connection.
        Connection con = getConnection();
        try (
                // Step 2:Create a statement using connection object
                PreparedStatement preparedStatement = con.prepareStatement(REJECT_REQUEST)) {
            preparedStatement.setInt(1, requestId);

//            System.out.println(preparedStatement);

            //EXECUTE THE QUERY
            preparedStatement.executeUpdate();
            infoBox("Borrow request rejected!", null, "Success!");

        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
    }

    public boolean verifyItem(Integer item_ID, String item_name) throws Exception{
        Connection con = getConnection();
        try (
                // Step 2:Create a statement using connection object
                PreparedStatement preparedStatement = con.prepareStatement(FIND_ITEM)) {
            preparedStatement.setInt(1, item_ID);

//            System.out.println(preparedStatement);

            //EXECUTE THE QUERY
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String value = resultSet.getString(1);

                if (value.equals(item_name)){
                    return true;
                }
                else{
                    return false;
                }
//
            }

        } catch (SQLException e) {
            // print SQL exception information
            System.out.print("FAILED TO EXECUTE ITEM NAME SEARCH PROPERLY");
            printSQLException(e);
        }
        return false;
    }

//    public static ResultSet getUserBorrows(int user_ID, ResultSet rs) throws Exception {
//        Connection con = DatabaseConnector.getConnection();
//        try (
//                // Step 2:Create a statement using connection object
//                PreparedStatement preparedStatement = con.prepareStatement(GET_USER_BORROWS)) {
//            preparedStatement.setInt(1, user_ID);
//
////            System.out.println(preparedStatement);
//
//            //EXECUTE THE QUERY
//            ResultSet resultSet = preparedStatement.executeQuery();
//            rs = resultSet;
//
//        } catch (SQLException e) {
//            // print SQL exception information
//            System.out.print("FAILED TO EXECUTE USERNAME SEARCH PROPERLY");
//            DatabaseConnector.printSQLException(e);
//        }
//        return null;
//
//    }

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

    public boolean getItemName(Integer item_id, String item_name) throws Exception{
        Connection con = getConnection();
        try (
                // Step 2:Create a statement using connection object
                PreparedStatement preparedStatement = con.prepareStatement(FIND_ITEM)) {
            preparedStatement.setInt(1, item_id);

//            System.out.println(preparedStatement);

            //EXECUTE THE QUERY
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String value = resultSet.getString(1);

                if (value.equals(item_name)){
                    return true;
                }
                else{
                    return false;
                }
//
            }

        } catch (SQLException e) {
            // print SQL exception information
            System.out.print("FAILED TO EXECUTE ITEM SEARCH VERIFICATION PROPERLY");
            printSQLException(e);
        }
        return false;
    }

    public boolean deleteItem(Integer item_id, String item_name) throws Exception{
        Connection con = getConnection();
        try (
                // Step 2:Create a statement using connection object
                PreparedStatement preparedStatement = con.prepareStatement(DELETE_ITEM)) {
            preparedStatement.setInt(1, item_id);
//            preparedStatement.setString(2,item_name);
//            System.out.println(preparedStatement);

            //EXECUTE THE QUERY
            preparedStatement.executeUpdate();
            infoBox("Item deleted!", null, "Success!");

        } catch (SQLException e) {
            // print SQL exception information
            System.out.print("FAILED TO EXECUTE DELETE ITEM PROPERLY");
            printSQLException(e);
        }
        return false;
    }

    public boolean editItem(BigDecimal price, Integer quantity, Integer item_id, String item_name, String option) throws SQLException {
        Connection con = getConnection();

        if (option.equals("price")){
            try (
                    // Step 2:Create a statement using connection object
                    PreparedStatement preparedStatement = con.prepareStatement(EDIT_ITEM_PRICE)) {
                preparedStatement.setBigDecimal(1, price);
                preparedStatement.setInt(2, item_id);
                //EXECUTE THE QUERY
                preparedStatement.executeUpdate();
                infoBox("Item information has been updated!", null, "Success!");

            } catch (SQLException e) {
                // print SQL exception information
                System.out.print("FAILED TO EXECUTE CREATE REQUEST PROPERLY");
                printSQLException(e);
            }
            return false;

        }

        if (option.equals("number_items")){
            try (
                    // Step 2:Create a statement using connection object
                    PreparedStatement preparedStatement = con.prepareStatement(EDIT_ITEM_QUANTITY)) {
                preparedStatement.setInt(1, quantity);
                preparedStatement.setInt(2, item_id);
                //EXECUTE THE QUERY
                preparedStatement.executeUpdate();
                infoBox("Item information has been updated!", null, "Success!");

            } catch (SQLException e) {
                // print SQL exception information
                System.out.print("FAILED TO EXECUTE CREATE REQUEST PROPERLY");
                printSQLException(e);
            }
            return false;
        }

        if (option.equals("both")) {
            try (
                    // Step 2:Create a statement using connection object
                    PreparedStatement preparedStatement = con.prepareStatement(EDIT_ITEM_BOTH)) {
                preparedStatement.setInt(1, quantity);
                preparedStatement.setBigDecimal(2, price);
                preparedStatement.setInt(3, item_id);
                preparedStatement.setString(4, item_name);
                //EXECUTE THE QUERY
                preparedStatement.executeUpdate();
                infoBox("Item information has been updated!", null, "Success!");

            } catch (SQLException e) {
                // print SQL exception information
                System.out.print("FAILED TO EXECUTE CREATE REQUEST PROPERLY");
                printSQLException(e);
            }
            return false;
        }
        return false;
    }

    public boolean createRequest(Integer user_id, Integer item_id, String time_in, String time_out) throws SQLException {
        Connection con = getConnection();

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

    public boolean getBorrowRequestCount(Integer user_id) throws SQLException {
        Connection con = getConnection();
        try (

                // Step 2:Create a statement using connection object
                PreparedStatement preparedStatement = con.prepareStatement(COUNT_BORROW_REQUESTS)) {
            preparedStatement.setInt(1, user_id);

//            System.out.println(preparedStatement);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Integer value = resultSet.getInt(1);
//                System.out.print(value);
                if (value.equals(3)){
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

    public boolean addFavorite(Integer item_id, String item_name, Integer user_id) throws SQLException {
        Connection con = getConnection();

        try (
                // Step 2:Create a statement using connection object
                PreparedStatement preparedStatement = con.prepareStatement(ADD_FAVORITE)) {
            preparedStatement.setInt(1, item_id);
            preparedStatement.setString(2, item_name);
            preparedStatement.setInt(3, user_id);
            //EXECUTE THE QUERY
            preparedStatement.executeUpdate();
            infoBox("Item added to favorites!", null, "Success!");

        } catch (SQLException e) {
            // print SQL exception information
            System.out.print("FAILED TO EXECUTE CREATE REQUEST PROPERLY");
            printSQLException(e);
        }
        return false;
    }

    public boolean deleteFavorite(Integer id_favorite, Integer user_id) throws SQLException {
        Connection con = getConnection();

        try (
                // Step 2:Create a statement using connection object
                PreparedStatement preparedStatement = con.prepareStatement(DELETE_FAVORITE)) {
            preparedStatement.setInt(1, user_id);
            preparedStatement.setInt(2,id_favorite);
//            System.out.println(preparedStatement);

            //EXECUTE THE QUERY
            preparedStatement.executeUpdate();
            infoBox("Favorite deleted!", null, "Success!");

        } catch (SQLException e) {
            // print SQL exception information
            System.out.print("FAILED TO EXECUTE DELETE PROPERLY");
            printSQLException(e);
        }
        return false;
    }

    public boolean getFavorite(Integer favorite_id) throws SQLException {
        Connection con = getConnection();
        try (

                // Step 2:Create a statement using connection object
                PreparedStatement preparedStatement = con.prepareStatement(FAVORITE_QUERY)) {
            preparedStatement.setInt(1, favorite_id);

//            System.out.println(preparedStatement);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Integer value = Integer.parseInt(resultSet.getString(1));
//                System.out.print(value);
                if (value.equals(favorite_id)){
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